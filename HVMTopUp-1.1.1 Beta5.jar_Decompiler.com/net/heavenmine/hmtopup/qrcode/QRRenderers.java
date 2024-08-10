/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.map.MapCanvas
 *  org.bukkit.map.MapRenderer
 *  org.bukkit.map.MapView
 */
package net.heavenmine.hmtopup.qrcode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class QRRenderers
extends MapRenderer {
    private final File imageUrl;

    public QRRenderers(File imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        try {
            BufferedImage image = ImageIO.read(this.imageUrl);
            image = QRRenderers.resize(image, 128, 128);
            mapCanvas.drawImage(0, 0, (Image)image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, 4);
        BufferedImage dimg = new BufferedImage(newW, newH, 2);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}

