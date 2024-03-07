package dataAccess;

import model.AuthData;

import java.sql.SQLException;

import java.util.Properties;

import static dataAccess.DatabaseManager.getConnection;

public class SqlAuthDOA implements AuthDataInterface{

    public SqlAuthDOA(){
        configure();
    }

    public AuthData getAuth(String authToken){
        try (var con = DatabaseManager.getConnection()){

            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = con.prepareStatement(statement)){
                ps.setString(1, authToken);

                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createAuth(AuthData auth){
        try (var con = DatabaseManager.getConnection()){

            try (var ps = con.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)")){
                ps.setString(1, auth.authToken());
                ps.setString(2, auth.username());

                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAuth(AuthData auth){
        try (var con = DatabaseManager.getConnection()){
            var statement = "DELETE FROM auth WHERE authToken=?";

            try (var ps = con.prepareStatement(statement)){
                ps.setString(1, auth.authToken());
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear(){
        try (var con = getConnection()) {
            var statement = "TRUNCATE auth";
            var clearTableStatement = con.prepareStatement(statement);
            clearTableStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void configure(){
        try {
            DatabaseManager.createDatabase();
            try (var con = DatabaseManager.getConnection()) {

                var statement = """
                             CREATE TABLE  IF NOT EXISTS auth (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL
                             )""";

                try (var createTableStatement = con.prepareStatement(statement)) {
                    createTableStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
