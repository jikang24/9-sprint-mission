//import entity.User;
//import service.UserService;
//import service.jcf.JCFUserService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class JavaApplication {
//    public static void main(String[] args) {
//        UserService userService = new JCFUserService();
//
//
//        userService.addUser(new User("강","ddd@gmail.com","01029398184","K"));
//        userService.addUser(new User("김","qqq@gmail.com","01030818392","S"));
////        users.add(new User("이","www@gmail.com","01002818921","L"));
////        users.add(new User("박","eee@gmail.com","01004928191","P"));
////        users.add(new User("육","rrr@gmail.com","01084729192","Y"));
//
//
//
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
//
//}
//
//




//        boolean addFlag = userService.addUser(user);
//        if(addFlag){
//            System.out.println("add success");
//        }else {
//            System.out.println("add failed");
//        }
//        System.out.println(userService.getAllUsers());
