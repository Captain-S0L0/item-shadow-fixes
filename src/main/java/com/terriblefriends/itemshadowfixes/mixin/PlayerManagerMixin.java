package com.terriblefriends.itemshadowfixes.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow protected void savePlayerData(ServerPlayerEntity player) {};

    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/server/PlayerManager;savePlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)V"),method="remove")
    private void removeShadowsPlayerInventory(PlayerManager instance, ServerPlayerEntity player) {
        savePlayerData(player);
        for (int slot = 0; slot <= 40; slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            stack.decrement(stack.getCount());
        }
        /*for (int slot = 0; slot < 4; slot++) {
            instance.getInventory().setStack(slot+100, ItemStack.EMPTY);
        }
        instance.getInventory().setStack(150, ItemStack.EMPTY);*/
    }
}
