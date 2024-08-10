/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.entity.Player
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup.commands.hmtopupcmd;

import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.form.NapTheForm;
import net.heavenmine.hmtopup.modal.TopUpCard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class ChooseTypeCard {
    public static void onCommand(CommandSender sender, String[] args, Configuration config, DataManager dataManager, HMTopUp plugin) {
        String prefix = config.getString("prefix");
        if (args.length == 2) {
            Player player = (Player)sender;
            String loaiThe = args[1];
            TopUpCard topUpCard = new TopUpCard(loaiThe, "", "", "");
            if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
                plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
            } else {
                FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                if (floodgatePlayer == null) {
                    plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                } else {
                    NapTheForm napTheForm = new NapTheForm(plugin);
                    napTheForm.openForm(player);
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aUse &e/hmtopup choosecard <loaithe>")));
        }
    }
}

