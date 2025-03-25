package client;

import dataaccess.DataAccessException;
import dataaccess.DbAuthDataAccess;
import dataaccess.DbGameDataAccess;
import dataaccess.DbUserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import result.RegisterResult;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static final DbUserDataAccess userDataAccess = new DbUserDataAccess();
    private static final DbGameDataAccess gameDataAccess = new DbGameDataAccess();
    private static final DbAuthDataAccess authDataAccess = new DbAuthDataAccess();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:"+port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    void resetDb(){
        try {
            userDataAccess.clear();
            gameDataAccess.clear();
            authDataAccess.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("Jimmy", "test1234", "me@fake.com");
        try{
            RegisterResult registerResult = serverFacade.register(registerRequest);
            assert registerResult.username().equals("Jimmy");
            assert registerResult.authToken() != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerFailure() {
        UserData userData = new UserData("John", "pass", "email");
        try{
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        RegisterRequest registerRequest = new RegisterRequest("John", "test1234", "me@fake.com");
        try{
            RegisterResult registerResult = serverFacade.register(registerRequest);
        } catch (ResponseException e) {
            assert e.getMessage().equals("Error: already taken");
        }
    }

}
