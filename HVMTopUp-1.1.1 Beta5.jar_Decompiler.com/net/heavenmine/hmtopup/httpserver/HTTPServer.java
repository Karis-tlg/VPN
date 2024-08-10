/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.heavenmine.hmtopup.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.cardmanager.OtherSiteManager;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.LogNapThe;
import org.bukkit.configuration.file.YamlConfiguration;

public class HTTPServer {
    private final HMTopUp plugin;
    private final DataManager dataManager;

    public HTTPServer(HMTopUp plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    public void onStart() throws IOException {
        YamlConfiguration cardConfig = null;
        File cardFile = new File(this.plugin.getDataFolder(), "card.yml");
        cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
        int port = cardConfig.getInt("other-site.port");
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new TopUpHandler(this.plugin, this.dataManager));
        server.setExecutor(null);
        server.start();
        this.plugin.getLogger().info("Server checkCard is running on port " + port);
    }

    static class TopUpHandler
    implements HttpHandler {
        private final HMTopUp plugin;
        private final DataManager dataManager;

        TopUpHandler(HMTopUp plugin, DataManager dataManager) {
            this.plugin = plugin;
            this.dataManager = dataManager;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            boolean debug = this.plugin.getConfig().getBoolean("debug");
            YamlConfiguration cardConfig = null;
            File cardFile = new File(this.plugin.getDataFolder(), "card.yml");
            cardConfig = YamlConfiguration.loadConfiguration((File)cardFile);
            String partnerKey = cardConfig.getString("other-site.partnerKey");
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String[] params = query.split("&");
                String status = null;
                String code = null;
                String serial = null;
                String trans_id = null;
                String telco = null;
                String callback_sign = null;
                String request_id = null;
                String message = null;
                String value = null;
                String amount = null;
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if ("status".equals(keyValue[0])) {
                        status = keyValue[1];
                        continue;
                    }
                    if ("code".equals(keyValue[0])) {
                        code = keyValue[1];
                        continue;
                    }
                    if ("serial".equals(keyValue[0])) {
                        serial = keyValue[1];
                        continue;
                    }
                    if ("trans_id".equals(keyValue[0])) {
                        trans_id = keyValue[1];
                        continue;
                    }
                    if ("telco".equals(keyValue[0])) {
                        telco = keyValue[1];
                        continue;
                    }
                    if ("callback_sign".equals(keyValue[0])) {
                        callback_sign = keyValue[1];
                        continue;
                    }
                    if ("request_id".equals(keyValue[0])) {
                        request_id = keyValue[1];
                        continue;
                    }
                    if ("message".equals(keyValue[0])) {
                        message = keyValue[1];
                        continue;
                    }
                    if ("value".equals(keyValue[0])) {
                        value = keyValue[1];
                        continue;
                    }
                    if (!"amount".equals(keyValue[0])) continue;
                    amount = keyValue[1];
                }
                OtherSiteManager otherSiteManager = new OtherSiteManager(this.plugin, this.dataManager);
                String check_sign = Utils.getMD5Hash(partnerKey + code + serial);
                if (status != null && code != null && serial != null && trans_id != null && telco != null && callback_sign != null) {
                    if (debug) {
                        this.plugin.getLogger().info("Status: " + status + " Code: " + code + " serial: " + serial + " trans_id: " + trans_id + " telco: " + telco + " callback_sign: " + callback_sign + " request_id: " + request_id + " message: " + message + " value: " + value + " amount: " + amount);
                        this.plugin.getLogger().info("check_sign: " + check_sign);
                    }
                    if (check_sign.equalsIgnoreCase(callback_sign)) {
                        List<LogNapThe> logNapThes = this.dataManager.getListCheckCard("99");
                        for (LogNapThe item : logNapThes) {
                            if (debug) {
                                this.plugin.getLogger().info(item.toString());
                            }
                            if (!item.getTransaction_id().equalsIgnoreCase(request_id) || !item.getSeri().equalsIgnoreCase(serial)) continue;
                            otherSiteManager.checkCard(request_id, item.getUsername(), value, status);
                        }
                    }
                }
                String response = "OK";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if ("POST".equals(exchange.getRequestMethod())) {
                String line;
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder requestBody = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    requestBody.append(line);
                }
                br.close();
                isr.close();
                this.plugin.getLogger().info("Received body: " + requestBody.toString());
                String response = "OK";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                this.plugin.getLogger().info("===Method Not Allowed===");
                String response = "OK";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}

