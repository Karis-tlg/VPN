/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.heavenmine.hmtopup.modal;

public class TopUpCard {
    private String loaiThe;
    private String menhGia;
    private String seri;
    private String code;

    public TopUpCard(String loaiThe, String menhGia, String seri, String code) {
        this.loaiThe = loaiThe;
        this.menhGia = menhGia;
        this.seri = seri;
        this.code = code;
    }

    public String toString() {
        return "TopUpCard{loaiThe='" + this.loaiThe + '\'' + ", menhGia='" + this.menhGia + '\'' + ", seri='" + this.seri + '\'' + ", code='" + this.code + '\'' + '}';
    }

    public String getLoaiThe() {
        return this.loaiThe;
    }

    public void setLoaiThe(String loaiThe) {
        this.loaiThe = loaiThe;
    }

    public String getMenhGia() {
        return this.menhGia;
    }

    public void setMenhGia(String menhGia) {
        this.menhGia = menhGia;
    }

    public String getSeri() {
        return this.seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

