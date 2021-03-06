package com.github.technus.tectech.elementalMatter.core.containers;

import com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.null__;

/**
 * Created by danie_000 on 20.11.2016.
 */
public final class cElementalDefinitionStack implements iHasElementalDefinition {
    public final iElementalDefinition definition;
    public final int amount;

    public cElementalDefinitionStack(iElementalDefinition def, int amount) {
        this.definition = def == null ? null__ : def;
        this.amount = amount;
    }

    @Override
    public final cElementalDefinitionStack clone() {
        return this;//IMMUTABLE
    }

    @Override
    public iElementalDefinition getDefinition() {
        return definition;//IMMUTABLE
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public int getCharge() {
        return definition.getCharge() * amount;
    }

    public float getMass() {
        return definition.getMass() * amount;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("d", definition.toNBT());
        nbt.setInteger("q", amount);
        return nbt;
    }

    public static cElementalDefinitionStack fromNBT(NBTTagCompound nbt) {
        return new cElementalDefinitionStack(
                cElementalDefinition.fromNBT(nbt.getCompoundTag("d")),
                nbt.getInteger("q"));
    }

    public cElementalDefinitionStack addAmountIntoNewInstance(int amount) {
        if(amount==0) return this;
        return new cElementalDefinitionStack(definition, amount + this.amount);
    }

    public cElementalDefinitionStack addAmountIntoNewInstance(cElementalDefinitionStack... other) {
        if (other == null || other.length == 0) return this;
        int i = 0;
        for (cElementalDefinitionStack stack : other)
            i += stack.amount;
        return addAmountIntoNewInstance(i);
    }

    @Override
    public int compareTo(iHasElementalDefinition o) {
        return definition.compareTo(o.getDefinition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof iElementalDefinition)
            return definition.compareTo((iElementalDefinition) obj) == 0;
        if (obj instanceof iHasElementalDefinition)
            return definition.compareTo(((iHasElementalDefinition) obj).getDefinition()) == 0;
        return false;
    }

    //Amount shouldn't be hashed if this is just indicating amount and not structure
    @Override
    public int hashCode() {
        return definition.hashCode();
    }
}
