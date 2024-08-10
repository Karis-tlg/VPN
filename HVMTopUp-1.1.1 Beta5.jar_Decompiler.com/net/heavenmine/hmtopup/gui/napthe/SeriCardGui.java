/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.gui.napthe;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.gui.napthe.PinCardGui;
import net.heavenmine.hmtopup.modal.TopUpCard;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SeriCardGui {
    private static HMTopUp plugin;

    public SeriCardGui(HMTopUp plugin) {
        SeriCardGui.plugin = plugin;
    }

    public void openGui(Player player) {
        YamlConfiguration message = null;
        File messageFile = new File(plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String seriText = message.getString("please-type-seri");
        new AnvilGUI.Builder().onClose(stateSnapshot -> {
            PinCardGui pinCardGui = new PinCardGui(plugin);
            pinCardGui.openGui(player);
        }).onClick((slot, stateSnapshot) -> {
            if (slot != 2) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().equalsIgnoreCase("")) {
                TopUpCard topUpCard = plugin.getTopUpCache(player.getUniqueId().toString());
                String seri = stateSnapshot.getText();
                topUpCard.setSeri(seri);
                plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                return Arrays.asList(AnvilGUI.ResponseAction.close());
            }
            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(seriText));
        }).text(seriText).title(seriText).plugin((Plugin)plugin).open(player);
    }
}

