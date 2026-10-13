package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.ServiceFactory;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class UserServiceTest {

    public static void main(String[] args) {
        JCFUserService userService = ServiceFactory.userService();

        User user = new User("테스트 사용자", "test@example.com");

        // 사용자 등록
        userService.create(user);
        System.out.println("사용자 등록: "
                + userService.findById(user.getId()).isPresent());

        // 사용자 조회
        System.out.println("사용자 조회: "
                + userService.findById(user.getId()).get().getName());

        // 사용자 수정
        userService.update(
                user.getId(),
                "수정된 사용자",
                "updated@example.com"
        );
        System.out.println("사용자 수정: "
                + userService.findById(user.getId()).get().getName());

        // 사용자 삭제
        userService.deleteById(user.getId());
        System.out.println("사용자 삭제: "
                + userService.findById(user.getId()).isEmpty());
    }
}