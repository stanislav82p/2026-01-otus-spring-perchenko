package ru.otus.hw.models;

import java.sql.Date;

public interface Comment {
    long getId();

    Book getBook();

    Reader getReader();

    String getText();

    Date getDate();
}
