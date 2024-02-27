package dataAccess;

import model.AuthData;

public class authDOA {
    public AuthData getAuth(String authToken, memoryDB data) throws DataAccessException {
        for (AuthData auth: data.authList){
            if (authToken == auth.authToken()){
                return auth;
            } else{
                return null;
            }
        }

        throw new DataAccessException("Error: Description");
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
