package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.ServiceFactory;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {

        // 1. 싱글턴 패턴 테스트
        JCFUserService user3 = JCFUserService.getInstance();
        JCFUserService user4 = JCFUserService.getInstance();

        System.out.println("=== 싱글턴 테스트 ===");
        System.out.println("같은 객체인가요? " + (user3 == user4));
        System.out.println(
                "팩토리와 싱글턴이 같은 사용자 서비스인가요? "
                        + (JCFUserService.getInstance() == ServiceFactory.userService())
        );

        // 서비스 가져오기
        UserService userService = ServiceFactory.userService();
        ChannelService channelService = ServiceFactory.channelService();
        MessageService messageService = ServiceFactory.messageService();

        // 2. 사용자 CRUD 테스트
        System.out.println("\n=== 사용자 CRUD 테스트 ===");

        User user1 = new User("민영", "minyeong@example.com");
        User user2 = new User("민석", "minseok@example.com");

        userService.create(user1);
        userService.create(user2);

        System.out.println("사용자 등록 완료");
        System.out.println("사용자 조회: "
                + userService.findById(user1.getId()).get().getName());

        System.out.println("전체 사용자 목록");
        for (User user : userService.findAll()) {
            System.out.println(user.getName() + " / " + user.getEmail());
        }

        userService.update(
                user1.getId(),
                "민영 수정",
                "minyeong@example.com"
        );

        System.out.println("수정 후 사용자: "
                + userService.findById(user1.getId()).get().getName());

        userService.deleteById(user2.getId());

        if (!userService.findById(user2.getId()).isPresent()) {
            System.out.println("사용자 삭제 테스트 성공");
        } else {
            throw new AssertionError("사용자 삭제 테스트 실패");
        }

        // 3. 채널 CRUD 테스트
        System.out.println("\n=== 채널 CRUD 테스트 ===");

        Channel channel1 = channelService.create(
                "자유 채팅",
                "자유롭게 이야기하는 채널"
        );

        System.out.println("채널 등록 완료");
        System.out.println("채널 조회: "
                + channelService.findById(channel1.getId()).getName());

        System.out.println("전체 채널 목록");
        for (Channel channel : channelService.findAll()) {
            System.out.println(channel.getName());
        }

        channelService.update(
                channel1.getId(),
                "공지 채널",
                "공지사항을 전달하는 채널"
        );

        System.out.println("수정 후 채널: "
                + channelService.findById(channel1.getId()).getName());

        channelService.delete(channel1.getId());

        if (channelService.findById(channel1.getId()) == null) {
            System.out.println("채널 삭제 테스트 성공");
        } else {
            throw new AssertionError("채널 삭제 테스트 실패");
        }

        // 4. 메시지 CRUD 테스트
        System.out.println("\n=== 메시지 CRUD 테스트 ===");

        User messageUser = new User(
                "테스트 사용자",
                "test@example.com"
        );
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

        System.out.println("메시지 등록 완료");
        System.out.println("메시지 조회: "
                + messageService.findById(message.getId()).getContent());

        System.out.println("전체 메시지 목록");
        for (Message item : messageService.findAll()) {
            System.out.println(item.getContent());
        }

        messageService.update(
                message.getId(),
                "수정된 메시지"
        );

        System.out.println("수정 후 메시지: "
                + messageService.findById(message.getId()).getContent());

        messageService.delete(message.getId());

        if (messageService.findById(message.getId()) == null) {
            System.out.println("메시지 삭제 테스트 성공");
        } else {
            throw new AssertionError("메시지 삭제 테스트 실패");
        }

        // 5. 잘못된 사용자 ID 검증
        System.out.println("\n=== 잘못된 사용자 테스트 ===");

        try {
            messageService.create(
                    "잘못된 사용자 메시지",
                    UUID.randomUUID(),
                    messageChannel.getId()
            );

            System.out.println("오류: 잘못된 사용자 메시지가 생성되었습니다.");

        } catch (IllegalArgumentException e) {
            System.out.println("검증 성공: " + e.getMessage());
        }

        // 6. 잘못된 채널 ID 검증
        System.out.println("\n=== 잘못된 채널 테스트 ===");

        try {
            messageService.create(
                    "잘못된 채널 메시지",
                    messageUser.getId(),
                    UUID.randomUUID()
            );

            System.out.println("오류: 잘못된 채널 메시지가 생성되었습니다.");

        } catch (IllegalArgumentException e) {
            System.out.println("검증 성공: " + e.getMessage());
        }

        System.out.println("\n=== 모든 테스트 종료 ===");
    }
}