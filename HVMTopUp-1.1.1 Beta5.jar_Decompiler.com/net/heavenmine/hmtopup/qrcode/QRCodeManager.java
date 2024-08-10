/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.map.MapCanvas
 *  org.bukkit.map.MapRenderer
 *  org.bukkit.map.MapView
 *  org.bukkit.map.MapView$Scale
 *  org.bukkit.plugin.Plugin
 */
package net.heavenmine.hmtopup.qrcode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import net.heavenmine.hmtopup.HMTopUp;
import net.heavenmine.hmtopup.modal.TopUpBank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;

public class QRCodeManager {
    private final HMTopUp plugin;

    public QRCodeManager(HMTopUp plugin) {
        this.plugin = plugin;
    }

    public void onCreateQR(TopUpBank topUpBank, Player player, String prefix, String qrCode) {
        YamlConfiguration bankConfig = null;
        File bankFile = new File(this.plugin.getDataFolder(), "bank.yml");
        bankConfig = YamlConfiguration.loadConfiguration((File)bankFile);
        String typeQR = bankConfig.getString("QRType");
        String typeBank = topUpBank.getTypeBank();
        String accNo = topUpBank.getAccNo();
        String accName = topUpBank.getAccName();
        String amount = topUpBank.getAmount();
        String noidung = topUpBank.getDescription();
        String imageUrl = "https://img.vietqr.io/image/" + typeBank + "-" + accNo + "-compact.png?amount=" + amount + "&addInfo=" + noidung.replace(" ", "%20") + "&accountName=" + accName.replace(" ", "%20");
        if (typeQR.equalsIgnoreCase("none")) {
            player.sendMessage("&a&nMa QR nap nhanh cua ban la:");
            player.sendMessage(imageUrl);
        } else if (typeQR.equalsIgnoreCase("safe")) {
            MapView mapView = this.plugin.getServer().createMap(player.getWorld());
            mapView.setScale(MapView.Scale.NORMAL);
            mapView.addRenderer((MapRenderer)new QRMapRenderer("MyQR.png"));
            player.sendMap(mapView);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&a\u0110ang t\u1ea3i QR...")));
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
                try {
                    BufferedImage image = ImageIO.read(new URL(imageUrl));
                    String imageName = player.getName() + System.currentTimeMillis() + amount;
                    if (image != null) {
                        File imageFile = new File(this.plugin.getDataFolder(), "images/" + imageName + ".png");
                        ImageIO.write((RenderedImage)image, "png", imageFile);
                        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                            ItemStack map = new ItemStack(Material.EMPTY_MAP, 1);
                            ItemMeta mapMeta = map.getItemMeta();
                            mapMeta.setDisplayName("\u00a7bQR Code");
                            map.setItemMeta(mapMeta);
                            MapView mapView = this.plugin.getServer().createMap((World)this.plugin.getServer().getWorlds().get(0));
                            topUpBank.setImageName(imageName);
                            this.plugin.saveBankCache(String.valueOf(mapView.getId()), topUpBank);
                            for (MapRenderer renderer : mapView.getRenderers()) {
                                mapView.removeRenderer(renderer);
                            }
                            player.getInventory().addItem(new ItemStack[]{map});
                            player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + qrCode)));
                        });
                    } else {
                        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&cKh\u00f4ng th\u1ec3 t\u1ea3i QR..."))));
                    }
                } catch (IOException e) {
                    Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                        player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(prefix + "&c\u0110\u00e3 x\u1ea3y ra l\u1ed7i khi l\u01b0u QR.")));
                        e.printStackTrace();
                    });
                }
            });
        }
    }

    private static class ImageRenderer
    extends MapRenderer {
        private final BufferedImage image;

        public ImageRenderer(BufferedImage image) {
            this.image = image;
        }

        public void render(MapView map, MapCanvas canvas, Player player) {
            canvas.drawImage(0, 0, (Image)this.image);
        }
    }

    private class QRMapRenderer
    extends MapRenderer {
        private final String imageName;

        private QRMapRenderer(String imageName) {
            this.imageName = imageName;
        }

        public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
            BufferedImage image;
            File imageFile = new File(QRCodeManager.this.plugin.getDataFolder(), "images/" + this.imageName);
            try {
                image = ImageIO.read(imageFile);
            } catch (IOException e) {
                QRCodeManager.this.plugin.getLogger().log(Level.SEVERE, "Kh\u00f4ng th\u1ec3 \u0111\u1ecdc h\u00ecnh \u1ea3nh t\u1eeb file " + this.imageName, e);
                return;
            }
            mapCanvas.drawImage(0, 0, (Image)image);
        }
    }
}

