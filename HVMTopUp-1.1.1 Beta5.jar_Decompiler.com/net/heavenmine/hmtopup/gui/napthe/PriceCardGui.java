/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
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
package net.heavenmine.hmtopup.gui.napthe;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.gui.napthe.SeriCardGui;
import net.heavenmine.hmtopup.modal.TopUpCard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PriceCardGui
implements Listener {
    private final HMTopUp plugin;
    private String typeCard;

    public PriceCardGui(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napthe-java-amountCard-title");
        String amountCard = message.getString("napthe-java-amountCard");
        Inventory gui = Bukkit.createInventory((InventoryHolder)player, (int)45, (String)title);
        ItemStack muoik = PriceCardGui.createItem(Material.BOOK, "&a10.000VN\u0110", amountCard + "10.000VN\u0110 &7.");
        ItemStack haimuoik = PriceCardGui.createItem(Material.BOOK, "&a20.000VN\u0110", amountCard + "20.000VN\u0110 &7.");
        ItemStack bamuoik = PriceCardGui.createItem(Material.BOOK, "&a30.000VN\u0110", amountCard + "30.000VN\u0110 &7.");
        ItemStack nammuoik = PriceCardGui.createItem(Material.BOOK, "&a50.000VN\u0110", amountCard + "50.000VN\u0110 &7.");
        ItemStack tramk = PriceCardGui.createItem(Material.BOOK, "&a100.000VN\u0110", amountCard + "100.000VN\u0110 &7.");
        ItemStack haitramk = PriceCardGui.createItem(Material.BOOK, "&a200.000VN\u0110", amountCard + "200.000VN\u0110 &7.");
        ItemStack batramk = PriceCardGui.createItem(Material.BOOK, "&a300.000VN\u0110", amountCard + "300.000VN\u0110 &7.");
        ItemStack namtramk = PriceCardGui.createItem(Material.BOOK, "&a500.000VN\u0110", amountCard + "500.000VN\u0110 &7.");
        ItemStack trieu = PriceCardGui.createItem(Material.BOOK, "&a1.000.000VN\u0110", amountCard + "1.000.000VN\u0110 &7.");
        gui.setItem(10, muoik);
        gui.setItem(12, haimuoik);
        gui.setItem(14, bamuoik);
        gui.setItem(16, nammuoik);
        gui.setItem(20, tramk);
        gui.setItem(22, haitramk);
        gui.setItem(24, batramk);
        gui.setItem(30, namtramk);
        gui.setItem(32, trieu);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napthe-java-amountCard-title");
        SeriCardGui seriCardGui = new SeriCardGui(this.plugin);
        if (event.getView().getTitle().equalsIgnoreCase(title)) {
            Player player = (Player)event.getWhoClicked();
            TopUpCard topUpCard = this.plugin.getTopUpCache(player.getUniqueId().toString());
            switch (event.getRawSlot()) {
                case 10: {
                    topUpCard.setMenhGia("10.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 12: {
                    topUpCard.setMenhGia("20.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 14: {
                    topUpCard.setMenhGia("30.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 16: {
                    topUpCard.setMenhGia("50.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 20: {
                    topUpCard.setMenhGia("100.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 22: {
                    topUpCard.setMenhGia("200.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 24: {
                    topUpCard.setMenhGia("300.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 30: {
                    topUpCard.setMenhGia("500.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
                case 32: {
                    topUpCard.setMenhGia("1.000.000");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    seriCardGui.openGui(player);
                    break;
                }
            }
        }
    }

    private static ItemStack createItem(Material material, String name, String ... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(PriceCardGui.colorize(name));
        meta.setLore(PriceCardGui.colorize(lore));
        item.setItemMeta(meta);
        return item;
    }

    private static String colorize(String message) {
        return message.replaceAll("&", "\u00a7");
    }

    private static List<String> colorize(String[] messages) {
        String[] coloredMessages = new String[messages.length];
        for (int i = 0; i < messages.length; ++i) {
            coloredMessages[i] = PriceCardGui.colorize(messages[i]);
        }
        return Arrays.asList(coloredMessages);
    }
}

