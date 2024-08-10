/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup.commands.hmtopupcmd;

import java.io.File;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.form.NapBankForm;
import net.heavenmine.hmtopup.gui.napbank.AmountBankGui;
import net.heavenmine.hmtopup.modal.TopUpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class ChooseTypeBank {
    public static void onCommand(CommandSender sender, String[] args, Configuration config, DataManager dataManager, HMTopUp plugin) {
        String prefix = config.getString("prefix");
        YamlConfiguration message = null;
        File messageFile = new File(plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        if (args.length == 2) {
            Player player = (Player)sender;
            String typeBank = args[1];
            boolean enableBank = config.getBoolean("bank.enable");
            TopUpBank topUpBank = new TopUpBank(typeBank, "", "", "", "", false);
            plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
            if (enableBank) {
                AmountBankGui amountBankGui = new AmountBankGui(plugin);
                if (player.getInventory().firstEmpty() != -1) {
                    if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
                        plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        amountBankGui.openGUI(player);
                    } else {
                        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                        if (floodgatePlayer == null) {
                            plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                            amountBankGui.openGUI(player);
                        } else {
                            NapBankForm napBankForm = new NapBankForm(plugin);
                            napBankForm.openForm(player);
                        }
                    }
                } else {
                    String fullInv = message.getString("full-inv");
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + fullInv)));
                }
            } else {
                String disableBank = message.getString("disable-bank");
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank)));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aUse &e/hmtopup choosebank <loaibank>")));
        }
    }
}

