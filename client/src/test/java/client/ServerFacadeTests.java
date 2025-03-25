package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import result.RegisterResult;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

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

}
