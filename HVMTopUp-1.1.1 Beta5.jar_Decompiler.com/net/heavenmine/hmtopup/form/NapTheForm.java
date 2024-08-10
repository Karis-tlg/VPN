/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.geysermc.cumulus.form.CustomForm
 *  org.geysermc.cumulus.form.CustomForm$Builder
 *  org.geysermc.cumulus.form.Form
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup.form;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.cardmanager.OtherSiteManager;
import net.heavenmine.hmtopup.cardmanager.TheSieuTocManager;
import net.heavenmine.hmtopup.database.DataManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class NapTheForm {
    private HMTopUp plugin;
    private final List<String> typeCard = Arrays.asList("Viettel", "Vinaphone", "Mobifone", "Vietnamobile", "Vcoin", "Gate", "Garena");
    private final List<String> priceCard = Arrays.asList("10.000", "20.000", "30.000", "50.000", "100.000", "200.000", "300.000", "500.000", "1.000.000");
    boolean confirmTopUp = true;

    public NapTheForm(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        TheSieuTocManager theSieuTocManager = new TheSieuTocManager(this.plugin);
        OtherSiteManager otherSiteManager = new OtherSiteManager(this.plugin, new DataManager(this.plugin));
        String prefix = this.plugin.getConfig().getString("prefix");
        YamlConfiguration cardConfig = null;
        File cardFile = new File(this.plugin.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        boolean enableOtherSite = cardConfig.getBoolean("other-site.enable");
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napthe-be-title");
        String header = message.getString("napthe-be-header");
        String listCard = message.getString("napthe-be-listCard");
        String menhgia = message.getString("napthe-be-amount");
        String seriText = message.getString("napthe-be-seri");
        String codeText = message.getString("napthe-be-code");
        String confirm = message.getString("napthe-be-confirm");
        String footer = message.getString("napthe-be-footer");
        String seriEmpty = message.getString("seri-empty");
        String pinEmpty = message.getString("pin-empty");
        String notConfirm = message.getString("pin-empty");
        CustomForm form = (CustomForm)((CustomForm.Builder)((CustomForm.Builder)CustomForm.builder().title(title)).label(header).dropdown(listCard, this.typeCard).dropdown(menhgia, this.priceCard).input(seriText).input(codeText).optionalToggle(confirm, false, this.confirmTopUp).label(footer).validResultHandler((customForm, response) -> {
            String type = this.typeCard.get(response.asDropdown());
            String amount = this.priceCard.get(response.asDropdown());
            String seri = response.asInput();
            if (seri == null || seri.isEmpty()) {
                player.sendMessage(prefix + seriEmpty);
                return;
            }
            String pin = response.asInput();
            if (pin == null || pin.isEmpty()) {
                player.sendMessage(prefix + pinEmpty);
                return;
            }
            if (this.confirmTopUp && response.hasNext() && !response.asToggle()) {
                player.sendMessage(prefix + notConfirm);
                return;
            }
            if (enableOtherSite) {
                try {
                    otherSiteManager.onTopUp(type, amount, seri, pin, (CommandSender)player);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                theSieuTocManager.onTopUp(type, amount, seri, pin, (CommandSender)player);
            }
        })).build();
        floodgatePlayer.sendForm((Form)form);
    }
}

