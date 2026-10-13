package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.ServiceFactory;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class MessageServiceTest {

    public static void main(String[] args) {
        JCFUserService userService = ServiceFactory.userService();
        JCFChannelService channelService = ServiceFactory.channelService();
        JCFMessageService messageService = ServiceFactory.messageService();

        User user = new User("메시지 테스트 사용자", "message@example.com");
        userService.create(user);

        Channel channel = channelService.create(
                "메시지 테스트 채널",
                "메시지 테스트용 채널"
        );
        Message message = messageService.create(
                "첫 번째 메시지",
                user.getId(),
                channel.getId()
        );
        System.out.println("메시지 등록: "
        +messageService .findById(message.getId()).getContent());

        System.out.println("메시지 전체 조회: "
        +messageService.findAll().size());

        messageService.update(message.getId(), "수정된 메시지");
        System.out.println("메시지 수정: "
                +messageService.findById(message.getId()).getContent());

        messageService.delete(message.getId());
        if (messageService.findById(message.getId())== null) {
            System.out.println("메시지 삭제 테스트: 통과");
        }else {
            throw new AssertionError("메시지 삭제 테스트 실패");
        }

        try {
            messageService.create(
                    "잘못된 사용자 메시지",
                    UUID.randomUUID(),
                    channel.getId()
            );
            System.out.println("잘못된 사용자 검증: 실패");
        }catch (IllegalArgumentException e) {
            System.out.println("잘못된 사용자 검증: 통과");
        }
        try {
            messageService.create(
                    "잘못된 채널 메시지",
                    user.getId(),
                    UUID.randomUUID()
            );
            System.out.println("잘못된 채널 검증: 실패");
        }catch (IllegalArgumentException e) {
            System.out.println("잘못된 채널 검증: 통과");
        }
    }
}
