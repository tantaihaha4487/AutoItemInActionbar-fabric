package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {
    @Shadow public abstract boolean isUsingItem();
    @Shadow public abstract InteractionHand getUsedItemHand();
    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);

    @Unique private ItemStack autoiteminactionbar$consumedStack = ItemStack.EMPTY;
    @Unique private InteractionHand autoiteminactionbar$consumedHand;

    @Inject(method = "completeUsingItem", at = @At("HEAD"))
    private void autoiteminactionbar$captureConsumption(CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayer && isUsingItem()) {
            autoiteminactionbar$consumedHand = getUsedItemHand();
            autoiteminactionbar$consumedStack = getItemInHand(autoiteminactionbar$consumedHand).copy();
        }
    }

    @Inject(method = "completeUsingItem", at = @At("RETURN"))
    private void autoiteminactionbar$queueConsumption(CallbackInfo ci) {
        if ((Object) this instanceof ServerPlayer serverPlayer && !autoiteminactionbar$consumedStack.isEmpty()) {
            RefillHooks.consumed(serverPlayer, autoiteminactionbar$consumedHand, autoiteminactionbar$consumedStack);
        }
        autoiteminactionbar$consumedStack = ItemStack.EMPTY;
        autoiteminactionbar$consumedHand = null;
    }
}
