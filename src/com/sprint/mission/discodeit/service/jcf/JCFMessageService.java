package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(
            UserService userService,
            ChannelService channelService
    ) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {

        // 1. 작성자가 존재하는지 확인
        User user = userService.findById(authorId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "존재하지 않는 사용자입니다."
                        ));

        // 2. 채널이 존재하는지 확인
        Channel channel = channelService.findById(channelId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "존재하지 않는 채널입니다."
                        ));

        // 3. 메시지 생성
        Message message = new Message(content, authorId, channelId);

        // 4. 메시지 저장
        data.put(message.getId(), message);

        // 5. 사용자와 채널의 메시지 목록에 추가
        user.addMessage(message);
        channel.addMessage(message);

        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return data.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Message update(UUID id, String content) {

        // 1. 수정할 메시지 조회
        Message message = data.get(id);

        // 2. 메시지가 없으면 예외 발생
        if (message == null) {
            throw new IllegalArgumentException(
                    "메시지를 찾을 수 없습니다."
            );
        }

        // 3. 메시지 수정
        message.update(content);

        return message;
    }

    @Override
    public void delete(UUID id) {

        // 1. 삭제할 메시지 조회
        Message message = data.get(id);

        // 2. 메시지가 없으면 예외 발생
        if (message == null) {
            throw new IllegalArgumentException(
                    "메시지를 찾을 수 없습니다."
            );
        }

        // 3. 작성자 조회
        User user = userService.findById(message.getAuthorId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "사용자를 찾을 수 없습니다."
                        ));

        // 4. 채널 조회
        Channel channel = channelService.findById(message.getChannelId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "채널을 찾을 수 없습니다."
                        ));

        // 5. 사용자와 채널의 메시지 목록에서 제거
        user.removeMessage(message);
        channel.removeMessage(message);

        // 6. 메시지 저장소에서 제거
        data.remove(id);
    }
}