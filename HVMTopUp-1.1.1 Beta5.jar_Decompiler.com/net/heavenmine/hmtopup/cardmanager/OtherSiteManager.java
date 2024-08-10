/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.cardmanager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.CardPrice;
import net.heavenmine.hmtopup.reward.NapDauManager;
import net.heavenmine.hmtopup.reward.NapTheoMocManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class OtherSiteManager {
    private final HMTopUp plugin;
    private final DataManager dataManager;

    public OtherSiteManager(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void onTopUp(String loaithe, String menhgia, String seri, String code, CommandSender sender) throws IOException {
        String prefix = this.plugin.getConfig().getString("prefix");
        YamlConfiguration cardConfig = null;
        File cardFile = new File(this.plugin.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        String partnerID = cardConfig.getString("other-site.partnerID");
        String partnerKey = cardConfig.getString("other-site.partnerKey");
        String site = cardConfig.getString("other-site.url");
        String request_id = Utils.generateRandomNumber();
        String sign = Utils.getMD5Hash(partnerKey + code + seri);
        JsonObject json = new JsonObject();
        json.addProperty("request_id", request_id);
        json.addProperty("code", code);
        json.addProperty("partner_id", partnerID);
        json.addProperty("serial", seri);
        json.addProperty("telco", loaithe.toUpperCase());
        json.addProperty("command", "charging");
        json.addProperty("amount", menhgia.replaceAll("\\.", ""));
        json.addProperty("sign", sign);
        URL url = new URL(site);
        boolean redirect = true;
        while (redirect) {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String inputLine;
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                JsonObject res = new JsonParser().parse(response.toString()).getAsJsonObject();
                if (res.get("status").getAsString().equalsIgnoreCase("99")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&a\u0110\u00e3 g\u1eedi th\u1ebb cho h\u1ec7 th\u1ed1ng th\u00e0nh c\u00f4ng! Vui l\u00f2ng ch\u1edd v\u00e0i ph\u00fat \u0111\u1ec3 h\u1ec7 th\u1ed1ng x\u1eed l\u00fd!")));
                    this.dataManager.createLogNapThe(sender.getName(), loaithe, CardPrice.getPrice(menhgia).getPrice().replace(".", ""), seri, code, request_id, new Date(), "99");
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&bNap the that bai: &c" + res.get("message").toString())));
                    this.plugin.getLogger().warning("Thanh nien " + sender.getName() + " da nap the that bai: " + res.get("message").toString());
                    this.dataManager.createLogNapThe(sender.getName(), loaithe, CardPrice.getPrice(menhgia).getPrice().replace(".", ""), seri, code, null, new Date(), res.get("status").getAsString());
                }
                bufferedReader.close();
                redirect = false;
            } else if (responseCode == 301) {
                String newLocation = connection.getHeaderField("Location");
                if (newLocation != null) {
                    url = new URL(newLocation);
                    this.plugin.getLogger().info("Redirecting to: " + newLocation);
                } else {
                    this.plugin.getLogger().warning("301 redirect received, but no Location header found.");
                    redirect = false;
                }
            } else {
                this.plugin.getLogger().warning("API request failed with response code: " + responseCode);
                sender.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&cNap the loi ! Bao admin di !")));
                redirect = false;
            }
            connection.disconnect();
        }
    }

    public void checkCard(String transaction_id, String username, String menhgia, String status) {
        String prefix = this.plugin.getConfig().getString("prefix");
        Boolean debug = this.plugin.getConfig().getBoolean("debug");
        if (debug.booleanValue()) {
            this.plugin.getLogger().info("Check card...");
            this.plugin.getLogger().info("status: " + status);
        }
        if (status.equalsIgnoreCase("1")) {
            Double ratio = this.plugin.getConfig().getDouble("topup." + menhgia + ".ratio");
            boolean event = this.plugin.getConfig().getBoolean("event.enable");
            if (event) {
                Double eventBonus = this.plugin.getConfig().getDouble("event.bonus");
                ratio = ratio + ratio * eventBonus;
            }
            List listCmd = this.plugin.getConfig().getStringList("topup." + menhgia + ".cmd");
            for (String item : listCmd) {
                Double finalRatio = ratio;
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username).replace("%amount%", finalRatio.intValue() + "")));
            }
            this.dataManager.editStatus(transaction_id, "00");
            YamlConfiguration napdau = null;
            File napdauFile = new File(this.plugin.getDataFolder(), "naplandau.yml");
            napdau = YamlConfiguration.loadConfiguration((File)napdauFile);
            boolean enableNapdau = napdau.getBoolean("main.enable");
            if (enableNapdau) {
                NapDauManager napDauManager = new NapDauManager(this.dataManager, (Plugin)this.plugin);
                napDauManager.checkNapDau(username, menhgia);
            }
            NapTheoMocManager napTheoMocManager = new NapTheoMocManager(this.dataManager, (Plugin)this.plugin);
            napTheoMocManager.checkAllTime(username);
            napTheoMocManager.checkDay(username);
            napTheoMocManager.checkWeek(username);
            napTheoMocManager.checkMonth(username);
            this.plugin.getLogger().info(username + " n\u1ea1p th\u1ebb th\u00e0nh c\u00f4ng. ");
        } else if (status.equalsIgnoreCase("2")) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "say [HMTopUP]" + username + "da nap the thanh cong nhung sai menh gia. Lien he admin de duoc giai quyet"));
            this.plugin.getLogger().warning(username + " n\u1ea1p th\u1ebb th\u00e0nh c\u00f4ng nh\u01b0ng sai m\u1ec7nh gia. ");
            this.dataManager.editStatus(transaction_id, status);
        } else {
            this.dataManager.editStatus(transaction_id, status);
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "say [HMTopUP]" + username + "da nap the that bai!"));
            this.plugin.getLogger().warning(username + " nap the that bai. ");
            this.plugin.getLogger().warning("The sai");
        }
    }
}

