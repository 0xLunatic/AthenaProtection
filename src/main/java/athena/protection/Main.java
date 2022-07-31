package athena.protection;

import athena.protection.command.AthenaProtectionCommand;
import athena.protection.data.DataManager;
import athena.protection.listener.Staff;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        data = new DataManager(this);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", true);
        }else{
            saveDefaultConfig();
        }

        System.out.println("§aAthena Protection System Enabled!");

        Bukkit.getPluginManager().registerEvents(new Staff(this), this);
        new Staff(this).runTaskTimer(this, 20, 20);

        Objects.requireNonNull(getCommand("athenaprotection")).setExecutor(new AthenaProtectionCommand(this));
        Objects.requireNonNull(getCommand("access")).setExecutor(new AthenaProtectionCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public FileConfiguration getConfigFile() {
        return config;
    }
}
