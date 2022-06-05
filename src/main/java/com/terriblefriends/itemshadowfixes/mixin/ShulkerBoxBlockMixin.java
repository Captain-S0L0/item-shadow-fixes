package com.terriblefriends.itemshadowfixes.mixin;

import com.terriblefriends.itemshadowfixes.access.BlockEntityAccessor;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {
    @Redirect(at=@At(value="INVOKE",target="Lnet/minecraft/block/entity/BlockEntity;setStackNbt(Lnet/minecraft/item/ItemStack;)V"),method="onBreak")
    public void setStackNbtDestroyShadows(BlockEntity instance, ItemStack stack) {
        System.out.println("nerd2");
        BlockItem.setBlockEntityNbt(stack, instance.getType(), ((BlockEntityAccessor)instance).createNbtShulkerDestroyShadows());
        return;
    }
}
