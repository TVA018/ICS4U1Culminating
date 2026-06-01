package data.robot.enums;

public enum Indexer {
    DYE_ROTOR,
    DOUBLE_DYE_ROTOR,
    ROLLER_FLOOR,
    BELT_FLOOR,
    SPINDEXER,
    DOUBLE_SPINDEXER,
    NONE;

    public static Indexer fromString(String string) {
        return switch (string) {
            case "dye rotor" -> Indexer.DYE_ROTOR;
            case "double dye rotor" -> Indexer.DOUBLE_DYE_ROTOR;
            case "roller floor" -> Indexer.ROLLER_FLOOR;
            case "belt floor" -> Indexer.BELT_FLOOR;
            case "spindexer" -> Indexer.SPINDEXER;
            case "double spindexer" -> Indexer.DOUBLE_SPINDEXER;
            case "none" -> Indexer.NONE;
            default -> throw new RuntimeException(string + " is not a valid Indexer String");
        };
    }
}