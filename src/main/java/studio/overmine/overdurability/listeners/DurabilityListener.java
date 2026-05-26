package studio.overmine.overdurability.listeners;

import org.bukkit.event.player.PlayerItemMendEvent;
import studio.overmine.overdurability.OverDurability;
import studio.overmine.overdurability.controllers.DurabilityController;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * @author Risas
 * @date 03-07-2025
 * @discord https://risas.me/discord
 */
public class DurabilityListener implements Listener {

    private final OverDurability plugin;
    private final DurabilityController durabilityController;

    private static final Set<String> REPAIR_COMMANDS = Set.of(
            "fix", "repair", "essentials:fix", "essentials:repair", "ref", "reparar"
    );

    public DurabilityListener(OverDurability plugin, DurabilityController durabilityController) {
        this.plugin = plugin;
        this.durabilityController = durabilityController;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> durabilityController.updateDurability(event.getItem()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemMending(PlayerItemMendEvent event) {
        Bukkit.getScheduler().runTask(plugin, () -> durabilityController.updateDurability(event.getItem()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().substring(1).toLowerCase();
        String[] args = message.split(" ");
        if (args.length == 0) return;

        String command = args[0];
        if (REPAIR_COMMANDS.contains(command)) {
            Player player = event.getPlayer();
            // Wait 1 tick for external plugin (like Essentials) to process the command
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) return;
                for (ItemStack item : player.getInventory().getContents()) {
                    durabilityController.updateDurability(item);
                }
            }, 1L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        durabilityController.updateDurability(item);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();

        durabilityController.updateDurability(currentItem);
        durabilityController.updateDurability(cursorItem);
    }
}
