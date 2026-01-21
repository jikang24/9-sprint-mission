import entity.ChannelType;
import entity.Message;

import java.util.List;
import entity.User;
import repository.ChannelRepository;
import repository.MessageRepository;
import repository.UserRepository;
import repository.file.FileChannelRepository;
import repository.file.FileMessageRepository;
import repository.file.FileUserRepository;
import repository.jcf.JCFChannelRepository;
import repository.jcf.JCFMessageRepository;
import repository.jcf.JCFUserRepository;
import service.ChannelService;
import service.MessageService;
import service.UserService;
import service.basic.BasicChannelService;
import service.basic.BasicMessageService;
import service.basic.BasicUserService;
import service.file.FileChannelService;
import service.file.FileMessageService;
import service.file.FileUserService;
import service.jcf.JCFChannelService;
import service.jcf.JCFMessageService;
import service.jcf.JCFUserService;
import entity.Channel;

import java.util.UUID;
import java.util.List;
import java.util.UUID;

public class NewJavaApplication {
    static void userCRUDTest(UserService userService) {
        // 생성
        User user = userService.createUser("강지원", "jiwon@codeit.com", "01837271939");
        System.out.println("유저 생성: " + user.getUserId());
        // 조회
        User foundUser = userService.findByUserId(user.getUserId());
        System.out.println("유저 조회(단건): " + foundUser.getUserId());
        List<User> foundUsers = userService.findAllUser();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        User updatedUser = userService.updateUser(user.getUserId(), null, null, "woody5678");
        System.out.println("유저 수정: " + String.join("/", updatedUser.getUserName(), updatedUser.getEmail(), updatedUser.getPhoneNumber()));
        // 삭제
        userService.deleteUser(user.getUserId());
        List<User> foundUsersAfterDelete = userService.findAllUser();
        System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
    }

    static void channelCRUDTest(ChannelService channelService) {
        // 생성
        Channel channel = channelService.createChannel(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성: " + channel.getChannelId());
        // 조회
        Channel foundChannel = channelService.findByChannelId(channel.getChannelId());
        System.out.println("채널 조회(단건): " + foundChannel.getChannelId());
        List<Channel> foundChannels = channelService.findAllChannel();
        System.out.println("채널 조회(다건): " + foundChannels.size());
        // 수정
        Channel updatedChannel = channelService.updateChannel(channel.getChannelId(), "공지사항", null, null);
        System.out.println("채널 수정: " + String.join("/", updatedChannel.getChannelName(), updatedChannel.getDescription()));
        // 삭제
//        channelService.deleteChannel(channel.getChannelId());
//        List<Channel> foundChannelsAfterDelete = channelService.findAllChannel();
//        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
    }

    static void messageCRUDTest(MessageService messageService) {
        // 생성
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Message message = messageService.createMessage("안녕하세요.", channelId, authorId);
        System.out.println("메시지 생성: " + message.getMessageId());
        // 조회
        Message foundMessage = messageService.findByMessageId(message.getMessageId());
        System.out.println("메시지 조회(단건): " + foundMessage.getMessageId());
        List<Message> foundMessages = messageService.findAllMessage();
        System.out.println("메시지 조회(다건): " + foundMessages.size());
        // 수정
        Message updatedMessage = messageService.updateMessage(message.getMessageId(), "반갑습니다.");
        System.out.println("메시지 수정: " + updatedMessage.getMessageText());
        // 삭재
//        messageService.deleteMessage(message.getMessageId());
//        List<Message> foundMessagesAfterDelete = messageService.findAllMessage();
//        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
    }

    static User setupUser(UserService userService) {
        User user = userService.createUser("woody", "woody@codeit.com", "821031039281");
        System.out.println(user.toString());
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.createChannel(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println(channel.toString());
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.createMessage("안녕하세요.", channel.getChannelId(), author.getUserId());
        System.out.println("메시지 생성: " + message.getMessageId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
//        UserService userService = new FileUserService();
//        ChannelService channelService = new FileChannelService();
//        MessageService messageService = new FileMessageService(channelService, userService);

        // 메모리 저장체
          UserRepository userRepository = new JCFUserRepository();
          ChannelRepository channelRepository = new JCFChannelRepository();
//          MessageRepository messageRepository = new JCFMessageRepository();

        //직렬화 저장체
//        UserRepository userRepository = new FileUserRepository();
//        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        //서비스 초기화
          UserService userService = new BasicUserService(userRepository);
          ChannelService channelService = new BasicChannelService(channelRepository);
          MessageService messageService
                  = new BasicMessageService(messageRepository, userRepository, channelRepository);

        // CRUD테스트
//        userCRUDTest(userService);
//        channelCRUDTest(channelService);
//        messageCRUDTest(messageService);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
//        // 메시지 생성 테스트
        setupUser(userService);
        setupChannel(channelService);
        messageCreateTest(messageService, channel, user);



    }
}
