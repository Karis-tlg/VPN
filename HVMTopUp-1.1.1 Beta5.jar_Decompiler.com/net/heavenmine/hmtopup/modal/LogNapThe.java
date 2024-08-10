/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.heavenmine.hmtopup.modal;

import java.util.Date;

public class LogNapThe {
    private int id;
    private String username;
    private String loaithe;
    private String menhgia;
    private String seri;
    private String code;
    private String transaction_id;
    private Date date;
    private String date2;
    private String status;

    public LogNapThe(int id, String username, String loaithe, String menhgia, String seri, String code, String transaction_id, Date date, String status) {
        this.id = id;
        this.username = username;
        this.loaithe = loaithe;
        this.menhgia = menhgia;
        this.seri = seri;
        this.code = code;
        this.transaction_id = transaction_id;
        this.date = date;
        this.status = status;
    }

    public LogNapThe(int id, String username, String loaithe, String menhgia, String seri, String code, String transactionId, String date2, String status) {
        this.id = id;
        this.username = username;
        this.loaithe = loaithe;
        this.menhgia = menhgia;
        this.seri = seri;
        this.code = code;
        this.transaction_id = transactionId;
        this.date2 = date2;
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoaithe() {
        return this.loaithe;
    }

    public void setLoaithe(String loaithe) {
        this.loaithe = loaithe;
    }

    public String getMenhgia() {
        return this.menhgia;
    }

    public void setMenhgia(String menhgia) {
        this.menhgia = menhgia;
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

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDate2() {
        return this.date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "LogNapThe{id=" + this.id + ", username='" + this.username + '\'' + ", loaithe='" + this.loaithe + '\'' + ", menhgia='" + this.menhgia + '\'' + ", seri='" + this.seri + '\'' + ", code='" + this.code + '\'' + ", transaction_id='" + this.transaction_id + '\'' + ", date=" + this.date + ", date2='" + this.date2 + '\'' + ", status='" + this.status + '\'' + '}';
    }
}

