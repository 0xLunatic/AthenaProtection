package athena.protection.command;

import athena.protection.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Base64;
import java.util.Objects;

public class AthenaProtectionCommand implements CommandExecutor {
    private final Main plugin;

    public AthenaProtectionCommand(Main plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("access")){
            String permission = plugin.getConfig().getString("permission-detect");
            if(sender.hasPermission(Objects.requireNonNull(permission))) {
                if (sender instanceof Player) {
                    if (args.length > 0) {
                        Player p = (Player) sender;
                        Base64.Decoder dec = Base64.getDecoder();

                        String defaultKey = plugin.getConfig().getString("default-key");
                        String code = plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".code");
                        String decoded = new String(dec.decode(code));

                        if(args[0].equalsIgnoreCase(decoded)){
                            plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".status", "false");
                            p.sendMessage("§aAccess Granted!");
                        }
                        else if(args[0].equalsIgnoreCase(defaultKey)){
                            plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".status", "false");
                            p.sendMessage("§aAccess Granted!");
                        }

                        plugin.data.saveConfig("data.yml");
                    }
                }
            }
        }
        else if(label.equalsIgnoreCase("setaccess")){
            String permission = plugin.getConfig().getString("permission-detect");
            if(sender.hasPermission(Objects.requireNonNull(permission))) {
                if (sender instanceof Player) {
                    if(args.length > 0){
                        Player p = (Player) sender;
                        Base64.Encoder enc = Base64.getEncoder();
                        String encoded = enc.encodeToString(args[0].getBytes());
                        plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".code", encoded);
                        plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".status", "true");
                        plugin.data.saveConfig("data.yml");
                        p.sendMessage("§aSucessfully changed your access!");
                    }else{
                        sender.sendMessage("§cPlease enter the code!");
                    }
                }
            }
        }
        else if(label.equalsIgnoreCase("athenaprotection")){
            String permission = plugin.getConfig().getString("permission-detect");
            if(!sender.hasPermission(Objects.requireNonNull(permission))) {
                sender.sendMessage("§cYou don't have permission to do this!");
                return true;
            }
            if(args.length == 0 ){
                sender.sendMessage("§aUse /athenaprotection reload!");
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                if(sender.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("reload.permission")))){
                    for(String msg : plugin.getConfig().getStringList("reload.message")) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                msg));
                    }
                    plugin.reloadConfig();
                }else{
                    sender.sendMessage("§cYou don't have permission to do this!");
                }
            }
        }
        return false;
    }
}
