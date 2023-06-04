package com.template.states;

import com.template.contracts.TemplateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import com.template.contracts.CarContract;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(CarContract.class)
public class CarState implements ContractState {


    private final String model;
    private final Party owner;
    private final Party manufacturer;

    public CarState(String model, Party owner, Party manufacturer) {
        this.model = model;
        this.owner = owner;
        this.manufacturer = manufacturer;
    }

    public String getModel() { return model; }
    public Party getOwner() { return owner; }
    public Party getManufacturer() { return manufacturer; }


    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner, manufacturer);
    }
}