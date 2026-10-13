package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;
    private static final JCFUserService INSTANCE = new JCFUserService();
    private JCFUserService() {
        this.data = new HashMap<>();
    }
    public static JCFUserService getInstance() {
        return INSTANCE;
    }
    @Override
    public User create(User user) {
        data.put(user.getId(), user);
        return user;
    }
    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }
    @Override
    public List<User> findAll() {
        return data.values()
                .stream()
                .collect(Collectors.toList());
    }
    @Override
    public User update(UUID id, String name, String email) {
        User user = data.get(id);

        if (user== null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        user.update(name, email);
        return user;
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
