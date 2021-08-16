package ru.mihkopylov.myspb.dao;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mihkopylov.myspb.model.User;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    @NonNull
    Optional<User> findByLogin(@NonNull String login);
}
