/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.heavenmine.hmtopup.modal;

public class HistoryGuiCache {
    private String playerName;
    private boolean isAllLog;
    private String type;

    public HistoryGuiCache(String playerName, boolean isAllLog, String type) {
        this.playerName = playerName;
        this.isAllLog = isAllLog;
        this.type = type;
    }

    public String toString() {
        return "HistoryGui{playerName='" + this.playerName + '\'' + ", isAllLog=" + this.isAllLog + ", type='" + this.type + '\'' + '}';
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isAllLog() {
        return this.isAllLog;
    }

    public void setAllLog(boolean allLog) {
        this.isAllLog = allLog;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

