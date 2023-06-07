package model.room.enums;

public enum RoomType {
    SINGLE("1"),
    DOUBLE("2");

    public final String value;

    private RoomType(String value) {
        this.value = value;
    }

    public static RoomType valueOfLabel(String label) {
        for (RoomType roomType : values()) {
            if (roomType.value.equals(label)) {
                return roomType;
            }
        }
        throw new IllegalArgumentException("This is an invalid room type label: " + label);
    }
}
