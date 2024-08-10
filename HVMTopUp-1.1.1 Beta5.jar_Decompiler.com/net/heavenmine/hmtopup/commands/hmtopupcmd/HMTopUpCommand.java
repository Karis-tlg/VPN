/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabExecutor
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.commands.hmtopupcmd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.commands.hmtopupcmd.ChoosePriceCard;
import net.heavenmine.hmtopup.commands.hmtopupcmd.ChooseTypeBank;
import net.heavenmine.hmtopup.commands.hmtopupcmd.ChooseTypeCard;
import net.heavenmine.hmtopup.commands.hmtopupcmd.HMTopUpGive;
import net.heavenmine.hmtopup.commands.hmtopupcmd.HMTopUpHistory;
import net.heavenmine.hmtopup.database.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class HMTopUpCommand
implements CommandExecutor,
TabExecutor {
    private final HMTopUp plugin;

    public HMTopUpCommand(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        YamlConfiguration config = null;
        DataManager dataManager = new DataManager(this.plugin);
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration((File)configFile);
        String prefix = config.getString("prefix");
        if (args.length > 0) {
            if (sender.hasPermission("hmtopup.reload") && args[0].equals("reload")) {
                File naptheomoc;
                File naplandau;
                File card;
                this.plugin.reloadConfig();
                File bank = new File(this.plugin.getDataFolder(), "bank.yml");
                if (bank.exists()) {
                    YamlConfiguration.loadConfiguration((File)bank);
                }
                if ((card = new File(this.plugin.getDataFolder(), "card.yml")).exists()) {
                    YamlConfiguration.loadConfiguration((File)card);
                }
                if ((naplandau = new File(this.plugin.getDataFolder(), "naplandau.yml")).exists()) {
                    YamlConfiguration.loadConfiguration((File)naplandau);
                }
                if ((naptheomoc = new File(this.plugin.getDataFolder(), "naptheomoc.yml")).exists()) {
                    YamlConfiguration.loadConfiguration((File)naptheomoc);
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aReload config th\u00e0nh c\u00f4ng !")));
                return false;
            }
            if (args[0].equals("top")) {
                Map<String, Double> topUsers = dataManager.getTopNapThe();
                sender.sendMessage("----Top Nap The----");
                topUsers.forEach((username, totalAmount) -> sender.sendMessage("Username: " + username + " - T\u1ed5ng n\u1ea1p: " + totalAmount + " VND"));
                return false;
            }
            if (args[0].equals("history") || args[0].equals("log") || args[0].equals("logs")) {
                HMTopUpHistory.onCommand(sender, args, (Configuration)config, dataManager, this.plugin);
            } else if (args[0].equals("give")) {
                HMTopUpGive.onCommand(sender, args, (Configuration)config, dataManager, (Plugin)this.plugin);
            } else if (args[0].equals("choosecard")) {
                ChooseTypeCard.onCommand(sender, args, (Configuration)config, dataManager, this.plugin);
            } else if (args[0].equals("chooseprice")) {
                ChoosePriceCard.onCommand(sender, args, (Configuration)config, dataManager, this.plugin);
            } else if (args[0].equals("choosebank")) {
                ChooseTypeBank.onCommand(sender, args, (Configuration)config, dataManager, this.plugin);
            }
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1 && args[0].equalsIgnoreCase("choosecard")) {
            completions.add("Viettel");
            completions.add("Vinaphone");
            completions.add("Mobifone");
            completions.add("Zing");
            completions.add("Vietnamobile");
            completions.add("Vcoin");
            completions.add("Gate");
            completions.add("Garena");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("chooseprice")) {
            completions.add("10.000");
            completions.add("20.000");
            completions.add("30.000");
            completions.add("50.000");
            completions.add("100.000");
            completions.add("200.000");
            completions.add("300.000");
            completions.add("500.000");
            completions.add("1.000.000");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("choosebank")) {
            completions.add("MBBank");
            completions.add("VCB");
            completions.add("ACB");
            completions.add("BIDV");
            completions.add("Vietinbank");
            completions.add("Seabank");
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            completions.add("10.000");
            completions.add("20.000");
            completions.add("30.000");
            completions.add("50.000");
            completions.add("100.000");
            completions.add("200.000");
            completions.add("300.000");
            completions.add("500.000");
            completions.add("1.000.000");
        }
        return completions;
    }
}

