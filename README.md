# AutoItemInActionbar

AutoItemInActionbar automatically refills a used item in the active hand from
an identical stack in the player's inventory.

## Requirements

- Minecraft 26.1.2
- Fabric Loader 0.19.3 or newer
- Fabric API 0.155.2+26.1.2 or newer for Minecraft 26.1.2
- Java 25 or newer

The mod is server-authoritative. Install it in a dedicated server's `mods/`
directory together with Fabric API; players can connect with an unmodified
client. It can also be installed on a client for single-player/integrated-server
use.

## Features

- Refills blocks after placement.
- Refills the selected slot after dropping its last item.
- Refills eggs and splash or lingering potions after impact.
- Replaces broken held tools or items.
- Refills food after the held stack is consumed.
- Swaps container remainders for matching buckets, potions, stews, soups, milk,
  and honey bottles.
- Supports main-hand and off-hand actions where the underlying action exposes
  the used hand.
- Shows an action-bar confirmation and plays a sound after a refill.
- Hold Shift while the refill executes to suppress it.

Matching includes all item data components. Custom names, enchantments, damage,
potion contents, and other data must match, preventing a customized item from
being replaced by a different variant of the same item type.

Creative and spectator players are ignored. There is no configuration and no
telemetry.

## Building

```bash
./gradlew build
```

The production JAR is written to `build/libs/AutoItemInActionbar-1.2.0.jar`.
