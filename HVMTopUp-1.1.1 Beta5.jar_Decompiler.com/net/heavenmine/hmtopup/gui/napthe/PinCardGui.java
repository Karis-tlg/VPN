/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.gui.napthe;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.cardmanager.OtherSiteManager;
import net.heavenmine.hmtopup.cardmanager.TheSieuTocManager;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.TopUpCard;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PinCardGui {
    private static HMTopUp plugin;

    public PinCardGui(HMTopUp plugin) {
        PinCardGui.plugin = plugin;
    }

    public void openGui(Player player) {
        TopUpCard topUpCard = plugin.getTopUpCache(player.getUniqueId().toString());
        TheSieuTocManager theSieuTocManager = new TheSieuTocManager(plugin);
        OtherSiteManager otherSiteManager = new OtherSiteManager(plugin, new DataManager(plugin));
        YamlConfiguration cardConfig = null;
        File cardFile = new File(plugin.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        boolean enableOtherSite = cardConfig.getBoolean("other-site.enable");
        YamlConfiguration message = null;
        File messageFile = new File(plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String pinText = message.getString("please-type-pin");
        new AnvilGUI.Builder().onClose(stateSnapshot -> {
            if (enableOtherSite) {
                try {
                    otherSiteManager.onTopUp(topUpCard.getLoaiThe(), topUpCard.getMenhGia(), topUpCard.getSeri(), topUpCard.getCode(), (CommandSender)player);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theSieuTocManager.onTopUp(topUpCard.getLoaiThe(), topUpCard.getMenhGia(), topUpCard.getSeri(), topUpCard.getCode(), (CommandSender)player);
            }
        }).onClick((slot, stateSnapshot) -> {
            if (slot != 2) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().equalsIgnoreCase("")) {
                String pin = stateSnapshot.getText();
                topUpCard.setCode(pin);
                return Arrays.asList(AnvilGUI.ResponseAction.close());
            }
            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(pinText));
        }).text(pinText).title(pinText).plugin((Plugin)plugin).open(player);
    }
}

