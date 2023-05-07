package name.uwu.feytox.itemwallet.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Modifier;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
                .setTitle(Text.literal("Item Wallet"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = ModConfig.get();

        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/cobbled_deepslate.png"));

        builder.getOrCreateCategory(Text.translatable("itemwallet.config.generalCategory"))
                .addEntry(entryBuilder.startBooleanToggle(
                            Text.translatable("itemwallet.config.enableMod"),
                            config.enableMod)
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableMod = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.simpleMode"),
                                config.simpleMode)
                        .setTooltip(Text.translatable("itemwallet.tooltip.simpleMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.simpleMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.isCountInStacks"),
                                config.isCountInStacks)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.isCountInStacks = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.isCountInBlocks"),
                                config.isCountInBlocks)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.isCountInBlocks = value)
                        .build())
                .addEntry(entryBuilder.startDropdownMenu(
                                Text.translatable("itemwallet.config.walletItem"),
                                DropdownMenuBuilder.TopCellElementBuilder.ofItemObject(getWalletItem()),
                                DropdownMenuBuilder.CellCreatorBuilder.ofItemObject())
                        .setDefaultValue(getWalletItem("minecraft:diamond"))
                        .setSelections(Registries.ITEM.stream().sorted(Comparator.comparing(Item::toString))
                                .collect(Collectors.toCollection(LinkedHashSet::new)))
                        .setSaveConsumer(item -> config.walletItem = Registries.ITEM.getId(item).toString())
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.enableExtraSupport"),
                                config.enableExtraSupport)
                        .setTooltip(Text.translatable("itemwallet.tooltip.enableExtraSupport"))
                        .setDefaultValue(true)
                        .setSaveConsumer(value -> config.enableExtraSupport = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.enableShulkerCount"),
                                config.enableShulkerCount)
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.enableShulkerCount = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.editorMode"),
                                config.editorMode)
                        .setTooltip(Text.translatable("itemwallet.tooltip.editorMode"))
                        .setDefaultValue(false)
                        .setSaveConsumer(value -> config.editorMode = value)
                        .build())
                .addEntry(entryBuilder.startBooleanToggle(
                                Text.translatable("itemwallet.config.coordsReset"),
                                false)
                        .setDefaultValue(false)
                        .setSaveConsumer(ModConfig::resetCoordsConfig)
                        .build());

        builder.getOrCreateCategory(Text.translatable("itemwallet.config.selectCategory"))
                .addEntry(entryBuilder.startColorField(
                                Text.translatable("itemwallet.config.select_color"),
                                config.select_color)
                        .setDefaultValue(9237731)
                        .setSaveConsumer(color -> config.select_color = color)
                        .build())
                .addEntry(entryBuilder.startIntField(
                                Text.translatable("itemwallet.config.select_alpha"),
                                config.select_alpha)
                        .setDefaultValue(127)
                        .setMin(0)
                        .setMax(255)
                        .setSaveConsumer(alpha -> config.select_alpha = alpha)
                        .build());

        builder.getOrCreateCategory(Text.translatable("itemwallet.config.keysCategory"))
                .addEntry(entryBuilder.startModifierKeyCodeField(
                                Text.translatable("itemwallet.config.selectKeybind_key"),
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
                                Text.translatable("itemwallet.config.showCountInStacks_key"),
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
                                Text.translatable("itemwallet.config.showCountInBlocks_key"),
                                ModifierKeyCode.of(getKeyCode(config.showCountInBlocks_key),
                                        Modifier.of((short) config.showCountInBlocks_mod)))
                        .setDefaultValue(ModifierKeyCode.of(InputUtil.Type.KEYSYM.createFromCode(72),
                                Modifier.none()))
                        .setModifierSaveConsumer(modifierKeyCode -> {
                            config.showCountInBlocks_key = modifierKeyCode.getKeyCode().getCode();
                            config.showCountInBlocks_mod = modifierKeyCode.getModifier().getValue();
                        })
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