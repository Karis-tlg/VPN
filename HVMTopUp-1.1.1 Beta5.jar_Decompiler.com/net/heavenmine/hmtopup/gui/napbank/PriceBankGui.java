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
package net.heavenmine.hmtopup.gui.napbank;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.TopUpBank;
import net.heavenmine.hmtopup.qrcode.QRCodeManager;
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

public class PriceBankGui
implements Listener {
    private final HMTopUp plugin;

    public PriceBankGui(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napthe-java-amountBank-title");
        String amountCard = message.getString("napthe-java-amountBank");
        Inventory gui = Bukkit.createInventory((InventoryHolder)player, (int)45, (String)title);
        ItemStack muoik = PriceBankGui.createItem(Material.BOOK, "&a10.000VN\u0110", amountCard + "10.000VN\u0110 &7.");
        ItemStack haimuoik = PriceBankGui.createItem(Material.BOOK, "&a20.000VN\u0110", amountCard + "20.000VN\u0110 &7.");
        ItemStack bamuoik = PriceBankGui.createItem(Material.BOOK, "&a30.000VN\u0110", amountCard + "30.000VN\u0110 &7.");
        ItemStack nammuoik = PriceBankGui.createItem(Material.BOOK, "&a50.000VN\u0110", amountCard + "50.000VN\u0110 &7.");
        ItemStack tramk = PriceBankGui.createItem(Material.BOOK, "&a100.000VN\u0110", amountCard + "100.000VN\u0110 &7.");
        ItemStack haitramk = PriceBankGui.createItem(Material.BOOK, "&a200.000VN\u0110", amountCard + "200.000VN\u0110 &7.");
        ItemStack batramk = PriceBankGui.createItem(Material.BOOK, "&a300.000VN\u0110", amountCard + "300.000VN\u0110 &7.");
        ItemStack namtramk = PriceBankGui.createItem(Material.BOOK, "&a500.000VN\u0110", amountCard + "500.000VN\u0110 &7.");
        ItemStack trieu = PriceBankGui.createItem(Material.BOOK, "&a1.000.000VN\u0110", amountCard + "1.000.000VN\u0110 &7.");
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
        String title = message.getString("napthe-java-amountBank-title");
        String qrCode = message.getString("receive-qr-code");
        if (event.getView().getTitle().equalsIgnoreCase(title)) {
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            TopUpBank topUpBank = this.plugin.getBankCache(player.getUniqueId().toString());
            switch (event.getRawSlot()) {
                case 10: {
                    topUpBank.setAmount("10000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 12: {
                    topUpBank.setAmount("20000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 14: {
                    topUpBank.setAmount("30000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 16: {
                    topUpBank.setAmount("50000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 20: {
                    topUpBank.setAmount("100000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 22: {
                    topUpBank.setAmount("200000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 24: {
                    topUpBank.setAmount("300000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 30: {
                    topUpBank.setAmount("500000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
                case 32: {
                    topUpBank.setAmount("1000000");
                    this.progressBank(topUpBank, player, new DataManager(this.plugin), qrCode);
                    player.closeInventory();
                    break;
                }
            }
        }
    }

    private static ItemStack createItem(Material material, String name, String ... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(PriceBankGui.colorize(name));
        meta.setLore(PriceBankGui.colorize(lore));
        item.setItemMeta(meta);
        return item;
    }

    private static String colorize(String message) {
        return message.replaceAll("&", "\u00a7");
    }

    private static List<String> colorize(String[] messages) {
        String[] coloredMessages = new String[messages.length];
        for (int i = 0; i < messages.length; ++i) {
            coloredMessages[i] = PriceBankGui.colorize(messages[i]);
        }
        return Arrays.asList(coloredMessages);
    }

    private void progressBank(TopUpBank topUpBank, Player player, DataManager dataManager, String qrCode) {
        YamlConfiguration config = null;
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration((File)configFile);
        String prefix = config.getString("prefix");
        YamlConfiguration bankConfig = null;
        File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
        bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
        topUpBank.setEnableRender(true);
        String accNo = "";
        String accName = "";
        int id = dataManager.getUserIdByUsername(player.getName());
        String description = config.getString("bank.noidung") + " " + id;
        if (topUpBank.getTypeBank().equalsIgnoreCase("MBBank")) {
            accNo = bankConfig.getString("mbBank.acc_no");
            accName = bankConfig.getString("mbBank.acc_name");
        }
        if (topUpBank.getTypeBank().equalsIgnoreCase("ACB")) {
            accNo = bankConfig.getString("ACB.acc_no");
            accName = bankConfig.getString("ACB.acc_name");
        }
        if (topUpBank.getTypeBank().equalsIgnoreCase("VCB")) {
            accNo = bankConfig.getString("VCB.acc_no");
            accName = bankConfig.getString("VCB.acc_name");
        }
        if (topUpBank.getTypeBank().equalsIgnoreCase("BIDV")) {
            accNo = bankConfig.getString("Bidv.acc_no");
            accName = bankConfig.getString("Bidv.acc_name");
        }
        if (topUpBank.getTypeBank().equalsIgnoreCase("Vietinbank")) {
            accNo = bankConfig.getString("Vietinbank.acc_no");
            accName = bankConfig.getString("Vietinbank.acc_name");
        }
        if (topUpBank.getTypeBank().equalsIgnoreCase("Seabank")) {
            accNo = bankConfig.getString("Seabank.acc_no");
            accName = bankConfig.getString("Seabank.acc_name");
        }
        topUpBank.setAccName(accName);
        topUpBank.setAccNo(accNo);
        topUpBank.setDescription(description);
        QRCodeManager qrCodeManager = new QRCodeManager(this.plugin);
        qrCodeManager.onCreateQR(topUpBank, player, prefix, qrCode);
    }
}

