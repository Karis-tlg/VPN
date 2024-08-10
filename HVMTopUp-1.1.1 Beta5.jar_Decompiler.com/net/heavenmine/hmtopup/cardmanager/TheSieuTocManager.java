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
 *  org.bukkit.entity.Player
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
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.CardPrice;
import net.heavenmine.hmtopup.reward.NapDauManager;
import net.heavenmine.hmtopup.reward.NapTheoMocManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TheSieuTocManager {
    private static HMTopUp plugin;

    public TheSieuTocManager(HMTopUp plugin) {
        TheSieuTocManager.plugin = plugin;
    }

    public void onTopUp(String loaithe, String menhgia, String seri, String code, CommandSender src) {
        DataManager dataManager = new DataManager(plugin);
        try {
            URL url = new URL("https://thesieutoc.net/API/transaction");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            YamlConfiguration cardConfig = null;
            File cardFile = new File(plugin.getDataFolder(), "card.yml");
            cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
            String APIkey = cardConfig.getString("thesieutoc.api-key");
            String APIsecret = cardConfig.getString("thesieutoc.api-secret");
            String prefix = plugin.getConfig().getString("prefix");
            JsonObject json = new JsonObject();
            json.addProperty("APIkey", APIkey);
            json.addProperty("APIsecret", APIsecret);
            json.addProperty("type", loaithe);
            json.addProperty("menhgia", CardPrice.getPrice(menhgia).getId());
            json.addProperty("seri", seri);
            json.addProperty("mathe", code);
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
                if (res.get("status").getAsString().equalsIgnoreCase("2")) {
                    src.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&4" + res.get("msg").toString())));
                } else if (res.get("status").getAsString().equalsIgnoreCase("00")) {
                    src.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&a\u0110\u00e3 g\u1eedi th\u1ebb cho h\u1ec7 th\u1ed1ng th\u00e0nh c\u00f4ng! Vui l\u00f2ng ch\u1edd v\u00e0i ph\u00fat \u0111\u1ec3 h\u1ec7 th\u1ed1ng x\u1eed l\u00fd!")));
                    dataManager.createLogNapThe(src.getName(), loaithe, CardPrice.getPrice(menhgia).getPrice().replace(".", ""), seri, code, res.get("transaction_id").getAsString(), new Date(), "-9");
                } else {
                    src.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&4N\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i... Vui l\u00f2ng b\u00e1o Admin")));
                    dataManager.createLogNapThe(src.getName(), loaithe, CardPrice.getPrice(menhgia).getPrice().replace(".", ""), seri, code, null, new Date(), res.get("status").getAsString());
                }
                bufferedReader.close();
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkCard(String transaction_id, String username, String menhgia) throws IOException {
        DataManager dataManager = new DataManager(plugin);
        YamlConfiguration cardConfig = null;
        File cardFile = new File(plugin.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        String APIkey = cardConfig.getString("thesieutoc.api-key");
        String APIsecret = cardConfig.getString("thesieutoc.api-secret");
        String prefix = plugin.getConfig().getString("prefix");
        if (transaction_id != null) {
            URL url = new URL("https://thesieutoc.net/API/get_status_card.php");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            JsonObject json = new JsonObject();
            json.addProperty("APIkey", APIkey);
            json.addProperty("APIsecret", APIsecret);
            json.addProperty("transaction_id", transaction_id);
            OutputStream outputStream = connection.getOutputStream();
            byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                String inputLine;
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuffer response = new StringBuffer();
                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                JsonObject res = new JsonParser().parse(response.toString()).getAsJsonObject();
                if (res.get("status").getAsString().equalsIgnoreCase("00")) {
                    Double ratio = plugin.getConfig().getDouble("topup." + menhgia + ".ratio");
                    boolean event = plugin.getConfig().getBoolean("event.enable");
                    if (event) {
                        Double eventBonus = plugin.getConfig().getDouble("event.bonus");
                        ratio = ratio + ratio * eventBonus;
                    }
                    List listCmd = plugin.getConfig().getStringList("topup." + menhgia + ".cmd");
                    for (String item : listCmd) {
                        Double finalRatio = ratio;
                        Bukkit.getScheduler().runTask((Plugin)plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username).replace("%amount%", finalRatio.intValue() + "")));
                    }
                    dataManager.editStatus(transaction_id, res.get("status").getAsString());
                    YamlConfiguration napdau = null;
                    File napdauFile = new File(plugin.getDataFolder(), "naplandau.yml");
                    napdau = YamlConfiguration.loadConfiguration((File)napdauFile);
                    boolean enableNapdau = napdau.getBoolean("main.enable");
                    if (enableNapdau) {
                        NapDauManager napDauManager = new NapDauManager(dataManager, (Plugin)plugin);
                        napDauManager.checkNapDau(username, menhgia);
                    }
                    NapTheoMocManager napTheoMocManager = new NapTheoMocManager(dataManager, (Plugin)plugin);
                    napTheoMocManager.checkAllTime(username);
                    napTheoMocManager.checkDay(username);
                    napTheoMocManager.checkWeek(username);
                    napTheoMocManager.checkMonth(username);
                    plugin.getLogger().info(username + " n\u1ea1p th\u1ebb th\u00e0nh c\u00f4ng. " + res);
                } else if (res.get("status").getAsString().equalsIgnoreCase("-10")) {
                    dataManager.editStatus(transaction_id, res.get("status").getAsString());
                    Player player = Bukkit.getPlayer((String)username);
                    if (player != null) {
                        player.sendMessage("\u00a7cn\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i " + res);
                    }
                    plugin.getLogger().info(username + " n\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i. " + res);
                } else if (res.get("status").getAsString().equalsIgnoreCase("10")) {
                    dataManager.editStatus(transaction_id, res.get("status").getAsString());
                    Player player = Bukkit.getPlayer((String)username);
                    if (player != null) {
                        player.sendMessage("\u00a7cn\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i " + res);
                    }
                    plugin.getLogger().info(username + " n\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i. " + res);
                } else if (!res.get("status").getAsString().equalsIgnoreCase("-9")) {
                    dataManager.editStatus(transaction_id, res.get("status").getAsString());
                    Player player = Bukkit.getPlayer((String)username);
                    if (player != null) {
                        player.sendMessage("\u00a7cn\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i " + res);
                    }
                    plugin.getLogger().info(username + " n\u1ea1p th\u1ebb th\u1ea5t b\u1ea1i. " + res);
                }
                bufferedReader.close();
            }
            connection.disconnect();
        }
    }
}

