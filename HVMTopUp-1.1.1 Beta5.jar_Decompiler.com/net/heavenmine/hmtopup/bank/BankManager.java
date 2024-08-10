/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.bank;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.reward.NapDauManager;
import net.heavenmine.hmtopup.reward.NapTheoMocManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class BankManager {
    private HMTopUp plugin;
    private String typeBank;

    public BankManager(HMTopUp plugin, String typeBank) {
        this.plugin = plugin;
        this.typeBank = typeBank;
    }

    public void getHistory() throws IOException {
        String tokenBank = this.getTokenBank(this.typeBank);
        if (tokenBank.equalsIgnoreCase("")) {
            return;
        }
        String charBank = this.plugin.getConfig().getString("bank.noidung");
        boolean debug = this.plugin.getConfig().getBoolean("debug");
        String urlBank = this.getURLBank(this.typeBank);
        String apiUrl = "https://api.hvmstudio.net/" + urlBank + "/" + tokenBank;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
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
                if (res.get("transactions") == null) {
                    this.plugin.getLogger().info("L\u1ed7i ki\u1ec3m tra l\u1ecbch s\u1eed ng\u00e2n h\u00e0ng " + this.typeBank + ", " + String.valueOf(res));
                    return;
                }
                JsonArray tranList = res.get("transactions").getAsJsonArray();
                for (JsonElement element : tranList) {
                    JsonObject transaction = element.getAsJsonObject();
                    String transactionID = transaction.get("transactionID").getAsString();
                    String description = transaction.get("description").getAsString();
                    String typeTranfer = transaction.get("type").getAsString();
                    String amountAPI = transaction.get("amount").getAsString();
                    if (!typeTranfer.equalsIgnoreCase("IN")) continue;
                    description = description.replaceAll("-", " ").replaceAll("\\.", " ").replaceAll("_", " ");
                    if (debug) {
                        this.plugin.getLogger().info("refNo: " + transactionID + " description: " + description + " typeTranfer: " + typeTranfer + " creditAmount: " + amountAPI);
                    }
                    String[] listChar = description.split("\\s+");
                    String findChar = charBank.toLowerCase();
                    int index = -1;
                    for (int i = 0; i < listChar.length; ++i) {
                        if (!listChar[i].toLowerCase().equalsIgnoreCase(findChar)) continue;
                        index = i;
                        break;
                    }
                    if (index < 0 || index >= listChar.length - 1) continue;
                    String id = listChar[index + 1].replace(".", "");
                    String transaction_id = transactionID.replace("\\", "");
                    DataManager dataManager = new DataManager(this.plugin);
                    String username = dataManager.getUsernameById(id);
                    if (username == null) {
                        throw new IOException("username not found: " + id);
                    }
                    boolean checkBank = dataManager.getListCheckBank(transaction_id, this.typeBank);
                    if (!checkBank) {
                        int menhgia = 0;
                        try {
                            menhgia = Integer.parseInt(amountAPI);
                        } catch (NumberFormatException e) {
                            this.plugin.getLogger().warning("Loi nap the ! Amount nap khong dung dinh dang: " + amountAPI);
                            throw new IOException("Loi nap the ! Amount nap khong dung dinh dang: " + amountAPI);
                        }
                        Double ratio = this.plugin.getConfig().getDouble("topup." + menhgia + ".ratio");
                        boolean event = this.plugin.getConfig().getBoolean("event.enable");
                        if (event) {
                            Double eventBonus = this.plugin.getConfig().getDouble("event.bonus");
                            Double bankBonus = this.plugin.getConfig().getDouble("bank.bonus");
                            ratio = ratio + ratio * eventBonus + ratio * bankBonus;
                        }
                        List listCmd = this.plugin.getConfig().getStringList("topup." + menhgia + ".cmd");
                        for (String item : listCmd) {
                            Double finalRatio = ratio;
                            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), item.replace("%player%", username).replace("%amount%", finalRatio.intValue() + "")));
                        }
                        dataManager.createLogNapThe(username, this.typeBank, amountAPI, "", "", transaction_id, new Date(), "00");
                        YamlConfiguration napdau = null;
                        File napdauFile = new File(this.plugin.getDataFolder(), "naplandau.yml");
                        napdau = YamlConfiguration.loadConfiguration((File)napdauFile);
                        boolean enableNapdau = napdau.getBoolean("main.enable");
                        if (enableNapdau) {
                            NapDauManager napDauManager = new NapDauManager(dataManager, (Plugin)this.plugin);
                            napDauManager.checkNapDau(username, String.valueOf(menhgia));
                        }
                        NapTheoMocManager napTheoMocManager = new NapTheoMocManager(dataManager, (Plugin)this.plugin);
                        napTheoMocManager.checkAllTime(username);
                        napTheoMocManager.checkDay(username);
                        napTheoMocManager.checkWeek(username);
                        napTheoMocManager.checkMonth(username);
                        this.plugin.getLogger().info(username + " n\u1ea1p th\u1ebb th\u00e0nh c\u00f4ng. ");
                    }
                    if (!debug) continue;
                    this.plugin.getLogger().info("refNo: " + transactionID.replace("\\", ""));
                    this.plugin.getLogger().info("creditAmount: " + amountAPI);
                    this.plugin.getLogger().info("Ingame: " + username);
                }
                bufferedReader.close();
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTokenBank(String typeBank) {
        YamlConfiguration bankConfig = null;
        File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
        bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
        String VCBToken = bankConfig.getString("VCB.token");
        String ACBToken = bankConfig.getString("ACB.token");
        String mbBankToken = bankConfig.getString("mbBank.token");
        String BidvToken = bankConfig.getString("Bidv.token");
        String ViettinbankToken = bankConfig.getString("Viettinbank.token");
        String SeabankToken = bankConfig.getString("Seabank.token");
        switch (typeBank) {
            case "VCB": {
                return VCBToken;
            }
            case "ACB": {
                return ACBToken;
            }
            case "mbBank": {
                return mbBankToken;
            }
            case "Bidv": {
                return BidvToken;
            }
            case "Viettinbank": {
                return ViettinbankToken;
            }
            case "Seabank": {
                return SeabankToken;
            }
        }
        return "";
    }

    public String getURLBank(String typeBank) {
        switch (typeBank) {
            case "VCB": {
                return "historyapivcbv2";
            }
            case "ACB": {
                return "historyapiacbv2";
            }
            case "mbBank": {
                return "historyapimbbankv2";
            }
            case "Bidv": {
                return "historyapibidvv2";
            }
            case "Viettinbank": {
                return "historyapiviettinv2";
            }
            case "Seabank": {
                return "historyapiseabankv2";
            }
        }
        return "";
    }
}

