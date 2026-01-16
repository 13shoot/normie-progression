package io.github._13shoot.normieprogression.mark;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class MarkNotifier {

    /* -------------------------------------------------
     * Message palette (tone per mark)
     * ------------------------------------------------- */
    private static final Map<MarkType, String> GAIN_MESSAGES = new EnumMap<>(MarkType.class);
    private static final Map<MarkType, String> LOSE_MESSAGES = new EnumMap<>(MarkType.class);

    static {
        /* ---------- TEMPORARY ---------- */
        GAIN_MESSAGES.put(MarkType.TRADE,
                "§eTrade§7 whispers follow your hands.");
        LOSE_MESSAGES.put(MarkType.TRADE,
                "§7The market no longer watches you.");

        GAIN_MESSAGES.put(MarkType.HUNGER,
                "§6Hunger§7 gnaws at your resolve.");
        LOSE_MESSAGES.put(MarkType.HUNGER,
                "§7Your stomach finds peace again.");

        GAIN_MESSAGES.put(MarkType.FEAR,
                "§5Fear§7 lingers behind your breath.");
        LOSE_MESSAGES.put(MarkType.FEAR,
                "§7Your heart steadies.");

        GAIN_MESSAGES.put(MarkType.BLOOD,
                "§4Blood§7 stains your path.");
        LOSE_MESSAGES.put(MarkType.BLOOD,
                "§7The blood dries.");

        GAIN_MESSAGES.put(MarkType.LOSS,
                "§8Loss§7 weighs on your steps.");
        LOSE_MESSAGES.put(MarkType.LOSS,
                "§7You let go of what was lost.");

        GAIN_MESSAGES.put(MarkType.FAVOR,
                "§aFavor§7 smiles upon you.");
        LOSE_MESSAGES.put(MarkType.FAVOR,
                "§7The favor fades.");

        /* ---------- PERMANENT ---------- */
        GAIN_MESSAGES.put(MarkType.SURVIVAL,
                "§bSurvival§7 acknowledges your endurance.");

        GAIN_MESSAGES.put(MarkType.PERSISTENCE,
                "§9Persistence§7 defines your journey.");

        GAIN_MESSAGES.put(MarkType.RECOGNITION,
                "§dRecognition§7 has found your name.");

        GAIN_MESSAGES.put(MarkType.WITNESS,
                "§3Witness§7 sees what you have endured.");

        GAIN_MESSAGES.put(MarkType.COLD,
                "§fCold§7 settles into your bones.");

        GAIN_MESSAGES.put(MarkType.INFLUENCE,
                "§6Influence§7 bends toward you.");
    }

    /* -------------------------------------------------
     * Public API
     * ------------------------------------------------- */
    public static void notifyGain(UUID playerId, MarkType type) {
        Player p = Bukkit.getPlayer(playerId);
        if (p == null) return;

        String msg = GAIN_MESSAGES.get(type);
        if (msg != null) {
            p.sendMessage(msg);
        }
    }

    public static void notifyLoss(UUID playerId, MarkType type) {
        Player p = Bukkit.getPlayer(playerId);
        if (p == null) return;

        String msg = LOSE_MESSAGES.get(type);
        if (msg != null) {
            p.sendMessage(msg);
        }
    }
}
