package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin {
    @Unique private ItemStack autoiteminactionbar$droppedStack = ItemStack.EMPTY;

    @Inject(method = "drop(Z)V", at = @At("HEAD"))
    private void autoiteminactionbar$captureDrop(boolean dropAll, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        autoiteminactionbar$droppedStack = player.getItemInHand(InteractionHand.MAIN_HAND).copy();
    }

    @Inject(method = "drop(Z)V", at = @At("RETURN"))
    private void autoiteminactionbar$queueDrop(boolean dropAll, CallbackInfo ci) {
        if (!autoiteminactionbar$droppedStack.isEmpty()) {
            RefillHooks.common((ServerPlayer) (Object) this, InteractionHand.MAIN_HAND, autoiteminactionbar$droppedStack);
        }
        autoiteminactionbar$droppedStack = ItemStack.EMPTY;
    }
}
