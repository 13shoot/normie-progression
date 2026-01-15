package io.github._13shoot.normieprogression.chronicle;

import io.github._13shoot.normieprogression.tier.Tier;

import java.util.List;
import java.util.UUID;

public class TierChronicleHook {

    public static void onTierReached(UUID player, Tier tier) {

        String id = "path.tier." + tier.name().toLowerCase();

        // เขียนครั้งเดียวเท่านั้น
        if (ChronicleStorage.has(player, id)) return;

        ChronicleEntry entry = switch (tier) {

            case T0_UNSEEN -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "Unseen",
                    List.of(
                            "You existed quietly.",
                            "The world did not notice."
                    )
            );

            case T1_RECOGNIZED -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "The World Noticed",
                    List.of(
                            "You were no longer just another name.",
                            "Something began watching."
                    )
            );

            case T2_ACKNOWLEDGED -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "Acknowledged",
                    List.of(
                            "The world accepted your presence.",
                            "Silence became expectation."
                    )
            );

            case T3_RESPONDED -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "A Response",
                    List.of(
                            "The world answered your actions.",
                            "Consequences followed."
                    )
            );

            case T4_REMEMBERED -> new ChronicleEntry(
                    ChronicleType.PATH,
                    id,
                    "Worthy of Memory",
                    List.of(
                            "You were no longer temporary.",
                            "The world decided to remember."
                    )
            );
        };

        ChronicleStorage.add(player, entry);
    }
}
