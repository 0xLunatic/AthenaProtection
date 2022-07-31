package athena.protection.command;

import athena.protection.Main;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

public class AthenaProtectionCommand implements CommandExecutor {
    private final Main plugin;

    public HashMap<Player, Integer> fail = new HashMap<>();


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

                        if(args[0].equalsIgnoreCase(defaultKey)) {
                            if (Objects.requireNonNull(plugin.data.getConfig("data.yml").getString("players." + p.getName() + "." + p.getUniqueId() + ".status")).equalsIgnoreCase("true")) {
                                plugin.data.getConfig("data.yml").set("players." + p.getName() + "." + p.getUniqueId() + ".status", "false");
                                p.sendMessage("§aAccess Granted!");
                            }
                        }
                        else if (!args[0].equals(defaultKey)) {
                            fail.merge(p, 1, Integer::sum);
                            if (fail.get(p) >= 3){
                                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), "", null, "Console");
                                p.kickPlayer("");
                            }
                        }
                        plugin.data.saveConfig("data.yml");
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
                    if (Objects.requireNonNull(plugin.getConfig().getString("auto-update")).equalsIgnoreCase("true")) {
                        for (String name : Objects.requireNonNull(plugin.data.getConfig("data.yml").getConfigurationSection("players")).getKeys(false)) {
                            for (String uuid : Objects.requireNonNull(plugin.data.getConfig("data.yml").getConfigurationSection("players." + name)).getKeys(false)) {
                                String code = plugin.data.getConfig("data.yml").getString("players." + name + "." + uuid + ".code");
                                String defaultKey = plugin.getConfig().getString("default-key");

                                Base64.Encoder enc = Base64.getEncoder();
                                String encoded = enc.encodeToString(Objects.requireNonNull(defaultKey).getBytes());

                                if (!Objects.requireNonNull(code).equalsIgnoreCase(encoded)) {
                                    plugin.data.getConfig("data.yml").set("players." + name + "." + uuid + ".code", encoded);
                                    plugin.data.saveConfig("data.yml");
                                    System.out.println("Some of Account Default-Key Updated!");

                                }
                            }
                        }
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
