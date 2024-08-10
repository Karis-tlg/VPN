/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
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
import net.heavenmine.hmtopup.gui.napbank.PriceBankGui;
import net.heavenmine.hmtopup.modal.TopUpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class BankListGui
implements Listener {
    private final HMTopUp plugin;

    public BankListGui(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napbank-java-title");
        String chooseBank = message.getString("napbank-java-chooseBank");
        String disableBank = message.getString("napbank-java-disableBank");
        Inventory gui = Bukkit.createInventory((InventoryHolder)player, (int)36, (String)title);
        YamlConfiguration bankConfig = null;
        File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
        bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
        String VCBToken = bankConfig.getString("VCB.token");
        String ACBToken = bankConfig.getString("ACB.token");
        String mbBankToken = bankConfig.getString("mbBank.token");
        String BidvToken = bankConfig.getString("Bidv.token");
        String VietinbankToken = bankConfig.getString("Vietinbank.token");
        String SeabankToken = bankConfig.getString("Seabank.token");
        ItemStack MBBank = BankListGui.createItem(Material.BOOK, "&aMBBank", chooseBank + "MBBank.");
        ItemStack VCB = BankListGui.createItem(Material.BOOK, "&aVCB", chooseBank + "Vietcombank.");
        ItemStack ACB = BankListGui.createItem(Material.BOOK, "&aACB", chooseBank + "ACB.");
        ItemStack BIDV = BankListGui.createItem(Material.BOOK, "&aBIDV", chooseBank + "BIDV.");
        ItemStack Vietinbank = BankListGui.createItem(Material.BOOK, "&aVietinbank", chooseBank + "Vietinbank.");
        ItemStack Seabank = BankListGui.createItem(Material.BOOK, "&aSeabank", chooseBank + "Seabank.");
        ItemStack MBBankError = BankListGui.createItem(Material.BOOK, "&aMBBank", disableBank + "MBBank.");
        ItemStack VCBError = BankListGui.createItem(Material.BOOK, "&aVCB", disableBank + "Vietcombank.");
        ItemStack ACBError = BankListGui.createItem(Material.BOOK, "&aACB", disableBank + "ACB.");
        ItemStack BIDVError = BankListGui.createItem(Material.BOOK, "&aBIDV", disableBank + "BIDV.");
        ItemStack VietinbankError = BankListGui.createItem(Material.BOOK, "&aVietinbank", disableBank + "Vietinbank.");
        ItemStack SeabankError = BankListGui.createItem(Material.BOOK, "&aSeabank", disableBank + "Seabank.");
        gui.setItem(10, mbBankToken.equalsIgnoreCase("") ? MBBankError : MBBank);
        gui.setItem(13, VCBToken.equalsIgnoreCase("") ? VCBError : VCB);
        gui.setItem(16, ACBToken.equalsIgnoreCase("") ? ACBError : ACB);
        gui.setItem(19, BidvToken.equalsIgnoreCase("") ? BIDVError : BIDV);
        gui.setItem(22, VietinbankToken.equalsIgnoreCase("") ? VietinbankError : Vietinbank);
        gui.setItem(25, SeabankToken.equalsIgnoreCase("") ? SeabankError : Seabank);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        YamlConfiguration message = null;
        File messageFile = new File(this.plugin.getDataFolder(), "message.yml");
        message = YamlConfiguration.loadConfiguration((File)messageFile);
        String title = message.getString("napbank-java-title");
        String disableBank = message.getString("napbank-java-disableBank");
        if (event.getView().getTitle().equalsIgnoreCase(title)) {
            Player player = (Player)event.getWhoClicked();
            TopUpBank topUpBank = this.plugin.getBankCache(player.getUniqueId().toString());
            PriceBankGui priceBankGui = new PriceBankGui(this.plugin);
            YamlConfiguration bankConfig = null;
            File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
            bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
            String VCBToken = bankConfig.getString("VCB.token");
            String ACBToken = bankConfig.getString("ACB.token");
            String mbBankToken = bankConfig.getString("mbBank.token");
            String BidvToken = bankConfig.getString("Bidv.token");
            String VietinbankToken = bankConfig.getString("Vietinbank.token");
            String SeabankToken = bankConfig.getString("Seabank.token");
            String prefix = this.plugin.getConfig().getString("prefix");
            switch (event.getRawSlot()) {
                case 10: {
                    if (!mbBankToken.equalsIgnoreCase("")) {
                        topUpBank.setTypeBank("MBBank");
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        priceBankGui.openGUI(player);
                        break;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank + "MBBank")));
                    player.closeInventory();
                    break;
                }
                case 13: {
                    if (!VCBToken.equalsIgnoreCase("")) {
                        topUpBank.setTypeBank("VCB");
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        priceBankGui.openGUI(player);
                        break;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank + "VCB")));
                    player.closeInventory();
                    break;
                }
                case 16: {
                    if (!ACBToken.equalsIgnoreCase("")) {
                        topUpBank.setTypeBank("ACB");
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        priceBankGui.openGUI(player);
                        break;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank + "ACB")));
                    player.closeInventory();
                    break;
                }
                case 19: {
                    if (!BidvToken.equalsIgnoreCase("")) {
                        topUpBank.setTypeBank("BIDV");
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        priceBankGui.openGUI(player);
                        break;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank + "BIDV")));
                    player.closeInventory();
                    break;
                }
                case 22: {
                    if (!VietinbankToken.equalsIgnoreCase("")) {
                        topUpBank.setTypeBank("Vietinbank");
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        priceBankGui.openGUI(player);
                        break;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank + "Vietinbank")));
                    player.closeInventory();
                    break;
                }
                case 25: {
                    if (!SeabankToken.equalsIgnoreCase("")) {
                        topUpBank.setTypeBank("Seabank");
                        this.plugin.saveBankCache(player.getUniqueId().toString(), topUpBank);
                        priceBankGui.openGUI(player);
                        break;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + disableBank + "Seabank")));
                    player.closeInventory();
                    break;
                }
                default: {
                    player.closeInventory();
                }
            }
        }
    }

    private static ItemStack createItem(Material material, String name, String ... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(BankListGui.colorize(name));
        meta.setLore(BankListGui.colorize(lore));
        item.setItemMeta(meta);
        return item;
    }

    private static String colorize(String message) {
        return message.replaceAll("&", "\u00a7");
    }

    private static List<String> colorize(String[] messages) {
        String[] coloredMessages = new String[messages.length];
        for (int i = 0; i < messages.length; ++i) {
            coloredMessages[i] = BankListGui.colorize(messages[i]);
        }
        return Arrays.asList(coloredMessages);
    }
}

