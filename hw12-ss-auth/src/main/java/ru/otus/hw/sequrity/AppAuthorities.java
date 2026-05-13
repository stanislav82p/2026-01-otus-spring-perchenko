package ru.otus.hw.sequrity;

public class AppAuthorities {
    /**
     * Смотреть список книг, жанров и авторов
     */
    public static final String VIEW_LIBRARY_INDEX = "view_lib_index";

    /**
     * Смотреть детали книги (читать книгу)
     */
    public static final String VIEW_BOOK_DETAILS = "view_book_details";

    /**
     * Написание комментариев. Редактировать/удалять свои комментарии
     */
    public static final String WRITE_EDIT_COMMENTS = "commenter";

    /**
     * Добавлять/редактировать/удалять книги
     */
    public static final String EDIT_BOOKS = "lib_editor";

    /**
     * Удалять чужие комментарии
     */
    public static final String MODERATE_ALL_COMMENTS = "comment_moderator";
}
