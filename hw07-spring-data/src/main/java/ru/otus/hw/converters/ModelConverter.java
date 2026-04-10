package ru.otus.hw.converters;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.ResolvableType;

// Передалал на дженерик, чтобы разобраться как они инжектятся
public interface ModelConverter<T> {
    String convertToString(T model);

    // Нашел интересную штуку как извлечь реализацию дженерика из контекста явно
    static <T> ModelConverter<T> getConverterFor(BeanFactory factory, Class<T> clazz) {
        var type = ResolvableType.forClassWithGenerics(ModelConverter.class, clazz);
        return (ModelConverter<T>) factory.getBeanProvider(type).getObject();
    }
}
