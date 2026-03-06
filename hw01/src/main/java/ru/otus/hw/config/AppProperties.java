package ru.otus.hw.config;

public record AppProperties(String testFileName) implements TestFileNameProvider {

    public AppProperties {
        if (testFileName == null) {
            throw new IllegalArgumentException("Test file name must not be NULL");
        } else if (testFileName.isBlank()) {
            throw new IllegalArgumentException("Test file name must not be empty or blank");
        } else if (testFileName.contains(" ")) {
            throw new IllegalArgumentException("Test file name must not be multiple words");
        }
    }
}
