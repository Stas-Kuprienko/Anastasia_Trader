package com.stanislav.entities.orders;

public enum Direction {
    Buy, Sell;


    public static Direction parse(String value) throws EnumConstantNotPresentException {
        for (Direction d : Direction.values()) {
            if (d.toString().equalsIgnoreCase(value)) {
                return d;
            }
        }
        throw new EnumConstantNotPresentException(Direction.class, value);
    }

    public static Direction parse(Enum<?> value) throws EnumConstantNotPresentException {
        for (Direction d : Direction.values()) {
            if (d.toString().equalsIgnoreCase(value.toString())) {
                return d;
            }
        }
        throw new EnumConstantNotPresentException(Direction.class, value.toString());
    }
}
