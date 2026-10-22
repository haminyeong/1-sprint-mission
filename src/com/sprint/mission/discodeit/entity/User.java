package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity {

    private String name;
    private String email;
    private List<Channel> channels = new ArrayList<>();

    public User(String name, String email) {
        super();

        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
        updateUpdatedAt();
    }

    public List<Channel> getChannels() {
        return List.copyOf(channels);
    }

    public void addChannel(Channel channel) {
        if (!channels.contains(channel)) {
            channels.add(channel);
        }
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public boolean hasChannel(Channel channel) {
        return channels.contains(channel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
