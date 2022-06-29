package athena.protection.listener;

import athena.protection.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Base64;
import java.util.Objects;

public class Staff extends BukkitRunnable implements Listener {
    private final Main plugin;
    
    public Staff(Main plugin){
        this.plugin=plugin;
    }
    @Override
    public void run() {
        if (Objects.requireNonNull(plugin.getConfig().getString("permission-grants-detect")).equalsIgnoreCase("true")){
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("permission-detect")))) {
                    if (plugin.data.getConfig("data.yml").getString("players." + p.getName()) == null) {
                        Base64.Encoder enc = Base64.getEncoder();
                        String code = plugin.getConfig().getString("default-key");
                        String encoded = enc.encodeToString(Objects.requireNonNull(code).getBytes());
                        plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".code", encoded);
                        plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".status", "true");
                        plugin.data.saveConfig("data.yml");
                    }

                }
            }
        }
    }
    @EventHandler
    public void onCommandCode(PlayerCommandPreprocessEvent e){
        if(!e.getMessage().contains("access")) {
            Player p = e.getPlayer();
            if (plugin.data.getConfig("data.yml").getString("players." + p.getName()) != null) {
                if (Objects.requireNonNull(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + e.getPlayer().getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onChatCode(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        String permission = plugin.getConfig().getString("permission-detect");
        if(p.hasPermission(Objects.requireNonNull(permission))) {
            if (Objects.requireNonNull(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockBreakCode(BlockBreakEvent e){
        Player p = e.getPlayer();
        String permission = plugin.getConfig().getString("permission-detect");
        if(p.hasPermission(Objects.requireNonNull(permission))) {
            if (Objects.requireNonNull(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDamageCode(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            String permission = plugin.getConfig().getString("permission-detect");
            if(p.hasPermission(Objects.requireNonNull(permission))) {
                if (Objects.requireNonNull(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void JoinSetCode(PlayerJoinEvent e){
        Player p = e.getPlayer();
        String permission = plugin.getConfig().getString("permission-detect");
        if(p.hasPermission(Objects.requireNonNull(permission))) {
            if(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".code") == null){
                Base64.Encoder enc = Base64.getEncoder();
                String code = plugin.getConfig().getString("default-key");
                String encoded = enc.encodeToString(Objects.requireNonNull(code).getBytes());
                plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".code", encoded);
                plugin.data.saveConfig("data.yml");
            }
            plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".status", "true");
            plugin.data.saveConfig("data.yml");
        }
    }
    @EventHandler
    public void WalkProtection(PlayerMoveEvent e){
        Player p = e.getPlayer();
        String permission = plugin.getConfig().getString("permission-detect");
        if(p.hasPermission(Objects.requireNonNull(permission))) {
            if (Objects.requireNonNull(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                e.setCancelled(true);
            }
        }
    }
    
}
