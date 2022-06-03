package com.terriblefriends.itemshadowfixes.mixin;

import com.mojang.logging.LogUtils;
import com.terriblefriends.itemshadowfixes.access.PlayerEntityAccessor;
import com.terriblefriends.itemshadowfixes.access.WorldSaveHandlerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Util;
import net.minecraft.world.WorldSaveHandler;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(WorldSaveHandler.class)
public class WorldSaveHandlerMixin implements WorldSaveHandlerAccessor {
    @Shadow @Final private File playerDataDir;
    @Shadow @Final private static Logger LOGGER;

    public void savePlayerDataDestroyShadows(PlayerEntity player) {
        try {
            NbtCompound nbtCompound = player.writeNbt(new NbtCompound());
            ((PlayerEntityAccessor)player).writeCustomDataToNbtDestroyShadows(nbtCompound);
            File file = File.createTempFile(player.getUuidAsString() + "-", ".dat", playerDataDir);
            NbtIo.writeCompressed(nbtCompound, file);
            File file2 = new File(playerDataDir, player.getUuidAsString() + ".dat");
            File file3 = new File(playerDataDir, player.getUuidAsString() + ".dat_old");
            Util.backupAndReplace(file2, file, file3);
        } catch (Exception var6) {
            LOGGER.warn("Failed to save player data for {}", player.getName().getString());
            StackTraceElement[] stack = var6.getStackTrace();
            for (StackTraceElement e : stack) {
                LOGGER.warn(e.toString());
            }
        }
    }
}
