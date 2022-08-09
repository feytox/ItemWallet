package name.uwu.feytox.itemwallet.counter;

import name.uwu.feytox.itemwallet.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static name.uwu.feytox.itemwallet.ItemWalletClient.containsAny;

public class SingleCounter {
    public ContainerType containerType;
    public int count;

    public static SingleCounter inventory() {
        if (SlotsSelector.isSlotsSelected()) {
            return SingleCounter.selectorCount();
        }
        return new SingleCounter(ContainerType.INVENTORY, getCount());
    }

    public static SingleCounter chest() {
        return new SingleCounter(ContainerType.CHEST);
    }

    public static SingleCounter doubleChest() {
        return new SingleCounter(ContainerType.DOUBLE_CHEST);
    }

    public static SingleCounter enderChest() {
        return new SingleCounter(ContainerType.ENDER_CHEST);
    }

    public static SingleCounter barrel() {
        return new SingleCounter(ContainerType.BARREL);
    }

    public static SingleCounter shulker(DefaultedList<ItemStack> itemStacks) {
        return new SingleCounter(ContainerType.SHULKER, getCount(itemStacks));
    }

    public static SingleCounter allCount(SingleCounter first, SingleCounter second) {
        if (SlotsSelector.isSlotsSelected()) {
            return SingleCounter.selectorCount();
        }
        return new SingleCounter(ContainerType.ALL_COUNT, first.count + second.count);
    }

    public static SingleCounter selectorCount() {
        return new SingleCounter(ContainerType.SELECTOR, SlotsSelector.getCount());
    }

    public static SingleCounter getChestCounter(Screen screen) {
        Text screenTitle = screen.getTitle();

        if (screenTitle instanceof TranslatableTextContent) {
            String screenName = ((TranslatableTextContent) screenTitle).getKey();

            switch (screenName) {
                case "container.chest" -> {
                    return SingleCounter.chest();
                }
                case "container.chestDouble" -> {
                    return SingleCounter.doubleChest();
                }
                case "container.enderchest" -> {
                    return SingleCounter.enderChest();
                }
                case "container.barrel" -> {
                    return SingleCounter.barrel();
                }
            }
        }

        return SingleCounter.chest();
    }

    private SingleCounter(ContainerType containerType, int count) {
        this.containerType = containerType;
        this.count = count;
    }

    private SingleCounter(ContainerType containerType) {
        this.containerType = containerType;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null) {
            ScreenHandler screenHandler = client.player.currentScreenHandler;
            if (screenHandler != null) {
                Inventory inventory = ((GenericContainerScreenHandler) screenHandler).getInventory();
                this.count = getCount(inventory);
                return;
            }
        }

        this.count = 0;
    }

    public static Item getWalletItem() {
        return getWalletItem(ModConfig.get().walletItem);
    }

    public static Item getWalletItem(String id) {
        return Registry.ITEM.get(new Identifier(id));
    }

    private static int getCount() {
        return getCount(getWalletItem());
    }

    private static int getCount(Inventory inventory) {
        return getCount(inventory, getWalletItem());
    }

    private static int getCount(Item item) {
        return getCount(MinecraftClient.getInstance().player.getInventory(), item);
    }

    private static int getCount(Inventory inventory, Item item) {
        int result = inventory.count(item);

        if (ModConfig.get().enableExtraSupport) {
            result += getCountExtra(inventory::count);
        }
        if (ModConfig.get().enableShulkerCount) {
            result += getShulkerCount(() -> {
               List<ItemStack> itemStacks = new ArrayList<>();

                for(int j = 0; j < inventory.size(); ++j) {
                    ItemStack itemStack = inventory.getStack(j);
                    if (Registry.ITEM.getId(itemStack.getItem()).toString().contains("shulker_box")) {
                        itemStacks.add(itemStack);
                    }
                }

                return itemStacks;
            });
        }

        return result;
    }

    public static int getCount(DefaultedList<ItemStack> itemStacks) {
        int count = getCountInItemStacks(itemStacks, getWalletItem());

        if (ModConfig.get().enableExtraSupport) {
            count += getCountExtra(item -> getCountInItemStacks(itemStacks, item));
        }
        if (ModConfig.get().enableShulkerCount) {
            count += getShulkerCount(() -> {
                List<ItemStack> result = new ArrayList<>();
                itemStacks.forEach(itemStack -> {
                    if (Registry.ITEM.getId(itemStack.getItem()).toString().contains("shulker_box")) {
                        result.add(itemStack);
                    }
                });
                return result;
            });
        }

        return count - getCount();
    }

    private static int getCountInItemStacks(DefaultedList<ItemStack> itemStacks, Item item) {
        int count = 0;
        for (ItemStack itemStack: itemStacks) {
            if (itemStack.getItem().equals(item)) {
                count += itemStack.getCount();
            }
        }

        return count;
    }

    public static int getCountExtra(Function<Item, Integer> countGetter) {
        Item walletItem = getWalletItem();
        String item_id = Registry.ITEM.getId(walletItem).toString();

        if (item_id.contains("minecraft:")) {
            item_id = item_id.replace("minecraft:", "");
            if (item_id.endsWith("ore") && !item_id.contains("nether") && !item_id.startsWith("deepslate")) {
                return countGetter.apply(Registry.ITEM.get(new Identifier("minecraft",
                        "deepslate_" + item_id)));
            } else if (item_id.endsWith("ore") && !item_id.contains("nether")) {
                return countGetter.apply(Registry.ITEM.get(new Identifier("minecraft",
                        item_id.replace("deepslate_", ""))));
            } else if (!item_id.contains("block") && containsAny(item_id, "iron", "gold", "lapis", "netherite",
                    "coal", "emerald", "diamond", "copper", "redstone", "raw")) {
                return 9 * countGetter.apply(Registry.ITEM.get(new Identifier("minecraft",
                        item_id.replace("_ingot", "").replace("_lazuli", "") + "_block")));
            }
        }

        return 0;
    }

    public static int getShulkerCount(Supplier<List<ItemStack>> shulkersGetter) {
        int count = 0;

        String walletItem_id = Registry.ITEM.getId(getWalletItem()).toString();
        List<ItemStack> shulkers = shulkersGetter.get();

        List<SimpleStack> allShulkerItems = new ArrayList<>();
        try {
            shulkers.forEach(itemStack -> {
                NbtList nbtItems = (NbtList) itemStack.getNbt().getCompound("BlockEntityTag").get("Items");
                nbtItems.forEach(nbtElement -> {
                    NbtCompound nbtItem = (NbtCompound) nbtElement;
                    allShulkerItems.add(new SimpleStack(nbtItem.getString("id"), nbtItem.getByte("Count")));
                });
            });
        } catch (NullPointerException ignored) {}

        for (SimpleStack simpleStack : allShulkerItems) {
            if (walletItem_id.equals(simpleStack.item_id)) {
                count += simpleStack.count;
            }
        }

        if (ModConfig.get().enableExtraSupport) {
            count += getCountExtra(item -> {
                int result = 0;

                for (SimpleStack simpleStack : allShulkerItems) {
                    if (Registry.ITEM.getId(item).toString().equals(simpleStack.item_id)) {
                        result += simpleStack.count;
                    }
                }

                return result;
            });
        }

        return count;
    }

    public static class SimpleStack {
        String item_id;
        int count;

        public SimpleStack(String item_id, int count) {
            this.item_id = item_id;
            this.count = count;
        }
    }
}
