package dataAccess;

import model.AuthData;

import java.util.Objects;

public class authDOA {
    public AuthData getAuth(String authToken, memoryDB data){
        for (AuthData token: data.authList){
            if (Objects.equals(authToken, token.authToken())){
                return token;
            }
        }
        return null;
    }

    public void createAuth(AuthData auth, memoryDB data){
        data.authList.add(auth);
    }

    public void deleteAuth(AuthData token, memoryDB data){
        data.authList.remove(token);
    }

    public void clear(memoryDB data){
        data.authList.clear();
    }
}
