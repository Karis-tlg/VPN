/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.server.MapInitializeEvent
 *  org.bukkit.map.MapRenderer
 *  org.bukkit.map.MapView
 *  org.bukkit.map.MapView$Scale
 */
package net.heavenmine.hmtopup.listeners;

import java.io.File;
import java.net.MalformedURLException;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.modal.TopUpBank;
import net.heavenmine.hmtopup.qrcode.QRRenderers;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class CreateQRCode
implements Listener {
    private final HMTopUp plugin;

    public CreateQRCode(HMTopUp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void create(MapInitializeEvent e) throws MalformedURLException {
        String idMap = String.valueOf(e.getMap().getId() - 1);
        TopUpBank topUpBank = this.plugin.getBankCache(idMap);
        if (topUpBank != null) {
            String typeBank = topUpBank.getTypeBank();
            String accNo = topUpBank.getAccNo();
            String accName = topUpBank.getAccName();
            String amount = topUpBank.getAmount();
            String noidung = topUpBank.getDescription();
            String imageName = topUpBank.getImageName();
            YamlConfiguration config = null;
            File configFile = new File(this.plugin.getDataFolder(), "config.yml");
            config = YamlConfiguration.loadConfiguration((File)configFile);
            boolean debug = config.getBoolean("debug");
            if (debug) {
                this.plugin.getLogger().info(imageName);
            }
            File imageFile = new File(this.plugin.getDataFolder(), "images/" + imageName + ".png");
            MapView view = e.getMap();
            view.setScale(MapView.Scale.FARTHEST);
            for (MapRenderer renderer : view.getRenderers()) {
                view.removeRenderer(renderer);
            }
            view.addRenderer((MapRenderer)new QRRenderers(imageFile));
            this.plugin.saveBankCache(idMap, null);
        }
    }
}

