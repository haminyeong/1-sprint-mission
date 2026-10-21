package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private MessageService messageService;

    public FileChannelService(UserService userService) {
        this.channelRepository = new FileChannelRepository();
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Channel create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("채널 이름은 필수입니다.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("채널 설명은 필수입니다.");
        }

        boolean nameExists = channelRepository.findAll()
                .stream()
                .anyMatch(channel -> channel.getName().equals(name));

        if (nameExists) {
            throw new IllegalArgumentException("이미 존재하는 채널 이름입니다.");
        }

        Channel channel = new Channel(name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String name, String description) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("채널을 찾을 수 없습니다."));

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("채널 이름은 필수입니다.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("채널 설명은 필수입니다.");
        }

        boolean nameExists = channelRepository.findAll()
                .stream()
                .anyMatch(existingChannel ->
                        !existingChannel.getId().equals(id)
                                && existingChannel.getName().equals(name));

        if (nameExists) {
            throw new IllegalArgumentException("이미 존재하는 채널 이름입니다.");
        }

        channel.update(name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel save(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("채널 정보는 필수입니다.");
        }

        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("채널을 찾을 수 없습니다."));

        // 채널에 연결된 메시지 삭제
        if (messageService != null) {
            for (Message message : new ArrayList<>(channel.getMessages())) {
                messageService.delete(message.getId());
            }
        }

        // 참여 사용자와 채널의 관계 제거
        for (User user : new ArrayList<>(channel.getUsers())) {
            user.removeChannel(channel);
            userService.save(user);
            channel.removeUser(user);
        }

        // 채널 삭제
        channelRepository.deleteById(id);
    }

    @Override
    public void join(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new IllegalArgumentException("채널을 찾을 수 없습니다."));

        User user = userService.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (channel.hasUser(user)) {
            throw new IllegalArgumentException("이미 참여 중인 채널입니다.");
        }

        channel.addUser(user);
        user.addChannel(channel);

        channelRepository.save(channel);
        userService.save(user);
    }

    @Override
    public void leave(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new IllegalArgumentException("채널을 찾을 수 없습니다."));

        User user = userService.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!channel.hasUser(user)) {
            throw new IllegalArgumentException("참여하지 않은 채널입니다.");
        }

        channel.removeUser(user);
        user.removeChannel(channel);

        channelRepository.save(channel);
        userService.save(user);
    }
}
