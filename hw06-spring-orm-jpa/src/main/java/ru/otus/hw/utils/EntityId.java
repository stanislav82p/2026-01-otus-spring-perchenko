package ru.otus.hw.utils;


public class EntityId<T> {


    public final long id;

    private EntityId(long id) {
        this.id = id;
    }

    public static <T> EntityId<T> forValue(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Not valid ID: " + id);
        }
        return new EntityId<T>(id);
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
