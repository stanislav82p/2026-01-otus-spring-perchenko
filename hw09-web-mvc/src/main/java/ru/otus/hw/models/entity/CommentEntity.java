package ru.otus.hw.models.entity;

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
import ru.otus.hw.models.Comment;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name = "comments")
public class CommentEntity implements Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = BookEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private BookEntity book;

    @ManyToOne(targetEntity = ReaderEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", referencedColumnName = "id", nullable = false)
    private ReaderEntity reader;

    @Column(nullable = false)
    private String text;

    @Column(name = "comment_date", nullable = false)
    private Date date;

    public CommentEntity.CommentEntityBuilder toBuilder() {
        return CommentEntity.builder()
                .id(id)
                .book(book)
                .reader(reader)
                .text(text)
                .date(date);
    }
}
