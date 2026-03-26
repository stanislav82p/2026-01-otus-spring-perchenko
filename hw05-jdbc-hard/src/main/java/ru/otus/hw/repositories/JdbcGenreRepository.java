package ru.otus.hw.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    public static final String TABLE_NAME = "genres";

    public static final String ID = "id";

    public static final String NAME = "name";

    private final NamedParameterJdbcOperations namedParamJdbc;

    @Autowired
    public JdbcGenreRepository(NamedParameterJdbcOperations namedParamJdbc) {
        this.namedParamJdbc = namedParamJdbc;
    }

    @Override
    public List<Genre> findAll() {
        var sql = "SELECT %1$s, %2$s FROM %3$s ORDER BY %2$s ASC".formatted(ID, NAME, TABLE_NAME);
        return namedParamJdbc.query(sql, new GenreRowMapper());
    }

    @Override
    public Set<Genre> findAllByIds(Set<Long> ids) {
        var sql = "SELECT %1$s, %2$s FROM %3$s WHERE %1$s IN (:ids)".formatted(ID, NAME, TABLE_NAME);

        Map<String, Set<Long>> params = Map.of("ids", ids);
        List<Genre> genres = namedParamJdbc.query(sql, params, new GenreRowMapper());
        return new HashSet<>(genres);
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong(ID);
            String name = rs.getString(NAME);
            if (id <= 0) {
                throw new SQLException("Invalid ID of a Genre: " + id);
            }
            if ((name == null) || name.isEmpty()) {
                throw new SQLException("Name of a Genre with id=%s is NULL or empty".formatted(id));
            }
            return new Genre(id, name);
        }
    }
}
