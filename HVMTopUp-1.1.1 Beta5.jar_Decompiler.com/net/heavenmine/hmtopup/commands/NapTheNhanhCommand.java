/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabExecutor
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.heavenmine.hmtopup.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.cardmanager.OtherSiteManager;
import net.heavenmine.hmtopup.cardmanager.TheSieuTocManager;
import net.heavenmine.hmtopup.database.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;

public class NapTheNhanhCommand
implements CommandExecutor,
TabExecutor {
    private final HMTopUp plugin;
    private final DataManager dataManager;

    public NapTheNhanhCommand(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        TheSieuTocManager theSieuTocManager = new TheSieuTocManager(this.plugin);
        OtherSiteManager otherSiteManager = new OtherSiteManager(this.plugin, this.dataManager);
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = null;
        config = YamlConfiguration.loadConfiguration((File)configFile);
        String prefix = config.getString("prefix");
        if (args.length == 4) {
            String loaiThe = args[0];
            String menhGia = args[1];
            String seri = args[2];
            String maThe = args[3];
            YamlConfiguration cardConfig = null;
            File cardFile = new File(this.plugin.getDataFolder(), "card.yml");
            cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
            boolean enableOtherSite = cardConfig.getBoolean("other-site.enable");
            if (enableOtherSite) {
                try {
                    otherSiteManager.onTopUp(loaiThe, menhGia, seri, maThe, sender);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theSieuTocManager.onTopUp(loaiThe, menhGia, seri, maThe, sender);
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aVui l\u00f2ng s\u1eed d\u1ee5ng: /napthenhanh <loaithe> <menhgia> <seri> <mathe> !")));
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add("Viettel");
            completions.add("Vinaphone");
            completions.add("Mobifone");
            completions.add("Zing");
            completions.add("Vietnamobile");
            completions.add("Vcoin");
            completions.add("Gate");
            completions.add("Garena");
        } else if (args.length == 2) {
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

