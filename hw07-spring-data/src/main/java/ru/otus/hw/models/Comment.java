package ru.otus.hw.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne(targetEntity = Reader.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", referencedColumnName = "id", nullable = false)
    private Reader reader;

    @Column(nullable = false)
    private String text;

    @Column(name = "comment_date", nullable = false)
    private Date date;

    public Comment.CommentBuilder toBuilder() {
        return Comment.builder()
                .id(id)
                .book(book)
                .reader(reader)
                .text(text)
                .date(date);
    }
}
