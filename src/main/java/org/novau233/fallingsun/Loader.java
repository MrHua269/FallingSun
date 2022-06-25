package org.novau233.fallingsun;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

public class Loader implements IFMLLoadingPlugin {
    private static boolean isObfuscatedEnvironment = false;
    private static final Logger LOGGER = LogManager.getLogger();

    public Loader() {
        Loader.LOGGER.info("\n\nLoading mixins by FallingSun");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.fallingsun.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        Loader.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map data) {
        isObfuscatedEnvironment = ((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
    }

    public String getAccessTransformerClass() {
        return null;
    }
}
