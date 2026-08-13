package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
    @Unique private ItemStack autoiteminactionbar$preBreakStack = ItemStack.EMPTY;

    @Inject(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;)V", at = @At("HEAD"))
    private void autoiteminactionbar$captureBreak(
            int amount, LivingEntity entity, InteractionHand hand, CallbackInfo ci) {
        if (entity instanceof ServerPlayer) {
            autoiteminactionbar$preBreakStack = ((ItemStack) (Object) this).copy();
        }
    }

    @Inject(method = "hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;)V", at = @At("RETURN"))
    private void autoiteminactionbar$queueBreak(
            int amount, LivingEntity entity, InteractionHand hand, CallbackInfo ci) {
        if (entity instanceof ServerPlayer serverPlayer
                && !autoiteminactionbar$preBreakStack.isEmpty()
                && ((ItemStack) (Object) this).isEmpty()) {
            RefillHooks.common(serverPlayer, hand, autoiteminactionbar$preBreakStack);
        }
        autoiteminactionbar$preBreakStack = ItemStack.EMPTY;
    }
}
