/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryAction
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.geysermc.geyser.api.GeyserApi
 */
package net.wesjd.anvilgui;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.logging.Level;
import net.wesjd.anvilgui.version.VersionMatcher;
import net.wesjd.anvilgui.version.VersionWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.geysermc.geyser.api.GeyserApi;

public class AnvilGUI {
    private static final VersionWrapper WRAPPER = new VersionMatcher().match();
    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private final Plugin plugin;
    private final Player player;
    private final Executor mainThreadExecutor;
    private final Object titleComponent;
    private final ItemStack[] initialContents;
    private final boolean preventClose;
    private final boolean geyserCompatibility;
    private final Set<Integer> interactableSlots;
    private final Consumer<StateSnapshot> closeListener;
    private final boolean concurrentClickHandlerExecution;
    private final ClickHandler clickHandler;
    private int containerId;
    private Inventory inventory;
    private final ListenUp listener = new ListenUp();
    private boolean open;
    private VersionWrapper.AnvilContainerWrapper container;

    private static ItemStack itemNotNull(ItemStack stack) {
        return stack == null ? AIR : stack;
    }

    private AnvilGUI(Plugin plugin, Player player, Executor mainThreadExecutor, Object titleComponent, ItemStack[] initialContents, boolean preventClose, boolean geyserCompatibility, Set<Integer> interactableSlots, Consumer<StateSnapshot> closeListener, boolean concurrentClickHandlerExecution, ClickHandler clickHandler) {
        this.plugin = plugin;
        this.player = player;
        this.mainThreadExecutor = mainThreadExecutor;
        this.titleComponent = titleComponent;
        this.initialContents = initialContents;
        this.preventClose = preventClose;
        this.geyserCompatibility = geyserCompatibility;
        this.interactableSlots = Collections.unmodifiableSet(interactableSlots);
        this.closeListener = closeListener;
        this.concurrentClickHandlerExecution = concurrentClickHandlerExecution;
        this.clickHandler = clickHandler;
    }

    @Deprecated
    private AnvilGUI(Plugin plugin, Player player, Executor mainThreadExecutor, Object titleComponent, ItemStack[] initialContents, boolean preventClose, Set<Integer> interactableSlots, Consumer<StateSnapshot> closeListener, boolean concurrentClickHandlerExecution, ClickHandler clickHandler) {
        this.plugin = plugin;
        this.player = player;
        this.mainThreadExecutor = mainThreadExecutor;
        this.titleComponent = titleComponent;
        this.initialContents = initialContents;
        this.preventClose = preventClose;
        this.geyserCompatibility = true;
        this.interactableSlots = Collections.unmodifiableSet(interactableSlots);
        this.closeListener = closeListener;
        this.concurrentClickHandlerExecution = concurrentClickHandlerExecution;
        this.clickHandler = clickHandler;
    }

    private void openInventory() {
        Bukkit.getPluginManager().registerEvents((Listener)this.listener, this.plugin);
        this.container = WRAPPER.newContainerAnvil(this.player, this.titleComponent);
        this.inventory = this.container.getBukkitInventory();
        for (int i = 0; i < this.initialContents.length; ++i) {
            this.inventory.setItem(i, this.initialContents[i]);
        }
        this.containerId = WRAPPER.getNextContainerId(this.player, this.container);
        WRAPPER.handleInventoryCloseEvent(this.player);
        WRAPPER.sendPacketOpenWindow(this.player, this.containerId, this.titleComponent);
        WRAPPER.setActiveContainer(this.player, this.container);
        WRAPPER.setActiveContainerId(this.container, this.containerId);
        WRAPPER.addActiveContainerSlotListener(this.container, this.player);
        if (this.geyserCompatibility && this.plugin.getServer().getPluginManager().getPlugin("Geyser-Spigot") != null && this.plugin.getServer().getPluginManager().getPlugin("Geyser-Spigot").isEnabled() && GeyserApi.api().isBedrockPlayer(this.player.getUniqueId())) {
            WRAPPER.sendPacketSetExperience(this.player, 1);
        }
        this.open = true;
    }

    public void closeInventory() {
        this.closeInventory(true);
    }

