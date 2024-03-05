package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryAuthDOA {

    private final ArrayList<AuthData> authList;

    public MemoryAuthDOA(){
        this.authList = new ArrayList<>();
    }
    public AuthData getAuth(String authToken){
        for (AuthData token: this.authList){
            if (Objects.equals(authToken, token.authToken())){
                return token;
            }
        }
        return null;
    }

    public void createAuth(AuthData auth){
        this.authList.add(auth);
    }

    public void deleteAuth(AuthData token){
        this.authList.remove(token);
    }

    public void clear(){
        this.authList.clear();
    }
}
