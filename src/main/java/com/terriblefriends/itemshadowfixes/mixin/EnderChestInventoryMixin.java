package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.EnderChestInventoryAccessor;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderChestInventory.class)
public class EnderChestInventoryMixin implements EnderChestInventoryAccessor {
    EnderChestInventory ECIM_instance = (EnderChestInventory) (Object) this;

    public NbtList writeNbtDestroyShadows() {
        NbtList nbtList = new NbtList();

        for(int i = 0; i < ECIM_instance.size(); ++i) {
            ItemStack itemStack = ECIM_instance.getStack(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                ECIM_instance.getStack(i).setCount(0);
            }
        }
        return nbtList;
    }
}
