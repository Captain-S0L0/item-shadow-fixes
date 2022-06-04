package com.terriblefriends.itemshadowfixes.mixin;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/block/entity/BlockEntity;createNbt()Lnet/minecraft/nbt/NbtCompound;"),method="createNbtWithIdentifyingData")

}
