/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPosition
 *  net.minecraft.core.HolderLookup$a
 *  net.minecraft.core.IRegistryCustom
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.network.chat.IChatBaseComponent$ChatSerializer
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.PacketPlayOutCloseWindow
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
 *  org.bukkit.craftbukkit.v1_20_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package net.wesjd.anvilgui.version;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutCloseWindow;
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
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R4.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class Wrapper1_20_R4
implements VersionWrapper {
    private int getRealNextContainerId(Player player) {
        return this.toNMS(player).nextContainerCounter();
    }

    private EntityPlayer toNMS(Player player) {
        return ((CraftPlayer)player).getHandle();
    }

    @Override
    public int getNextContainerId(Player player, VersionWrapper.AnvilContainerWrapper container) {
        return ((AnvilContainer)container).getContainerId();
    }

    @Override
    public void handleInventoryCloseEvent(Player player) {
        CraftEventFactory.handleInventoryCloseEvent((EntityHuman)this.toNMS(player));
        this.toNMS(player).s();
    }

    @Override
    public void sendPacketOpenWindow(Player player, int containerId, Object inventoryTitle) {
        this.toNMS((Player)player).c.b((Packet)new PacketPlayOutOpenWindow(containerId, Containers.i, (IChatBaseComponent)inventoryTitle));
    }

    @Override
    public void sendPacketCloseWindow(Player player, int containerId) {
        this.toNMS((Player)player).c.b((Packet)new PacketPlayOutCloseWindow(containerId));
    }

    @Override
    public void sendPacketSetExperience(Player player, int experienceLevel) {
        this.toNMS((Player)player).c.b((Packet)new PacketPlayOutCloseWindow(experienceLevel));
    }

    @Override
    public void setActiveContainerDefault(Player player) {
        this.toNMS((Player)player).cb = this.toNMS((Player)player).ca;
    }

    @Override
    public void setActiveContainer(Player player, VersionWrapper.AnvilContainerWrapper container) {
        this.toNMS((Player)player).cb = (Container)container;
    }

    @Override
    public void setActiveContainerId(VersionWrapper.AnvilContainerWrapper container, int containerId) {
    }

    @Override
    public void addActiveContainerSlotListener(VersionWrapper.AnvilContainerWrapper container, Player player) {
        this.toNMS(player).a((Container)container);
    }

    @Override
    public VersionWrapper.AnvilContainerWrapper newContainerAnvil(Player player, Object title) {
        return new AnvilContainer(player, this.getRealNextContainerId(player), (IChatBaseComponent)title);
    }

    @Override
    public Object literalChatComponent(String content) {
        return IChatBaseComponent.b((String)content);
    }

    @Override
    public Object jsonChatComponent(String json) {
        return IChatBaseComponent.ChatSerializer.a((String)json, (HolderLookup.a)IRegistryCustom.b);
    }

    private static class AnvilContainer
    extends ContainerAnvil
    implements VersionWrapper.AnvilContainerWrapper {
        public AnvilContainer(Player player, int containerId, IChatBaseComponent guiTitle) {
            super(containerId, ((CraftPlayer)player).getHandle().gc(), ContainerAccess.a((World)((CraftWorld)player.getWorld()).getHandle(), (BlockPosition)new BlockPosition(0, 0, 0)));
            this.checkReachable = false;
            this.setTitle(guiTitle);
        }

        public void m() {
            Slot output = this.b(2);
            if (!output.h()) {
                output.f(this.b(0).g().s());
            }
            this.w.a(0);
            this.b();
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
            Slot inputLeft = this.b(0);
            if (inputLeft.h()) {
                inputLeft.g().b(DataComponents.g, (Object)IChatBaseComponent.b((String)text));
            }
        }

        @Override
        public Inventory getBukkitInventory() {
            return this.getBukkitView().getTopInventory();
        }
    }
}

