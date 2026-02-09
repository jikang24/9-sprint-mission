package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
//	static User setupUser(UserService userService) {
//		String unique = String.valueOf(System.currentTimeMillis());
//		UserDTO.CreateUserDTO createUserDTO = new UserDTO.CreateUserDTO(
//				"user: " + unique, "email: " + unique, "789","101112");
//		return userService.createUser(createUserDTO);
//	}
//
//	static Channel setupChannel(ChannelService channelService){
//		ChannelDTO.CreatePublicChannelDTO createPublicChannelDTO
//				= new ChannelDTO.CreatePublicChannelDTO("공지", ChannelType.PUBLIC,"공지채널입니다",null );
//		return channelService.createPublicChannel(createPublicChannelDTO);
//	}
//
//	static void messageCRUDTest(MessageService messageService, Channel channel, User user) {
//		MessageDTO.CreateMessageDTO createMessageDTO
//				= new MessageDTO.CreateMessageDTO("안녕하세요",channel.getChannelId(), user.getUserId(),null);
//		Message message = messageService.createMessage(createMessageDTO);
//		System.out.println("메시지 생성: " + message.getMessageId());
//    }

	public static void main(String[] args) {
//		SpringApplication.run(DiscodeitApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(
				DiscodeitApplication.class, args);

//		// 메모리 저장체
//		UserRepository userRepository = new JCFUserRepository();
//		ChannelRepository channelRepository = new JCFChannelRepository();
//		MessageRepository messageRepository = new JCFMessageRepository();
//
//		//직렬화 저장체
//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
//		MessageRepository messageRepository = new FileMessageRepository();
//
//		//서비스 초기화
//		UserService userService = context.getBean(BasicUserService.class);
//		ChannelService channelService = context.getBean(BasicChannelService.class);
//		MessageService messageService= context.getBean(BasicMessageService.class);
//
//		User user = setupUser(userService);
//		Channel channel = setupChannel(channelService);
//
//		messageCRUDTest(messageService, channel, user);





	}

}
