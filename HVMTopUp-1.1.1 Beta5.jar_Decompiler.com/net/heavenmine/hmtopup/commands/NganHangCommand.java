/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.heavenmine.hmtopup.commands;

import java.io.File;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class NganHangCommand
implements CommandExecutor {
    private final HMTopUp plugin;
    private final DataManager dataManager;

    public NganHangCommand(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = null;
        config = YamlConfiguration.loadConfiguration((File)configFile);
        List message = config.getStringList("bank.message");
        String bankRatio = config.getString("bank.ratio");
        String bankChar = config.getString("bank.noidung");
        YamlConfiguration bankConfig = null;
        File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
        bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
        String stkVCB = bankConfig.getString("VCB.acc_no");
        String nameVCB = bankConfig.getString("VCB.acc_name");
        String stkACB = bankConfig.getString("ACB.acc_no");
        String nameACB = bankConfig.getString("ACB.acc_name");
        String stkMB = bankConfig.getString("mbBank.acc_no");
        String nameMB = bankConfig.getString("mbBank.acc_name");
        String stkBidv = bankConfig.getString("Bidv.acc_no");
        String nameBidv = bankConfig.getString("Bidv.acc_name");
        String stkVietinbank = bankConfig.getString("Vietinbank.acc_no");
        String nameVietinbank = bankConfig.getString("Vietinbank.acc_name");
        String stkSeabank = bankConfig.getString("Seabank.acc_no");
        String nameSeabank = bankConfig.getString("Seabank.acc_name");
        int IDPlayer = this.dataManager.getUserIdByUsername(sender.getName());
        String bankString = bankChar + " " + IDPlayer;
        for (String item : message) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)item.replace("%char%", bankString).replace("%mb_acc_no%", stkMB).replace("%vcb_acc_no%", stkVCB).replace("%acb_acc_no%", stkACB).replace("%bidv_acc_no%", stkBidv).replace("%vtb_acc_no%", stkVietinbank).replace("%see_acc_no%", stkSeabank).replace("%mb_acc_name%", nameMB).replace("%vcb_acc_name%", nameVCB).replace("%acb_acc_name%", nameACB).replace("%bidv_acc_name%", nameBidv).replace("%vtb_acc_name%", nameVietinbank).replace("%see_acc_name%", nameSeabank).replace("%bank_ratio%", bankRatio)));
        }
        return false;
    }
}

