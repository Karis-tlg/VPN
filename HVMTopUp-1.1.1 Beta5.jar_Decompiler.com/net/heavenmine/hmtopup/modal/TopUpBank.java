/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.heavenmine.hmtopup.modal;

public class TopUpBank {
    private String typeBank;
    private String accNo;
    private String accName;
    private String amount;
    private String description;
    private boolean enableRender;
    String imageName;

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public TopUpBank(String typeBank, String accNo, String accName, String amount, String description, boolean enableRender) {
        this.typeBank = typeBank;
        this.accNo = accNo;
        this.accName = accName;
        this.amount = amount;
        this.description = description;
        this.enableRender = enableRender;
    }

    public String toString() {
        return "TopUpBank{typeBank='" + this.typeBank + '\'' + ", accNo='" + this.accNo + '\'' + ", accName='" + this.accName + '\'' + ", amount='" + this.amount + '\'' + ", description='" + this.description + '\'' + ", enableRender=" + this.enableRender + ", imageName='" + this.imageName + '\'' + '}';
    }

    public String getTypeBank() {
        return this.typeBank;
    }

    public void setTypeBank(String typeBank) {
        this.typeBank = typeBank;
    }

    public String getAccNo() {
        return this.accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAccName() {
        return this.accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnableRender() {
        return this.enableRender;
    }

    public void setEnableRender(boolean enableRender) {
        this.enableRender = enableRender;
    }
}

