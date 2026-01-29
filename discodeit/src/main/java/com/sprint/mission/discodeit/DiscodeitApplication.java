package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.Channel;
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

		UserDTO.CreateUserDTO createUserDTO
				= new UserDTO.CreateUserDTO("123", "456", "789");
		userService.createUser(createUserDTO);




	}

}
