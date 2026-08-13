package net.thanachot.autoiteminactionbar.refill;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public final class RefillCoordinator {
    private static final Queue<RefillRequest> PENDING = new ArrayDeque<>();
    private static final Set<RequestKey> PENDING_KEYS = new HashSet<>();

    private RefillCoordinator() {
    }

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(RefillCoordinator::processPending);
    }

    public static void schedule(ServerPlayer player, InteractionHand hand, ItemStack usedStack, RefillMode mode) {
        if (usedStack.isEmpty() || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        RequestKey key = new RequestKey(player.getUUID(), hand, mode);
        if (!PENDING_KEYS.add(key)) {
            return;
        }

        PENDING.add(new RefillRequest(
                serverLevel.getServer(), player.getUUID(), hand, usedStack.copyWithCount(1), mode,
                serverLevel.getServer().getTickCount() + 1, key));
    }

    private static void processPending(MinecraftServer server) {
        int pendingCount = PENDING.size();
        for (int i = 0; i < pendingCount; i++) {
            RefillRequest request = PENDING.remove();
            if (request.server() != server || server.getTickCount() < request.targetTick()) {
                PENDING.add(request);
                continue;
            }

            PENDING_KEYS.remove(request.key());
            ServerPlayer player = server.getPlayerList().getPlayer(request.playerId());
            if (player != null) {
                refill(player, request);
            }
        }
    }

    private static void refill(ServerPlayer player, RefillRequest request) {
        if (player.isShiftKeyDown() || player.isCreative() || player.isSpectator()) {
            return;
        }
        if (isArmor(request.usedStack())) {
            return;
        }

        Inventory inventory = player.getInventory();
        int targetSlot = request.hand() == InteractionHand.MAIN_HAND
                ? inventory.getSelectedSlot()
                : Inventory.SLOT_OFFHAND;
        int sourceSlot = findMatchingSlot(inventory, request.usedStack(), targetSlot);
        if (sourceSlot < 0) {
            return;
        }

        ItemStack currentHandStack = player.getItemInHand(request.hand());
        ItemStack sourceStack = inventory.getItem(sourceSlot);
        if (request.mode() == RefillMode.COMMON) {
            if (!currentHandStack.isEmpty()) {
                return;
            }
            player.setItemInHand(request.hand(), sourceStack.copy());
            inventory.setItem(sourceSlot, ItemStack.EMPTY);
        } else {
            player.setItemInHand(request.hand(), sourceStack.copy());
            inventory.setItem(sourceSlot, currentHandStack.copy());
        }

        sendFeedback(player);
    }

    private static boolean isArmor(ItemStack stack) {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        return path.endsWith("_helmet")
                || path.endsWith("_chestplate")
                || path.endsWith("_leggings")
                || path.endsWith("_boots");
    }

    static int findMatchingSlot(Inventory inventory, ItemStack usedStack, int excludedSlot) {
        List<ItemStack> slots = IntStream.range(0, inventory.getContainerSize())
                .mapToObj(inventory::getItem)
                .toList();
        return RefillSelection.firstMatchingSlot(
                slots,
                excludedSlot,
                usedStack,
                stack -> !stack.isEmpty(),
                ItemStack::isSameItemSameComponents)
                .orElse(-1);
    }

    private static void sendFeedback(ServerPlayer player) {
        MutableComponent prefix = Component.literal("(i) ")
                .withStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0xFFD700)));
        MutableComponent name = Component.literal("Auto Item In Actionbar")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x55EA80)));
        player.sendOverlayMessage(prefix.append(name));
        player.playSound(SoundEvents.PLAYER_LEVELUP, 0.6F, 0.7F);
    }

    private record RequestKey(UUID playerId, InteractionHand hand, RefillMode mode) {
    }

    private record RefillRequest(
            MinecraftServer server,
            UUID playerId,
            InteractionHand hand,
            ItemStack usedStack,
            RefillMode mode,
            int targetTick,
            RequestKey key) {
    }
}
