package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.BlockEntityAccessor;
import net.minecraft.block.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityAccessor {
    BlockEntity BE_instance = (BlockEntity) (Object) this;

    public NbtCompound createNbtWithIdentifyingDataDestroyShadows() {
        NbtCompound returnValue = BE_instance.createNbtWithIdentifyingData();

        if (BE_instance instanceof ChestBlockEntity) {
            if (!returnValue.contains("LootTable")) {
                NbtList nbtList = new NbtList();
                for(int i = 0; i < ((ChestBlockEntity) BE_instance).inventory.size(); ++i) {
                    ItemStack itemStack = ((ChestBlockEntity) BE_instance).inventory.get(i);
                    if (!itemStack.isEmpty()) {
                        NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.putByte("Slot", (byte)i);
                        itemStack.writeNbt(nbtCompound);
                        nbtList.add(nbtCompound);
                        ((ChestBlockEntity)BE_instance).inventory.get(i).setCount(0);
                    }
                }
                returnValue.put("Items", nbtList);
            }
        }
        else if (BE_instance instanceof BarrelBlockEntity) {
            for (int slot = 0; slot < 27; slot++) {
                ((BarrelBlockEntity)BE_instance).inventory.get(slot).setCount(0);
            }
        }
        else if (BE_instance instanceof ShulkerBoxBlockEntity) {
            for (int slot = 0; slot < 27; slot++) {
                ((ShulkerBoxBlockEntity)BE_instance).inventory.get(slot).setCount(0);
            }
        }
        else if (BE_instance instanceof DispenserBlockEntity) {
            for (int slot = 0; slot < 9; slot++) {
                ((DispenserBlockEntity)BE_instance).inventory.get(slot).setCount(0);
            }
        }
        else if (BE_instance instanceof HopperBlockEntity) {
            for (int slot = 0; slot < 5; slot++) {
                ((HopperBlockEntity)BE_instance).inventory.get(slot).setCount(0);
            }
        }
        else if (BE_instance instanceof BrewingStandBlockEntity) {
            for (int slot = 0; slot < 5; slot++) {
                ((BrewingStandBlockEntity)BE_instance).inventory.get(slot).setCount(0);
            }
        }
        else if (BE_instance instanceof AbstractFurnaceBlockEntity) {
            for (int slot = 0; slot < 3; slot++) {
                ((AbstractFurnaceBlockEntity)BE_instance).inventory.get(slot).setCount(0);
            }
        }

        return returnValue;
    }
}
