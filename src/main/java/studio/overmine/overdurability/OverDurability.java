package studio.overmine.overdurability;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import studio.overmine.overdurability.commands.OverDurabilityCommand;
import studio.overmine.overdurability.controllers.DurabilityController;
import studio.overmine.overdurability.listeners.DurabilityListener;
import org.bukkit.plugin.java.JavaPlugin;
import studio.overmine.overdurability.utilities.FileConfig;

import java.util.Objects;

public class OverDurability extends JavaPlugin {

    private FileConfig configFile;
    private FileConfig languageFile;
    private Economy econ = null;

    @Override
    public void onEnable() {
        this.configFile = new FileConfig(this, "config.yml");
        this.languageFile = new FileConfig(this, "language.yml");

        if (!setupEconomy()) {
            this.getLogger().warning("Vault/Economy not found! Economy features will be disabled.");
        }

        DurabilityController durabilityController = new DurabilityController(this);

        Objects.requireNonNull(this.getCommand("overdurability"))
                .setExecutor(new OverDurabilityCommand(this, configFile, languageFile, durabilityController));
        Objects.requireNonNull(this.getCommand("overdurability"))
                .setTabCompleter(new OverDurabilityCommand(this, configFile, languageFile, durabilityController));

        this.getServer().getPluginManager().registerEvents(new DurabilityListener(this, durabilityController), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return this.econ;
    }

    public FileConfig getConfigFile() {
        return this.configFile;
    }

    public void onReload() {
        this.configFile.reload();
        this.languageFile.reload();
    }
}
