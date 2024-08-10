/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.geysermc.floodgate.api.FloodgateApi
 *  org.geysermc.floodgate.api.player.FloodgatePlayer
 */
package net.heavenmine.hmtopup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class Utils {
    public static String DateFormat(Date date) {
        if (date != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            try {
                String formattedDate = outputFormat.format(date);
                return formattedDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "null";
    }

    public static String DateFormat(Date date, boolean simple) {
        if (date != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            SimpleDateFormat outputFormatSimple = new SimpleDateFormat("dd/MM");
            try {
                String formattedDate = outputFormat.format(date);
                return simple ? outputFormatSimple.format(date) : formattedDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "null";
    }

    public static String DateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xFF));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; ++i) {
            int randomNumber = random.nextInt(10);
            sb.append(randomNumber);
        }
        return sb.toString();
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)message);
    }

    public static List<String> colorize(String[] messages) {
        String[] coloredMessages = new String[messages.length];
        for (int i = 0; i < messages.length; ++i) {
            coloredMessages[i] = Utils.colorize(messages[i]);
        }
        return Arrays.asList(coloredMessages);
    }

    public static boolean isBedRockPlayer(Player player) {
        if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
            return false;
        }
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        return floodgatePlayer != null;
    }

    public static String formatNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }
}

