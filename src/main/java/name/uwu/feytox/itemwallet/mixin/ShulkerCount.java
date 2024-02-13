package name.uwu.feytox.itemwallet.mixin;

import name.uwu.feytox.itemwallet.ItemWalletClient;
import name.uwu.feytox.itemwallet.config.ModConfig;
import name.uwu.feytox.itemwallet.counter.ScreenType;
import name.uwu.feytox.itemwallet.counter.SingleCounter;
import name.uwu.feytox.itemwallet.counter.SlotsSelector;
import name.uwu.feytox.itemwallet.gui.CounterHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxScreen.class)
public class ShulkerCount {

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void onDrawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemWalletClient.lastScreen = client.currentScreen;
        ModConfig config = ModConfig.get();

        if (config.enableMod && client.currentScreen != null && client.player != null) {
            ScreenHandler screenHandler = client.player.currentScreenHandler;
            ShulkerBoxScreenHandler shulkerBoxScreenHandler = ((ShulkerBoxScreenHandler) screenHandler);
            DefaultedList<ItemStack> itemStacks = shulkerBoxScreenHandler.getStacks();

            SingleCounter inventoryCounter = SingleCounter.inventory();
            SingleCounter shulkerCounter = SingleCounter.shulker(itemStacks);

            CounterHUD hud = new CounterHUD(ScreenType.CHEST)
                    .add(inventoryCounter)
                    .add(shulkerCounter)
                    .add(SingleCounter.allCount(inventoryCounter, shulkerCounter));
            hud.draw(context);
        }
        SlotsSelector.highlightSlots(context);
    }
}
