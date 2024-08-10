/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package net.heavenmine.hmtopup.gui.history;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.HistoryGuiCache;
import net.heavenmine.hmtopup.modal.LogNapThe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HistoryGui
implements Listener {
    private final HMTopUp plugin;
    private final DataManager dataManager;
    private int page = 1;

    public HistoryGui(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void openGUI(Player player, String playerNameParam, boolean isAllLogParam) {
        ItemStack currPage;
        Double allTime;
        HistoryGuiCache historyGuiCache = new HistoryGuiCache(playerNameParam, isAllLogParam, "");
        this.plugin.saveHistoryGuiCache(player.getUniqueId().toString(), historyGuiCache);
        FileConfiguration config = this.plugin.getConfig();
        String prefix = config.getString("prefix");
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        Inventory gui = Bukkit.createInventory((InventoryHolder)player, (int)54, (String)Utils.colorize(message.getString("history-title")));
        List<LogNapThe> logNapTheList = isAllLogParam ? this.dataManager.getAllNapTheHistory(this.page) : this.dataManager.getNapTheHistory(playerNameParam, this.page);
        if (logNapTheList.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&aNo history found")));
        }
        ItemStack prevPage = HistoryGui.createItem(Material.ARROW, message.getString("prevPage"), message.getString("prevPage-lore"));
        ItemStack close = HistoryGui.createItem(Material.BARRIER, message.getString("closePage"), message.getString("closePage-lore"));
        ItemStack nextPage = HistoryGui.createItem(Material.ARROW, message.getString("nextPage"), message.getString("nextPage-lore"));
        gui.setItem(0, prevPage);
        gui.setItem(49, close);
        gui.setItem(8, nextPage);
        int index = 0;
        for (LogNapThe logNapThe : logNapTheList) {
            ItemStack item01 = HistoryGui.createItem(Material.BOOK, "&a" + logNapThe.getMenhgia(), isAllLogParam ? "&7Player: &f" + logNapThe.getUsername() : "", "&7Date: &f" + Utils.DateFormat(logNapThe.getDate()), "&7Type: &f" + logNapThe.getLoaithe(), "&7Status: &f" + (logNapThe.getStatus().equalsIgnoreCase("00") ? message.getString("status-success") : (logNapThe.getStatus().equalsIgnoreCase("-9") ? message.getString("status-inProgress") : message.getString("status-fail"))));
            gui.setItem(9 + index, item01);
            ++index;
        }
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        if (isAllLogParam) {
            allTime = this.dataManager.getTongNapAllPlayer();
            Double toDay = this.dataManager.getTongNapTodayAllPlayer();
            Double thisWeek = this.dataManager.getTongNapThisWeekAllPlayer();
            Double thisMonth = this.dataManager.getTongNapThisMonthAllPlayer();
            currPage = HistoryGui.createItem(Material.REDSTONE_BLOCK, message.getString("currentPage") + this.page, message.getString("sum-topup-day") + toDay.intValue(), message.getString("sum-topup-week") + thisWeek.intValue(), message.getString("sum-topup-month") + thisMonth.intValue(), message.getString("sum-topup-all-time") + allTime.intValue());
        } else {
            allTime = this.dataManager.getTongNap(playerNameParam);
            Double toDay = this.dataManager.getTongNapToday(playerNameParam);
            Double thisWeek = this.dataManager.getTongNapThisWeek(playerNameParam);
            Double thisMonth = this.dataManager.getTongNapThisMonth(playerNameParam);
            currPage = HistoryGui.createItem(Material.REDSTONE_BLOCK, message.getString("currentPage") + this.page, message.getString("sum-topup-day") + toDay.intValue(), message.getString("sum-topup-week") + thisWeek.intValue(), message.getString("sum-topup-month") + thisMonth.intValue(), message.getString("sum-topup-all-time") + allTime.intValue());
        }
        gui.setItem(4, currPage);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        if (event.getView().getTitle().equalsIgnoreCase(Utils.colorize(message.getString("history-title"))) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            HistoryGuiCache historyGuiCache = this.plugin.getHistoryGuiCache(player.getUniqueId().toString());
            String playerName = historyGuiCache.getPlayerName();
            boolean isAllLog = historyGuiCache.isAllLog();
            switch (event.getRawSlot()) {
                case 0: {
                    if (this.page == 1) {
                        return;
                    }
                    if (this.page <= 1) break;
                    --this.page;
                    player.closeInventory();
                    break;
                }
                case 8: {
                    ++this.page;
                    player.closeInventory();
                    break;
                }
                case 49: {
                    player.closeInventory();
                    return;
                }
                default: {
                    return;
                }
            }
            this.openGUI(player, playerName, isAllLog);
        }
    }

    private static ItemStack createItem(Material material, String name, String ... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.colorize(name));
        meta.setLore(Utils.colorize(lore));
        item.setItemMeta(meta);
        return item;
    }
}

