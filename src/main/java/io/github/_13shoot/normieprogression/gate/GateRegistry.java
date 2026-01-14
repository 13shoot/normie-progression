package io.github._13shoot.normieprogression.gate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GateRegistry {

    private static final List<Gate> gates = new ArrayList<>();

    private GateRegistry() {
    }

    public static void register(Gate gate) {
        gates.add(gate);
    }

    public static List<Gate> getAll() {
        return Collections.unmodifiableList(gates);
    }
}
