package io.github._13shoot.normieprogression.chronicle;

import java.util.List;

public class ChronicleEntry {

    private final ChronicleType type;
    private final String id;
    private final String title;
    private final List<String> body;

    public ChronicleEntry(ChronicleType type, String id, String title, List<String> body) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public ChronicleType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getBody() {
        return body;
    }
}
