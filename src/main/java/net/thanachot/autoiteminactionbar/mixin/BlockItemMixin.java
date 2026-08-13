package net.thanachot.autoiteminactionbar.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerPlayer;
import net.thanachot.autoiteminactionbar.refill.RefillHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
abstract class BlockItemMixin {
    @Unique private ServerPlayer autoiteminactionbar$player;
    @Unique private InteractionHand autoiteminactionbar$hand;
    @Unique private ItemStack autoiteminactionbar$usedStack = ItemStack.EMPTY;

    @Inject(method = "place", at = @At("HEAD"))
    private void autoiteminactionbar$capturePlacement(
            BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            autoiteminactionbar$player = serverPlayer;
            autoiteminactionbar$hand = context.getHand();
            autoiteminactionbar$usedStack = context.getItemInHand().copy();
        }
    }

    @Inject(method = "place", at = @At("RETURN"))
    private void autoiteminactionbar$queuePlacement(
            BlockPlaceContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (autoiteminactionbar$player != null && cir.getReturnValue().consumesAction()) {
            RefillHooks.common(autoiteminactionbar$player, autoiteminactionbar$hand, autoiteminactionbar$usedStack);
        }
        autoiteminactionbar$player = null;
        autoiteminactionbar$usedStack = ItemStack.EMPTY;
    }
}
