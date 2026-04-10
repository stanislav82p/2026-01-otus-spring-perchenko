insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'), ('Author_4');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into readers(full_name)
values ('Reader_1'), ('Reader_2'), ('Reader_3'), ('Reader_4');

insert into comments(book_id, reader_id, text, comment_date)
values (1, 1, 'comment-1-1', '2000-03-22'),
       (1, 2, 'comment-1-2', '2000-04-22'),
       (1, 3, 'comment-1-3', '2000-05-22'),
       (2, 3, 'comment-2-3', '2000-06-22'),
       (2, 1, 'comment-2-4', '2001-03-22');
