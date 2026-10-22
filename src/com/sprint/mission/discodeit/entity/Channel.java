package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends BaseEntity {

    private String name;
    private String description;
    private List<User> users = new ArrayList<>();

    public Channel(String name, String description) {
        super();

        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
        updateUpdatedAt();
    }

    public List<User> getUsers() {
        return List.copyOf(users);
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public boolean hasUser(User user) {
        return users.contains(user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Channel channel = (Channel) obj;
        return getId().equals(channel.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}