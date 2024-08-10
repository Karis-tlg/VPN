/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package net.heavenmine.hmtopup.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.modal.LogNapThe;
import net.heavenmine.hmtopup.modal.TopNapThe;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataManager {
    private static HikariConfig configSQL = new HikariConfig();
    private static HikariDataSource dataSource;
    private final HMTopUp plugin;

    public DataManager(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void onLoad() throws IOException {
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)configFile);
        String type = config.getString("storage.type");
        if (type.equalsIgnoreCase("mysql")) {
            String host = config.getString("storage.host");
            String port = config.getString("storage.port");
            String username = config.getString("storage.username");
            String password = config.getString("storage.password");
            String dbname = config.getString("storage.dbname");
            configSQL.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + dbname);
            configSQL.setUsername(username);
            configSQL.setPassword(password);
            dataSource = new HikariDataSource(configSQL);
            String log_nap_the = "CREATE TABLE IF NOT EXISTS log_nap_the (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), loaithe VARCHAR(255), menhgia VARCHAR(255), seri VARCHAR(255), code VARCHAR(255), transaction_id VARCHAR(255), date VARCHAR(255), status VARCHAR(255));";
            String log_nap_dau = "CREATE TABLE IF NOT EXISTS log_nap_dau (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, status INT NOT NULL, date VARCHAR(255) NOT NULL);";
            String log_moc_nap = "CREATE TABLE IF NOT EXISTS log_moc_nap (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, money VARCHAR(255) NOT NULL, status INT NOT NULL, date VARCHAR(255) NOT NULL);";
            String hmtopup_user = "CREATE TABLE IF NOT EXISTS hmtopup_user (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255) NOT NULL, date VARCHAR(255) NOT NULL);";
            try (Connection connection2 = dataSource.getConnection();){
                PreparedStatement statement = connection2.prepareStatement(log_nap_the);
                statement.executeUpdate();
                PreparedStatement statement2 = connection2.prepareStatement(log_nap_dau);
                statement2.executeUpdate();
                PreparedStatement statement3 = connection2.prepareStatement(log_moc_nap);
                statement3.executeUpdate();
                PreparedStatement statement4 = connection2.prepareStatement(hmtopup_user);
                statement4.executeUpdate();
            } catch (SQLException connection2) {}
        } else {
            File dbFile = new File(this.plugin.getDataFolder(), "database.db");
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            dataSource = new HikariDataSource(hikariConfig);
            String log_nap_the = "CREATE TABLE IF NOT EXISTS log_nap_the (\n    id INTEGER PRIMARY KEY AUTOINCREMENT,\n    username TEXT,\n    loaithe TEXT,\n    menhgia TEXT,\n    seri TEXT,\n    code TEXT,\n    transaction_id TEXT,\n    date TEXT,\n    status TEXT\n);\n";
            String log_nap_dau = "CREATE TABLE IF NOT EXISTS log_nap_dau (    id INTEGER PRIMARY KEY AUTOINCREMENT,    username TEXT NOT NULL,    status INTEGER NOT NULL,    date TEXT NOT NULL);";
            String log_moc_nap = "CREATE TABLE IF NOT EXISTS log_moc_nap (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, type TEXT NOT NULL, money TEXT NOT NULL, status INTEGER NOT NULL, date TEXT NOT NULL);";
            String hmtopup_user = "CREATE TABLE IF NOT EXISTS hmtopup_user (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, date TEXT NOT NULL);";
            try (Connection connection = dataSource.getConnection();){
                PreparedStatement statement = connection.prepareStatement(log_nap_the);
                statement.executeUpdate();
                PreparedStatement statement2 = connection.prepareStatement(log_nap_dau);
                statement2.executeUpdate();
                PreparedStatement statement3 = connection.prepareStatement(log_moc_nap);
                statement3.executeUpdate();
                PreparedStatement statement4 = connection.prepareStatement(hmtopup_user);
                statement4.executeUpdate();
            } catch (SQLException e) {
                this.plugin.getLogger().warning("Failed to create table\n" + e);
            }
        }
    }

    public void createLogNapThe(String username, String loaithe, String menhgia, String seri, String code, String transaction_id, Date date, String status) {
        String sql = "INSERT INTO log_nap_the (username, loaithe, menhgia, seri, code, transaction_id, date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, loaithe);
            statement.setString(3, menhgia);
            statement.setString(4, seri);
            statement.setString(5, code);
            statement.setString(6, transaction_id);
            statement.setString(7, Utils.DateToString(date));
            statement.setString(8, status);
            statement.executeUpdate();
        } catch (SQLException sQLException) {
            // empty catch block
        }
    }

    public List<LogNapThe> getListCheckCard(String checkStatus) {
        String sql = "SELECT * FROM log_nap_the WHERE status = ?";
        ArrayList<LogNapThe> logNapThes = new ArrayList<LogNapThe>();
        try (Connection connection = dataSource.getConnection();){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, checkStatus);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String loaithe = resultSet.getString("loaithe");
                String menhgia = resultSet.getString("menhgia");
                String seri = resultSet.getString("seri");
                String code = resultSet.getString("code");
                String transaction_id = resultSet.getString("transaction_id");
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");
                LogNapThe logNapThe = new LogNapThe(id, username, loaithe, menhgia, seri, code, transaction_id, date, status);
                logNapThes.add(logNapThe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logNapThes;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean getListCheckBank(String transactionId, String typeBank) {
        String sql = "SELECT * FROM log_nap_the WHERE loaithe = ? AND transaction_id = ?";
        try (Connection connection = dataSource.getConnection();){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, typeBank);
            statement.setString(2, transactionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean bl2 = true;
                return bl2;
            }
            boolean bl = false;
            return bl;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void editStatus(String transaction_id, String status) {
        String sql = "UPDATE log_nap_the SET status = ? WHERE transaction_id = ?";
        try (Connection connection = dataSource.getConnection();){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setString(2, transaction_id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // empty if block
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LogNapThe> getNapTheHistory(String username, int page) {
        int limit = 36;
        int offset = (page - 1) * limit;
        String sql = "SELECT * FROM log_nap_the WHERE username = ? ORDER BY date ASC LIMIT ? OFFSET ?";
        ArrayList<LogNapThe> logNapThe = new ArrayList<LogNapThe>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String loaithe = resultSet.getString("loaithe");
                String menhgia = resultSet.getString("menhgia");
                String seri = resultSet.getString("seri");
                String code = resultSet.getString("code");
                String transaction_id = resultSet.getString("transaction_id");
                Timestamp date = resultSet.getTimestamp("date");
                String status = resultSet.getString("status");
                logNapThe.add(new LogNapThe(id, username, loaithe, menhgia, seri, code, transaction_id, date, status));
            }
            if (logNapThe.isEmpty()) {
                // empty if block
            }
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error: \n" + e);
        }
        return logNapThe;
    }

    public List<LogNapThe> getAllNapTheHistory(int page) {
        int limit = 36;
        int offset = (page - 1) * limit;
        String sql = "SELECT * FROM log_nap_the ORDER BY date ASC LIMIT ? OFFSET ?";
        ArrayList<LogNapThe> logNapThe = new ArrayList<LogNapThe>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String loaithe = resultSet.getString("loaithe");
                String menhgia = resultSet.getString("menhgia");
                String seri = resultSet.getString("seri");
                String code = resultSet.getString("code");
                String transaction_id = resultSet.getString("transaction_id");
                Timestamp date = resultSet.getTimestamp("date");
                String status = resultSet.getString("status");
                String username = resultSet.getString("username");
                logNapThe.add(new LogNapThe(id, username, loaithe, menhgia, seri, code, transaction_id, date, status));
            }
            if (logNapThe.isEmpty()) {
                // empty if block
            }
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error: \n" + e);
        }
        return logNapThe;
    }

    public Map<String, Double> getTopNapThe() {
        HashMap<String, Double> topUsers = new HashMap<String, Double>();
        String sql = "SELECT username, SUM(menhgia) AS totalAmount FROM log_nap_the WHERE status = '00' GROUP BY username ORDER BY totalAmount DESC LIMIT 5";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                double totalAmount = resultSet.getDouble("totalAmount");
                topUsers.put(username, totalAmount);
            }
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error: \n" + e);
        }
        return topUsers;
    }

    public double getTongNap(String username) {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE username = ? AND status = '00'";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapToday(String username) {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE username = ? AND status = '00' AND SUBSTR(date, 1, 10) = ?";
        LocalDate today = LocalDate.now();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, today.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapThisWeek(String username) {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE username = ? AND status = '00' AND DATE(date) BETWEEN ? AND ?";
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, startOfWeek.toString());
            statement.setString(3, endOfWeek.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapThisMonth(String username) {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE username = ? AND status = '00' AND SUBSTR(date, 1, 7) = ?";
        LocalDate today = LocalDate.now();
        String currentMonth = today.toString().substring(0, 7);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, currentMonth);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapAllPlayer() {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE status = '00'";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapTodayAllPlayer() {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE status = '00' AND SUBSTR(date, 1, 10) = ?";
        LocalDate today = LocalDate.now();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, today.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapThisWeekAllPlayer() {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE status = '00' AND DATE(date) BETWEEN ? AND ?";
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, startOfWeek.toString());
            statement.setString(2, endOfWeek.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public double getTongNapThisMonthAllPlayer() {
        double totalMenhgia = 0.0;
        String sql = "SELECT SUM(menhgia) AS total_menhgia FROM log_nap_the WHERE status = '00' AND SUBSTR(date, 1, 7) = ?";
        LocalDate today = LocalDate.now();
        String currentMonth = today.toString().substring(0, 7);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, currentMonth);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalMenhgia = resultSet.getDouble("total_menhgia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalMenhgia;
    }

    public void addLogNapDau(String username, int status, Date date) {
        String sql = "INSERT INTO log_nap_dau (username, status, date) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setInt(2, status);
            statement.setString(3, Utils.DateToString(date));
            statement.executeUpdate();
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error adding log_nap_dau: \n" + e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean checkNapDau(String username) {
        String sql = "SELECT COUNT(*) FROM log_nap_dau WHERE username = ? AND status = 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            int count = resultSet.getInt(1);
            boolean bl = count > 0;
            return bl;
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error checking username status: \n" + e);
        }
        return false;
    }

    public void addLogMocNap(String username, String type, String money, int status, Date date) {
        String sql = "INSERT INTO log_moc_nap (username, type, money, status, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, type);
            statement.setString(3, money);
            statement.setInt(4, status);
            statement.setString(5, Utils.DateToString(date));
            statement.executeUpdate();
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error adding log_moc_nap: \n" + e);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean checkMocNap(String username, String type, String money) {
        String sql = "SELECT COUNT(*) FROM log_moc_nap WHERE username = ? AND type = ? AND money = ? AND status = 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, type);
            statement.setString(3, money);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            int count = resultSet.getInt(1);
            boolean bl = count > 0;
            return bl;
        } catch (SQLException sQLException) {
            // empty catch block
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean checkMocNapToDay(String username, String type, String money) {
        String sql = "SELECT COUNT(*) FROM log_moc_nap WHERE username = ? AND type = ? AND money = ? AND status = 1 AND date = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, type);
            statement.setString(3, money);
            statement.setString(4, LocalDate.now().toString());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            int count = resultSet.getInt(1);
            boolean bl = count > 0;
            return bl;
        } catch (SQLException sQLException) {
            // empty catch block
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean checkMocNapThisWeek(String username, String type, String money) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        String sql = "SELECT COUNT(*) FROM log_moc_nap WHERE username = ? AND type = ? AND money = ? AND status = 1 AND date >= ? AND date <= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, type);
            statement.setString(3, money);
            statement.setString(4, startOfWeek.toString());
            statement.setString(5, endOfWeek.toString());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            int count = resultSet.getInt(1);
            boolean bl = count > 0;
            return bl;
        } catch (SQLException sQLException) {
            // empty catch block
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean checkMocNapThisMonth(String username, String type, String money) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        String sql = "SELECT COUNT(*) FROM log_moc_nap WHERE username = ? AND type = ? AND money = ? AND status = 1 AND date >= ? AND date <= ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            statement.setString(2, type);
            statement.setString(3, money);
            statement.setString(4, startOfMonth.toString());
            statement.setString(5, endOfMonth.toString());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            int count = resultSet.getInt(1);
            boolean bl = count > 0;
            return bl;
        } catch (SQLException sQLException) {
            // empty catch block
        }
        return false;
    }

    public void createUser(String username, Date date) {
        String sql = "INSERT INTO hmtopup_user (username, date) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, Utils.DateToString(date));
            statement.executeUpdate();
        } catch (SQLException sQLException) {
            // empty catch block
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM hmtopup_user WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return -1;
            int n = resultSet.getInt("id");
            return n;
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error getting user ID: \n" + e);
        }
        return -1;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getUsernameById(String id) {
        String sql = "SELECT username FROM hmtopup_user WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return null;
            String string = resultSet.getString("username");
            return string;
        } catch (SQLException e) {
            this.plugin.getLogger().info("Error getting username: \n" + e);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean checkUserName(String username) {
        String sql = "SELECT COUNT(*) FROM hmtopup_user WHERE username = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return false;
            int count = resultSet.getInt(1);
            boolean bl = count > 0;
            return bl;
        } catch (SQLException sQLException) {
            // empty catch block
        }
        return false;
    }

    public TopNapThe getTopUsersByMenhGia(int offset) throws SQLException {
        TopNapThe topNapThe = new TopNapThe("-", 0);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT username, SUM(menhgia) AS tong_menh_gia FROM log_nap_the WHERE status = '00' GROUP BY username ORDER BY tong_menh_gia DESC LIMIT 1 OFFSET " + (offset - 1));
             ResultSet rs = pstmt.executeQuery();){
            if (rs.next()) {
                String username = rs.getString("username");
                int tongMenhGia = rs.getInt("tong_menh_gia");
                topNapThe.setUsername(username);
                topNapThe.setTongMenhGia(tongMenhGia);
            }
        }
        return topNapThe;
    }

    public TopNapThe getTop10UsersByMenhGiaToday(int offset) throws SQLException {
        TopNapThe topNapThe = new TopNapThe("-", 0);
        LocalDate today = LocalDate.now();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT username, SUM(menhgia) AS tong_menh_gia FROM log_nap_the WHERE status = '00' AND SUBSTR(date, 1, 10) = ? GROUP BY username ORDER BY tong_menh_gia DESC LIMIT 1 OFFSET " + (offset - 1));){
            pstmt.setString(1, today.toString());
            try (ResultSet rs = pstmt.executeQuery();){
                if (rs.next()) {
                    String username = rs.getString("username");
                    int tongMenhGia = rs.getInt("tong_menh_gia");
                    topNapThe.setUsername(username);
                    topNapThe.setTongMenhGia(tongMenhGia);
                }
            }
        }
        return topNapThe;
    }

    public TopNapThe getTop10UsersByMenhGiaThisWeek(int offset) throws SQLException {
        TopNapThe topNapThe = new TopNapThe("-", 0);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6L);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT username, SUM(menhgia) AS tong_menh_gia FROM log_nap_the WHERE status = '00' AND DATE(date) BETWEEN ? AND ? GROUP BY username ORDER BY tong_menh_gia DESC LIMIT 1 OFFSET " + (offset - 1));){
            pstmt.setString(1, startOfWeek.toString());
            pstmt.setString(2, endOfWeek.toString());
            try (ResultSet rs = pstmt.executeQuery();){
                if (rs.next()) {
                    String username = rs.getString("username");
                    int tongMenhGia = rs.getInt("tong_menh_gia");
                    topNapThe.setUsername(username);
                    topNapThe.setTongMenhGia(tongMenhGia);
                }
            }
        }
        return topNapThe;
    }

    public TopNapThe getTop10UsersByMenhGiaThisMonth(int offset) throws SQLException {
        TopNapThe topNapThe = new TopNapThe("-", 0);
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT username, SUM(menhgia) AS tong_menh_gia FROM log_nap_the WHERE status = '00' AND DATE(date) BETWEEN ? AND ? GROUP BY username ORDER BY tong_menh_gia DESC LIMIT 1 OFFSET " + (offset - 1));){
            pstmt.setString(1, startOfMonth.toString());
            pstmt.setString(2, endOfMonth.toString());
            try (ResultSet rs = pstmt.executeQuery();){
                if (rs.next()) {
                    String username = rs.getString("username");
                    int tongMenhGia = rs.getInt("tong_menh_gia");
                    topNapThe.setUsername(username);
                    topNapThe.setTongMenhGia(tongMenhGia);
                }
            }
        }
        return topNapThe;
    }
}

