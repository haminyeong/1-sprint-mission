package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.ServiceFactory;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {

        JCFUserService user3 = JCFUserService.getInstance();
        JCFUserService user4 = JCFUserService.getInstance();

        System.out.println("같은 객체인가?" + (user3 == user4));

        // 1. 사용자 CRUD 테스트
        JCFUserService userService = ServiceFactory.userService();

        User user1 = new User("민영", "minyeong@example.com");
        User user2 = new User("민석", "minseok@example.com");

        userService.create(user1);
        userService.create(user2);

        System.out.println("=== 사용자 등록 완료 ===");

        System.out.println(userService.findById(user1.getId()).get().getName());

        System.out.println("=== 전체 사용자 목록 ===");

        for (User user : userService.findAll()) {
            System.out.println(user.getName() + "/" + user.getEmail());
        }

        userService.update(
                user1.getId(),
                "민영 수정",
                "kei08035@naver.com"
        );

        System.out.println("=== 사용자 수정 후 ===");
        System.out.println(userService.findById(user1.getId()).get().getName());

        userService.deleteById(user2.getId());

        System.out.println("=== 사용자 삭제 후 ===");

        for (User user : userService.findAll()) {
            System.out.println(user.getName());
        }


        // 2. 채널 CRUD 테스트
        JCFChannelService channelService = ServiceFactory.channelService();

        Channel channel1 = channelService.create(
                "자유 채팅",
                "자유롭게 이야기하는 채널"
        );

        System.out.println("=== 채널 등록 완료 ===");
        System.out.println(channelService.findById(channel1.getId()).getName());

        System.out.println("=== 전체 채널 목록 ===");

        for (Channel channel : channelService.findAll()) {
            System.out.println(channel.getName());
        }

        channelService.update(
                channel1.getId(),
                "공지 채널",
                "공지사항을 전달하는 채널"
        );

        System.out.println("=== 채널 수정 후 ===");
        System.out.println(channelService.findById(channel1.getId()).getName());

        channelService.delete(channel1.getId());

        System.out.println("=== 채널 삭제 후 ===");
        System.out.println(channelService.findAll().size());


        // 3. 메시지 CRUD 테스트
        JCFMessageService messageService = ServiceFactory.messageService();

        // 메시지에서 사용할 사용자와 채널을 생성
        User messageUser = new User("테스트 사용자", "test@example.com");
        userService.create(messageUser);

        Channel messageChannel = channelService.create(
                "메시지 테스트 채널",
                "메시지를 테스트하기 위한 채널"
        );

        Message message = messageService.create(
                "첫 번째 메시지",
                messageUser.getId(),
                messageChannel.getId()
        );

        System.out.println("=== 메시지 등록 완료 ===");
        System.out.println(messageService.findById(message.getId()).getContent());

        System.out.println("=== 전체 메시지 목록 ===");

        for (Message item : messageService.findAll()) {
            System.out.println(item.getContent());
        }

        messageService.update(message.getId(), "수정된 메시지");

        System.out.println("=== 메시지 수정 후 ===");
        System.out.println(messageService.findById(message.getId()).getContent());

        messageService.delete(message.getId());

        System.out.println("=== 메시지 삭제 후 ===");
        System.out.println(messageService.findAll().size());

        System.out.println("\n=== 잘못된 사용자 테스트 ===");
        try {
            messageService.create(
                    "잘못된 사용자 메시지",
                    UUID.randomUUID(),
                    channel1.getId()
            );
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n===잘못된 채널 테스트===");

        try {
            messageService.create(
                    "잘못된 채널 메시지",
                    user1.getId(),
                    UUID.randomUUID()
            );
        }catch (IllegalArgumentException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
