package studio.overmine.overdurability.listeners;

import org.bukkit.event.player.PlayerItemMendEvent;
import studio.overmine.overdurability.OverDurability;
import studio.overmine.overdurability.controllers.DurabilityController;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

/**
 * @author Risas
 * @date 03-07-2025
 * @discord https://risas.me/discord
 */
public class DurabilityListener implements Listener {

    private final OverDurability plugin;
    private final DurabilityController durabilityController;

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
}
