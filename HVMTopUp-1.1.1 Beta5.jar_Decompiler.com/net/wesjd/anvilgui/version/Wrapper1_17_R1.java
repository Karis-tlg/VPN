/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPosition
 *  net.minecraft.network.chat.ChatComponentText
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.chat.IChatBaseComponent$ChatSerializer
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutCloseWindow
 *  net.minecraft.network.protocol.game.PacketPlayOutExperience
 *  net.minecraft.network.protocol.game.PacketPlayOutOpenWindow
 *  net.minecraft.server.level.EntityPlayer
 *  net.minecraft.world.IInventory
 *  net.minecraft.world.entity.player.EntityHuman
 *  net.minecraft.world.inventory.Container
 *  net.minecraft.world.inventory.ContainerAccess
 *  net.minecraft.world.inventory.ContainerAnvil
 *  net.minecraft.world.inventory.Containers
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.level.World
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_17_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_17_R1.event.CraftEventFactory
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
import net.minecraft.network.protocol.game.PacketPlayOutExperience;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAccess;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.World;
import net.wesjd.anvilgui.version.VersionWrapper;
import net.wesjd.anvilgui.version.special.AnvilContainer1_17_1_R1;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Wrapper1_17_R1
implements VersionWrapper {
    private final boolean IS_ONE_SEVENTEEN_ONE = Bukkit.getBukkitVersion().contains("1.17.1");

    private int getRealNextContainerId(Player player) {
        return this.toNMS(player).nextContainerCounter();
    }

    @Override
    public int getNextContainerId(Player player, VersionWrapper.AnvilContainerWrapper container) {
        if (this.IS_ONE_SEVENTEEN_ONE) {
            return ((AnvilContainer1_17_1_R1)container).getContainerId();
        }
        return ((AnvilContainer)container).getContainerId();
    }

    @Override
    public void handleInventoryCloseEvent(Player player) {
        CraftEventFactory.handleInventoryCloseEvent((EntityHuman)this.toNMS(player));
        this.toNMS(player).o();
    }

    @Override
    public void sendPacketOpenWindow(Player player, int containerId, Object guiTitle) {
        this.toNMS((Player)player).b.sendPacket((Packet)new PacketPlayOutOpenWindow(containerId, Containers.h, (IChatBaseComponent)guiTitle));
    }

    @Override
    public void sendPacketCloseWindow(Player player, int containerId) {
        this.toNMS((Player)player).b.sendPacket((Packet)new PacketPlayOutCloseWindow(containerId));
    }

    @Override
    public void sendPacketSetExperience(Player player, int experienceLevel) {
        this.toNMS((Player)player).b.sendPacket((Packet)new PacketPlayOutExperience(0.0f, experienceLevel, 0));
    }

    @Override
    public void setActiveContainerDefault(Player player) {
        this.toNMS((Player)player).bV = this.toNMS((Player)player).bU;
    }

    @Override
    public void setActiveContainer(Player player, VersionWrapper.AnvilContainerWrapper container) {
        this.toNMS((Player)player).bV = (Container)container;
    }

    @Override
    public void setActiveContainerId(VersionWrapper.AnvilContainerWrapper container, int containerId) {
    }

    @Override
    public void addActiveContainerSlotListener(VersionWrapper.AnvilContainerWrapper container, Player player) {
        this.toNMS(player).initMenu((Container)container);
    }

    @Override
    public VersionWrapper.AnvilContainerWrapper newContainerAnvil(Player player, Object guiTitle) {
        if (this.IS_ONE_SEVENTEEN_ONE) {
            return new AnvilContainer1_17_1_R1(player, this.getRealNextContainerId(player), (IChatBaseComponent)guiTitle);
        }
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
            super(Wrapper1_17_R1.this.getRealNextContainerId(player), ((CraftPlayer)player).getHandle().getInventory(), ContainerAccess.at((World)((CraftWorld)player.getWorld()).getHandle(), (BlockPosition)new BlockPosition(0, 0, 0)));
            this.checkReachable = false;
            this.setTitle(guiTitle);
        }

        public void i() {
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
}

