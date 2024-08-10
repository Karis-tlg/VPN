/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPosition
 *  net.minecraft.network.chat.ChatComponentText
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.world.IInventory
 *  net.minecraft.world.entity.player.EntityHuman
 *  net.minecraft.world.inventory.ContainerAccess
 *  net.minecraft.world.inventory.ContainerAnvil
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.level.World
 *  org.bukkit.craftbukkit.v1_17_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version.special;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.World;
import net.wesjd.anvilgui.version.VersionWrapper;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AnvilContainer1_17_1_R1
extends ContainerAnvil
implements VersionWrapper.AnvilContainerWrapper {
    public AnvilContainer1_17_1_R1(Player player, int containerId, IChatBaseComponent guiTitle) {
        super(containerId, ((CraftPlayer)player).getHandle().getInventory(), ContainerAccess.at((World)((CraftWorld)player.getWorld()).getHandle(), (BlockPosition)new BlockPosition(0, 0, 0)));
        this.checkReachable = false;
        this.setTitle(guiTitle);
    }

    public void l() {
        Slot input;
        Slot output = this.getSlot(2);
        if (!output.hasItem() && (input = this.getSlot(0)).hasItem()) {
            output.set(input.getItem().cloneItemStack());
        }
        this.w.set(0);
        this.updateInventory();
        this.d();
    }

    public void b(EntityHuman player) {
    }

    protected void a(EntityHuman player, IInventory container) {
    }

    public int getContainerId() {
        return this.j;
    }

    @Override
    public String getRenameText() {
        return this.v;
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

