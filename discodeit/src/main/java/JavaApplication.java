import entity.ChannelType;
import entity.Message;
import java.util.List;
import entity.User;
import service.ChannelService;
import service.MessageService;
import service.UserService;
import service.jcf.JCFChannelService;
import service.jcf.JCFMessageService;
import service.jcf.JCFUserService;
import entity.Channel;

import java.util.UUID;

public class JavaApplication {
    static void userPrint(UserService userService) {
        System.out.println("==========================");
        // 생성
        User user = userService.create("woody", "woody@codeit.com", "01029483919");
        System.out.println("유저 생성: " + user.getId());
        System.out.println("유저 이름: " + user.getUsername());
        System.out.println("유저 email: " + user.getEmail());
        System.out.println("유저 전화번호: " + user.getPhonenumber());
        // 조회
        User foundUser = userService.find(user.getId());
        System.out.println("유저 조회(단건): " + foundUser.getId());
        List<User> foundUsers = userService.findAll();
        System.out.println("유저 조회(다건): " + foundUsers.size());
        // 수정
        User updatedUser = userService.update(user.getId(), null, null, "01029384839");
        System.out.println("유저 수정: " + String.join("/", updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getPhonenumber()));
//        // 삭제
/*       String inputId = user.getId().toString();
//        UUID id = UUID.fromString(inputId);
 */
        userService.delete(user.getId());
        List<User> foundUsersAfterDelete = userService.findAll();
        System.out.println("삭제 후 유저수 : " + foundUsersAfterDelete.size());
        System.out.println("==========================");
    }

    static void channelPrint(ChannelService channelService) {
        System.out.println("==========================");
        // 생성
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        System.out.println("채널 생성: " + channel.getChannelId());
        System.out.println("채널 이름 :" + channel.getName());
//        // 조회
        Channel foundChannel = channelService.find(channel.getChannelId());
        System.out.println("채널 조회(단건): " + foundChannel.getChannelId());
        List<Channel> foundChannels = channelService.findAll();
        System.out.println("채널 조회(다건): " + foundChannels.size());
//        // 수정
        Channel updatedChannel = channelService.update(channel.getChannelId(), "공지사항", null);
        System.out.println("채널 수정: " + String.join("/", updatedChannel.getName(), updatedChannel.getDescription()));
        // 삭제
        channelService.deleteChannel(channel.getChannelId());
        List<Channel> foundChannelsAfterDelete = channelService.findAll();
        System.out.println("채널 삭제후 : " + foundChannelsAfterDelete.size());
        System.out.println("==========================");
    }
    static void messagePrint(MessageService messageService) {
        System.out.println("==========================");
        // 생성
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Message message = messageService.create("안녕하세요.", channelId, authorId);
        System.out.println("메시지 생성: " + message.getMessageId());
        System.out.println("메시지 내용: " + message.getMessagetext());
        // 조회
        Message foundMessage = messageService.find(message.getMessageId());
        System.out.println("메시지 조회(단건): " + foundMessage.getMessageId());
        List<Message> foundMessages = messageService.findAll();
        System.out.println("메시지 조회(다건): " + foundMessages.size());
        // 수정
        Message updatedMessage = messageService.update(message.getMessageId(), "반갑습니다.");
        System.out.println("메시지 수정: " + updatedMessage.getMessagetext());
        // 삭재
        messageService.deleteMessage(message.getMessageId());
        List<Message> foundMessagesAfterDelete = messageService.findAll();
        System.out.println("메시지 삭제후 : " + foundMessagesAfterDelete.size());
        System.out.println("==========================");
    }

    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

        userPrint(userService);
        channelPrint(channelService);
        messagePrint(messageService);




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