    private void closeInventory(boolean sendClosePacket) {
        if (!this.open) {
            return;
        }
        this.open = false;
        HandlerList.unregisterAll((Listener)this.listener);
        if (sendClosePacket) {
            WRAPPER.handleInventoryCloseEvent(this.player);
            WRAPPER.setActiveContainerDefault(this.player);
            WRAPPER.sendPacketCloseWindow(this.player, this.containerId);
        }
        if (this.geyserCompatibility && this.plugin.getServer().getPluginManager().getPlugin("Geyser-Spigot") != null && this.plugin.getServer().getPluginManager().getPlugin("Geyser-Spigot").isEnabled() && GeyserApi.api().isBedrockPlayer(this.player.getUniqueId())) {
            WRAPPER.sendPacketSetExperience(this.player, this.player.getLevel());
        }
        if (this.closeListener != null) {
            this.closeListener.accept(StateSnapshot.fromAnvilGUI(this));
        }
    }

    public void setTitle(String literalTitle, boolean preserveRenameText) {
        Validate.notNull((Object)literalTitle, (String)"literalTitle cannot be null");
        this.setTitle(WRAPPER.literalChatComponent(literalTitle), preserveRenameText);
    }

    public void setJsonTitle(String json, boolean preserveRenameText) {
        Validate.notNull((Object)json, (String)"json cannot be null");
        this.setTitle(WRAPPER.jsonChatComponent(json), preserveRenameText);
    }

