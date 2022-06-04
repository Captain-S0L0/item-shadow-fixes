package com.terriblefriends.itemshadowfixes.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.world.ServerEntityManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerEntityManager.class)
public class ServerEntityManagerMixin {
    /*@Shadow @Final private Long2ObjectMap<ServerEntityManager.Status> managedStatuses = new Long2ObjectOpenHashMap();

    @Inject(at=@At("HEAD"),method="Lnet/minecraft/server/world/ServerEntityManager;unload(J)Z")
    private void unloadDestroyShadows(long chunkPos, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = this.trySave(chunkPos, (entity) -> {
            entity.streamPassengersAndSelf().forEach(this::unload);
        });
        if (!bl) {
            cir.setReturnValue(false);
        } else {
            this.managedStatuses.remove(chunkPos);
            return true;
        }
    }*/
}
