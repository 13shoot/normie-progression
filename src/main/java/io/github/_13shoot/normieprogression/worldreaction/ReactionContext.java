package io.github._13shoot.normieprogression.worldreaction;

import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

/**
 * ReactionContext
 *
 * Snapshot of player + world state for world reaction evaluation.
 * Immutable per tick.
 */
public class ReactionContext {

    private final Player player;
    private final World world;
    private final Biome biome;
    private final long worldTime;
    private final int visibility;

    private ReactionContext(Player player) {
        this.player = player;
        this.world = player.getWorld();
        this.biome = player.getLocation().getBlock().getBiome();
        this.worldTime = world.getTime();
        this.visibility = VisibilityManager.getVisibility(player.getUniqueId());
    }

    public static ReactionContext from(Player player) {
        return new ReactionContext(player);
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Biome getBiome() {
        return biome;
    }

    public long getWorldTime() {
        return worldTime;
    }

    public boolean isNight() {
        return worldTime >= 13000 && worldTime <= 23000;
    }

    public int getVisibility() {
        return visibility;
    }
}
