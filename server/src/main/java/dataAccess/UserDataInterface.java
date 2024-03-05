package dataAccess;

import model.UserData;

public interface UserDataInterface {
     Object getUser(String username, String password);

    void createUser(UserData user);

    void clear();

}
