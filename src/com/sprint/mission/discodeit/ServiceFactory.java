package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class ServiceFactory {
    private static final JCFUserService USER_SERVICE =
            new JCFUserService();
    
    private static final JCFChannelService CHANNEL_SERVICE =
            new JCFChannelService(USER_SERVICE);

    private static final JCFMessageService MESSAGE_SERVICE =
            new JCFMessageService(USER_SERVICE, CHANNEL_SERVICE);

    private ServiceFactory() {
    }

    public static JCFUserService userService() {
        return USER_SERVICE;
    }

    public static JCFChannelService channelService() {
        return CHANNEL_SERVICE;
    }

    public static JCFMessageService messageService() {
        return MESSAGE_SERVICE;
    }
}
