package athena.protection;

import athena.protection.command.AthenaProtectionCommand;
import athena.protection.data.DataManager;
import athena.protection.listener.Staff;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();
    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        data = new DataManager(this);
        config.options().copyDefaults(true);
        saveConfig();
        System.out.println("§aAthena Protection System Enabled!");
        Bukkit.getPluginManager().registerEvents(new Staff(this), this);

        if (Objects.requireNonNull(this.getConfig().getString("development.locked-server")).equalsIgnoreCase(Bukkit.getServer().getName())) {
            Bukkit.getPluginManager().registerEvents(this, this);
            Bukkit.broadcastMessage("§4[APS] §cDevelopment Server Detected! Locked All Player!");
            Bukkit.broadcastMessage("§4[APS] §cYou can whitelist player in config.yml!");
        }
        Objects.requireNonNull(getCommand("athenaprotection")).setExecutor(new AthenaProtectionCommand(this));
        Objects.requireNonNull(getCommand("access")).setExecutor(new AthenaProtectionCommand(this));
        Objects.requireNonNull(getCommand("setaccess")).setExecutor(new AthenaProtectionCommand(this));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getConfigFile() {
        return config;
    }

    @EventHandler
    public void whitelistStaff(PlayerJoinEvent e) {
        if (!this.getConfig().getStringList("development.whitelisted").contains(e.getPlayer().getName())) {
            e.getPlayer().kickPlayer("[APS] You don't have any permission to join this server!");
        }
    }
}
