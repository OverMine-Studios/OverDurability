package studio.overmine.overdurability.controllers;

import studio.overmine.overdurability.OverDurability;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Risas
 * @date 03-07-2025
 * @discord https://risas.me/discord
 */
public class DurabilityController {

    private final NamespacedKey BASE_LORE_KEY;

    public DurabilityController(OverDurability plugin) {
        this.BASE_LORE_KEY = new NamespacedKey(plugin, "od_base_lore");
    }

    public boolean canProcessDurability(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return false;

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) return false;

        boolean hasDurabilityPlaceholder = lore.stream()
                .anyMatch(line -> line.contains("%od_durability%") || line.contains("%od_max_durability%"));
        if (!hasDurabilityPlaceholder) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(BASE_LORE_KEY, PersistentDataType.STRING, String.join("\n", lore));

        int maxDurabilityValue = itemStack.getType().getMaxDurability();

        String maxDurability = String.valueOf(maxDurabilityValue);
        String durability = String.valueOf(maxDurabilityValue - damageable.getDamage());

        List<String> newLore = lore.stream()
                .map(line -> line
                        .replace("%od_durability%", durability)
                        .replace("%od_max_durability%", maxDurability))
                .collect(Collectors.toList());

        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
        return true;
    }

    public void updateDurability(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) return;

        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof Damageable damageable)) return;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(BASE_LORE_KEY, PersistentDataType.STRING)) return;

        int maxDurabilityValue = itemStack.getType().getMaxDurability();
        int durabilityValue = maxDurabilityValue - damageable.getDamage();
        if (maxDurabilityValue == durabilityValue) return;

        String maxDurability = String.valueOf(maxDurabilityValue);
        String durability = String.valueOf(durabilityValue);

        String baseLoreData = data.get(BASE_LORE_KEY, PersistentDataType.STRING);
        if (baseLoreData == null) return;

        List<String> lore = Arrays.asList(baseLoreData.split("\n"));
        List<String> newLore = lore.stream()
                .map(line -> line
                        .replace("%od_durability%", durability)
                        .replace("%od_max_durability%", maxDurability))
                .collect(Collectors.toList());

        meta.setLore(newLore);
        itemStack.setItemMeta(meta);
    }
}
