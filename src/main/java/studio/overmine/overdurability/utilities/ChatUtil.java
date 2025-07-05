package studio.overmine.overdurability.utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author Risas
 * @date 03-07-2025
 * @discord https://risas.me/discord
 */
public class ChatUtil {

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(translate(text));
    }
}
