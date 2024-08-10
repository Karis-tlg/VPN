/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.image;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import net.heavenmine.hmtopup.HMTopUp;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ImageManager {
    private final HMTopUp plugin;

    public ImageManager(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void downloadImage(String imageUrl, String fileName) {
        File pluginFolder = this.plugin.getDataFolder();
        File imageFolder = new File(pluginFolder, "images");
        this.plugin.getLogger().info("\u0110ang t\u1ea3i QR...");
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            try {
                BufferedImage image = ImageIO.read(new URL(imageUrl));
                if (image != null) {
                    File imageFile = new File(imageFolder, System.currentTimeMillis() + ".png");
                    ImageIO.write((RenderedImage)image, "png", imageFile);
                    Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.plugin.getLogger().info("QR \u0111\u00e3 \u0111\u01b0\u1ee3c l\u01b0u!"));
                } else {
                    Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.plugin.getLogger().severe("Kh\u00f4ng th\u1ec3 t\u1ea3i QR..."));
                }
            } catch (IOException e) {
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                    this.plugin.getLogger().severe("\u0110\u00e3 x\u1ea3y ra l\u1ed7i khi l\u01b0u QR.");
                    e.printStackTrace();
                });
            }
        });
    }
}

