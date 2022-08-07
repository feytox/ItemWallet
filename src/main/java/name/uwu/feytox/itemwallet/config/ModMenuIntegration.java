package name.uwu.feytox.itemwallet.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.math.Color;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static name.uwu.feytox.itemwallet.counter.SingleCounter.getWalletItem;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModMenuIntegration::createConfigScreen;
    }

    private static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new LiteralText("Item Wallet"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = ModConfig.get();

        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/cobbled_deepslate.png"));

        builder.getOrCreateCategory(new LiteralText(""))
                .addEntry(entryBuilder.startBooleanToggle(
                            new TranslatableText("itemwallet.config.enableMod"),
                            config.enableMod)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableMod = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.simpleMode"),
                                config.simpleMode)
                        .setTooltip(new TranslatableText("itemwallet.tooltip.simpleMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.simpleMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.isCountInStacks"),
                                config.isCountInStacks)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.isCountInStacks = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.isCountInBlocks"),
                                config.isCountInBlocks)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.isCountInBlocks = value)
                        .build())
                .addEntry(entryBuilder.startDropdownMenu(
                                new TranslatableText("itemwallet.config.walletItem"),
                                DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(getWalletItem()),
                                DropdownMenuBuilder.CellCreatorBuilder.ofItemObject())
                        .setDefaultValue(getWalletItem("minecraft:diamond"))
                        .setSelections(Registry.ITEM.stream().sorted(Comparator.comparing(Item::toString))
                                .collect(Collectors.toCollection(LinkedHashSet::new)))
                        .setSaveConsumer(item -> config.walletItem = Registry.ITEM.getId(item).toString())
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.enableExtraSupport"),
                                config.enableExtraSupport)
                        .setTooltip(new TranslatableText("itemwallet.tooltip.enableExtraSupport"))
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableExtraSupport = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.enableShulkerCount"),
                                config.enableShulkerCount)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.enableShulkerCount = value)
                        .build())
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                new TranslatableText("itemwallet.config.selectKeybind_key"),
                                ModifierKeyCode.of(getKeyCode(config.selectKeybind_key),
                                        Modifier.of((short) config.selectKeybind_mod)))
                        .setDefaultValue(ModifierKeyCode.of(getKeyCode(2),
                                Modifier.none()))
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            config.selectKeybind_key = modifierKeyCode.getKeyCode().getCode();
                            config.selectKeybind_mod = modifierKeyCode.getModifier().getValue();
                        })
                        .build())
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                new TranslatableText("itemwallet.config.showCountInStacks_key"),
                                ModifierKeyCode.of(getKeyCode(config.showCountInStacks_key),
                                        Modifier.of((short) config.showCountInStacks_mod)))
                        .setDefaultValue(ModifierKeyCode.of(InputUtil.Type.KEYSYM.createFromCode(71),
                                Modifier.none()))
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            config.showCountInStacks_key = modifierKeyCode.getKeyCode().getCode();
                            config.showCountInStacks_mod = modifierKeyCode.getModifier().getValue();
                        })
                        .build())
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                new TranslatableText("itemwallet.config.showCountInBlocks_key"),
                                ModifierKeyCode.of(getKeyCode(config.showCountInBlocks_key),
                                        Modifier.of((short) config.showCountInBlocks_mod)))
                        .setDefaultValue(ModifierKeyCode.of(InputUtil.Type.KEYSYM.createFromCode(72),
                                Modifier.none()))
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            config.showCountInBlocks_key = modifierKeyCode.getKeyCode().getCode();
                            config.showCountInBlocks_mod = modifierKeyCode.getModifier().getValue();
                        })
                        .build())
                .addEntry(entryBuilder.startColorField(
                                new TranslatableText("itemwallet.config.select_color"),
                                config.select_color)
                        .setDefaultValue(9237731)
                        .setSaveConsumer(color -> config.select_color = color)
                        .build())
                .addEntry(entryBuilder.startIntField(
                                new TranslatableText("itemwallet.config.select_alpha"),
                                config.select_alpha)
                        .setDefaultValue(127)
                        .setMin(0)
                        .setMax(255)
                        .setSaveConsumer(alpha -> config.select_alpha = alpha)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.editorMode"),
                                config.editorMode)
                        .setTooltip(new TranslatableText("itemwallet.tooltip.editorMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.editorMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                new TranslatableText("itemwallet.config.coordsReset"),
                                false)
                        .setDefaultValue(false)
                        .setSaveConsumer(ModConfig::resetCoordsConfig)
                        .build());
        builder.transparentBackground();

        return builder
                .setSavingRunnable(ModConfig::save)
                .build();
    }

    private static InputUtil.Key getKeyCode(int code) {
        return code > 3 ? InputUtil.Type.KEYSYM.createFromCode(code) : InputUtil.Type.MOUSE.createFromCode(code);
    }
}