/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
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
import net.heavenmine.hmtopup.form.NapTheForm;
import net.heavenmine.hmtopup.gui.napthe.TypeCardGui;
import net.heavenmine.hmtopup.modal.TopUpCard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class NapTheCommand
implements CommandExecutor {
    private final HMTopUp plugin;

    public NapTheCommand(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String onlyPlayer = message.getString("only-player");
        if (sender instanceof Player) {
            TypeCardGui typeCardGui = new TypeCardGui(this.plugin);
            Player player = (Player)sender;
            if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
                TopUpCard topUpCard = new TopUpCard("", "", "", "");
                this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                typeCardGui.openGUI(player);
            } else {
                FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                if (floodgatePlayer == null) {
                    TopUpCard topUpCard = new TopUpCard("", "", "", "");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    typeCardGui.openGUI(player);
                } else {
                    NapTheForm napTheForm = new NapTheForm(this.plugin);
                    napTheForm.openForm(player);
                }
            }
        } else {
            this.plugin.getLogger().warning(onlyPlayer);
        }
        return false;
    }
}

