package net.thanachot.autoiteminactionbar.refill;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class RefillHooks {
    private RefillHooks() {
    }

    public static void common(ServerPlayer player, InteractionHand hand, ItemStack usedStack) {
        RefillCoordinator.schedule(player, hand, usedStack, RefillMode.COMMON);
    }

    public static void remainder(ServerPlayer player, InteractionHand hand, ItemStack usedStack) {
        RefillCoordinator.schedule(player, hand, usedStack, RefillMode.REMAINDER_SWAP);
    }

    public static void consumed(ServerPlayer player, InteractionHand hand, ItemStack usedStack) {
        if (hasRemainderBehavior(usedStack)) {
            remainder(player, hand, usedStack);
        } else if (usedStack.has(DataComponents.FOOD)) {
            common(player, hand, usedStack);
        }
    }

    private static boolean hasRemainderBehavior(ItemStack stack) {
        return stack.is(Items.MILK_BUCKET)
                || stack.is(Items.SUSPICIOUS_STEW)
                || stack.is(Items.MUSHROOM_STEW)
                || stack.is(Items.RABBIT_STEW)
                || stack.is(Items.BEETROOT_SOUP)
                || stack.is(Items.POTION)
                || stack.is(Items.HONEY_BOTTLE);
    }
}
