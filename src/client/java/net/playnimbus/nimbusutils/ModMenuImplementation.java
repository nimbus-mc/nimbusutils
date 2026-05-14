package net.playnimbus.nimbusutils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

import static net.playnimbus.nimbusutils.NimbusUtilsClient.CONFIG;

@Environment(EnvType.CLIENT)
public class ModMenuImplementation implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("title.nimbusutils.config"))
                    ;
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            builder.getOrCreateCategory(Component.translatable("title.nimbusutils.config"))
                            .addEntry(entryBuilder.startBooleanToggle(Component.translatable("setting.nimbusutils.enabled"), CONFIG.modEnabled)
                                    .setDefaultValue(true)
                                    .setTooltip(Component.translatable("setting.nimbusutils.enabled.tooltip"))
                                    .setSaveConsumer(newValue -> CONFIG.modEnabled = newValue)
                                    .build())
                    ;

            builder.getOrCreateCategory(Component.translatable("key.category.nimbusutils.nimnite"))
                    .addEntry(entryBuilder.startBooleanToggle(Component.translatable("setting.nimnite.toggleads"), CONFIG.nimniteToggleAds)
                            .setDefaultValue(false)
                            .setTooltip(Component.translatable("setting.nimnite.toggleads.tooltip"))
                            .setSaveConsumer(newValue -> CONFIG.nimniteToggleAds = newValue)
                            .build())
                    .addEntry(entryBuilder.startBooleanToggle(Component.translatable("setting.nimnite.lagcomp"), CONFIG.nimniteClientPrediction)
                            .setDefaultValue(true)
                            .setTooltip(Component.translatable("setting.nimnite.lagcomp.tooltip"))
                            .setSaveConsumer(newValue -> CONFIG.nimniteClientPrediction = newValue)
                            .build())
                    ;



            return builder.build();
        };
    }
}
