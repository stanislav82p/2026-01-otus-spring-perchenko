package ru.otus.hw.service.mix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomEntityMixerTest {

    @DisplayName("Выход должен содержать все элементы входа")
    @Test
    void mustContainAllItems() {
        List<Integer> source = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        var mixer = new RandomEntityMixer(new Random());
        var mixed = mixer.mixEntityList(source);
        assertEquals(mixed.size(), source.size());

        Set<Integer> mixedItems = new HashSet<>();
        mixedItems.addAll(mixed);
        assertEquals(mixedItems.size(), source.size());

        source.forEach(it -> {
            assertTrue(mixedItems.contains(it));
        });
    }

    @DisplayName("Должен перемешивать в правильном порядке")
    @Test
    void mustMixInCorrectOrder() {
        List<Integer> testData = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> mixedDataReference = mixTestData(testData, getMockedRandomGenerator());

        var mixer = new RandomEntityMixer(getMockedRandomGenerator());
        var mixed = mixer.mixEntityList(testData);

        assertEquals(mixed, mixedDataReference);
    }

    private List<Integer> mixTestData(List<Integer> src, RandomGenerator rnd) {
        List<Integer> result = new ArrayList<>(src.size());
        List<Integer> workList = new LinkedList<>(src);
        for (int i = 0; i < src.size() - 1; i ++) {
            int index = rnd.nextInt(workList.size());
            var element = workList.remove(index);
            result.add(element);
        }
        result.add(workList.get(0));
        return result;
    }

    private RandomGenerator getMockedRandomGenerator() {
        return new RandomGenerator() {
            private final int[] data = {1, 7, 6, 0, 4, 2, 2, 0};
            private int index = 0;
            @Override
            public int nextInt(int bound) {
                return data[index++];
            }

            @Override
            public long nextLong() {
                return 0;
            }
        };
    }
}
