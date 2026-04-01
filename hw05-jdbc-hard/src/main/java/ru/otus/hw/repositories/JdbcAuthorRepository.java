package ru.otus.hw.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    public static final String TABLE_NAME = "authors";

    public static final String ID = "id";

    public static final String FULL_NAME = "full_name";

    private final NamedParameterJdbcOperations namedParamJdbc;

    @Autowired
    public JdbcAuthorRepository(NamedParameterJdbcOperations namedParamJdbc) {
        this.namedParamJdbc = namedParamJdbc;
    }

    @Override
    public List<Author> findAll() {
        var sql = "SELECT %1$s, %2$s FROM %3$s".formatted(ID, FULL_NAME, TABLE_NAME);
        return namedParamJdbc.query(sql, new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var params = Map.of("id", id);
        var sql = "SELECT %1$s, %2$s FROM %3$s WHERE %1$s = :id".formatted(ID, FULL_NAME, TABLE_NAME);
        List<Author> authors = namedParamJdbc.query(sql, params, new AuthorRowMapper());
        return authors.isEmpty() ? Optional.empty() : Optional.of(authors.get(0));
    }

    @Override
    public Map<Long, Author> findByIds(Set<Long> ids) {
        var params = Map.of("ids", ids);
        var sql = "SELECT %1$s, %2$s FROM %3$s WHERE %1$s IN (:ids)".formatted(ID, FULL_NAME, TABLE_NAME);
        List<Author> authors = namedParamJdbc.query(sql, params, new AuthorRowMapper());

        Map<Long, Author> result = new HashMap<>(authors.size());
        for (Author author : authors) {
            result.put(author.getId(), author);
        }
        return result;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong(ID);
            String name = rs.getString(FULL_NAME);
            if (id <= 0) {
                throw new SQLException("Invalid ID of an Author: " + id);
            }
            if ((name == null) || name.isEmpty()) {
                throw new SQLException("Name of an author with id=%s is NULL or empty".formatted(id));
            }
            return new Author(id, name);
        }
    }
}
