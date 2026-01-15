package io.github._13shoot.normieprogression.chronicle;

import org.bukkit.entity.Player;

import java.util.List;

public class GateChronicleHook {

    public static void onGatePassed(Player player, String gateId) {

        String id = "path.gate." + gateId;

        if (ChronicleStorage.has(player.getUniqueId(), id)) return;

        ChronicleEntry entry = switch (gateId) {

            case "recognition" -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "No Longer Ignored",
                    List.of(
                            "Your presence became undeniable.",
                            "The world adjusted."
                    )
            );

            case "responded" -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "A Reaction",
                    List.of(
                            "The world did not stay silent.",
                            "Your actions demanded an answer."
                    )
            );

            case "remembered" -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "Worthy of Memory",
                    List.of(
                            "You were no longer temporary.",
                            "The world decided to remember."
                    )
            );

            default -> null;
        };

        if (entry != null) {
            ChronicleStorage.add(player.getUniqueId(), entry);
        }
    }
}
