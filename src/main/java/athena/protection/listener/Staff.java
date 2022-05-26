package athena.protection.listener;

import athena.protection.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Base64;
import java.util.Objects;

public class Staff implements Listener {
    private Main plugin;
    
    public Staff(Main plugin){
        this.plugin=plugin;
    }
    @EventHandler
    public void onCommandCode(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("essentials.jail")) {
            if (Objects.requireNonNull(plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                if (!e.getMessage().contains("access")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onChatCode(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("essentials.jail")) {
            if (Objects.requireNonNull(plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockBreakCode(BlockBreakEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("essentials.jail")) {
            if (Objects.requireNonNull(plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onDamageCode(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            if(p.hasPermission("essentials.jail")) {
                if (Objects.requireNonNull(plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void JoinSetCode(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("essentials.jail")){
            if(plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".code") == null){
                Base64.Encoder enc = Base64.getEncoder();
                String code = "ProtectionStaff@CN2022";
                String encoded = enc.encodeToString(code.getBytes());
                plugin.data.getConfig().set("players." + p.getName() + "." + p.getUniqueId() + ".code", encoded);
                plugin.data.saveConfig();

            }
            plugin.data.getConfig().set("players." + p.getName() + "." + p.getUniqueId() + ".status", "true");
            plugin.data.saveConfig();
        }
    }
    @EventHandler
    public void WalkProtection(PlayerMoveEvent e){
        Player p = e.getPlayer();
        if(p.hasPermission("essentials.jail")) {
            if (Objects.requireNonNull(plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                e.setCancelled(true);
            }
        }
    }
    
}
