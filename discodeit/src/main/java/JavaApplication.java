import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public class JavaApplication {
    static void userPrint(UserService userService) {
        System.out.println("==========================");
        // 생성
        User user1 = userService.createUser("woody", "woody@codeit.com", "01029483919");
        User user2 = userService.createUser("강지원","jiwon@gmail.com","01073829184");
        System.out.println("유저 생성ID : " + user1.getUserId().toString());
        System.out.println("유저 이름: " + user1.getUserName());
        System.out.println("유저 email: " + user1.getEmail());
        System.out.println("유저 전화번호: " + user1.getPassword());
        System.out.println("유저 생성시간: " + user1.getCreatedAtLocalDateTime());
        // 조회
        User foundUser = userService.findByUserId(user1.getUserId());
        System.out.println("유저 조회(단건): " + foundUser.getUserId());
        List<User> foundUsers = userService.findAllUser();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        User updatedUser = userService.updateUser(user2.getUserId(), null, null, "01029384839");
        System.out.println("유저 수정: " + String.join("/", updatedUser.getUserName(), updatedUser.getEmail(), updatedUser.getPassword()));
        System.out.println("유저 수정시간: " + updatedUser.getUpdatedAtLocalDateTime());
        // 삭제
//        boolean removedUser = userService.deleteUser(user1.getUserId());
//        System.out.println("유저 삭제여부 : " + removedUser);
//        System.out.println(String.format("유저 삭제여부 %b, %s, 12312312312", removed, "123"));
        List<User> foundUsersAfterDelete = userService.findAllUser();
        System.out.println("삭제 후 유저수 : " + foundUsersAfterDelete.size());
        System.out.println("==========================");
    }

    static void channelPrint(ChannelService channelService) {
        System.out.println("==========================");
        // 생성
        Channel channel = channelService.createChannel(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성ID : " + channel.getChannelId().toString());
        System.out.println("채널 이름 :" + channel.getChannelName());
        System.out.println("채널 생성 시간: " + channel.getCreatedAtLocalDateTime());
        // 조회
        Channel foundChannel = channelService.findByChannelId(channel.getChannelId());
        System.out.println("채널 조회(단건): " + foundChannel.getChannelId());
        List<Channel> foundChannels = channelService.findAllChannel();
        System.out.println("채널 조회(다건): " + foundChannels.size());
        // 수정
        Channel updatedChannel = channelService.updateChannel(channel.getChannelId(), "공지사항", null,null);
        System.out.println("채널 수정: " + String.join("/", updatedChannel.getChannelName(), updatedChannel.getDescription()));
        System.out.println("채널 수정시간: " + updatedChannel.getUpdatedAtLocalDateTime());
        // 삭제
//        boolean removedChannel = channelService.deleteChannel(channel.getChannelId());
//        System.out.println("채널 삭제여부: " + removedChannel);
        List<Channel> foundChannelsAfterDelete = channelService.findAllChannel();
        System.out.println("채널 삭제후 : " + foundChannelsAfterDelete.size());
        System.out.println("==========================");
    }
    static void messagePrint(MessageService messageService) {
        System.out.println("==========================");
        // 생성
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        //하드코딩을 위한 아이디 부여
        Message message = messageService.createMessage("안녕하세요.", channelId, authorId);
        System.out.println("메시지 생성ID : " + message.getMessageId().toString());
        System.out.println("메시지 내용: " + message.getMessageText());
        System.out.println("메시지 생성 시간: " + message.getCreatedAtLocalDateTime());
        // 조회
        Message foundMessage = messageService.findByMessageId(message.getMessageId());
        System.out.println("메시지 조회(단건): " + foundMessage.getMessageId());
        List<Message> foundMessages = messageService.findAllMessage();
        System.out.println("메시지 조회(다건): " + foundMessages.size());
        // 수정
        Message updatedMessage = messageService.updateMessage(message.getMessageId(), "반갑습니다.");
        System.out.println("메시지 수정: " + updatedMessage.getMessageText());
        System.out.println("메시지 수정시간: " + updatedMessage.getUpdatedAtLocalDateTime());
        // 삭재
//        boolean removedMessage = messageService.deleteMessage(message.getMessageId());
//        System.out.println("메시지 삭제여뷰: " + removedMessage);
        List<Message> foundMessagesAfterDelete = messageService.findAllMessage();
        System.out.println("메시지 삭제후 : " + foundMessagesAfterDelete.size());
        System.out.println("==========================");
    }

    static void sendMessage(UserService userService, ChannelService channelService, MessageService messageService) {
        Channel channel = channelService.createChannel(
                ChannelType.PUBLIC,
                "자유방",
                "맘껏 떠드세요"
        );

        UserDTO.CreateUserDTO createUserDTO = new UserDTO.CreateUserDTO("123", "456", "789");
        userService.createUser(createUserDTO);

        User user1 = userService.createUser(
                "용땡땡",
                "abc@email.com",
                "01012345678"
        );

        User user2 = userService.createUser(
                "강땡땡",
                "def@email.com",
                "01092739271"
        );

        Message message1 = messageService.createMessage("ㅎㅇ", channel.getChannelId(), user1.getUserId());
        System.out.println("유저1 \"" + user1.getUserName() + "\"이(가) 메세지를 보냈습니다 :" + message1.getMessageText());

        Message message2 = messageService.createMessage("안냐세여", channel.getChannelId(), user2.getUserId() );
        System.out.println("유저2 \"" + user2.getUserName() +"\"이(가) 메세지를 보냈습니다 :" + message2.getMessageText());
    }

        public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        userPrint(userService);
        channelPrint(channelService);
        messagePrint(messageService);

        sendMessage(userService, channelService, messageService);
    }

}








/*
// 이전 사용 코드
//
// UserService userService = new JCFUserService();
//        userService.addUser(new User("강","ddd@gmail.com","01029398184","K"));
//        userService.addUser(new User("김","qqq@gmail.com","01030818392","S"));
//        users.add(new User("이","www@gmail.com","01002818921","L"));
//        users.add(new User("박","eee@gmail.com","01004928191","P"));
//        users.add(new User("육","rrr@gmail.com","01084729192","Y"));
//        // 유저를 toString으로 정리
//        String Users = userService.getAllUsers().toString();
//        // 유저 전체 호출
//        System.out.println(Users);
//        //유저 단일 호출
//        User user = userService.getUser("강");
//
//        // 유저 정보 수정
//
//        // 유저 정보 삭제
//        System.out.println(user.toString());
//    }
//        boolean addFlag = userService.addUser(user);
//        if(addFlag){
//            System.out.println("add success");
//        }else {
//            System.out.println("add failed");
//        }
//        System.out.println(userService.getAllUsers());
 */