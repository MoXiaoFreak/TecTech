package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalInstanceStack;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.Util.VN;
import static com.github.technus.tectech.elementalMatter.core.interfaces.iElementalDefinition.stableRawLifeTime;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_decay extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;

    //region structure
    private static final String[][] shape = new String[][]{
            {"0C0","A   ","A . ","A   ","0C0",},
            {"00000","00000","00000","00000","00000",},
            {"0C0","A!!!","A!0!","A!!!","0C0",},
            {"01110","12221","12221","12221","01110",},
            {"01310","12221","32223","12221","01310",},
            {"01110","12221","12221","12221","01110",},
            {"0C0","A!!!","A!0!","A!!!","0C0",},
            {"00000","00000","00000","00000","00000",},
            {"0C0","A   ","A   ","A   ","0C0",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT ,sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 5, 8, 6};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_decay(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_decay(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_decay(this.mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_DECAY");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_DECAY_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12], new GT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][12]};
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Is life time too long?",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Make it half-life (3) instead!"
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        cElementalInstanceStackMap map= getInputsClone_EM();
        if(map!=null && map.hasStacks() && map.getFirst().getLifeTime()<stableRawLifeTime){
            return startRecipe(map.getFirst());
        }
        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return false;
    }

    private float m1,m2,m3;
    private boolean startRecipe(cElementalInstanceStack input) {
        m3=(float)Math.ceil(input.getLifeTime() / Math.pow(input.amount,3));
        if(m3<1) explodeMultiblock();
        if(m3>=Integer.MAX_VALUE) return false;
        mMaxProgresstime = 1;//(int)m3;
        mEfficiencyIncrease = 10000;
        m1 = input.getMass()/input.amount;
        cElementalInstanceStackMap decayed=input.decayCompute(input.getDefinition().getDecayArray(),1,0,0);
        m2 = decayed.getMass()/input.amount;
        //TecTech.Logger.info("I " + input.toString());
        //TecTech.Logger.info("O " + decayed.toString());
        return true;
    }

    @Override
    public String[] getInfoData() {//TODO Do it
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                "Progress:",
                EnumChatFormatting.GREEN + Integer.toString(mProgresstime / 20) + EnumChatFormatting.RESET + " s / " +
                        EnumChatFormatting.YELLOW + Integer.toString(mMaxProgresstime / 20) + EnumChatFormatting.RESET + " s",
                "Energy Hatches:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET + " EU",
                (mEUt <= 0 ? "Probably uses: " : "Probably makes: ") +
                        EnumChatFormatting.RED + Integer.toString(Math.abs(mEUt)) + EnumChatFormatting.RESET + " EU/t at " +
                        EnumChatFormatting.RED + eAmpereFlow + EnumChatFormatting.RESET + " A",
                "Tier Rating: " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier_EM()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier_EM()] + EnumChatFormatting.RESET +
                        " Amp Rating: " + EnumChatFormatting.GREEN + eMaxAmpereFlow + EnumChatFormatting.RESET + " A",
                "Problems: " + EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " Efficiency: " + EnumChatFormatting.YELLOW + Float.toString(mEfficiency / 100.0F) + EnumChatFormatting.RESET + " %",
                "PowerPass: " + EnumChatFormatting.BLUE + ePowerPass + EnumChatFormatting.RESET +
                        " SafeVoid: " + EnumChatFormatting.BLUE + eSafeVoid,
                "Computation: " + EnumChatFormatting.GREEN + eAvailableData + EnumChatFormatting.RESET + " / " + EnumChatFormatting.YELLOW + eRequiredData + EnumChatFormatting.RESET,
                m1+" "+m2+" "+m3
        };
    }
}
