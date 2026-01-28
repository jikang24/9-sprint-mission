package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public DataInitializer(UserService userService, ChannelService channelService, MessageService messageService) {
        this.userService = userService;
        this.channelService = channelService;
        this.messageService = messageService;
    }

    @Override
    public void run(String... args){
        User user = setupUser();
        Channel channel = setupChannel();
        messageCreateTest(user, channel);

    }

    private User setupUser() {
        User user = userService.createUser(
                "Kang", "jiwon@codeit.com","0281739102"
        );
        System.out.println(user);
        return user;
    }

    private Channel setupChannel() {
        Channel channel = channelService.createChannel(
                ChannelType.PUBLIC,
                "공지 채널",
                "공지 채널입니다"
        );
        System.out.println(channel);
        return channel;
    }

    private void messageCreateTest(User user, Channel channel) {
        Message message = messageService.createMessage(
                "안녕하세요.",
                channel.getChannelId(),
                user.getUserId()
        );

        System.out.println("메시지 생성: " + message.getMessageId());
    }


}
