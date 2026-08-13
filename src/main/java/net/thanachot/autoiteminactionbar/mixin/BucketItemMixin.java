package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
abstract class BucketItemMixin {
    @Unique private ItemStack autoiteminactionbar$usedStack = ItemStack.EMPTY;

    @Inject(method = "use", at = @At("HEAD"))
    private void autoiteminactionbar$captureBucket(
            Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player instanceof ServerPlayer) {
            autoiteminactionbar$usedStack = player.getItemInHand(hand).copy();
        }
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void autoiteminactionbar$queueBucket(
            Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (player instanceof ServerPlayer serverPlayer
                && !autoiteminactionbar$usedStack.isEmpty()
                && cir.getReturnValue().consumesAction()) {
            RefillHooks.remainder(serverPlayer, hand, autoiteminactionbar$usedStack);
        }
        autoiteminactionbar$usedStack = ItemStack.EMPTY;
    }
}
