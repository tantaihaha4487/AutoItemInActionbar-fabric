package net.thanachot.autoiteminactionbar.refill;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Pure selection logic shared by refill implementations.
 *
 * <p>The selector deliberately knows nothing about Minecraft inventory or item
 * classes. Callers provide the definition of an occupied slot and the exact
 * item-matching rule they need (for example, matching all data components).
 */
public final class RefillSelection {
    private RefillSelection() {
    }

    /**
     * Returns the first occupied slot matching {@code trigger}, in list order,
     * while excluding the slot currently used by the player.
     *
     * @param slots       inventory slots in their authoritative scan order
     * @param excludedSlot slot that must never be selected, or {@code -1} when
     *                     no slot is excluded
     * @param trigger     item description that the source must match
     * @param present     predicate identifying a non-empty slot
     * @param matches     predicate comparing a candidate with the trigger
     * @param <T>         caller-defined item representation
     * @return the selected slot index, or an empty result when no source exists
     */
    public static <T> OptionalInt firstMatchingSlot(
            List<? extends T> slots,
            int excludedSlot,
            T trigger,
            Predicate<? super T> present,
            BiPredicate<? super T, ? super T> matches) {
        Objects.requireNonNull(slots, "slots");
        Objects.requireNonNull(present, "present");
        Objects.requireNonNull(matches, "matches");

        for (int slot = 0; slot < slots.size(); slot++) {
            if (slot == excludedSlot) {
                continue;
            }

            T candidate = slots.get(slot);
            if (present.test(candidate) && matches.test(candidate, trigger)) {
                return OptionalInt.of(slot);
            }
        }

        return OptionalInt.empty();
    }
}
