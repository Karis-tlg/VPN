/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup.commands;

import java.io.File;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.form.NapBankForm;
import net.heavenmine.hmtopup.gui.napbank.BankListGui;
import net.heavenmine.hmtopup.modal.TopUpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class NapBankCommand
implements CommandExecutor {
    private final HMTopUp plugin;

    public NapBankCommand(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        YamlConfiguration config = null;
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration((File)configFile);
        String prefix = config.getString("prefix");
        boolean enableBank = config.getBoolean("bank.enable");
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        if (enableBank) {
            Player player = (Player)sender;
            if (player.getInventory().firstEmpty() != -1) {
                if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
                    TopUpBank topUpBank = new TopUpBank("", "", "", "", "", false);
                    this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                    BankListGui bankListGui = new BankListGui(this.plugin);
                    bankListGui.openGUI(player);
                } else {
                    FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                    if (floodgatePlayer == null) {
                        TopUpBank topUpBank = new TopUpBank("", "", "", "", "", false);
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        BankListGui bankListGui = new BankListGui(this.plugin);
                        bankListGui.openGUI(player);
                    } else {
                        NapBankForm napBankForm = new NapBankForm(this.plugin);
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
        return false;
    }
}

