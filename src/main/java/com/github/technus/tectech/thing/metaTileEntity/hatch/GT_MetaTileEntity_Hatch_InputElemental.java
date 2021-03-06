package com.github.technus.tectech.thing.metaTileEntity.hatch;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

/**
 * Created by danie_000 on 27.10.2016.
 */
public class GT_MetaTileEntity_Hatch_InputElemental extends GT_MetaTileEntity_Hatch_ElementalContainer {
    public GT_MetaTileEntity_Hatch_InputElemental(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Elemental Input for Multiblocks (" + 1000 * aTier * (aTier - 7) + "U, " + aTier * 2 + " stacks)");
    }

    //public GT_MetaTileEntity_Hatch_InputElemental(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
    //    super(aName, aTier, aDescription, aTextures);
    //}

    public GT_MetaTileEntity_Hatch_InputElemental(String aName, int aTier, int eTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, eTier,aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputElemental(mName, mTier, eTier, mDescription, mTextures);
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean canConnect(byte side) {
        return isInputFacing(side);
    }
}
