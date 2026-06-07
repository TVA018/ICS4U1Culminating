package data.robot.enums;

public enum Indexer {
    DYE_ROTOR("Dye Rotor"),
    DOUBLE_DYE_ROTOR("Double Dye Rotor"),
    ROLLER_FLOOR("Roller Floor"),
    BELT_FLOOR("Belt Floor"),
    SPINDEXER("Spindexer"),
    DOUBLE_SPINDEXER("Double Spindexer"),
    NONE("None");

    private final String name;

    private Indexer(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}