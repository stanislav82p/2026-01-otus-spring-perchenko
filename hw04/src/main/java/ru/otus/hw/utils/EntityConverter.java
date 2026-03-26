package ru.otus.hw.utils;

public interface EntityConverter<T, R> {
    R convert(T from);
}
