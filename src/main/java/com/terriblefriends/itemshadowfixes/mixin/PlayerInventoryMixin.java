package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.PlayerInventoryAccessor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements PlayerInventoryAccessor {
    PlayerInventory PI_instance = (PlayerInventory) (Object) this;

    public NbtList writeNbtDestroyShadows(NbtList nbtList) {
        int i;
        NbtCompound nbtCompound;
        for(i = 0; i < PI_instance.main.size(); ++i) {
            if (!(PI_instance.main.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                (PI_instance.main.get(i)).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                PI_instance.main.get(i).setCount(0);
            }
        }

        for(i = 0; i < PI_instance.armor.size(); ++i) {
            if (!(PI_instance.armor.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)(i + 100));
                (PI_instance.armor.get(i)).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                PI_instance.armor.get(i).setCount(0);
            }
        }

        for(i = 0; i < PI_instance.offHand.size(); ++i) {
            if (!(PI_instance.offHand.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)(i + 150));
                (PI_instance.offHand.get(i)).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                PI_instance.offHand.get(i).setCount(0);
            }
        }

        return nbtList;
    }
}
