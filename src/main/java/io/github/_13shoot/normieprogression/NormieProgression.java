package io.github._13shoot.normieprogression;

import io.github._13shoot.normieprogression.command.ProgressionCommand;
import io.github._13shoot.normieprogression.gate.GateRegistry;
import io.github._13shoot.normieprogression.gate.impl.AcknowledgementGate;
import io.github._13shoot.normieprogression.gate.impl.RecognitionGate;
import io.github._13shoot.normieprogression.gate.impl.RememberedGate;
import io.github._13shoot.normieprogression.gate.impl.RespondedGate;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.placeholder.ProgressionPlaceholder;
import io.github._13shoot.normieprogression.tier.TierStorage;
import io.github._13shoot.normieprogression.visibility.EconomyBalanceTracker;
import io.github._13shoot.normieprogression.visibility.VaultEconomyHook;
import io.github._13shoot.normieprogression.visibility.VisibilityListener;
import io.github._13shoot.normieprogression.visibility.VisibilityStorage;
import io.github._13shoot.normieprogression.worldreaction.WorldReactionManager;
import io.github._13shoot.normieprogression.chronicle.ChronicleFileStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * NormieProgression
 *
 * Core plugin bootstrap.
 * This class ONLY wires systems together.
 */
public class NormieProgression extends JavaPlugin {

    private VisibilityStorage visibilityStorage;
    private TierStorage tierStorage;
    private ChronicleFileStorage chronicleStorage;

    @Override
    public void onEnable() {

        /* ------------------------------------------------
         * PlaceholderAPI
         * ------------------------------------------------ */
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ProgressionPlaceholder(this).register();
            getLogger().info("Progression placeholders registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found. Placeholders disabled.");
        }

        /* ------------------------------------------------
         * Visibility (daysAlive)
         * ------------------------------------------------ */
        VisibilityListener visibilityListener =
                new VisibilityListener(this);

        Bukkit.getPluginManager().registerEvents(
                visibilityListener,
                this
        );

        visibilityListener.startDayCounter();

        /* ------------------------------------------------
         * Visibility persistence
         * ------------------------------------------------ */
        visibilityStorage = new VisibilityStorage(this);
        visibilityStorage.loadAll();

        /* ------------------------------------------------
         * Tier persistence
         * ------------------------------------------------ */
        tierStorage = new TierStorage(this);
        tierStorage.loadAll();

        /* ------------------------------------------------
         * Mark persistence (marks.yml)
         * ------------------------------------------------ */
        MarkStorage.init(this);

        /* ------------------------------------------------
         * Chronicle persistence (chronicle.yml)
         * ------------------------------------------------ */
        chronicleStorage = new ChronicleFileStorage(this);
        chronicleStorage.load();

        /* ------------------------------------------------
         * Register Gates
         * ------------------------------------------------ */
        GateRegistry.register(new RecognitionGate());
        GateRegistry.register(new AcknowledgementGate());
        GateRegistry.register(new RespondedGate());
        GateRegistry.register(new RememberedGate());

        /* ------------------------------------------------
         * Economy visibility (Vault)
         * ------------------------------------------------ */
        if (VaultEconomyHook.init()) {

            Economy economy = VaultEconomyHook.getEconomy();

            EconomyBalanceTracker tracker =
                    new EconomyBalanceTracker(this, economy);

            tracker.start();

            io.github._13shoot.normieprogression.mark.trigger.MarkTriggerListeners
                    .startEconomyWatcher(economy);

            getLogger().info("Economy visibility tracking enabled.");
        } else {
            getLogger().warning("Vault economy not found. Economic visibility disabled.");
        }

        /* ------------------------------------------------
         * World Reaction
         * ------------------------------------------------ */
        WorldReactionManager reactionManager =
                new WorldReactionManager(this);

        reactionManager.registerDefaults();
        reactionManager.start();

        /* ------------------------------------------------
         * GUI Listener
         * ------------------------------------------------ */
        Bukkit.getPluginManager().registerEvents(
                new io.github._13shoot.normieprogression.gui.ProgressionGUIListener(),
                this
        );

        /* ------------------------------------------------
         * Mark Trigger Listeners
         * ------------------------------------------------ */
        Bukkit.getPluginManager().registerEvents(
                new io.github._13shoot.normieprogression.mark.trigger.DeathTriggerListener(),
                this
        );

        Bukkit.getPluginManager().registerEvents(
                new io.github._13shoot.normieprogression.mark.trigger.MarkTriggerListeners(),
                this
        );

        Bukkit.getPluginManager().registerEvents(
                new io.github._13shoot.normieprogression.mark.trigger.MarkTriggerListeners_Advanced(),
                this
        );

        /* ------------------------------------------------
         * Command (/np)
         * ------------------------------------------------ */
        if (getCommand("np") != null) {
            ProgressionCommand cmd = new ProgressionCommand();
            getCommand("np").setExecutor(cmd);
            getCommand("np").setTabCompleter(cmd);
        }

        getLogger().info("NormieProgression enabled.");
    }

    @Override
    public void onDisable() {

        forceSave();
        getLogger().info("NormieProgression disabled.");
    }

    /* ------------------------------------------------
     * Admin helpers (force save/load)
     * ------------------------------------------------ */
    public void forceSave() {

        if (visibilityStorage != null) {
            visibilityStorage.saveAll();
        }

        if (tierStorage != null) {
            tierStorage.saveAll();
        }

        MarkStorage.saveAll();

        if (chronicleStorage != null) {
            chronicleStorage.save();
        }
    }

    public void forceLoad() {

        if (visibilityStorage != null) {
            visibilityStorage.loadAll();
        }

        if (tierStorage != null) {
            tierStorage.loadAll();
        }

        MarkStorage.loadAll();

        if (chronicleStorage != null) {
            chronicleStorage.load();
        }
    }
}
