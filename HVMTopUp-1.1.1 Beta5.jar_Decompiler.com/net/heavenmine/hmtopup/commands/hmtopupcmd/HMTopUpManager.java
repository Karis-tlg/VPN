/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 */
package net.heavenmine.hmtopup.commands.hmtopupcmd;

import java.util.List;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.LogNapThe;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HMTopUpManager {
    public static void getLogNapThe(String name, CommandSender sender, DataManager dataManager, int page, String prefix) {
        List<LogNapThe> logNapTheList = dataManager.getNapTheHistory(name, page);
        if (logNapTheList.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aNo history found")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&a=======Logs of &e" + name + "&a=======")));
            int index = 1;
            for (LogNapThe logNapThe : logNapTheList) {
                sender.sendMessage(index + " - " + Utils.DateFormat(logNapThe.getDate()) + " Type:" + logNapThe.getLoaithe() + " Amount:" + logNapThe.getMenhgia() + " Seri:" + logNapThe.getSeri() + " Code:" + logNapThe.getCode() + " Status:" + (logNapThe.getStatus().equalsIgnoreCase("00") ? "Thanh cong" : (logNapThe.getStatus().equalsIgnoreCase("-9") ? "Dang xu ly" : "That bai")));
                ++index;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)("&a=======End logs of &e" + name + "&a=======")));
        }
    }

    public static void getAllLogNapThe(CommandSender sender, DataManager dataManager, int page, String prefix) {
        List<LogNapThe> logNapTheList = dataManager.getAllNapTheHistory(page);
        if (logNapTheList.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aNo history found")));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&a=======Logs nap the&a======="));
            int index = 1;
            for (LogNapThe logNapThe : logNapTheList) {
                sender.sendMessage(index + " - Player: " + logNapThe.getUsername() + " Date: " + Utils.DateFormat(logNapThe.getDate()) + " Type:" + logNapThe.getLoaithe() + " Amount:" + logNapThe.getMenhgia() + " Seri:" + logNapThe.getSeri() + " Code:" + logNapThe.getCode() + " Status:" + (logNapThe.getStatus().equalsIgnoreCase("00") ? "Thanh cong" : (logNapThe.getStatus().equalsIgnoreCase("-9") ? "Dang xu ly" : "That bai")));
                ++index;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&a=======End logs nap the&a======="));
        }
    }
}

