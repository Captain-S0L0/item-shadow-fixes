package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.EnderChestInventoryAccessor;
import com.terriblefriends.itemshadowfixes.access.PlayerEntityAccessor;
import com.terriblefriends.itemshadowfixes.access.PlayerInventoryAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityAccessor {
    PlayerEntity PE_instance = (PlayerEntity) (Object) this;

    public void writeCustomDataToNbtDestroyShadows(NbtCompound nbt) {
        nbt.put("Inventory",((PlayerInventoryAccessor)PE_instance.getInventory()).writeNbtDestroyShadows(new NbtList()));
        nbt.put("EnderItems", ((EnderChestInventoryAccessor)PE_instance.getEnderChestInventory()).writeNbtDestroyShadows());
    }
}
