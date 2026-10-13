package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.ServiceFactory;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

public class ChannelServiceTest {
    public static void main(String[] args) {
        JCFChannelService channelService =
                ServiceFactory.channelService();

        Channel channel = channelService.create(
                "테스트 채널",
                "채널 테스트용 설명"
        );
        System.out.println("채널 등록:"
        +channelService.findById(channel.getId()).getName());

        System.out.println("채널 전체 조회:" + channelService.findAll().size());

        channelService.update(
                channel.getId(),
                "수정 채널",
                "수정된 설명"
        );
        System.out.println("채널 수정:"
        + channelService.findById(channel.getId()).getName());

        channelService.delete(channel.getId());
        System.out.println("채널 삭제: "+
                (channelService.findById(channel.getId()) == null));
    }
}
