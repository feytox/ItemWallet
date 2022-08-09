package name.uwu.feytox.itemwallet.counter;

import name.uwu.feytox.itemwallet.gui.GuiTexture;

public enum ContainerType {
    INVENTORY(new GuiTexture(0, 0, 5, 5)),
    CHEST(new GuiTexture(0, 8, 5, 13)),
    DOUBLE_CHEST(new GuiTexture(0, 8, 5, 13)),
    ENDER_CHEST(new GuiTexture(7, 8, 12, 13)),
    BARREL(new GuiTexture(14, 8, 19, 13)),
    SHULKER(new GuiTexture(21, 8, 26, 13)),
    ALL_COUNT(new GuiTexture(14, 0, 19, 6)),
    SELECTOR(new GuiTexture(21, 0, 26, 5));

    private final GuiTexture containerTexture;
    ContainerType(GuiTexture containerTexture) {
        this.containerTexture = containerTexture;
    }

    public GuiTexture getContainerTexture() {
        return this.containerTexture;
    }
}
