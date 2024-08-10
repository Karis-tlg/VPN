/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.heavenmine.hmtopup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.heavenmine.hmtopup.HMLicense;
import net.heavenmine.hmtopup.bank.BankManager;
import net.heavenmine.hmtopup.cardmanager.TheSieuTocManager;
import net.heavenmine.hmtopup.commands.NapBankCommand;
import net.heavenmine.hmtopup.commands.NapTheCommand;
import net.heavenmine.hmtopup.commands.NapTheNhanhCommand;
import net.heavenmine.hmtopup.commands.NganHangCommand;
import net.heavenmine.hmtopup.commands.hmtopupcmd.HMTopUpCommand;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.gui.history.HistoryGui;
import net.heavenmine.hmtopup.gui.napbank.BankListGui;
import net.heavenmine.hmtopup.gui.napbank.PriceBankGui;
import net.heavenmine.hmtopup.gui.napthe.PriceCardGui;
import net.heavenmine.hmtopup.gui.napthe.TypeCardGui;
import net.heavenmine.hmtopup.httpserver.HTTPServer;
import net.heavenmine.hmtopup.listeners.CreateQRCode;
import net.heavenmine.hmtopup.listeners.PlayerJoinEvent;
import net.heavenmine.hmtopup.modal.HistoryGuiCache;
import net.heavenmine.hmtopup.modal.LogNapThe;
import net.heavenmine.hmtopup.modal.TopUpBank;
import net.heavenmine.hmtopup.modal.TopUpCard;
import net.heavenmine.hmtopup.placeholderapi.TongNapValue;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HMTopUp
extends JavaPlugin {
    private File configFile;
    private HMLicense HMLicense;
    private Map<String, TopUpCard> topUpCache = new HashMap<String, TopUpCard>();
    private Map<String, TopUpBank> bankCache = new HashMap<String, TopUpBank>();
    private Map<String, HistoryGuiCache> historyGuiCache = new HashMap<String, HistoryGuiCache>();

    public void saveBankCache(String playerId, TopUpBank topUpBank) {
        this.bankCache.put(playerId, topUpBank);
    }

    public TopUpBank getBankCache(String playerId) {
        return this.bankCache.get(playerId);
    }

    public void saveTopUpCache(String playerId, TopUpCard topUpCard) {
        this.topUpCache.put(playerId, topUpCard);
    }

    public TopUpCard getTopUpCache(String playerId) {
        return this.topUpCache.get(playerId);
    }

    public void saveHistoryGuiCache(String playerId, HistoryGuiCache historyGui) {
        this.historyGuiCache.put(playerId, historyGui);
    }

    public HistoryGuiCache getHistoryGuiCache(String playerId) {
        return this.historyGuiCache.get(playerId);
    }

    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getLogger().warning("Could not find PlaceholderAPI! Placeholder will not work...");
        }
        if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
            this.getLogger().warning("Could not find floodgate! Bedrock form will not work...");
        }
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        this.reloadConfig();
        this.createDatabaseFile();
        this.createNapLanDauFile();
        this.createNapTheoMocFile();
        this.createBankFile();
        this.createCardFile();
        this.createMessageFile();
        try {
            this.checkHMLicense();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getListBank(String typeBank) {
        boolean debug = this.getConfig().getBoolean("debug");
        if (debug) {
            this.getLogger().info("Check history " + typeBank + "...");
        }
        BankManager bankManager = new BankManager(this, typeBank);
        try {
            bankManager.getHistory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkHMLicense() throws IOException, SQLException {
        try {
            String licenseKey = this.getConfig().getString("license.key");
            this.HMLicense = new HMLicense(licenseKey, "HMTopUp_Spigot", "1.0.0");
        } catch (IOException e) {
            e.printStackTrace();
            this.getLogger().info("License key kh\u00f4ng h\u1ee3p l\u1ec7. T\u1eaft plugin...");
            this.getLogger().info("Join discord: https://discord.gg/rbVpgMFcz3 \u0111\u1ec3 \u0111\u01b0\u1ee3c c\u1ea5p license key !");
            this.onDisable();
        }
        if (this.HMLicense.isValid()) {
            this.getLogger().info("License key validated " + this.HMLicense.getDiscordName() + " (" + this.HMLicense.getDiscordID() + ")");
            this.getLogger().info("Thank for your purchase");
            this.loadPlugin();
        } else {
            this.getLogger().info("License is not valid. Shutdown server...");
            this.onDisable();
        }
    }

    public void createDatabaseFile() {
        File dbFile = new File(this.getDataFolder(), "database.db");
        if (!dbFile.exists()) {
            try {
                if (!dbFile.createNewFile()) {
                    this.getLogger().severe("Failed to create database file!");
                }
            } catch (IOException e) {
                this.getLogger().severe("Error while creating database file: " + e.getMessage());
            }
        }
    }

    public void createNapLanDauFile() {
        File itemsFile = new File(this.getDataFolder(), "naplandau.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("naplandau.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNapTheoMocFile() {
        File itemsFile = new File(this.getDataFolder(), "naptheomoc.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("naptheomoc.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createBankFile() {
        File itemsFile = new File(this.getDataFolder(), "bank.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("bank.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createCardFile() {
        File itemsFile = new File(this.getDataFolder(), "card.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("card.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createMessageFile() {
        File itemsFile = new File(this.getDataFolder(), "message.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("message.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTypeCardFile() {
        File itemsFile = new File(this.getDataFolder(), "gui/type-card.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("gui/type-card.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createPriceCardFile() {
        File itemsFile = new File(this.getDataFolder(), "gui/price-card.yml");
        if (!itemsFile.exists()) {
            itemsFile.getParentFile().mkdirs();
            try (InputStream in = this.getResource("gui/price-card.yml");){
                Files.copy(in, itemsFile.toPath(), new CopyOption[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlugin() throws IOException, SQLException {
        this.getLogger().info("Plugin running !");
        DataManager dataBaseManager = new DataManager(this);
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        try {
            dataBaseManager.onLoad();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.getCommand("napthe").setExecutor((CommandExecutor)new NapTheCommand(this));
        this.getCommand("napthenhanh").setExecutor((CommandExecutor)new NapTheNhanhCommand(this, dataBaseManager));
        this.getCommand("nganhang").setExecutor((CommandExecutor)new NganHangCommand(this, dataBaseManager));
        this.getCommand("hmtopup").setExecutor((CommandExecutor)new HMTopUpCommand(this));
        this.getCommand("napbank").setExecutor((CommandExecutor)new NapBankCommand(this));
        this.getServer().getPluginManager().registerEvents((Listener)new TypeCardGui(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PriceCardGui(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BankListGui(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PriceBankGui(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new CreateQRCode(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoinEvent(this, dataBaseManager), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new HistoryGui(this, dataBaseManager), (Plugin)this);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TongNapValue((Plugin)this, dataBaseManager).register();
        }
        YamlConfiguration cardConfig = null;
        File cardFile = new File(this.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        boolean enableOtherSite = cardConfig.getBoolean("other-site.enable");
        if (enableOtherSite) {
            HTTPServer httpServer = new HTTPServer(this, dataBaseManager);
            httpServer.onStart();
        } else {
            executorService.scheduleAtFixedRate(() -> {
                try {
                    this.scheduleCheckCard(dataBaseManager);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, 0L, 1L, TimeUnit.MINUTES);
        }
        boolean enableBank = this.getConfig().getBoolean("bank.enable");
        if (enableBank) {
            YamlConfiguration bankConfig = null;
            File bankFile = new File(this.getDataFolder(), "bank.yml");
            bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
            String VCBToken = bankConfig.getString("VCB.token");
            String ACBToken = bankConfig.getString("ACB.token");
            String mbBankToken = bankConfig.getString("mbBank.token");
            String BidvToken = bankConfig.getString("Bidv.token");
            String VietinbankToken = bankConfig.getString("Vietinbank.token");
            String SeabankToken = bankConfig.getString("Seabank.token");
            int timeCheck = this.getConfig().getInt("bank.time-check");
            if (!VCBToken.equalsIgnoreCase("")) {
                this.getLogger().info("Loading VCB Token...");
                executorService.scheduleAtFixedRate(() -> this.getListBank("VCB"), 0L, timeCheck > 0 ? (long)timeCheck : 5L, TimeUnit.MINUTES);
            }
            if (!ACBToken.equalsIgnoreCase("")) {
                this.getLogger().info("Loading ACB Token...");
                executorService.scheduleAtFixedRate(() -> this.getListBank("ACB"), 0L, timeCheck > 0 ? (long)timeCheck : 5L, TimeUnit.MINUTES);
            }
            if (!mbBankToken.equalsIgnoreCase("")) {
                this.getLogger().info("Loading mbBank Token...");
                executorService.scheduleAtFixedRate(() -> this.getListBank("mbBank"), 0L, timeCheck > 0 ? (long)timeCheck : 5L, TimeUnit.MINUTES);
            }
            if (!BidvToken.equalsIgnoreCase("")) {
                this.getLogger().info("Loading Bidv Token...");
                executorService.scheduleAtFixedRate(() -> this.getListBank("Bidv"), 0L, timeCheck > 0 ? (long)timeCheck : 5L, TimeUnit.MINUTES);
            }
            if (!VietinbankToken.equalsIgnoreCase("")) {
                this.getLogger().info("Loading Vietinbank Token...");
                executorService.scheduleAtFixedRate(() -> this.getListBank("Vietinbank"), 0L, timeCheck > 0 ? (long)timeCheck : 5L, TimeUnit.MINUTES);
            }
            if (!SeabankToken.equalsIgnoreCase("")) {
                this.getLogger().info("Loading Seabank Token...");
                executorService.scheduleAtFixedRate(() -> this.getListBank("Seabank"), 0L, timeCheck > 0 ? (long)timeCheck : 5L, TimeUnit.MINUTES);
            }
            this.cleanupOldImages();
        }
    }

    public void scheduleCheckCard(DataManager dataManager) throws IOException {
        boolean debug = this.getConfig().getBoolean("debug");
        if (debug) {
            this.getLogger().info("Checking list card...");
        }
        TheSieuTocManager theSieuTocManager = new TheSieuTocManager(this);
        YamlConfiguration cardConfig = null;
        File cardFile = new File(this.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        List<Object> logNapThes = new ArrayList();
        boolean enableOtherSite = cardConfig.getBoolean("other-site.enable");
        if (!enableOtherSite) {
            logNapThes = dataManager.getListCheckCard("-9");
            for (LogNapThe logNapThe : logNapThes) {
                theSieuTocManager.checkCard(logNapThe.getTransaction_id(), logNapThe.getUsername(), logNapThe.getMenhgia());
            }
        }
    }

    private void cleanupOldImages() {
        File imageFolder;
        boolean debug = this.getConfig().getBoolean("debug");
        if (debug) {
            this.getLogger().info("Cleaning up old images...");
        }
        if (!(imageFolder = new File(this.getDataFolder(), "images")).exists()) {
            imageFolder.mkdirs();
        }
        long currentTime = System.currentTimeMillis();
        int count = 0;
        for (File imageFile : imageFolder.listFiles()) {
            if (imageFile.getName().equals("MyQR.png") || currentTime - imageFile.lastModified() <= TimeUnit.MINUTES.toMillis(30L)) continue;
            imageFile.delete();
            ++count;
        }
        if (debug) {
            this.getLogger().info("Deleted " + count + " old images...");
        }
    }

    public void onDisable() {
    }
}

