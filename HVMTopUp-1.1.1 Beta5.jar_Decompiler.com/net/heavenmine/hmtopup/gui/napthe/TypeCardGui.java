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
import net.heavenmine.hmtopup.gui.napthe.PriceCardGui;
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

public class TypeCardGui
implements Listener {
    private final HMTopUp plugin;

    public TypeCardGui(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napthe-java-listCard-title");
        String chooseCard = message.getString("napthe-java-chooseCard");
        Inventory gui = Bukkit.createInventory((InventoryHolder)player, (int)36, (String)title);
        ItemStack Viettel = TypeCardGui.createItem(Material.BOOK, "&aViettel", chooseCard + "Viettel.");
        ItemStack Vinaphone = TypeCardGui.createItem(Material.BOOK, "&aVinaphone", chooseCard + "Vinaphone.");
        ItemStack Mobifone = TypeCardGui.createItem(Material.BOOK, "&aMobifone", chooseCard + "Mobifone.");
        ItemStack Vietnamobile = TypeCardGui.createItem(Material.BOOK, "&aVietnamobile", chooseCard + "Vietnamobile.");
        ItemStack Vcoin = TypeCardGui.createItem(Material.BOOK, "&aVcoin", chooseCard + "Vcoin.");
        ItemStack Gate = TypeCardGui.createItem(Material.BOOK, "&aGate", chooseCard + "Gate.");
        ItemStack Garena = TypeCardGui.createItem(Material.BOOK, "&aGarena", chooseCard + "Garena.");
        gui.setItem(10, Viettel);
        gui.setItem(12, Vinaphone);
        gui.setItem(14, Mobifone);
        gui.setItem(16, Vietnamobile);
        gui.setItem(20, Vcoin);
        gui.setItem(22, Gate);
        gui.setItem(24, Garena);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        PriceCardGui priceCardGui = new PriceCardGui(this.plugin);
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napthe-java-listCard-title");
        if (event.getView().getTitle().equalsIgnoreCase(title)) {
            Player player = (Player)event.getWhoClicked();
            TopUpCard topUpCard = this.plugin.getTopUpCache(player.getUniqueId().toString());
            switch (event.getRawSlot()) {
                case 10: {
                    topUpCard.setLoaiThe("Viettel");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                case 12: {
                    topUpCard.setLoaiThe("Vinaphone");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                case 14: {
                    topUpCard.setLoaiThe("Mobifone");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                case 16: {
                    topUpCard.setLoaiThe("Vietnamobile");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                case 20: {
                    topUpCard.setLoaiThe("Vcoin");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                case 22: {
                    topUpCard.setLoaiThe("Gate");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                case 24: {
                    topUpCard.setLoaiThe("Garena");
                    this.plugin.saveTopUpCache(player.getUniqueId().toString(), topUpCard);
                    priceCardGui.openGUI(player);
                    break;
                }
                default: {
                    player.closeInventory();
                }
            }
        }
    }

    private static ItemStack createItem(Material material, String name, String ... lore) {
        ItemStack item = new ItemStack(material, 1, 0);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(TypeCardGui.colorize(name));
        meta.setLore(TypeCardGui.colorize(lore));
        item.setItemMeta(meta);
        return item;
    }

    private static String colorize(String message) {
        return message.replaceAll("&", "\u00a7");
    }

    private static List<String> colorize(String[] messages) {
        String[] coloredMessages = new String[messages.length];
        for (int i = 0; i < messages.length; ++i) {
            coloredMessages[i] = TypeCardGui.colorize(messages[i]);
        }
        return Arrays.asList(coloredMessages);
    }
}

