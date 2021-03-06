package com.github.technus.tectech.thing.metaTileEntity.hatch.gui;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Rack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

/**
 * Created by Tec on 09.04.2017.
 */
public class GT_Container_Rack extends GT_ContainerMetaTile_Machine {
    public boolean heat = false;

    public GT_Container_Rack(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    public void addSlots(InventoryPlayer aInventoryPlayer) {
        this.addSlotToContainer(new Slot(this.mTileEntity, 0, 69, 28));
        this.addSlotToContainer(new Slot(this.mTileEntity, 1, 91, 28));
        this.addSlotToContainer(new Slot(this.mTileEntity, 2, 69, 50));
        this.addSlotToContainer(new Slot(this.mTileEntity, 3, 91, 50));
    }

    public int getSlotCount() {
        return 4;
    }

    public int getShiftClickSlotCount() {
        return 4;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if ((this.mTileEntity.isClientSide()) || (this.mTileEntity.getMetaTileEntity() == null)) {
            return;
        }
        this.heat = ((GT_MetaTileEntity_Hatch_Rack) this.mTileEntity.getMetaTileEntity()).heat > 0;

        Iterator var2 = this.crafters.iterator();
        while (var2.hasNext()) {
            ICrafting var1 = (ICrafting) var2.next();
            var1.sendProgressBarUpdate(this, 100, this.heat ? 1 : 0);
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        super.updateProgressBar(par1, par2);
        switch (par1) {
            case 100:
                this.heat = par2 != 0;
                return;
        }
    }

    @Override
    public ItemStack slotClick(int aSlotIndex, int aMouseclick, int aShifthold, EntityPlayer aPlayer) {
        if (heat || mActive != 0) return null;
        return super.slotClick(aSlotIndex, aMouseclick, aShifthold, aPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
        if (heat || mActive != 0) return null;
        return super.transferStackInSlot(aPlayer, aSlotIndex);
    }

    @Override
    public boolean canDragIntoSlot(Slot par1Slot) {
        if (heat || mActive != 0) return false;
        return super.canDragIntoSlot(par1Slot);
    }

    @Override
    public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
        if (heat || mActive != 0) return;
        super.putStacksInSlots(par1ArrayOfItemStack);
    }

    @Override
    protected boolean mergeItemStack(ItemStack aStack, int aStartIndex, int aSlotCount, boolean par4) {
        if (heat || mActive != 0) return false;
        return super.mergeItemStack(aStack, aStartIndex, aSlotCount, par4);
    }

    @Override
    public void putStackInSlot(int par1, ItemStack par2ItemStack) {
        if (heat || mActive != 0) return;
        super.putStackInSlot(par1, par2ItemStack);
    }
}
