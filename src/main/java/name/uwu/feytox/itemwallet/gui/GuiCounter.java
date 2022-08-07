package name.uwu.feytox.itemwallet.gui;

import name.uwu.feytox.itemwallet.config.ModConfig;
import name.uwu.feytox.itemwallet.counter.ContainerType;
import name.uwu.feytox.itemwallet.counter.SingleCounter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiCounter {
    ContainerType containerType;
    GuiTexture containerTexture;
    GuiTexture countTexture;
    String count;
    int count_ax;

    public GuiCounter(SingleCounter counter) {
        this.containerType = counter.containerType;
        this.containerTexture = counter.containerType.getContainerTexture();

        this.count = String.valueOf(counter.count);
        ModConfig config = ModConfig.get();

        if (config.isCountInBlocks && config.isCountInStacks) {
            this.count = getCountInBlocksStacks(counter.count);
        } else if (config.isCountInStacks) {
            this.count = getCountInStacks(counter.count);
        } else if (config.isCountInBlocks) {
            this.count = getCountInBlocks(counter.count);
        }

        int u1;
        if (counter.count < 64) {
            u1 = 0;
        } else if (counter.count < 576) {
            u1 = 7;
        } else if (counter.count < 1728) {
            u1 = 14;
        } else {
            u1 = 21;
        }
        this.countTexture = new GuiTexture(u1, 15, u1 + 5, 20);

        this.count_ax = getCountPxLength(count);
    }

    public void drawCount(MatrixStack matrices, int x, int y) {
        DrawableHelper.drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, new LiteralText(this.count),
                x, y, -1);
    }

    private static String getCountInStacks(int count) {
        if (count >= 64) {
            if (count % 64 != 0) {
                return count / 64 + I18n.translate("itemwallet.st") + " " + (int) Math.round((((double) count) / 64 - count / 64) * 64);
            } else {
                return count / 64 + I18n.translate("itemwallet.st");
            }
        }
        return String.valueOf(count);
    }

    private static String getCountInBlocksStacks(int count) {
        List<Integer> countList = getCountListInBlocks(count);
        if (countList != null) {
            String suffix_count = "";
            if (countList.size() == 2) {
                suffix_count += " " + countList.get(1);
            }

            if (countList.get(0) % 64 != 0) {
                return getCountInStacks(countList.get(0)) + I18n.translate("itemwallet.b") + suffix_count;
            } else {
                return getCountInStacks(countList.get(0)) + suffix_count;
            }
        }

        return String.valueOf(count);
    }

    private static String getCountInBlocks(int count) {
        List<Integer> countList = getCountListInBlocks(count);
        if (countList != null) {
            if (countList.size() == 2) {
                return countList.get(0) + I18n.translate("itemwallet.b") + " " + countList.get(1);
            }
            return countList.get(0) + I18n.translate("itemwallet.b");
        }

        return String.valueOf(count);
    }

    @Nullable
    private static List<Integer> getCountListInBlocks(int count) {
        if (count >= 9) {
            if (count % 9 != 0) {
                return new ArrayList<>(Arrays.asList(count / 9, (int) Math.round((((double) count) / 9 - count / 9) * 9)));
            } else {
                return new ArrayList<>(List.of(count / 9));
            }
        }
        return null;
    }

    protected static int getCountPxLength(String count) {
        int length = 0;
        for (String char1 : count.split("")) {
            switch (char1) {
                case "." -> length += 2;
                case " " -> length += 4;
                default -> length += 6;
            }
        }
        return length + 2;
    }
}
