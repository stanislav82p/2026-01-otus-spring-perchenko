package ru.otus.hw.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.hw.sequrity.UserRole;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "readers")
public class ReaderEntity {

    @Id
    @Column(name = "id")
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "pwd_hash", nullable = false, unique = true)
    private String pwdHash;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public String getFullName() {
        return "%s %s".formatted(firstName, lastName);
    }

    public static ReaderEntity forId(String username) {
        return new ReaderEntity(username, "", "", "", UserRole.PUBLIC);
    }
}
