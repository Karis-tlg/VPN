/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.geysermc.cumulus.form.Form
 *  org.geysermc.cumulus.form.ModalForm
 *  org.geysermc.cumulus.form.ModalForm$Builder
 *  org.geysermc.cumulus.form.SimpleForm
 *  org.geysermc.cumulus.form.SimpleForm$Builder
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup.form;

import java.io.File;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.LogNapThe;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class HistoryForm {
    private final HMTopUp plugin;
    private final DataManager dataManager;
    private int page = 1;

    public HistoryForm(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void openForm(Player player, String playerName, boolean isAllLog) {
        List<LogNapThe> logNapTheList = isAllLog ? this.dataManager.getAllNapTheHistory(this.page) : this.dataManager.getNapTheHistory(playerName, this.page);
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        SimpleForm.Builder formBuilder = (SimpleForm.Builder)SimpleForm.builder().title(message.getString("history-title"));
        for (LogNapThe logNapThe : logNapTheList) {
            String username = isAllLog ? "&f" + logNapThe.getUsername() + " &e- " : "";
            String amount = logNapThe.getMenhgia();
            String date = Utils.DateFormat(logNapThe.getDate(), true);
            String type = logNapThe.getLoaithe();
            String status = logNapThe.getStatus().equalsIgnoreCase("00") ? message.getString("status-success") : (logNapThe.getStatus().equalsIgnoreCase("-9") ? message.getString("status-inProgress") : message.getString("status-fail"));
            String line1 = username + "&f" + amount + " &e- &f" + date;
            String line2 = "&f" + type + " &e- &f" + status;
            String text = line1 + "\n" + line2;
            formBuilder.button(Utils.colorize(text)).validResultHandler(response -> this.openDetail(logNapThe, floodgatePlayer));
        }
        SimpleForm form = (SimpleForm)formBuilder.build();
        floodgatePlayer.sendForm((Form)form);
    }

    public void openDetail(LogNapThe logNapThe, FloodgatePlayer floodgatePlayer) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String username = "&7Player: &f" + logNapThe.getUsername() + "\n";
        String amount = "&7Amount: &f" + logNapThe.getMenhgia() + "\n";
        String date = "&7Date: &f" + Utils.DateFormat(logNapThe.getDate()) + "\n";
        String type = "&7Type: &f" + logNapThe.getLoaithe() + "\n";
        String status = "&Status: &f" + (logNapThe.getStatus().equalsIgnoreCase("00") ? message.getString("status-success") : (logNapThe.getStatus().equalsIgnoreCase("-9") ? message.getString("status-inProgress") : message.getString("status-fail"))) + "\n";
        String content = username + amount + date + type + status;
        ModalForm form = (ModalForm)((ModalForm.Builder)((ModalForm.Builder)ModalForm.builder().title(message.getString("detail-title"))).content(Utils.colorize(content)).button1("Back").button2("Cancel").validResultHandler(response -> {
            if (response.clickedFirst()) {
                this.plugin.getLogger().info(logNapThe.toString());
            }
        })).build();
        floodgatePlayer.sendForm((Form)form);
    }
}

