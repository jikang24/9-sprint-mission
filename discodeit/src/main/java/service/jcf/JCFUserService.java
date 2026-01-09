package service.jcf;

import entity.User;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFUserService implements UserService {

    private final List<User> data;

    public JCFUserService(){
        this.data = new ArrayList<>();
    }

    @Override
    public User addUser(String displayName, String email, String phoneNumber)
    {
        return null;
    }

    @Override
    public User getUser(String displayName) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public User updateUser(String displayName, String email, String phoneNumber) {
        return null;
    }

    @Override
    public boolean deleteUser(String displayName) {
        return false;
    }
}
