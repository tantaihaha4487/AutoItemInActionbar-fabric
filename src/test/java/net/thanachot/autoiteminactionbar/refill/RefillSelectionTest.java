package net.thanachot.autoiteminactionbar.refill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

class RefillSelectionTest {
    @Test
    void selectsFirstMatchingOccupiedSlotInInventoryOrder() {
        Item trigger = item("stone", Map.of("custom_name", "wall"));
        List<Item> slots = List.of(
                item("dirt", Map.of()),
                Item.EMPTY,
                item("stone", Map.of("custom_name", "wall")),
                item("stone", Map.of("custom_name", "wall")));

        OptionalInt result = select(slots, 0, trigger);

        assertEquals(OptionalInt.of(2), result);
    }

    @Test
    void excludesHeldSlotEvenWhenItMatches() {
        Item trigger = item("stone", Map.of());
        List<Item> slots = List.of(trigger, item("dirt", Map.of()), item("stone", Map.of()));

        OptionalInt result = select(slots, 0, trigger);

        assertEquals(OptionalInt.of(2), result);
    }

    @Test
    void matchingCanRequireAllItemData() {
        Item trigger = item("potion", Map.of("potion", "healing"));
        List<Item> slots = List.of(
                item("potion", Map.of("potion", "swiftness")),
                item("potion", Map.of("potion", "healing")));

        OptionalInt result = select(slots, -1, trigger);

        assertEquals(OptionalInt.of(1), result);
    }

    @Test
    void returnsEmptyWhenNoOccupiedMatchingSourceExists() {
        Item trigger = item("arrow", Map.of());
        List<Item> slots = List.of(Item.EMPTY, item("stone", Map.of()), item("arrow", Map.of("variant", "spectral")));

        OptionalInt result = select(slots, -1, trigger);

        assertTrue(result.isEmpty());
    }

    @Test
    void supportsNoExcludedSlotWithNegativeIndex() {
        Item trigger = item("torch", Map.of());
        List<Item> slots = List.of(item("torch", Map.of()));

        OptionalInt result = select(slots, -1, trigger);

        assertEquals(OptionalInt.of(0), result);
    }

    private static OptionalInt select(List<Item> slots, int excludedSlot, Item trigger) {
        return RefillSelection.firstMatchingSlot(
                slots,
                excludedSlot,
                trigger,
                item -> !item.empty,
                (candidate, expected) -> candidate.id.equals(expected.id)
                        && candidate.components.equals(expected.components));
    }

    private static Item item(String id, Map<String, String> components) {
        return new Item(id, components, false);
    }

    private record Item(String id, Map<String, String> components, boolean empty) {
        private static final Item EMPTY = new Item("", Map.of(), true);
    }
}
