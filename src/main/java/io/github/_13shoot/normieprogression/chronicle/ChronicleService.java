package io.github._13shoot.normieprogression.chronicle;

import java.util.List;
import java.util.UUID;

public class ChronicleService {

    public static void addMarkChronicle(
            UUID player,
            String id,
            String title,
            List<String> body
    ) {

        ChronicleEntry entry = new ChronicleEntry(
                ChronicleType.MARK,
                id,
                title,
                body
        );

        ChronicleStorage.add(player, entry);
    }

    public static void addPathChronicle(
            UUID player,
            String id,
            String title,
            List<String> body
    ) {

        ChronicleEntry entry = new ChronicleEntry(
                ChronicleType.PATH,
                id,
                title,
                body
        );

        ChronicleStorage.add(player, entry);
    }
}
