/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.commands.hmtopupcmd;

import java.io.File;
import java.util.Date;
import java.util.List;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.reward.NapDauManager;
import net.heavenmine.hmtopup.reward.NapTheoMocManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class HMTopUpGive {
    public static void onCommand(CommandSender sender, String[] args, Configuration config, DataManager dataManager, Plugin plugin) {
        String prefix = config.getString("prefix");
        if (sender.hasPermission("hmtopup.command.give")) {
            if (args.length == 3) {
                try {
                    int menhgia = Integer.parseInt(args[2].replace(".", ""));
                    Double ratio = plugin.getConfig().getDouble("topup." + menhgia + ".ratio");
                    boolean event = plugin.getConfig().getBoolean("event.enable");
                    if (event) {
                        Double eventBonus = plugin.getConfig().getDouble("event.bonus");
                        Double bankBonus = plugin.getConfig().getDouble("bank.bonus");
                        ratio = ratio + ratio * eventBonus + ratio * bankBonus;
                    }
                    List listCmd = plugin.getConfig().getStringList("topup." + menhgia + ".cmd");
                    for (String item : listCmd) {
                        Double finalRatio = ratio;
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", args[1]).replace("%amount%", finalRatio.intValue() + "")));
                    }
                    dataManager.createLogNapThe(args[1], "give", args[2].replace(".", ""), "", "", "give", new Date(), "00");
                    YamlConfiguration napdau = null;
                    File napdauFile = new File(plugin.getDataFolder(), "naplandau.yml");
                    napdau = YamlConfiguration.loadConfiguration((File)napdauFile);
                    boolean enableNapdau = napdau.getBoolean("main.enable");
                    if (enableNapdau) {
                        NapDauManager napDauManager = new NapDauManager(dataManager, plugin);
                        napDauManager.checkNapDau(args[1], String.valueOf(menhgia));
                    }
                    NapTheoMocManager napTheoMocManager = new NapTheoMocManager(dataManager, plugin);
                    napTheoMocManager.checkAllTime(args[1]);
                    napTheoMocManager.checkDay(args[1]);
                    napTheoMocManager.checkWeek(args[1]);
                    napTheoMocManager.checkMonth(args[1]);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&7Da add thanh cong !")));
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&a" + args[2] + " &ckh\u00f4ng ph\u1ea3i l\u00e0 m\u1ed9t s\u1ed1 nguy\u00ean h\u1ee3p l\u1ec7.")));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aUse &e/hmtopup give <player> <amount>")));
            }
        }
    }
}

