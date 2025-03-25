package client;

import dataaccess.DataAccessException;
import dataaccess.DbAuthDataAccess;
import dataaccess.DbGameDataAccess;
import dataaccess.DbUserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.ListGamesRequest;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.ListGamesResult;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;
import server.Server;
import service.UserService;


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

    @Test
    public void loginSuccess(){
        try{
            //password is encrypted version of test1234
            UserData userData = new UserData("Johnny", "$2a$10$wecv.H9ZkuxxPd0iieNz/ufX4Dq8tx73LvpVlSlRF.B.YD4IfUMV6", "fake@email.com");
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        LoginRequest loginRequest = new LoginRequest("Johnny", "test1234");
        try {
            LoginResult loginResult = serverFacade.login(loginRequest);
            assert loginResult.username().equals("Johnny");
            assert loginResult.authToken() != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loginFailure(){
        try{
            //password is encrypted version of test1234
            UserData userData = new UserData("Johnny", "$2a$10$wecv.H9ZkuxxPd0iieNz/ufX4Dq8tx73LvpVlSlRF.B.YD4IfUMV6", "fake@email.com");
            userDataAccess.createUser(userData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        LoginRequest loginRequest = new LoginRequest("Johnny", "wrongPassword");
        try {
            LoginResult loginResult = serverFacade.login(loginRequest);
        } catch (ResponseException e) {
            assert e.getMessage().equals("Error: unauthorized");
        }
    }

    @Test
    public void logoutSuccess(){
        String authToken = "exampleAuthToken1234";
        try{
            AuthData authData = new AuthData(authToken, "Joey");
            authDataAccess.createAuth(authData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            LogoutResult logoutResult = serverFacade.logout(logoutRequest);
        }
        catch(ResponseException e){
            throw new RuntimeException(e);
        }

        try{
            AuthData authData = authDataAccess.getAuth(authToken);
            assert authData == null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void logoutFailure(){
        String authToken = UserService.generateToken();
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            LogoutResult logoutResult = serverFacade.logout(logoutRequest);
        } catch (ResponseException e) {
            assert e.getMessage().equals("Error: unauthorized");
        }
    }

    @Test
    public void listGamesSuccess(){
        String authToken = "exampleAuthToken1234";
        try{
            AuthData authData = new AuthData(authToken, "Sammy");
            authDataAccess.createAuth(authData);
            GameData gameData1 = new GameData(4, "user1", null, "game", null);
            GameData gameData2 = new GameData(6, null, "Gerald", "alsoagame", null);
            gameDataAccess.createGame(gameData1);
            gameDataAccess.createGame(gameData2);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        try {
            ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);
            assert listGamesResult.games().getFirst().gameID() == 4;
            assert listGamesResult.games().get(1).gameID() == 6;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void listGamesFailure(){
        String authToken = "exampleAuthToken1234";
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        try {
            ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);
        } catch (ResponseException e) {
            assert e.getMessage().equals("Error: unauthorized");
        }
    }

}
