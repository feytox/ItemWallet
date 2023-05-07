package name.uwu.feytox.itemwallet;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import name.uwu.feytox.itemwallet.config.ModConfig;
import name.uwu.feytox.itemwallet.counter.SlotsSelector;
import name.uwu.feytox.itemwallet.gui.CounterEditor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

@Environment(EnvType.CLIENT)
public class ItemWalletClient implements ClientModInitializer {

    public static final String MOD_ID = "itemwallet";

    public static Screen lastScreen;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (lastScreen != client.currentScreen) {
                SlotsSelector.cleanSelectedSlots();
                if (CounterEditor.isActive()) {
                    CounterEditor.discard();
                }
                lastScreen = client.currentScreen;
            }
        });
    }

    public static void playSound(SoundEvent sound) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world != null && client.player != null) {
            client.world.playSound(null, client.player.getBlockPos(), sound, SoundCategory.BLOCKS, 0.5f, 1);
        }
    }

    public static boolean containsAny(String string, String... strings) {
        for (String string_check : strings) {
            if (string.contains(string_check)) {
                return true;
            }
        }
        return false;
    }
}
