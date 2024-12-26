package com.cursee.overclocked_watches;

import com.cursee.monolib.platform.Services;
import com.cursee.monolib.util.toml.Toml;
import com.cursee.monolib.util.toml.TomlWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigFabric implements IMixinConfigPlugin {

    public static final Map<String, Object> defaults = new HashMap<String, Object>();
    public static final AtomicBoolean DAY_NIGHT_CHANGE_ALLOWED = new AtomicBoolean(true);

    @Override
    public void onLoad(String mixinPackage) {

        // write or load config for toggling day/night packets from applying

        defaults.put("day_night_change_allowed", DAY_NIGHT_CHANGE_ALLOWED.get());

        final File CONFIG_DIRECTORY = new File(Services.PLATFORM.getGameDirectory() + File.separator + "config");

        if (!CONFIG_DIRECTORY.isDirectory()) CONFIG_DIRECTORY.mkdir();

        final String CONFIG_FILEPATH = CONFIG_DIRECTORY + File.separator + Constants.MOD_ID + ".toml";

        File CONFIG_FILE = new File(CONFIG_FILEPATH);
        handle(CONFIG_FILE);
    }

    private static void handle(File file) {
        if (!file.isFile()) createConfigurationFile(file);
        else loadConfigurationFile(file);
    }

    private static void createConfigurationFile(File file) {
        try {
            TomlWriter writer = new TomlWriter();
            writer.write(defaults, file);
        }
        catch (IOException exception) {
            Constants.LOG.error("Fatal error occurred while attempting to write " + Constants.MOD_ID + ".toml");
            Constants.LOG.error("Did another process delete the config directory during writing?");
            Constants.LOG.error(exception.getMessage());
        }
    }

    private static void loadConfigurationFile(File file) {
        try {
            Toml toml = new Toml().read(file);
            DAY_NIGHT_CHANGE_ALLOWED.set(toml.getBoolean("day_night_change_allowed"));
        }
        catch (IllegalStateException exception) {
            Constants.LOG.error("Fatal error occurred while attempting to read " + Constants.MOD_ID + ".toml");
            Constants.LOG.error("Did another process delete the file during reading?");
            Constants.LOG.error(exception.getMessage());
        }
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
