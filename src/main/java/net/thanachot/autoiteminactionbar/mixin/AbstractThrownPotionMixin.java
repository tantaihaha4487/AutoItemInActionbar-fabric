package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractThrownPotion.class)
abstract class AbstractThrownPotionMixin {
    @Inject(method = "onHit", at = @At("TAIL"))
    private void autoiteminactionbar$queuePotion(HitResult hitResult, CallbackInfo ci) {
        AbstractThrownPotion potion = (AbstractThrownPotion) (Object) this;
        if (potion.getOwner() instanceof ServerPlayer serverPlayer) {
            RefillHooks.common(serverPlayer, InteractionHand.MAIN_HAND, potion.getItem().copy());
        }
    }
}
