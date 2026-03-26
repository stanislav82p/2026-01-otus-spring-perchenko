package ru.otus.hw.service.mix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RandomEntityMixerTest {

    @Configuration
    @ComponentScan("ru.otus.hw.service.mix")
    static class NestedConfig {

        @Bean("randomGenerator")
        // Этот мок можно использовать только 1 раз. Поэтому PROTOTYPE
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        RandomGenerator getMockedRandomGenerator() {
            System.out.println("get random!!!");
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

    @Autowired
    private EntityMixer mixerToTest;

    @Autowired
    private ApplicationContext context;

    @DisplayName("Выход должен содержать все элементы входа")
    @Test
    void mustContainAllItems() {
        List<Integer> source = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        var mixed = mixerToTest.mixEntityList(source);
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
        var rnd = context.getBean(RandomGenerator.class);
        List<Integer> mixedDataReference = mixTestData(testData, rnd);

        var mixed = mixerToTest.mixEntityList(testData);

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
}
