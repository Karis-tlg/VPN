/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.gui.napbank;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.TopUpBank;
import net.heavenmine.hmtopup.qrcode.QRCodeManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AmountBankGui {
    private static HMTopUp plugin;

    public AmountBankGui(HMTopUp plugin) {
        AmountBankGui.plugin = plugin;
    }

    public void openGUI(Player player) {
        TopUpBank topUpBank = plugin.getBankCache(player.getUniqueId().toString());
        YamlConfiguration message = null;
        File messageFile = new File(plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String qrCode = message.getString("receive-qr-code");
        String amountEmpty = message.getString("amount-empty");
        String pleaseType = message.getString("please-type-amount");
        new AnvilGUI.Builder().onClose(stateSnapshot -> {
            String amount = stateSnapshot.getText();
            YamlConfiguration config = null;
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            config = YamlConfiguration.loadConfiguration((File)configFile);
            String prefix = config.getString("prefix");
            YamlConfiguration bankConfig = null;
            File bankFile = new File(plugin.getDataFolder(), "bank.yml");
            bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
            DataManager dataManager = new DataManager(plugin);
            try {
                Integer.parseInt(amount);
                topUpBank.setEnableRender(true);
                String accNo = "";
                String accName = "";
                int id = dataManager.getUserIdByUsername(player.getName());
                String description = config.getString("bank.noidung") + " " + id;
                if (topUpBank.getTypeBank().equalsIgnoreCase("MBBank")) {
                    accNo = bankConfig.getString("mbBank.acc_no");
                    accName = bankConfig.getString("mbBank.acc_name");
                }
                if (topUpBank.getTypeBank().equalsIgnoreCase("ACB")) {
                    accNo = bankConfig.getString("ACB.acc_no");
                    accName = bankConfig.getString("ACB.acc_name");
                }
                if (topUpBank.getTypeBank().equalsIgnoreCase("VCB")) {
                    accNo = bankConfig.getString("VCB.acc_no");
                    accName = bankConfig.getString("VCB.acc_name");
                }
                if (topUpBank.getTypeBank().equalsIgnoreCase("BIDV")) {
                    accNo = bankConfig.getString("Bidv.acc_no");
                    accName = bankConfig.getString("Bidv.acc_name");
                }
                if (topUpBank.getTypeBank().equalsIgnoreCase("Vietinbank")) {
                    accNo = bankConfig.getString("Vietinbank.acc_no");
                    accName = bankConfig.getString("Vietinbank.acc_name");
                }
                if (topUpBank.getTypeBank().equalsIgnoreCase("Seabank")) {
                    accNo = bankConfig.getString("Seabank.acc_no");
                    accName = bankConfig.getString("Seabank.acc_name");
                }
                topUpBank.setAccName(accName);
                topUpBank.setAccNo(accNo);
                topUpBank.setDescription(description);
                QRCodeManager qrCodeManager = new QRCodeManager(plugin);
                qrCodeManager.onCreateQR(topUpBank, player, prefix, qrCode);
            } catch (NumberFormatException e) {
                stateSnapshot.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + amountEmpty)));
            }
        }).onClick((slot, stateSnapshot) -> {
            if (slot != 2) {
                return Collections.emptyList();
            }
            if (!stateSnapshot.getText().equalsIgnoreCase("")) {
                String amount = stateSnapshot.getText();
                topUpBank.setAmount(amount);
                return Arrays.asList(AnvilGUI.ResponseAction.close());
            }
            return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(pleaseType));
        }).text(pleaseType).title(pleaseType).plugin((Plugin)plugin).open(player);
    }
}

