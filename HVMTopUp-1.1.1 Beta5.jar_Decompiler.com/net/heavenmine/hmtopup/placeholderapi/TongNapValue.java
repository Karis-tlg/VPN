/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.expansion.PlaceholderExpansion
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.heavenmine.hmtopup.placeholderapi;

import java.sql.SQLException;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.heavenmine.hmtopup.Utils;
import net.heavenmine.hmtopup.database.DataManager;
import net.heavenmine.hmtopup.modal.TopNapThe;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class TongNapValue
extends PlaceholderExpansion {
    private final Plugin plugin;
    private final DataManager dataManager;

    public TongNapValue(Plugin plugin, DataManager dataManager) {
        this.plugin = plugin;
        this.dataManager = dataManager;
    }

    @NotNull
    public String getIdentifier() {
        return "hmtopup";
    }

    @NotNull
    public String getAuthor() {
        return String.join((CharSequence)", ", this.plugin.getDescription().getAuthors());
    }

    @NotNull
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String params) {
        if (params.equalsIgnoreCase("tongnap")) {
            double alltime = this.dataManager.getTongNap(player.getName());
            return alltime + "";
        }
        if (params.equalsIgnoreCase("tongnap_day")) {
            double day = this.dataManager.getTongNapToday(player.getName());
            return day + "";
        }
        if (params.equalsIgnoreCase("tongnap_week")) {
            double week = this.dataManager.getTongNapThisWeek(player.getName());
            return week + "";
        }
        if (params.equalsIgnoreCase("tongnap_month")) {
            double month = this.dataManager.getTongNapThisMonth(player.getName());
            return month + "";
        }
        if (params.startsWith("tongnap_") && params.endsWith("_alltime_name")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "-";
                }
                TopNapThe topNapThe = this.dataManager.getTopUsersByMenhGia(offset);
                return topNapThe.getUsername();
            } catch (NumberFormatException e) {
                return "-";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_alltime_value")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "0";
                }
                TopNapThe topNapThe = this.dataManager.getTopUsersByMenhGia(offset);
                return Utils.formatNumber(topNapThe.getTongMenhGia());
            } catch (NumberFormatException e) {
                return "0";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_day_name")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "-";
                }
                TopNapThe topNapThe = this.dataManager.getTop10UsersByMenhGiaToday(offset);
                return topNapThe.getUsername();
            } catch (NumberFormatException e) {
                return "-";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_day_value")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "0";
                }
                TopNapThe topNapThe = this.dataManager.getTop10UsersByMenhGiaToday(offset);
                return Utils.formatNumber(topNapThe.getTongMenhGia());
            } catch (NumberFormatException e) {
                return "0";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_week_name")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "-";
                }
                TopNapThe topNapThe = this.dataManager.getTop10UsersByMenhGiaThisWeek(offset);
                return topNapThe.getUsername();
            } catch (NumberFormatException e) {
                return "-";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_week_value")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "0";
                }
                TopNapThe topNapThe = this.dataManager.getTop10UsersByMenhGiaThisWeek(offset);
                return Utils.formatNumber(topNapThe.getTongMenhGia());
            } catch (NumberFormatException e) {
                return "0";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_month_name")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "-";
                }
                TopNapThe topNapThe = this.dataManager.getTop10UsersByMenhGiaThisMonth(offset);
                return topNapThe.getUsername();
            } catch (NumberFormatException e) {
                return "-";
            } catch (SQLException e) {
                return null;
            }
        }
        if (params.startsWith("tongnap_") && params.endsWith("_month_value")) {
            String[] parts = params.split("_");
            try {
                int offset = Integer.parseInt(parts[1]);
                if (offset <= 0) {
                    return "0";
                }
                TopNapThe topNapThe = this.dataManager.getTop10UsersByMenhGiaThisMonth(offset);
                return Utils.formatNumber(topNapThe.getTongMenhGia());
            } catch (NumberFormatException e) {
                return "0";
            } catch (SQLException e) {
                return null;
            }
        }
        return null;
    }
}

