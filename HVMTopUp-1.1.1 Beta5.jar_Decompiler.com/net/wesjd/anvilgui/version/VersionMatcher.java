/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package net.wesjd.anvilgui.version;

import java.util.HashMap;
import java.util.Map;
import net.wesjd.anvilgui.version.VersionWrapper;
import org.bukkit.Bukkit;

public class VersionMatcher {
    private static final Map<String, String> VERSION_TO_REVISION = new HashMap<String, String>(){
        {
            this.put("1.20", "1_20_R1");
            this.put("1.20.1", "1_20_R1");
            this.put("1.20.2", "1_20_R2");
            this.put("1.20.3", "1_20_R3");
            this.put("1.20.4", "1_20_R3");
            this.put("1.20.5", "1_20_R4");
            this.put("1.20.6", "1_20_R4");
            this.put("1.21", VersionMatcher.FALLBACK_REVISION);
        }
    };
    private static final String FALLBACK_REVISION = "1_21_R1";

    public VersionWrapper match() {
        String rVersion;
        String craftBukkitPackage = Bukkit.getServer().getClass().getPackage().getName();
        if (!craftBukkitPackage.contains(".v")) {
            String version = Bukkit.getBukkitVersion().split("-")[0];
            rVersion = VERSION_TO_REVISION.getOrDefault(version, FALLBACK_REVISION);
        } else {
            rVersion = craftBukkitPackage.split("\\.")[3].substring(1);
        }
        try {
            return (VersionWrapper)Class.forName(this.getClass().getPackage().getName() + ".Wrapper" + rVersion).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("AnvilGUI does not support server version \"" + rVersion + "\"", exception);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + rVersion, exception);
        }
    }
}

