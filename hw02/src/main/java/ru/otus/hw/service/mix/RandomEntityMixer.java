package ru.otus.hw.service.mix;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
public class RandomEntityMixer implements EntityMixer {
    private final RandomGenerator rnd;

    @Override
    public <E> List<E> mixEntityList(List<E> src) {
        if (src.size() < 2) {
            return src;
        }
        List<E> result = new ArrayList<>(src.size());
        List<E> workList = new LinkedList<>(src);
        synchronized (rnd) {
            for (int i = 0; i < src.size() - 1; i ++) {
                int index = rnd.nextInt(workList.size());
                var element = workList.remove(index);
                result.add(element);
            }
            result.add(workList.get(0));
        }
        return result;
    }
}
