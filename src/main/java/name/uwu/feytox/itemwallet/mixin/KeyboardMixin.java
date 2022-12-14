package name.uwu.feytox.itemwallet.mixin;

import name.uwu.feytox.itemwallet.ItemWalletClient;
import name.uwu.feytox.itemwallet.config.ModConfig;
import name.uwu.feytox.itemwallet.counter.SlotsSelector;
import name.uwu.feytox.itemwallet.gui.CounterEditor;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("RETURN"))
    public void onOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        ModConfig config = ModConfig.get();

        if (modifiers == 1) modifiers = 4;
        else if (modifiers == 4) modifiers = 1;

        if (screen instanceof InventoryScreen || screen instanceof GenericContainerScreen || screen instanceof ShulkerBoxScreen) {
            if (action != 1) {
                if (CounterEditor.isActive()) {
                    switch (key) {
                        case GLFW.GLFW_KEY_LEFT -> CounterEditor.change(-1, 0);
                        case GLFW.GLFW_KEY_RIGHT -> CounterEditor.change(1, 0);
                        case GLFW.GLFW_KEY_UP -> CounterEditor.change(0, 1);
                        case GLFW.GLFW_KEY_DOWN -> CounterEditor.change(0, -1);
                        case GLFW.GLFW_KEY_ENTER -> CounterEditor.save();
                        case GLFW.GLFW_KEY_ESCAPE -> CounterEditor.discard();
                    }
                }
            }

            if (action == 1) {
                if (config.selectKeybind_key == key && config.selectKeybind_mod == modifiers) {
                    SlotsSelector.selectSlot();
                } else if (config.showCountInStacks_key == key && config.showCountInStacks_mod == modifiers) {
                    config.isCountInStacks = !config.isCountInStacks;
                    ModConfig.save();
                } else if (config.showCountInBlocks_key == key && config.showCountInBlocks_mod == modifiers) {
                    config.isCountInBlocks = !config.isCountInBlocks;
                    ModConfig.save();
                }
            }
        }
    }
}
