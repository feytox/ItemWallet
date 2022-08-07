package name.uwu.feytox.itemwallet.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.lwjgl.glfw.GLFW;

import static name.uwu.feytox.itemwallet.ItemWalletClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }

    public static void load() {
        AutoConfig.getConfigHolder(ModConfig.class).load();
    }

    public String config_version = "v1";

    // main settings
    public boolean enableMod = true;
    public boolean simpleMode = false;
    public boolean isCountInStacks = false;
    public boolean isCountInBlocks = false;
    public String walletItem = "minecraft:diamond";
    public boolean enableExtraSupport = true;
    public boolean enableShulkerCount = false;
    public boolean editorMode = false;

    // selector settings
    public int select_color = 9237731;
    public int select_alpha = 127;

    // keybinds settings
    public int selectKeybind_key = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
    public int selectKeybind_mod = 0;
    public int showCountInStacks_key = GLFW.GLFW_KEY_G;
    public int showCountInStacks_mod = 0;
    public int showCountInBlocks_key = GLFW.GLFW_KEY_H;
    public int showCountInBlocks_mod = 0;

    // non-simple mode
    public int inventoryOffsetX = -24;
    public int inventoryOffsetY = -84;
    public int inventoryOffsetX_withRecipeBook = -24;
    public int inventoryOffsetY_withRecipeBook = -84;

    // for SP Wallet
//    public int onlineOffsetX = 1;
//    public int onlineOffsetY = -84;
//    public int onlineOffsetX_withRecipeBook = 1;
//    public int onlineOffsetY_withRecipeBook = -84;

    
    public int chestOffsetX = -24;
    public int chestOffsetY = -84;
    
    public int doubleChestOffsetX = 89;
    public int doubleChestOffsetY = -12;

    // simple mode
    public int simpleInvX = 40;
    public int simpleInvY = 10;
    public int simpleInvX_withRecipeBook = 116;
    public int simpleInvY_withRecipeBook = 10;

    // for SP Wallet
//    public int simpleOnlineOffsetX = 40;
//    public int simpleOnlineOffsetY = 20;
//    public int simpleOnlineOffsetX_withRecipeBook = 116;
//    public int simpleOnlineOffsetY_withRecipeBook = 20;
    
    public int simpleChestX_inv = 40;
    public int simpleChestY_inv = 10;
    
    public int simpleChestX_chest = 40;
    public int simpleChestY_chest = 76;
    
    public int simpleChestX_all = 0;
    public int simpleChestY_all = 10;
    
    public int simpleDoubleChestX_inv = 40;
    public int simpleDoubleChestY_inv = -18;
    
    public int simpleDoubleChestX_chest = 40;
    public int simpleDoubleChestY_chest = 104;
    
    public int simpleDoubleChestX_all = 0;
    public int simpleDoubleChestY_all = -18;

    public static void resetCoordsConfig(boolean isReset) {
        if (isReset) {
            ModConfig config = get();

            // non-simple mode
            config.inventoryOffsetX = -24;
            config.inventoryOffsetY = -84;
            config.inventoryOffsetX_withRecipeBook = -24;
            config.inventoryOffsetY_withRecipeBook = -84;

            // for SP Wallet
//          config.onlineOffsetX = 1;
//          config.onlineOffsetY = -84;
//          config.onlineOffsetX_withRecipeBook = 1;
//          config.onlineOffsetY_withRecipeBook = -84;

            config.chestOffsetX = -24;
            config.chestOffsetY = -84;
            config.doubleChestOffsetX = 89;
            config.doubleChestOffsetY = -12;

            // simple mode
            config.simpleInvX = 40;
            config.simpleInvY = 10;
            config.simpleInvX_withRecipeBook = 116;
            config.simpleInvY_withRecipeBook = 10;

            // for SP Wallet
//          config.simpleOnlineOffsetX = 40;
//          config.simpleOnlineOffsetY = 20;
//          config.simpleOnlineOffsetX_withRecipeBook = 116;
//          config.simpleOnlineOffsetY_withRecipeBook = 20;


            config.simpleChestX_inv = 40;
            config.simpleChestY_inv = 10;

            config.simpleChestX_chest = 40;
            config.simpleChestY_chest = 76;

            config.simpleChestX_all = 0;
            config.simpleChestY_all = 10;

            config.simpleDoubleChestX_inv = 40;
            config.simpleDoubleChestY_inv = -18;

            config.simpleDoubleChestX_chest = 40;
            config.simpleDoubleChestY_chest = 104;

            config.simpleDoubleChestX_all = 0;
            config.simpleDoubleChestY_all = -18;
        }
    }
}
