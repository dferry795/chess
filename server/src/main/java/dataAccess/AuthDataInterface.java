package dataAccess;

import model.AuthData;

public interface AuthDataInterface {

    AuthData getAuth(String authToken);
    void createAuth(AuthData auth);
    void deleteAuth(AuthData auth);
    void clear();
}
