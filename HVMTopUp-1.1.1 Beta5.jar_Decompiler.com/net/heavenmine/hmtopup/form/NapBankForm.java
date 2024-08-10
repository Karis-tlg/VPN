/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.Configuration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.geysermc.cumulus.form.CustomForm
 *  org.geysermc.cumulus.form.CustomForm$Builder
 *  org.geysermc.cumulus.form.Form
 *  org.geysermc.cumulus.response.CustomFormResponse
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup.form;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.TopUpBank;
import net.heavenmine.hmtopup.qrcode.QRCodeManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class NapBankForm {
    private HMTopUp plugin;
    private final List<String> typeBank = new ArrayList<String>();
    boolean confirmTopUp = true;

    public NapBankForm(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        DataManager dataManager = new DataManager(this.plugin);
        YamlConfiguration bankConfig = null;
        File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
        bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String tokenMB = bankConfig.getString("mbBank.token");
        String tokenVCB = bankConfig.getString("VCB.token");
        String tokenACB = bankConfig.getString("ACB.token");
        String BidvToken = bankConfig.getString("Bidv.token");
        String VietinbankToken = bankConfig.getString("Vietinbank.token");
        String SeabankToken = bankConfig.getString("Seabank.token");
        if (!tokenMB.equalsIgnoreCase("")) {
            this.typeBank.add("MBBank");
        }
        if (!tokenACB.equalsIgnoreCase("")) {
            this.typeBank.add("ACB");
        }
        if (!tokenVCB.equalsIgnoreCase("")) {
            this.typeBank.add("VCB");
        }
        if (!BidvToken.equalsIgnoreCase("")) {
            this.typeBank.add("BIDV");
        }
        if (!VietinbankToken.equalsIgnoreCase("")) {
            this.typeBank.add("Vietinbank");
        }
        if (!SeabankToken.equalsIgnoreCase("")) {
            this.typeBank.add("Seabank");
        }
        String title = message.getString("napbank-be-title");
        String header = message.getString("napbank-be-header");
        String listBank = message.getString("napbank-be-listBank");
        String amountText = message.getString("napbank-be-amount");
        String footer = message.getString("napbank-be-footer");
        String qrCode = message.getString("receive-qr-code");
        YamlConfiguration finalBankConfig = bankConfig;
        CustomForm form = (CustomForm)((CustomForm.Builder)((CustomForm.Builder)CustomForm.builder().title(title)).label(header).dropdown(listBank, this.typeBank).input(amountText).label(footer).validResultHandler((arg_0, arg_1) -> this.lambda$openForm$0(dataManager, player, (Configuration)finalBankConfig, qrCode, arg_0, arg_1))).build();
        floodgatePlayer.sendForm((Form)form);
    }

    private /* synthetic */ void lambda$openForm$0(DataManager dataManager, Player player, Configuration finalBankConfig, String qrCode, CustomForm customForm, CustomFormResponse response) {
        String type = this.typeBank.get(response.asDropdown());
        String amount = response.asInput();
        YamlConfiguration config = null;
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration((File)configFile);
        String prefix = config.getString("prefix");
        String accNo = "";
        String accName = "";
        int id = dataManager.getUserIdByUsername(player.getName());
        String description = config.getString("bank.noidung") + " " + id;
        if (type.equalsIgnoreCase("MBBank")) {
            accNo = finalBankConfig.getString("mbBank.acc_no");
            accName = finalBankConfig.getString("mbBank.acc_name");
        }
        if (type.equalsIgnoreCase("ACB")) {
            accNo = finalBankConfig.getString("ACB.acc_no");
            accName = finalBankConfig.getString("ACB.acc_name");
        }
        if (type.equalsIgnoreCase("VCB")) {
            accNo = finalBankConfig.getString("VCB.acc_no");
            accName = finalBankConfig.getString("VCB.acc_name");
        }
        if (type.equalsIgnoreCase("BIDV")) {
            accNo = finalBankConfig.getString("Bidv.acc_no");
            accName = finalBankConfig.getString("Bidv.acc_name");
        }
        if (type.equalsIgnoreCase("Vietinbank")) {
            accNo = finalBankConfig.getString("Vietinbank.acc_no");
            accName = finalBankConfig.getString("Vietinbank.acc_name");
        }
        if (type.equalsIgnoreCase("Seabank")) {
            accNo = finalBankConfig.getString("Seabank.acc_no");
            accName = finalBankConfig.getString("Seabank.acc_name");
        }
        TopUpBank topUpBank = new TopUpBank(type, accNo, accName, amount, description, false);
        QRCodeManager qrCodeManager = new QRCodeManager(this.plugin);
        qrCodeManager.onCreateQR(topUpBank, player, prefix, qrCode);
    }
}

