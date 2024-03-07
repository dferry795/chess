package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class MemoryAuthDOA  implements AuthDataInterface{

    private final HashSet<AuthData> authList  = new HashSet<>();

    public AuthData getAuth(String authToken){
        for (AuthData token: this.authList){
            if (Objects.equals(authToken, token.authToken())){
                return token;
            }
        }
        return null;
    }

    public void createAuth(AuthData auth){
        for (AuthData token: this.authList){
            if (auth.authToken() == token.authToken()){
                return;
            }
        }
        this.authList.add(auth);
    }

    public void deleteAuth(AuthData token){
        this.authList.remove(token);
    }

    public void clear(){
        this.authList.clear();
    }
}
