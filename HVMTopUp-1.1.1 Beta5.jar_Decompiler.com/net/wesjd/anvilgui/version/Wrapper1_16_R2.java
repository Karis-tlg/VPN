/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_16_R2.BlockPosition
 *  net.minecraft.server.v1_16_R2.ChatComponentText
 *  net.minecraft.server.v1_16_R2.Container
 *  net.minecraft.server.v1_16_R2.ContainerAccess
 *  net.minecraft.server.v1_16_R2.ContainerAnvil
 *  net.minecraft.server.v1_16_R2.Containers
 *  net.minecraft.server.v1_16_R2.EntityHuman
 *  net.minecraft.server.v1_16_R2.EntityPlayer
 *  net.minecraft.server.v1_16_R2.IChatBaseComponent
 *  net.minecraft.server.v1_16_R2.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_16_R2.ICrafting
 *  net.minecraft.server.v1_16_R2.IInventory
 *  net.minecraft.server.v1_16_R2.Packet
 *  net.minecraft.server.v1_16_R2.PacketPlayOutCloseWindow
 *  net.minecraft.server.v1_16_R2.PacketPlayOutExperience
 *  net.minecraft.server.v1_16_R2.PacketPlayOutOpenWindow
 *  net.minecraft.server.v1_16_R2.Slot
 *  net.minecraft.server.v1_16_R2.World
 *  org.bukkit.craftbukkit.v1_16_R2.CraftWorld
 *  org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_16_R2.event.CraftEventFactory
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version;

import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.ChatComponentText;
import net.minecraft.server.v1_16_R2.Container;
import net.minecraft.server.v1_16_R2.ContainerAccess;
import net.minecraft.server.v1_16_R2.ContainerAnvil;
import net.minecraft.server.v1_16_R2.Containers;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.ICrafting;
import net.minecraft.server.v1_16_R2.IInventory;
import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_16_R2.PacketPlayOutExperience;
import net.minecraft.server.v1_16_R2.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_16_R2.Slot;
import net.minecraft.server.v1_16_R2.World;
import net.wesjd.anvilgui.version.VersionWrapper;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Wrapper1_16_R2
implements VersionWrapper {
    private int getRealNextContainerId(Player player) {
        return this.toNMS(player).nextContainerCounter();
    }

    @Override
    public int getNextContainerId(Player player, VersionWrapper.AnvilContainerWrapper container) {
        return ((AnvilContainer)container).getContainerId();
    }

    @Override
    public void handleInventoryCloseEvent(Player player) {
        CraftEventFactory.handleInventoryCloseEvent((EntityHuman)this.toNMS(player));
        this.toNMS(player).o();
    }

    @Override
    public void sendPacketOpenWindow(Player player, int containerId, Object guiTitle) {
        this.toNMS((Player)player).playerConnection.sendPacket((Packet)new PacketPlayOutOpenWindow(containerId, Containers.ANVIL, (IChatBaseComponent)guiTitle));
    }

    @Override
    public void sendPacketCloseWindow(Player player, int containerId) {
        this.toNMS((Player)player).playerConnection.sendPacket((Packet)new PacketPlayOutCloseWindow(containerId));
    }

    @Override
    public void sendPacketSetExperience(Player player, int experienceLevel) {
        this.toNMS((Player)player).playerConnection.sendPacket((Packet)new PacketPlayOutExperience(0.0f, experienceLevel, 0));
    }

    @Override
    public void setActiveContainerDefault(Player player) {
        this.toNMS((Player)player).activeContainer = this.toNMS((Player)player).defaultContainer;
    }

    @Override
    public void setActiveContainer(Player player, VersionWrapper.AnvilContainerWrapper container) {
        this.toNMS((Player)player).activeContainer = (Container)container;
    }

    @Override
    public void setActiveContainerId(VersionWrapper.AnvilContainerWrapper container, int containerId) {
    }

    @Override
    public void addActiveContainerSlotListener(VersionWrapper.AnvilContainerWrapper container, Player player) {
        ((Container)container).addSlotListener((ICrafting)this.toNMS(player));
    }

    @Override
    public VersionWrapper.AnvilContainerWrapper newContainerAnvil(Player player, Object guiTitle) {
        return new AnvilContainer(player, (IChatBaseComponent)guiTitle);
    }

    @Override
    public Object literalChatComponent(String content) {
        return new ChatComponentText(content);
    }

    @Override
    public Object jsonChatComponent(String json) {
        return IChatBaseComponent.ChatSerializer.a((String)json);
    }

    private EntityPlayer toNMS(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    private class AnvilContainer
    extends ContainerAnvil
    implements VersionWrapper.AnvilContainerWrapper {
        public AnvilContainer(Player player, IChatBaseComponent guiTitle) {
            super(Wrapper1_16_R2.this.getRealNextContainerId(player), ((CraftPlayer)player).getHandle().inventory, ContainerAccess.at((World)((CraftWorld)player.getWorld()).getHandle(), (BlockPosition)new BlockPosition(0, 0, 0)));
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
}

