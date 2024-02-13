package name.uwu.feytox.itemwallet.mixin;

import name.uwu.feytox.itemwallet.ItemWalletClient;
import name.uwu.feytox.itemwallet.config.ModConfig;
import name.uwu.feytox.itemwallet.counter.ScreenType;
import name.uwu.feytox.itemwallet.counter.SingleCounter;
import name.uwu.feytox.itemwallet.counter.SlotsSelector;
import name.uwu.feytox.itemwallet.gui.CounterHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryCount {

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void onDrawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemWalletClient.lastScreen = client.currentScreen;
        ModConfig config = ModConfig.get();

        if (config.enableMod && client.currentScreen != null && client.player != null) {
            boolean isRecipeBookOpen = ((InventoryScreen) client.currentScreen).getRecipeBookWidget().isOpen();
            ScreenType screenType = ScreenType.INVENTORY;
            if (isRecipeBookOpen) {
                screenType = ScreenType.INVENTORY_WITH_RECIPE;
            }

            CounterHUD hud = new CounterHUD(screenType).add(SingleCounter.inventory());
            hud.draw(context);
        }
        SlotsSelector.highlightSlots(context);
    }
}

