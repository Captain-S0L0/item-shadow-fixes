package com.terriblefriends.itemshadowfixes.mixin;

import net.minecraft.block.entity.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {
    @Shadow @Final private PointOfInterestStorage pointOfInterestStorage;
    @Shadow private boolean isLevelChunk(ChunkPos pos) {return false;}
    @Shadow @Final private static Logger LOGGER;
    @Shadow @Final ServerWorld world;
    ThreadedAnvilChunkStorage TACS_instance = (ThreadedAnvilChunkStorage) (Object) this;
    @Shadow private byte mark(ChunkPos pos, ChunkStatus.ChunkType type) {return 0;}

    @Inject(at=@At("HEAD"),method="Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;save(Lnet/minecraft/world/chunk/Chunk;)Z", cancellable = true)
    private void testHook(Chunk chunk, CallbackInfoReturnable<Boolean> cir) {
        pointOfInterestStorage.saveChunk(chunk.getPos());
        if (!chunk.needsSaving()) {
            cir.setReturnValue(false);
        } else {
            chunk.setNeedsSaving(false);
            ChunkPos chunkPos = chunk.getPos();

            try {
                ChunkStatus chunkStatus = chunk.getStatus();
                if (chunkStatus.getChunkType() != ChunkStatus.ChunkType.LEVELCHUNK) {
                    if (this.isLevelChunk(chunkPos)) {
                        cir.setReturnValue(false);
                    }

                    if (chunkStatus == ChunkStatus.EMPTY && chunk.getStructureStarts().values().stream().noneMatch(StructureStart::hasChildren)) {
                        cir.setReturnValue(false);
                    }
                }

                this.world.getProfiler().visit("chunkSave");
                NbtCompound nbtCompound = ChunkSerializer.serialize(this.world, chunk);

                if (!this.world.getChunkManager().isChunkLoaded(chunk.getPos().x, chunk.getPos().z)) {

                    for (BlockPos blockPos : chunk.getBlockEntityPositions()) {
                        BlockEntity blockEntity = chunk.getBlockEntity(blockPos);
                        if (blockEntity != null && !blockEntity.isRemoved()) {
                            if (blockEntity instanceof ChestBlockEntity) {
                                for (int slot = 0; slot < 27; slot++) {
                                    ((ChestBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                            else if (blockEntity instanceof BarrelBlockEntity) {
                                for (int slot = 0; slot < 27; slot++) {
                                    ((BarrelBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                            else if (blockEntity instanceof ShulkerBoxBlockEntity) {
                                for (int slot = 0; slot < 27; slot++) {
                                    ((ShulkerBoxBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                            else if (blockEntity instanceof DispenserBlockEntity) {
                                for (int slot = 0; slot < 9; slot++) {
                                    ((DispenserBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                            else if (blockEntity instanceof HopperBlockEntity) {
                                for (int slot = 0; slot < 5; slot++) {
                                    ((HopperBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                            else if (blockEntity instanceof BrewingStandBlockEntity) {
                                for (int slot = 0; slot < 5; slot++) {
                                    ((BrewingStandBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                            else if (blockEntity instanceof AbstractFurnaceBlockEntity) {
                                for (int slot = 0; slot < 3; slot++) {
                                    ((AbstractFurnaceBlockEntity)blockEntity).inventory.get(slot).setCount(0);
                                }
                            }
                        }
                    }

                    //System.out.println("deleting shadows");
                }

                TACS_instance.setNbt(chunkPos, nbtCompound);
                this.mark(chunkPos, chunkStatus.getChunkType());
                cir.setReturnValue(true);
            } catch (Exception var5) {
                LOGGER.error("Failed to save chunk {},{}", new Object[]{chunkPos.x, chunkPos.z, var5});
                cir.setReturnValue(false);
            }
        }

        if (cir.isCancellable()) {cir.cancel();}
    }
}
