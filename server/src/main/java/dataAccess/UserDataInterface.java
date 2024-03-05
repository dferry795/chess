package dataAccess;

import model.UserData;

public interface UserDataInterface {
     UserData getUser(String username, String password);

    void createUser(UserData user);

    void clear();

}
