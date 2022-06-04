package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.PlayerEntityAccessor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.integrated.IntegratedPlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IntegratedPlayerManager.class)
public class IntegratedPlayerManagerMixin {
    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/server/network/ServerPlayerEntity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"),method="savePlayerData")
    private NbtCompound singlePlayerFix(ServerPlayerEntity instance, NbtCompound nbtCompound) {
        instance.writeNbt(nbtCompound);
        ((PlayerEntityAccessor)instance).writeCustomDataToNbtDestroyShadows(nbtCompound);
        return nbtCompound;
    }
}
