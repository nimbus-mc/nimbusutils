package net.playnimbus.nimbusutils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.CONFIG;

@Environment(EnvType.CLIENT)
public class ModMenuImplementation implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.nimbusutils.config"))
                    ;
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            builder.getOrCreateCategory(Text.translatable("title.nimbusutils.config"))
                            .addEntry(entryBuilder.startBooleanToggle(Text.translatable("setting.nimbusutils.enabled"), CONFIG.modEnabled)
                                    .setDefaultValue(true)
                                    .setTooltip(Text.translatable("setting.nimbusutils.enabled.tooltip"))
                                    .setSaveConsumer(newValue -> CONFIG.modEnabled = newValue)
                                    .build())
                    ;

            builder.getOrCreateCategory(Text.translatable("category.nimbusutils.nimnite"))
                    .addEntry(entryBuilder.startBooleanToggle(Text.translatable("setting.nimnite.toggleads"), CONFIG.toggleADS)
                            .setDefaultValue(false)
                            .setTooltip(Text.translatable("setting.nimnite.toggleads.tooltip"))
                            .setSaveConsumer(newValue -> CONFIG.toggleADS = newValue)
                            .build())
                    ;



            return builder.build();
        };
    }
}
