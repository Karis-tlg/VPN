/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_14_R1.BlockPosition
 *  net.minecraft.server.v1_14_R1.ChatComponentText
 *  net.minecraft.server.v1_14_R1.ContainerAccess
 *  net.minecraft.server.v1_14_R1.ContainerAnvil
 *  net.minecraft.server.v1_14_R1.EntityHuman
 *  net.minecraft.server.v1_14_R1.IChatBaseComponent
 *  net.minecraft.server.v1_14_R1.IInventory
 *  net.minecraft.server.v1_14_R1.Slot
 *  net.minecraft.server.v1_14_R1.World
 *  org.bukkit.craftbukkit.v1_14_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version.special;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.ChatComponentText;
import net.minecraft.server.v1_14_R1.ContainerAccess;
import net.minecraft.server.v1_14_R1.ContainerAnvil;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.IInventory;
import net.minecraft.server.v1_14_R1.Slot;
import net.minecraft.server.v1_14_R1.World;
import net.wesjd.anvilgui.version.VersionWrapper;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AnvilContainer1_14_4_R1
extends ContainerAnvil
implements VersionWrapper.AnvilContainerWrapper {
    public AnvilContainer1_14_4_R1(Player player, int containerId, IChatBaseComponent guiTitle) {
        super(containerId, ((CraftPlayer)player).getHandle().inventory, ContainerAccess.at((World)((CraftWorld)player.getWorld()).getHandle(), (BlockPosition)new BlockPosition(0, 0, 0)));
        this.checkReachable = false;
        this.setTitle(guiTitle);
    }

    public void e() {
        Slot input;
        Slot output = this.getSlot(2);
        if (!output.hasItem() && (input = this.getSlot(0)).hasItem()) {
            output.set(input.getItem().cloneItemStack());
        }
        this.levelCost.set(0);
        this.c();
    }

    public void b(EntityHuman entityhuman) {
    }

    protected void a(EntityHuman entityhuman, World world, IInventory iinventory) {
    }

    public int getContainerId() {
        return this.windowId;
    }

    @Override
    public String getRenameText() {
        return this.renameText;
    }

    @Override
    public void setRenameText(String text) {
        Slot inputLeft = this.getSlot(0);
        if (inputLeft.hasItem()) {
            inputLeft.getItem().a((IChatBaseComponent)new ChatComponentText(text));
        }
    }

    @Override
    public Inventory getBukkitInventory() {
        return this.getBukkitView().getTopInventory();
    }
}

