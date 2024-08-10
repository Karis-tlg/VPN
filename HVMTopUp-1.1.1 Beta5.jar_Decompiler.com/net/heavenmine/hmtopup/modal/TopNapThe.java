/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.heavenmine.hmtopup.modal;

public class TopNapThe {
    private String username;
    private int tongMenhGia;

    public TopNapThe(String username, int tongMenhGia) {
        this.username = username;
        this.tongMenhGia = tongMenhGia;
    }

    public String toString() {
        return "TopNapThe{username='" + this.username + '\'' + ", tongMenhGia=" + this.tongMenhGia + '}';
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTongMenhGia() {
        return this.tongMenhGia;
    }

    public void setTongMenhGia(int tongMenhGia) {
        this.tongMenhGia = tongMenhGia;
    }
}

