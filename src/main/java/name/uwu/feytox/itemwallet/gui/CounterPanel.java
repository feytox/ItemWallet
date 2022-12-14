package name.uwu.feytox.itemwallet.gui;

import juuxel.libninepatch.NinePatch;
import juuxel.libninepatch.TextureRegion;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static name.uwu.feytox.itemwallet.ItemWalletClient.MOD_ID;

public class CounterPanel {
    public Identifier image;
    public int width;
    public int height;

    public CounterPanel(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new Identifier(MOD_ID, "textures/hud/panel_light.png");
    }

    public static NinePatchPanelPainter createNinePatch(CounterPanel panel) {
        Consumer<NinePatch.Builder<Identifier>> configurator = builder -> builder.cornerSize(4).cornerUv(0.25F);
        TextureRegion<Identifier> region = new TextureRegion<>(panel.image, 0, 0, 1, 1);
        var builder = NinePatch.builder(region);
        configurator.accept(builder);
        return new NinePatchPanelPainter(builder.build(), panel);
    }
}
