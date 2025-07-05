package studio.overmine.overdurability.commands;

import org.bukkit.command.TabCompleter;
import studio.overmine.overdurability.OverDurability;
import studio.overmine.overdurability.controllers.DurabilityController;
import studio.overmine.overdurability.utilities.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import studio.overmine.overdurability.utilities.FileConfig;

import java.util.List;

/**
 * @author Risas
 * @date 03-07-2025
 * @discord https://risas.me/discord
 */
public class OverDurabilityCommand implements CommandExecutor, TabCompleter {

    private final OverDurability plugin;
    private final FileConfig languageFile;
    private final DurabilityController durabilityController;

    public OverDurabilityCommand(
            OverDurability plugin,
            FileConfig languageFile,
            DurabilityController durabilityController) {
        this.plugin = plugin;
        this.languageFile = languageFile;
        this.durabilityController = durabilityController;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            languageFile.getStringList("durability-message.help").forEach(message ->
                    ChatUtil.sendMessage(sender, message
                            .replace("%label%", label)));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "apply" -> {
                if (!(sender instanceof Player player)) {
                    ChatUtil.sendMessage(sender, "&cYou must be a player to use this command.");
                    return true;
                }

                ItemStack itemStack = player.getInventory().getItemInMainHand();

                if (itemStack.getType().isAir()) {
                    ChatUtil.sendMessage(player, languageFile.getString("durability-message.no-item"));
                    return true;
                }

                if (!durabilityController.canProcessDurability(itemStack)) {
                    languageFile.getStringList("durability-message.error")
                            .forEach(message -> ChatUtil.sendMessage(player, message));
                    return true;
                }

                ChatUtil.sendMessage(player, languageFile.getString("durability-message.applied"));
            }
            case "reload" -> {
                plugin.onReload();
                ChatUtil.sendMessage(sender, "&aOverDurability has been reloaded.");
            }
            default -> ChatUtil.sendMessage(sender, "&cUnknown command. Use /" + label + " for help.");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("apply", "reload");
        }
        return null;
    }
}
