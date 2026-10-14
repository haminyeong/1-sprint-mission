package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        if (userService.findById(authorId).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다.");
        }
        if (channelService.findById(channelId) ==null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        Message message = new Message(content,authorId,channelId);
        data.put(message.getId(),message);
        return message;
    }
    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }
    @Override
    public List<Message> findAll() {
        return data.values()
                .stream()
                .collect(Collectors.toList());
    }
    @Override
    public Message update(UUID id, String content) {
        Message message = data.get(id);

        if (message == null) {
            return null;
        }
        message.update(content);
        return message;
    }
    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
