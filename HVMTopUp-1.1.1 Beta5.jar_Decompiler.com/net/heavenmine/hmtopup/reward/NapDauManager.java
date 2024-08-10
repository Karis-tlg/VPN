/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.reward;

import java.io.File;
import java.util.Date;
import java.util.List;
import net.heavenmine.hmtopup.database.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class NapDauManager {
    private DataManager dataManager;
    private Plugin plugin;

    public NapDauManager(DataManager dataManager, Plugin plugin) {
        this.dataManager = dataManager;
        this.plugin = plugin;
    }

    public boolean checkNapDau(String username, String menhGia) {
        int menhGiaNap = 0;
        try {
            menhGiaNap = Integer.parseInt(menhGia);
        } catch (NumberFormatException e) {
            this.plugin.getLogger().warning("Menh gia nap bi sai: " + menhGia);
        }
        YamlConfiguration napdau = null;
        File napdauFile = new File(this.plugin.getDataFolder(), "naplandau.yml");
        napdau = YamlConfiguration.loadConfiguration((File)napdauFile);
        boolean enableNapdau = napdau.getBoolean("main.enable");
        boolean isNapDau = this.dataManager.checkNapDau(username);
        if (!isNapDau) {
            ConfigurationSection moneySection = napdau.getConfigurationSection("main.money");
            String finalMoney = "";
            for (String money : moneySection.getKeys(false)) {
                try {
                    int moneyConfig = Integer.parseInt(money);
                    if (moneyConfig <= menhGiaNap) {
                        finalMoney = String.valueOf(moneyConfig);
                        continue;
                    }
                    List listCmd = napdau.getStringList("main.money." + finalMoney);
                    for (String item : listCmd) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username)));
                    }
                    this.dataManager.addLogNapDau(username, 1, new Date());
                } catch (NumberFormatException e) {
                    this.plugin.getLogger().warning("File naplandau.yml config bi sai o: " + money);
                }
                break;
            }
        }
        return false;
    }
}

