package io.github._13shoot.normieprogression;

import org.bukkit.plugin.java.JavaPlugin;

public final class NormieProgression extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("NormieProgression enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NormieProgression disabled.");
    }
}
