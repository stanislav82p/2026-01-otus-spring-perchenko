package ru.otus.hw.service.mix;

import java.util.List;

public interface EntityMixer {
    <E> List<E> mixEntityList(List<E> src);
}
