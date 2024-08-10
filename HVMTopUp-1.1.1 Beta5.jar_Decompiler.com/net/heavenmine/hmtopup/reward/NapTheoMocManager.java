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

public class NapTheoMocManager {
    private DataManager dataManager;
    private Plugin plugin;

    public NapTheoMocManager(DataManager dataManager, Plugin plugin) {
        this.dataManager = dataManager;
        this.plugin = plugin;
    }

    public void checkAllTime(String username) {
        Double tongnap = this.dataManager.getTongNap(username);
        YamlConfiguration mocNapConfig = null;
        File mocNapFile = new File(this.plugin.getDataFolder(), "naptheomoc.yml");
        mocNapConfig = YamlConfiguration.loadConfiguration((File)mocNapFile);
        boolean enableNapdau = mocNapConfig.getBoolean("alltime.enable");
        if (enableNapdau) {
            ConfigurationSection moneySection = mocNapConfig.getConfigurationSection("alltime.money");
            for (String money : moneySection.getKeys(false)) {
                try {
                    int moneyConfig = Integer.parseInt(money);
                    if (!((double)moneyConfig <= tongnap)) break;
                    boolean isHasMocNap = this.dataManager.checkMocNap(username, "allTime", money);
                    if (isHasMocNap) continue;
                    List listCmd = mocNapConfig.getStringList("alltime.money." + money);
                    for (String item : listCmd) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username)));
                    }
                    this.dataManager.addLogMocNap(username, "allTime", money, 1, new Date());
                } catch (NumberFormatException e) {
                    this.plugin.getLogger().warning("File naplandau.yml config bi sai o: " + money);
                    break;
                }
            }
        }
    }

    public void checkDay(String username) {
        Double day = this.dataManager.getTongNapToday(username);
        YamlConfiguration mocNapConfig = null;
        File mocNapFile = new File(this.plugin.getDataFolder(), "naptheomoc.yml");
        mocNapConfig = YamlConfiguration.loadConfiguration((File)mocNapFile);
        boolean enableNapdau = mocNapConfig.getBoolean("day.enable");
        if (enableNapdau) {
            ConfigurationSection moneySection = mocNapConfig.getConfigurationSection("day.money");
            for (String money : moneySection.getKeys(false)) {
                try {
                    int moneyConfig = Integer.parseInt(money);
                    if (!((double)moneyConfig <= day)) break;
                    boolean isHasMocNap = this.dataManager.checkMocNapToDay(username, "day", money);
                    if (isHasMocNap) continue;
                    List listCmd = mocNapConfig.getStringList("day.money." + money);
                    for (String item : listCmd) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username)));
                    }
                    this.dataManager.addLogMocNap(username, "day", money, 1, new Date());
                } catch (NumberFormatException e) {
                    this.plugin.getLogger().warning("File naplandau.yml config bi sai o: " + money);
                    break;
                }
            }
        }
    }

    public void checkWeek(String username) {
        Double week = this.dataManager.getTongNapThisWeek(username);
        YamlConfiguration mocNapConfig = null;
        File mocNapFile = new File(this.plugin.getDataFolder(), "naptheomoc.yml");
        mocNapConfig = YamlConfiguration.loadConfiguration((File)mocNapFile);
        boolean enableNapdau = mocNapConfig.getBoolean("week.enable");
        if (enableNapdau) {
            ConfigurationSection moneySection = mocNapConfig.getConfigurationSection("week.money");
            for (String money : moneySection.getKeys(false)) {
                try {
                    int moneyConfig = Integer.parseInt(money);
                    if (!((double)moneyConfig <= week)) break;
                    boolean isHasMocNap = this.dataManager.checkMocNapThisWeek(username, "week", money);
                    if (isHasMocNap) continue;
                    List listCmd = mocNapConfig.getStringList("week.money." + money);
                    for (String item : listCmd) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username)));
                    }
                    this.dataManager.addLogMocNap(username, "week", money, 1, new Date());
                } catch (NumberFormatException e) {
                    this.plugin.getLogger().warning("File naplandau.yml config bi sai o: " + money);
                    break;
                }
            }
        }
    }

    public void checkMonth(String username) {
        Double month = this.dataManager.getTongNapThisMonth(username);
        YamlConfiguration mocNapConfig = null;
        File mocNapFile = new File(this.plugin.getDataFolder(), "naptheomoc.yml");
        mocNapConfig = YamlConfiguration.loadConfiguration((File)mocNapFile);
        boolean enableNapdau = mocNapConfig.getBoolean("month.enable");
        if (enableNapdau) {
            ConfigurationSection moneySection = mocNapConfig.getConfigurationSection("month.money");
            for (String money : moneySection.getKeys(false)) {
                try {
                    int moneyConfig = Integer.parseInt(money);
                    if (!((double)moneyConfig <= month)) break;
                    boolean isHasMocNap = this.dataManager.checkMocNapThisMonth(username, "month", money);
                    if (isHasMocNap) continue;
                    List listCmd = mocNapConfig.getStringList("month.money." + money);
                    for (String item : listCmd) {
                        Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username)));
                    }
                    this.dataManager.addLogMocNap(username, "month", money, 1, new Date());
                } catch (NumberFormatException e) {
                    this.plugin.getLogger().warning("File naplandau.yml config bi sai o: " + money);
                    break;
                }
            }
        }
    }
}

