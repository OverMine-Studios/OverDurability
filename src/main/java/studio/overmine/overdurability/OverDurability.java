package studio.overmine.overdurability;

import studio.overmine.overdurability.commands.OverDurabilityCommand;
import studio.overmine.overdurability.controllers.DurabilityController;
import studio.overmine.overdurability.listeners.DurabilityListener;
import org.bukkit.plugin.java.JavaPlugin;
import studio.overmine.overdurability.utilities.FileConfig;

import java.util.Objects;

public class OverDurability extends JavaPlugin {

    private FileConfig languageFile;

    @Override
    public void onEnable() {
        this.languageFile = new FileConfig(this, "language.yml");

        DurabilityController durabilityController = new DurabilityController(this);

        Objects.requireNonNull(this.getCommand("overdurability"))
                .setExecutor(new OverDurabilityCommand(this, languageFile, durabilityController));
        Objects.requireNonNull(this.getCommand("overdurability"))
                .setTabCompleter(new OverDurabilityCommand(this, languageFile, durabilityController));

        this.getServer().getPluginManager().registerEvents(new DurabilityListener(this, durabilityController), this);
    }

    public void onReload() {
        this.languageFile.reload();
    }
}
