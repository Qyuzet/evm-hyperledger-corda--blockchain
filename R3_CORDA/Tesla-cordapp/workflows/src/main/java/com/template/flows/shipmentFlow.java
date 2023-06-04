package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.states.CarState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import com.template.contracts.CarContract;
import static com.template.contracts.CarContract.CAR_CONTRACT_ID;


// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class shipmentFlow extends FlowLogic<SignedTransaction> {
    private String model;
    private Party owner;

    private final ProgressTracker progressTracker = new ProgressTracker();

    public shipmentFlow(String model, Party owner) {
        this.model = model;
        this.owner = owner;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Initiator flow logic goes here.

        // Checking if the node is Tesla
        if (getOurIdentity().getName().getOrganisation().equals("Tesla")) {
            System.out.println("Identity Verified");
        } else {
            throw  new FlowException("This is not Tesla");
        }


        // Retrieve the notary identity from the network map
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

        // Create the transaction components (Inputs/outputs)
        CarState outputState = new CarState(model, owner, getOurIdentity());

        // Create the transaction builder and add components
        TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(outputState, CAR_CONTRACT_ID)
                .addCommand(new CarContract.Shipment(), getOurIdentity().getOwningKey());



        // Signing the transaction
        SignedTransaction shipmentTx = getServiceHub().signInitialTransaction(txBuilder);

        // Create session with counterparty
        FlowSession otherPartySession = initiateFlow(owner);

        // Finalising the transaction
        return subFlow(new FinalityFlow(shipmentTx, otherPartySession));


    }
}
