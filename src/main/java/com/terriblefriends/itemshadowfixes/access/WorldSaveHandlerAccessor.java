package com.terriblefriends.itemshadowfixes.access;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public interface WorldSaveHandlerAccessor {
    void savePlayerDataFromNbt(PlayerEntity player, NbtCompound nbtCompound);
}
