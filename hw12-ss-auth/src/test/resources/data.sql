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

insert into readers(id, first_name, last_name, pwd_hash, role)
values ('usr1', 'firstname_1', 'lastname_1', 'hash_1', 'PUBLIC'),
       ('usr2', 'firstname_2', 'lastname_2', 'hash_2', 'READER'),
       ('usr3', 'firstname_3', 'lastname_3', 'hash_3', 'EDITOR'),
       ('usr4', 'firstname_4', 'lastname_4', 'hash_4', 'MODERATOR');

insert into comments(book_id, reader_id, text, comment_date)
values (1, 'usr1', 'comment-1-1', '2000-03-22'),
       (1, 'usr2', 'comment-1-2', '2000-04-22'),
       (1, 'usr3', 'comment-1-3', '2000-05-22'),
       (2, 'usr3', 'comment-2-3', '2000-06-22'),
       (2, 'usr1', 'comment-2-1', '2001-03-22'),
       (2, 'usr4', 'comment-2-4', '2002-03-22'),
       (2, 'usr2', 'comment-2-2', '2003-03-22'),
       (2, 'usr1', 'comment-2-1', '2004-03-22');
