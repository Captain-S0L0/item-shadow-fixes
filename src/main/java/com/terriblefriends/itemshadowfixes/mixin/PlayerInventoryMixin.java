package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.PlayerInventoryAccessor;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements PlayerInventoryAccessor {
    PlayerInventory PI_instance = (PlayerInventory) (Object) this;

    public NbtList writeNbtDestroyShadows(NbtList nbtList) {
        System.out.println("PlayerInventoryMixin");
        int i;
        NbtCompound nbtCompound;
        for(i = 0; i < PI_instance.main.size(); ++i) {
            if (!(PI_instance.main.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                (PI_instance.main.get(i)).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                PI_instance.main.get(i).decrement(PI_instance.main.get(i).getCount());
            }
        }

        for(i = 0; i < PI_instance.armor.size(); ++i) {
            if (!(PI_instance.armor.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)(i + 100));
                (PI_instance.armor.get(i)).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                PI_instance.armor.get(i).decrement(PI_instance.armor.get(i).getCount());
            }
        }

        for(i = 0; i < PI_instance.offHand.size(); ++i) {
            if (!(PI_instance.offHand.get(i)).isEmpty()) {
                nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)(i + 150));
                (PI_instance.offHand.get(i)).writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
                PI_instance.offHand.get(i).decrement(PI_instance.offHand.get(i).getCount());
            }
        }

        return nbtList;
    }
}
