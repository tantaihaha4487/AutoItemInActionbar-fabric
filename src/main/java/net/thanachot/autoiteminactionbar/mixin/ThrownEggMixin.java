package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEgg.class)
abstract class ThrownEggMixin {
    @Inject(method = "onHit", at = @At("TAIL"))
    private void autoiteminactionbar$queueEgg(HitResult hitResult, CallbackInfo ci) {
        ThrownEgg egg = (ThrownEgg) (Object) this;
        if (egg.getOwner() instanceof ServerPlayer serverPlayer) {
            RefillHooks.common(serverPlayer, InteractionHand.MAIN_HAND, egg.getItem().copy());
        }
    }
}
