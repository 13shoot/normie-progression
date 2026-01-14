package io.github._13shoot.normieprogression;

import io.github._13shoot.normieprogression.placeholder.ProgressionPlaceholder;
import io.github._13shoot.normieprogression.visibility.VisibilityListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NormieProgression extends JavaPlugin {

    @Override
    public void onEnable() {

        // Register Placeholder
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ProgressionPlaceholder(this).register();
            getLogger().info("Progression placeholders registered.");
        }

        // Register Visibility Listener
        VisibilityListener visibilityListener =
                new VisibilityListener(this);

        Bukkit.getPluginManager().registerEvents(
                visibilityListener,
                this
        );

        // Start day counter
        visibilityListener.startDayCounter();

        getLogger().info("NormieProgression enabled.");
    }
}
