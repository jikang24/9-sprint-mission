package com.sprint.mission.discodeit;

import entity.Channel;
import entity.ChannelType;
import entity.Message;
import entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import repository.ChannelRepository;
import repository.MessageRepository;
import repository.UserRepository;
import repository.file.FileMessageRepository;
import repository.jcf.JCFChannelRepository;
import repository.jcf.JCFUserRepository;
import service.ChannelService;
import service.MessageService;
import service.UserService;
import service.basic.BasicChannelService;
import service.basic.BasicMessageService;
import service.basic.BasicUserService;

@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(
				DiscodeitApplication.class, args);





//		User setupUser(UserService userService) {
//			User user = userService.createUser("woody", "woody@codeit.com", "821031039281");
//			System.out.println(user.toString());
//		}
//
//		Channel setupChannel(ChannelService channelService) {
//			Channel channel = channelService.createChannel(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
//			System.out.println(channel.toString());
//		}
//
//		void messageCreateTest(MessageService messageService, Channel channel, User author) {
//			Message message = messageService.createMessage("안녕하세요.", channel.getChannelId(), author.getUserId());
//			System.out.println("메시지 생성: " + message.getMessageId());
//		}



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

////		 셋업
//		User user = setupUser(userService);
//		Channel channel = setupChannel(channelService);
//        // 메시지 생성 테스트
//		setupUser(userService);
//		setupChannel(channelService);
//		messageCreateTest(messageService, channel, user);




	}

}
