package models;

public enum FuelType {
    GASOLINE,
    DIESEL,
    ELECTRIC,
    HYBRID;

    public static String names() {
        StringBuilder names = new StringBuilder();
        for (FuelType type : values()) {
            names.append(type.name()).append(", ");
        }
        return names.substring(0, names.length() - 2); // Убираем последнее ", "
    }
}
