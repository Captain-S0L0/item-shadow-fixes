package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.PlayerInventoryAccessor;
import com.terriblefriends.itemshadowfixes.access.WorldSaveHandlerAccessor;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.UUID;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Shadow @Final private WorldSaveHandler saveHandler;
    @Shadow @Final private Map<UUID, ServerStatHandler> statisticsMap;
    @Shadow @Final private Map<UUID, PlayerAdvancementTracker> advancementTrackers;


    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/server/PlayerManager;savePlayerData(Lnet/minecraft/server/network/ServerPlayerEntity;)V"),method="remove")
    private void removeShadowsPlayerInventory(PlayerManager instance, ServerPlayerEntity player) {
        /*savePlayerData(player);
        for (int slot = 0; slot <= 40; slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            stack.decrement(stack.getCount());
        }
        for (int slot = 0; slot < 4; slot++) {
            instance.getInventory().setStack(slot+100, ItemStack.EMPTY);
        }
        instance.getInventory().setStack(150, ItemStack.EMPTY);*/
        ((WorldSaveHandlerAccessor)saveHandler).savePlayerDataDestroyShadows(player);
        ServerStatHandler serverStatHandler = statisticsMap.get(player.getUuid());
        if (serverStatHandler != null) {
            serverStatHandler.save();
        }

        PlayerAdvancementTracker playerAdvancementTracker = advancementTrackers.get(player.getUuid());
        if (playerAdvancementTracker != null) {
            playerAdvancementTracker.save();
        }
    }
}
