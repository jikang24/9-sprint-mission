package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.DTO.MessageDTO;
import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

@SpringBootApplication
public class DiscodeitApplication {
	static User setupUser(UserService userService) {
		UserDTO.CreateUserDTO createUserDTO = new UserDTO.CreateUserDTO("123", "456", "789","101112");
		User user = userService.createUser(createUserDTO);
        return user;
	}

	static Channel setupChannel(ChannelService channelService){
		ChannelDTO.CreatePublicChannelDTO createPublicChannelDTO
				= new ChannelDTO.CreatePublicChannelDTO("공지", ChannelType.PUBLIC,"공지채널입니다",null );
		Channel channel = channelService.createPublicChannel(createPublicChannelDTO);
		return channel;
	}

	static void messageCRUDTest(MessageService messageService, Channel channel, User user) {
		MessageDTO.CreateMessageDTO createMessageDTO
				= new MessageDTO.CreateMessageDTO("안녕하세요", channel.getChannelId(),channel.getUserId(),null);
		Message message = messageService.createMessage(createMessageDTO);
		System.out.println("메시지 생성: " + message.getMessageId());
    }

	public static void main(String[] args) {
//		SpringApplication.run(DiscodeitApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(
				DiscodeitApplication.class, args);

		// 메모리 저장체
		UserRepository userRepository = new JCFUserRepository();
		ChannelRepository channelRepository = new JCFChannelRepository();
//          MessageRepository messageRepository = new JCFMessageRepository();

		//직렬화 저장체
//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
		MessageRepository messageRepository = new FileMessageRepository();

		//서비스 초기화
		// TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
		UserService userService = context.getBean(BasicUserService.class);
		ChannelService channelService = context.getBean(BasicChannelService.class);
		MessageService messageService= context.getBean(BasicMessageService.class);

		User user = setupUser(userService);
		Channel channel = setupChannel(channelService);

		messageCRUDTest(messageService, channel, user);





	}

}
