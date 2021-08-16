package ru.mihkopylov.myspb.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mihkopylov.myspb.Api;
import ru.mihkopylov.myspb.dao.UserDao;
import ru.mihkopylov.myspb.model.User;
import ru.mihkopylov.myspb.service.dto.ProfileResponse;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    @NonNull
    private final UserDao userDao;
    @NonNull
    private final HttpService httpService;

    @NonNull
    public ProfileResponse getProfile() {
        return httpService.get(Api.PROFILE, ProfileResponse.class);
    }

    @NonNull
    public User createUserIfNotExists(@NonNull String login) {
        return userDao.findByLogin(login).orElseGet(() -> {
            User user = new User();
            user.setLogin(login);
            return userDao.save(user);
        });
    }

    @NonNull
    public Optional<User> findUserByLogin(@NonNull String login) {
        return userDao.findByLogin(login);
    }
}
