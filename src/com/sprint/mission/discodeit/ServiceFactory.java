package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;

public class ServiceFactory {

    private static final FileUserService USER_SERVICE =
            new FileUserService();

    private static final FileChannelService CHANNEL_SERVICE =
            new FileChannelService(USER_SERVICE);

    private static final FileMessageService MESSAGE_SERVICE =
            new FileMessageService(USER_SERVICE, CHANNEL_SERVICE);

    static {
        USER_SERVICE.setMessageService(MESSAGE_SERVICE);
        USER_SERVICE.setChannelService(CHANNEL_SERVICE);
        CHANNEL_SERVICE.setMessageService(MESSAGE_SERVICE);
    }

    private ServiceFactory() {
    }

    public static UserService userService() {
        return USER_SERVICE;
    }

    public static ChannelService channelService() {
        return CHANNEL_SERVICE;
    }

    public static MessageService messageService() {
        return MESSAGE_SERVICE;
    }
}
