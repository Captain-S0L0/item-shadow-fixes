package com.terriblefriends.itemshadowfixes.mixin.blockentity;

import com.terriblefriends.itemshadowfixes.access.BlockEntityAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;

@Mixin(ThreadedAnvilChunkStorage.class)
public class ThreadedAnvilChunkStorageMixin {
    @Shadow @Final private PointOfInterestStorage pointOfInterestStorage;
    @Shadow private boolean isLevelChunk(ChunkPos pos) {return false;}
    @Shadow @Final private static Logger LOGGER;
    @Shadow @Final ServerWorld world;
    ThreadedAnvilChunkStorage TACS_instance = (ThreadedAnvilChunkStorage) (Object) this;
    @Shadow private byte mark(ChunkPos pos, ChunkStatus.ChunkType type) {return 0;}

    /*@Inject(at=@At("HEAD"),method="Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;save(Lnet/minecraft/world/chunk/Chunk;)Z", cancellable = true)
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
    }*/

    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/world/ChunkSerializer;serialize(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;)Lnet/minecraft/nbt/NbtCompound;"),method="save(Lnet/minecraft/world/chunk/Chunk;)Z")
    private NbtCompound serializeDestroyShadows(ServerWorld world, Chunk chunk) {
        NbtCompound returnValue = ChunkSerializer.serialize(world,chunk);

        if (!this.world.getChunkManager().isChunkLoaded(chunk.getPos().x, chunk.getPos().z)) {
            NbtList nbtList2 = new NbtList();

            for (BlockPos blockPos : chunk.getBlockEntityPositions()) {
                //nbtCompound3 = chunk.getPackedBlockEntityNbt(blockPos);

                if (chunk instanceof WorldChunk) {
                    BlockEntity blockEntity = chunk.getBlockEntity(blockPos);
                    NbtCompound nbtCompound;
                    if (blockEntity != null && !blockEntity.isRemoved()) {
                        //nbtCompound = blockEntity.createNbtWithIdentifyingData();

                        nbtCompound = ((BlockEntityAccessor)blockEntity).createNbtWithIdentifyingDataDestroyShadows();

                        //
                        nbtCompound.putBoolean("keepPacked", false);
                    } else {
                        nbtCompound = chunk.blockEntityNbts.get(blockPos);
                        if (nbtCompound != null) {
                            nbtCompound = nbtCompound.copy();
                            nbtCompound.putBoolean("keepPacked", true);
                        }
                    }
                    if (nbtCompound != null) {
                        nbtList2.add(nbtCompound);
                    }
                }
                //
                if (chunk instanceof ProtoChunk) {
                    BlockEntity blockEntity = chunk.getBlockEntity(blockPos);
                    NbtCompound nbtCompound;
                    nbtCompound =  blockEntity != null ?
                            blockEntity.createNbtWithIdentifyingData()



                            : chunk.blockEntityNbts.get(blockPos);
                    if (nbtCompound != null) {
                        nbtList2.add(nbtCompound);
                    }
                }

            }

            returnValue.put("block_entities", nbtList2);
        }

        return returnValue;
    }
}
