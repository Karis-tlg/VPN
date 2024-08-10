/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package net.heavenmine.hmtopup.listeners;

import java.util.Date;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent
implements Listener {
    private final HMTopUp plugin;
    private final DataManager dataManager;

    public PlayerJoinEvent(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        boolean check = this.dataManager.checkUserName(e.getPlayer().getName());
        if (!check) {
            this.dataManager.createUser(e.getPlayer().getName(), new Date());
        }
    }
}

