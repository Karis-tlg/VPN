/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 */
package net.heavenmine.hmtopup.commands.hmtopupcmd;

import java.io.File;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.form.HistoryForm;
import net.heavenmine.hmtopup.gui.history.HistoryGui;
import net.heavenmine.hmtopup.modal.HistoryGuiCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HMTopUpHistory {
    public static void onCommand(CommandSender sender, String[] args, Configuration config, DataManager dataManager, HMTopUp plugin) {
        String prefix = config.getString("prefix");
        YamlConfiguration message = null;
        File messageFile = new File(plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String onlyPlayer = message.getString("only-player");
        String noPermission = message.getString("no-permission");
        if (!(sender instanceof Player)) {
            plugin.getLogger().warning(onlyPlayer);
            return;
        }
        Player player = (Player)sender;
        HistoryGuiCache historyGuiCache = new HistoryGuiCache("", false, "");
        plugin.saveHistoryGuiCache(String.valueOf(player.getUniqueId()), historyGuiCache);
        if (Utils.isBedRockPlayer(player)) {
            HistoryForm historyForm = new HistoryForm(plugin, dataManager);
            if (args.length == 1) {
                historyForm.openForm(player, player.getName(), false);
            } else if (args.length == 2) {
                if (sender.hasPermission("hmtopup.history.other")) {
                    historyForm.openForm(player, args[1], args[1].equalsIgnoreCase("all"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + noPermission)));
                }
            }
        } else {
            HistoryGui historyGui = new HistoryGui(plugin, dataManager);
            if (args.length == 1) {
                historyGui.openGUI(player, player.getName(), false);
            } else if (args.length == 2) {
                if (sender.hasPermission("hmtopup.history.other")) {
                    historyGui.openGUI(player, args[1], args[1].equalsIgnoreCase("all"));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + noPermission)));
                }
            }
        }
    }
}