    private void setTitle(Object title, boolean preserveRenameText) {
        if (!WRAPPER.isCustomTitleSupported()) {
            return;
        }
        String renameText = this.container.getRenameText();
        WRAPPER.sendPacketOpenWindow(this.player, this.containerId, title);
        if (preserveRenameText) {
            this.container.setRenameText(renameText == null ? "" : renameText);
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    @FunctionalInterface
    public static interface ClickHandler
    extends BiFunction<Integer, StateSnapshot, CompletableFuture<List<ResponseAction>>> {
    }

    private class ListenUp
    implements Listener {
        private boolean clickHandlerRunning = false;

        private ListenUp() {
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if (!event.getInventory().equals((Object)AnvilGUI.this.inventory)) {
                return;
            }
            int rawSlot = event.getRawSlot();
            if (rawSlot == -999) {
                return;
            }
            Player clicker = (Player)event.getWhoClicked();
            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory != null) {
                if (clickedInventory.equals((Object)clicker.getInventory())) {
                    if (event.getClick().equals((Object)ClickType.DOUBLE_CLICK)) {
                        event.setCancelled(true);
                        return;
                    }
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                        return;
                    }
                }
                if (event.getCursor() != null && event.getCursor().getType() != Material.AIR && !AnvilGUI.this.interactableSlots.contains(rawSlot) && event.getClickedInventory().equals((Object)AnvilGUI.this.inventory)) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (rawSlot < 3 && rawSlot >= 0 || event.getAction().equals((Object)InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                event.setCancelled(!AnvilGUI.this.interactableSlots.contains(rawSlot));
                if (this.clickHandlerRunning && !AnvilGUI.this.concurrentClickHandlerExecution) {
                    return;
                }
                CompletableFuture actionsFuture = (CompletableFuture)AnvilGUI.this.clickHandler.apply(rawSlot, StateSnapshot.fromAnvilGUI(AnvilGUI.this));
                Consumer<List> actionsConsumer = actions -> {
                    for (ResponseAction action : actions) {
                        action.accept(AnvilGUI.this, clicker);
                    }
                };
                if (actionsFuture.isDone()) {
                    ((CompletableFuture)actionsFuture.thenAccept(actionsConsumer)).join();
                } else {
                    this.clickHandlerRunning = true;
                    ((CompletableFuture)actionsFuture.thenAcceptAsync(actionsConsumer, AnvilGUI.this.mainThreadExecutor)).handle((results, exception) -> {
                        if (exception != null) {
                            AnvilGUI.this.plugin.getLogger().log(Level.SEVERE, "An exception occurred in the AnvilGUI clickHandler", (Throwable)exception);
                        }
                        this.clickHandlerRunning = false;
                        return null;
                    });
                }
            }
        }

        @EventHandler
        public void onInventoryDrag(InventoryDragEvent event) {
            if (event.getInventory().equals((Object)AnvilGUI.this.inventory)) {
                for (int slot : Slot.values()) {
                    if (!event.getRawSlots().contains(slot)) continue;
                    event.setCancelled(!AnvilGUI.this.interactableSlots.contains(slot));
                    break;
                }
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (AnvilGUI.this.open && event.getInventory().equals((Object)AnvilGUI.this.inventory)) {
                AnvilGUI.this.closeInventory(false);
                if (AnvilGUI.this.preventClose) {
                    AnvilGUI.this.mainThreadExecutor.execute(() -> AnvilGUI.this.openInventory());
                }
            }
        }
    }

    public static final class StateSnapshot {
        private final ItemStack leftItem;
        private final ItemStack rightItem;
        private final ItemStack outputItem;
        private final Player player;

        private static StateSnapshot fromAnvilGUI(AnvilGUI anvilGUI) {
            Inventory inventory = anvilGUI.getInventory();
            return new StateSnapshot(AnvilGUI.itemNotNull(inventory.getItem(0)).clone(), AnvilGUI.itemNotNull(inventory.getItem(1)).clone(), AnvilGUI.itemNotNull(inventory.getItem(2)).clone(), anvilGUI.player);
        }

        public StateSnapshot(ItemStack leftItem, ItemStack rightItem, ItemStack outputItem, Player player) {
            this.leftItem = leftItem;
            this.rightItem = rightItem;
            this.outputItem = outputItem;
            this.player = player;
        }

        public ItemStack getLeftItem() {
            return this.leftItem;
        }

        public ItemStack getRightItem() {
            return this.rightItem;
        }

        public ItemStack getOutputItem() {
            return this.outputItem;
        }

        public Player getPlayer() {
            return this.player;
        }

        public String getText() {
            return this.outputItem.hasItemMeta() ? this.outputItem.getItemMeta().getDisplayName() : "";
        }
    }

    public static class Slot {
        private static final int[] values = new int[]{0, 1, 2};
        public static final int INPUT_LEFT = 0;
        public static final int INPUT_RIGHT = 1;
        public static final int OUTPUT = 2;

        public static int[] values() {
            return values;
        }
    }

    @Deprecated
    public static class Response {
        public static List<ResponseAction> close() {
            return Arrays.asList(ResponseAction.close());
        }

        public static List<ResponseAction> text(String text) {
            return Arrays.asList(ResponseAction.replaceInputText(text));
        }

        public static List<ResponseAction> openInventory(Inventory inventory) {
            return Arrays.asList(ResponseAction.openInventory(inventory));
        }
    }

    @FunctionalInterface
    public static interface ResponseAction
    extends BiConsumer<AnvilGUI, Player> {
        public static ResponseAction replaceInputText(String text) {
            Validate.notNull((Object)text, (String)"text cannot be null");
            return (anvilgui, player) -> {
                ItemStack item = anvilgui.getInventory().getItem(2);
                if (item == null) {
                    item = anvilgui.getInventory().getItem(0);
                }
                if (item == null) {
                    throw new IllegalStateException("replaceInputText can only be used if slots OUTPUT or INPUT_LEFT are not empty");
                }
                ItemStack cloned = item.clone();
                ItemMeta meta = cloned.getItemMeta();
                meta.setDisplayName(text);
                cloned.setItemMeta(meta);
                anvilgui.getInventory().setItem(0, cloned);
            };
        }

        public static ResponseAction updateTitle(String literalTitle, boolean preserveRenameText) {
            Validate.notNull((Object)literalTitle, (String)"literalTitle cannot be null");
            return (anvilGUI, player) -> anvilGUI.setTitle(literalTitle, preserveRenameText);
        }

        public static ResponseAction updateJsonTitle(String json, boolean preserveRenameText) {
            Validate.notNull((Object)json, (String)"json cannot be null");
            return (anvilGUI, player) -> anvilGUI.setJsonTitle(json, preserveRenameText);
        }

        public static ResponseAction openInventory(Inventory otherInventory) {
            Validate.notNull((Object)otherInventory, (String)"otherInventory cannot be null");
            return (anvilgui, player) -> player.openInventory(otherInventory);
        }

        public static ResponseAction close() {
            return (anvilgui, player) -> anvilgui.closeInventory();
        }

        public static ResponseAction run(Runnable runnable) {
            Validate.notNull((Object)runnable, (String)"runnable cannot be null");
            return (anvilgui, player) -> runnable.run();
        }
    }

    public static class Builder {
        private Executor mainThreadExecutor;
        private Consumer<StateSnapshot> closeListener;
        private boolean concurrentClickHandlerExecution = false;
        private ClickHandler clickHandler;
        private boolean preventClose = false;
        private Set<Integer> interactableSlots = Collections.emptySet();
        private Plugin plugin;
        private Object titleComponent = AnvilGUI.access$1200().literalChatComponent("Repair & Name");
        private String itemText;
        private ItemStack itemLeft;
        private ItemStack itemRight;
        private ItemStack itemOutput;

        public Builder mainThreadExecutor(Executor executor) {
            Validate.notNull((Object)executor, (String)"Executor cannot be null");
            this.mainThreadExecutor = executor;
            return this;
        }

        public Builder preventClose() {
            this.preventClose = true;
            return this;
        }

        public Builder interactableSlots(int ... slots) {
            HashSet<Integer> newValue = new HashSet<Integer>();
            for (int slot : slots) {
                newValue.add(slot);
            }
            this.interactableSlots = newValue;
            return this;
        }

        public Builder onClose(Consumer<StateSnapshot> closeListener) {
            Validate.notNull(closeListener, (String)"closeListener cannot be null");
            this.closeListener = closeListener;
            return this;
        }

        public Builder onClickAsync(ClickHandler clickHandler) {
            Validate.notNull((Object)clickHandler, (String)"click function cannot be null");
            this.clickHandler = clickHandler;
            return this;
        }

        public Builder allowConcurrentClickHandlerExecution() {
            this.concurrentClickHandlerExecution = true;
            return this;
        }

        public Builder onClick(BiFunction<Integer, StateSnapshot, List<ResponseAction>> clickHandler) {
            Validate.notNull(clickHandler, (String)"click function cannot be null");
            this.clickHandler = (slot, stateSnapshot) -> CompletableFuture.completedFuture((List)clickHandler.apply((Integer)slot, (StateSnapshot)stateSnapshot));
            return this;
        }

        public Builder plugin(Plugin plugin) {
            Validate.notNull((Object)plugin, (String)"Plugin cannot be null");
            this.plugin = plugin;
            return this;
        }

        public Builder text(String text) {
            Validate.notNull((Object)text, (String)"Text cannot be null");
            this.itemText = text;
            return this;
        }

        public Builder title(String title) {
            Validate.notNull((Object)title, (String)"title cannot be null");
            this.titleComponent = WRAPPER.literalChatComponent(title);
            return this;
        }

        public Builder jsonTitle(String json) {
            Validate.notNull((Object)json, (String)"json cannot be null");
            this.titleComponent = WRAPPER.jsonChatComponent(json);
            return this;
        }

        public Builder itemLeft(ItemStack item) {
            Validate.notNull((Object)item, (String)"item cannot be null");
            this.itemLeft = item;
            return this;
        }

        public Builder itemRight(ItemStack item) {
            this.itemRight = item;
            return this;
        }

        public Builder itemOutput(ItemStack item) {
            this.itemOutput = item;
            return this;
        }

        public AnvilGUI open(Player player) {
            Validate.notNull((Object)this.plugin, (String)"Plugin cannot be null");
            Validate.notNull((Object)this.clickHandler, (String)"click handler cannot be null");
            Validate.notNull((Object)player, (String)"Player cannot be null");
            if (this.itemText != null) {
                if (this.itemLeft == null) {
                    this.itemLeft = new ItemStack(Material.PAPER);
                }
                ItemMeta paperMeta = this.itemLeft.getItemMeta();
                paperMeta.setDisplayName(this.itemText);
                this.itemLeft.setItemMeta(paperMeta);
            }
            if (this.mainThreadExecutor == null) {
                this.mainThreadExecutor = task -> Bukkit.getScheduler().runTask(this.plugin, task);
            }
            AnvilGUI anvilGUI = new AnvilGUI(this.plugin, player, this.mainThreadExecutor, this.titleComponent, new ItemStack[]{this.itemLeft, this.itemRight, this.itemOutput}, this.preventClose, this.interactableSlots, this.closeListener, this.concurrentClickHandlerExecution, this.clickHandler);
            anvilGUI.openInventory();
            return anvilGUI;
        }
    }
}

