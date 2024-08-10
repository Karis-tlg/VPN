/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface VersionWrapper {
    public int getNextContainerId(Player var1, AnvilContainerWrapper var2);

    public void handleInventoryCloseEvent(Player var1);

    public void sendPacketOpenWindow(Player var1, int var2, Object var3);

    public void sendPacketCloseWindow(Player var1, int var2);

    public void sendPacketSetExperience(Player var1, int var2);

    public void setActiveContainerDefault(Player var1);

    public void setActiveContainer(Player var1, AnvilContainerWrapper var2);

    public void setActiveContainerId(AnvilContainerWrapper var1, int var2);

    public void addActiveContainerSlotListener(AnvilContainerWrapper var1, Player var2);

    public AnvilContainerWrapper newContainerAnvil(Player var1, Object var2);

    default public boolean isCustomTitleSupported() {
        return true;
    }

    public Object literalChatComponent(String var1);

    public Object jsonChatComponent(String var1);

    public static interface AnvilContainerWrapper {
        default public String getRenameText() {
            return null;
        }

        default public void setRenameText(String text) {
        }

        public Inventory getBukkitInventory();
    }
}

