package net.playnimbus.nimbusutils;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.playnimbus.NimbusUtils;

@Config(name = NimbusUtils.MOD_ID)
public class ModConfig implements ConfigData {
    public boolean modEnabled = true;
    public boolean toggleADS = false;
}
