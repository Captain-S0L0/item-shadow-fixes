package com.terriblefriends.itemshadowfixes.access;

import net.minecraft.nbt.NbtCompound;

public interface PlayerEntityAccessor {
    void writeCustomDataToNbtDestroyShadows(NbtCompound nbt);
}
