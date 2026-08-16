<p align="center">
  <a href="https://github.com/tantaihaha4487/AutoItemInActionbar-fabric/actions/workflows/build-mod.yml" style="text-decoration:none;"><img src="https://github.com/tantaihaha4487/AutoItemInActionbar-fabric/actions/workflows/build-mod.yml/badge.svg" alt="Build Status"></a>
  <a href="https://github.com/tantaihaha4487/AutoItemInActionbar-fabric/releases/latest" style="text-decoration:none;"><img src="https://img.shields.io/github/v/release/tantaihaha4487/AutoItemInActionbar-fabric?style=flat-square" alt="Latest Release"></a>
</p>

# AutoItemInActionbar

Server-authoritative Fabric automatic item refill for Minecraft Java Edition.

When an action consumes, breaks, or empties a held item, AutoItemInActionbar
finds the first matching stack in the player's inventory and refills or swaps
the affected hand. Matching uses the complete item data, so customized items
are not silently replaced by a different variant of the same item.

## Preview

### Simple Refill

![Preview](https://github.com/tantaihaha4487/assets/raw/refs/heads/main/AutoItemInActionbar/preview.gif)

### Smart Bucket Refill

![Bucket Handle Preview](https://github.com/tantaihaha4487/assets/raw/refs/heads/main/AutoItemInActionbar/bucket_handle.gif)

## Features

- **Automatic Item Refill:** Automatically refills your hand with a matching
  item from your inventory after you use it. This works for:
    - Placing blocks
    - Dropping the last item in the main hand
    - Breaking tools or other held items
    - Throwing eggs
    - Throwing splash or lingering potions
    - Consuming food
- **Smart Bucket Handling:**
    - Gives you a new empty bucket when you fill one with water, lava, fish, or
      another supported fluid container.
    - Gives you a new matching filled bucket after you empty one.
- **Food & Drink Refill:** After consuming items that leave a remainder, such
  as stews, soups, milk, potions, or honey bottles, it swaps in a fresh matching
  item when one is available.
- **Action Bar Feedback:** Shows a confirmation and plays a sound only for the
  player whose hand was refilled.
- **Zero Configuration:** Drop the mod in and it works without a configuration
  file or commands.
- **Hold Shift to Disable:** Hold Shift while the queued refill executes to
  temporarily prevent the refill.
- **Exact Item Matching:** Custom names, enchantments, damage, potion contents,
  and other item data components must match.
- **Dedicated-Server Friendly:** Players can connect without installing the mod
  on their client.
- **No Telemetry:** The Fabric port does not include bStats or other metrics.

## How to Use

1. Install Fabric Loader `0.19.3` or newer for Minecraft `26.1.2`.
2. Install the matching Fabric API in the server's `mods` directory.
3. Put `AutoItemInActionbar-1.2.0.jar` in the same `mods` directory.
4. Start or restart the server. The mod is now active for all players.

Players can connect with an unmodified client. For single-player or an
integrated server, install the mod and Fabric API in the client instance.

### Normal use

Keep a second stack of the same item somewhere in the player's inventory, then
perform a supported action normally. The refill is processed one server tick
after the action succeeds.

| Action | Refill behavior |
| --- | --- |
| Place a block | Refills the hand whose block was placed when that hand becomes empty. |
| Drop the last main-hand item | Refills the selected main-hand slot. |
| Break a held item | Replaces the empty hand after the item breaks. |
| Consume food | Refills the hand after the food stack is consumed. |
| Use a container | Swaps the consumed or emptied container with a matching source. |
| Throw an egg or splash/lingering potion | Refills the main hand after the projectile impacts. |

## Matching and exclusions

The source stack must match the used stack with
`ItemStack.isSameItemSameComponents`. Item type alone is not enough: custom
names, enchantments, damage, potion contents, and other data components must
also match.

The refill is skipped when:

- the player is sneaking when the queued refill executes;
- the player is in Creative or Spectator mode;
- the used item is armor;
- no matching source exists; or
- a common refill action leaves the hand occupied.

The first matching source in inventory order is selected. Container remainder
actions use a swap, preserving the empty or consumed item in the source slot.

## Server behavior

The logical server owns the refill queue and all inventory changes. Requests are
captured at the action seams and processed by the server tick event, keeping
dedicated-server gameplay authoritative and safe for clients without the mod.

Successful refills send an action-bar confirmation and the level-up sound only
to the player whose inventory changed.

## License

AutoItemInActionbar is licensed under the [MIT License](LICENSE).
