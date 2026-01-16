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

//public class JavaApplicationTest {
//    static void userCRUDTest(UserService userService) {
//        System.out.println("==========================");
//        // 생성
//        User user = userService.createUser("woody", "woody@codeit.com", "01029483919");
//        System.out.println("유저 생성: " + user.getUserId());
//        // 조회
//        User foundUser = userService.findByUserId(user.getUserId());
//        System.out.println("유저 조회(단건): " + foundUser.getUserId());
//        List<User> foundUsers = userService.findAllUser();
//        System.out.println("유저 조회(다건): " + foundUsers.size());
//        // 수정
//        User updatedUser = userService.updateUser(user.getUserId(), null, null, "01029384839");
//        System.out.println("유저 수정: " + String.join("/", updatedUser.getUserName(), updatedUser.getEmail(), updatedUser.getPhoneNumber()));
////        // 삭제
///*       String inputId = user.getId().toString();
////        UUID id = UUID.fromString(inputId);
// */
//        userService.deleteUser(user.getUserId());
//        List<User> foundUsersAfterDelete = userService.findAllUser();
//        System.out.println("유저 삭제: " + foundUsersAfterDelete.size());
//        System.out.println("==========================");
//    }
//
//    static void channelCRUDTest(ChannelService channelService) {
//        System.out.println("==========================");
//        // 생성
//        Channel channel = channelService.createChannel(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
//        System.out.println("채널 생성: " + channel.getChannelId());
////        // 조회
//        Channel foundChannel = channelService.findByChannelId(channel.getChannelId());
//        System.out.println("채널 조회(단건): " + foundChannel.getChannelId());
//        List<Channel> foundChannels = channelService.findAllChannel();
//        System.out.println("채널 조회(다건): " + foundChannels.size());
////        // 수정
//        Channel updatedChannel = channelService.updateChannel(channel.getChannelId(), "공지사항", null);
//        System.out.println("채널 수정: " + String.join("/", updatedChannel.getChannelName(), updatedChannel.getDescription()));
//        // 삭제
//        channelService.deleteChannel(channel.getChannelId());
//        List<Channel> foundChannelsAfterDelete = channelService.findAllChannel();
//        System.out.println("채널 삭제: " + foundChannelsAfterDelete.size());
//        System.out.println("==========================");
//    }
//
//    static void messageCRUDTest(MessageService messageService) {
//        System.out.println("==========================");
//        // 생성
//        UUID channelId = UUID.randomUUID();
//        UUID authorId = UUID.randomUUID();
//        Message message = messageService.createMessage("안녕하세요.", channelId, authorId);
//        System.out.println("메시지 생성: " + message.getMessageId());
//        // 조회
//        Message foundMessage = messageService.findByMessageId(message.getMessageId());
//        System.out.println("메시지 조회(단건): " + foundMessage.getMessageId());
//        List<Message> foundMessages = messageService.findAllMessage();
//        System.out.println("메시지 조회(다건): " + foundMessages.size());
//        // 수정
//        Message updatedMessage = messageService.updateMessage(message.getMessageId(), "반갑습니다.");
//        System.out.println("메시지 수정: " + updatedMessage.getMessageText());
//        // 삭재
//        messageService.deleteMessage(message.getMessageId());
//        List<Message> foundMessagesAfterDelete = messageService.findAllMessage();
//        System.out.println("메시지 삭제: " + foundMessagesAfterDelete.size());
//        System.out.println("==========================");
//    }
//
//    public static void main(String[] args) {
//        // 서비스 초기화
//        UserService userService = new JCFUserService();
//        ChannelService channelService = new JCFChannelService();
//        MessageService messageService = new JCFMessageService();
//
//        // 테스트
//        userCRUDTest(userService);
//        channelCRUDTest(channelService);
//        messageCRUDTest(messageService);
//    }
//}
//
