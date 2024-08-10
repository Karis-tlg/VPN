package net.heavenmine.hmtopup;

public class HMLicense {
    private final String licenseKey;
    private final String product;
    private final String version;
    private int statusCode;
    private String discordName;
    private String discordID;
    private String statusMsg;
    private boolean valid;

    public HMLicense(String licenseKey, String product, String version) {
        this.licenseKey = licenseKey;
        this.product = product;
        this.version = version;

        this.statusCode = 200; 
        this.statusMsg = "License is valid (bypassed)";
        this.discordName = "BypassedUser";
        this.discordID = "123456789";
        this.valid = true; 
    }

    public boolean isValid() {
        return this.valid;
    }

    public String getStatusMsg() {
        return this.statusMsg;
    }

    public String getDiscordName() {
        return this.discordName;
    }

    public String getDiscordID() {
        return this.discordID;
    }
}
