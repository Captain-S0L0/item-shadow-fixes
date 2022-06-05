package com.terriblefriends.itemshadowfixes.access;

import net.minecraft.nbt.NbtCompound;

public interface BlockEntityAccessor {
    NbtCompound createNbtWithIdentifyingDataDestroyShadows();
    NbtCompound createNbtShulkerDestroyShadows();
}
