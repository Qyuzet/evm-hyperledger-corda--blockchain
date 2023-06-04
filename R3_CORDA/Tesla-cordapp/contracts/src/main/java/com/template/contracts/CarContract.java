package com.template.contracts;

import com.template.states.CarState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

// ************
// * Contract *
// ************
public class CarContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String CAR_CONTRACT_ID = "com.template.contracts.CarContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

        if (tx.getCommands().size() != 1) throw new IllegalArgumentException("There can only be one command");

        Command command = tx.getCommand(0);
        CommandData commandType = command.getValue();
        List<PublicKey> requiredSigners = command.getSigners();

        if (commandType instanceof Shipment) {

            // Shipment Rules

            // Shape rules

            if (tx.getInputStates().size() != 0) {
                throw new IllegalArgumentException("There cannot be input states");
            }

            if (tx.getOutputStates().size() != 1 ) {
                throw new IllegalArgumentException("Only one vehicle can be shipped at a time");
            }

            // Content rules

            ContractState outputState = tx.getOutput(0);

            if (!(outputState instanceof CarState)) {
                throw new IllegalArgumentException("Output has to be of type CarState");
            }

            CarState carState = (CarState) outputState;
            if (!carState.getModel().equals("Cybertruck")
                    && !carState.getModel().equals("Semi")
                    && !carState.getModel().equals("Model S")
                    && !carState.getModel().equals("Model 3")
                    && !carState.getModel().equals("Model X")
                    && !carState.getModel().equals("Model Y")
            ){
                throw new IllegalArgumentException("This is not a Cybertruck");
            }

            // Signer rules

            PublicKey manufacturerKey = carState.getManufacturer().getOwningKey();

            if (!(requiredSigners.contains(manufacturerKey))) {
                throw new IllegalArgumentException("Manufacturer must sign the transaction");
            }



        }

    }

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.


    // Used to indicate the transaction's intent.
    public static class Shipment implements CommandData {}

}