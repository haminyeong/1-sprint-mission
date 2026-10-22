package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private MessageService messageService;
    private ChannelService channelService;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public User create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보는 필수입니다.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(existing ->
                        existing.getEmail().equals(user.getEmail()));

        if (emailExists) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String name, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(existing ->
                        !existing.getId().equals(id)
                                && existing.getEmail().equals(email));

        if (emailExists) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        user.update(name, email);
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 정보는 필수입니다.");
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (messageService != null) {
            for (Message message : messageService.findAll()) {
                if (message.getAuthorId().equals(id)) {
                    messageService.delete(message.getId());
                }
            }
        }

        if (channelService != null) {
            for (Channel channel : new ArrayList<>(user.getChannels())) {
                Channel latestChannel = channelService.findById(channel.getId())
                        .orElseThrow(() ->
                                new IllegalArgumentException("채널을 찾을 수 없습니다."));

                latestChannel.removeUser(user);
                channelService.save(latestChannel);
            }
        }

        userRepository.deleteById(id);
    }
}
