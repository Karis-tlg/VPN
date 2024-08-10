/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_11_R1.BlockPosition
 *  net.minecraft.server.v1_11_R1.Blocks
 *  net.minecraft.server.v1_11_R1.ChatMessage
 *  net.minecraft.server.v1_11_R1.Container
 *  net.minecraft.server.v1_11_R1.ContainerAnvil
 *  net.minecraft.server.v1_11_R1.EntityHuman
 *  net.minecraft.server.v1_11_R1.EntityPlayer
 *  net.minecraft.server.v1_11_R1.IChatBaseComponent
 *  net.minecraft.server.v1_11_R1.ICrafting
 *  net.minecraft.server.v1_11_R1.Packet
 *  net.minecraft.server.v1_11_R1.PacketPlayOutCloseWindow
 *  net.minecraft.server.v1_11_R1.PacketPlayOutExperience
 *  net.minecraft.server.v1_11_R1.PacketPlayOutOpenWindow
 *  net.minecraft.server.v1_11_R1.Slot
 *  org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_11_R1.event.CraftEventFactory
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.ChatMessage;
import net.minecraft.server.v1_11_R1.Container;
import net.minecraft.server.v1_11_R1.ContainerAnvil;
import net.minecraft.server.v1_11_R1.EntityHuman;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.ICrafting;
import net.minecraft.server.v1_11_R1.Packet;
import net.minecraft.server.v1_11_R1.PacketPlayOutCloseWindow;
import net.minecraft.server.v1_11_R1.PacketPlayOutExperience;
import net.minecraft.server.v1_11_R1.PacketPlayOutOpenWindow;
import net.minecraft.server.v1_11_R1.Slot;
import net.wesjd.anvilgui.version.VersionWrapper;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Wrapper1_11_R1
implements VersionWrapper {
    @Override
    public int getNextContainerId(Player player, VersionWrapper.AnvilContainerWrapper container) {
        return this.toNMS(player).nextContainerCounter();
    }

    @Override
    public void handleInventoryCloseEvent(Player player) {
        CraftEventFactory.handleInventoryCloseEvent((EntityHuman)this.toNMS(player));
        this.toNMS(player).s();
    }

    @Override
    public void sendPacketOpenWindow(Player player, int containerId, Object guiTitle) {
        this.toNMS((Player)player).playerConnection.sendPacket((Packet)new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", (IChatBaseComponent)new ChatMessage(Blocks.ANVIL.a() + ".name", new Object[0])));
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
        ((Container)container).windowId = containerId;
    }

    @Override
    public void addActiveContainerSlotListener(VersionWrapper.AnvilContainerWrapper container, Player player) {
        ((Container)container).addSlotListener((ICrafting)this.toNMS(player));
    }

    @Override
    public VersionWrapper.AnvilContainerWrapper newContainerAnvil(Player player, Object guiTitle) {
        return new AnvilContainer((EntityHuman)this.toNMS(player));
    }

    @Override
    public boolean isCustomTitleSupported() {
        return false;
    }

    @Override
    public Object literalChatComponent(String content) {
        return null;
    }

    @Override
    public Object jsonChatComponent(String json) {
        return null;
    }

    private EntityPlayer toNMS(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    private class AnvilContainer
    extends ContainerAnvil
    implements VersionWrapper.AnvilContainerWrapper {
        public AnvilContainer(EntityHuman entityhuman) {
            super(entityhuman.inventory, entityhuman.world, new BlockPosition(0, 0, 0), entityhuman);
        }

        public void e() {
            Slot input;
            Slot output = this.getSlot(2);
            if (!output.hasItem() && (input = this.getSlot(0)).hasItem()) {
                output.set(input.getItem().cloneItemStack());
            }
            this.a = 0;
            this.b();
        }

        public boolean a(EntityHuman entityhuman) {
            return true;
        }

        public void b(EntityHuman entityhuman) {
        }

        @Override
        public Inventory getBukkitInventory() {
            return this.getBukkitView().getTopInventory();
        }
    }
}

