package com.terriblefriends.itemshadowfixes.mixin.entity;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.SharedConstants;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.storage.ChunkDataList;
import net.minecraft.world.storage.EntityChunkDataAccess;
import net.minecraft.world.storage.StorageIoWorker;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityChunkDataAccess.class)
public class EntityChunkDataAccessMixin {
    @Shadow @Final private LongSet emptyChunks;
    @Shadow @Final private StorageIoWorker dataLoadWorker;
    @Shadow private static void putChunkPos(NbtCompound chunkNbt, ChunkPos pos) {}
    @Shadow @Final private static Logger LOGGER;

    @Inject(at=@At("HEAD"),method="Lnet/minecraft/world/storage/EntityChunkDataAccess;writeChunkData(Lnet/minecraft/world/storage/ChunkDataList;)V",cancellable = true)
    private void writeChunkDataDestroyShadows(ChunkDataList<Entity> dataList, CallbackInfo ci) {
        ChunkPos chunkPos = dataList.getChunkPos();
        if (dataList.isEmpty()) {
            if (this.emptyChunks.add(chunkPos.toLong())) {
                this.dataLoadWorker.setResult(chunkPos, null);
            }

        } else {
            NbtList nbtList = new NbtList();
            dataList.stream().forEach((entity) -> {
                NbtCompound nbtCompound = new NbtCompound();
                if (entity.saveNbt(nbtCompound)) {
                    nbtList.add(nbtCompound);
                }

            });
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("DataVersion", SharedConstants.getGameVersion().getWorldVersion());
            nbtCompound.put("Entities", nbtList);
            putChunkPos(nbtCompound, chunkPos);
            this.dataLoadWorker.setResult(chunkPos, nbtCompound).exceptionally((ex) -> {
                LOGGER.error("Failed to store chunk {}", chunkPos, ex);
                return null;
            });
            this.emptyChunks.remove(chunkPos.toLong());
        }

        if (ci.isCancellable()) {ci.cancel();}
    }

}
