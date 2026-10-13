package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> channels;

    public JCFChannelService() {
        this.channels = new HashMap<>();
    }

    @Override
    public Channel create(String name, String description) {
        Channel channel = new Channel(name, description);
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return channels.values()
                .stream()
                .collect(Collectors.toList());
    }
    @Override
    public Channel update(UUID id, String name, String description) {
        Channel channel = channels.get(id);
        if (channel == null) {
            return null;
        }
        channel.update(name, description);
        return  channel;
    }
    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}