package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private MessageService messageService;

    public JCFUserService() {
        this.userRepository = new JCFUserRepository();
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
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

        boolean emailExists = userRepository.findAll()
                .stream()
                .anyMatch(existingUser ->
                        existingUser.getEmail().equals(user.getEmail()));

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

        boolean emailExists = userRepository.findAll()
                .stream()
                .anyMatch(existingUser ->
                        !existingUser.getId().equals(id)
                                && existingUser.getEmail().equals(email));

        if (emailExists) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        user.update(name, email);

        return userRepository.save(user);
    }




    @Override
    public void deleteById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 사용자가 작성한 메시지 삭제
        for (Message message : new java.util.ArrayList<>(user.getMessages())) {
            messageService.delete(message.getId());
        }

        // 사용자가 참여한 채널과의 관계 정리
        for (Channel channel : new java.util.ArrayList<>(user.getChannels())) {
            user.removeChannel(channel);
            channel.removeUser(user);
        }

        // 저장소에서 사용자 삭제
        userRepository.deleteById(id);
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}