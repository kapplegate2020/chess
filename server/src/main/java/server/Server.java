package server;

import request.*;
import com.google.gson.Gson;
import dataaccess.*;
import result.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final UserDataAccess userDataAccess = new DbUserDataAccess();
    private final GameDataAccess gameDataAccess = new DbGameDataAccess();
    private final AuthDataAccess authDataAccess = new DbAuthDataAccess();
    private final UserService userService = new UserService(userDataAccess, authDataAccess);
    private final GameService gameService = new GameService(gameDataAccess, authDataAccess);
    private final ClearService clearService = new ClearService(userDataAccess, gameDataAccess, authDataAccess);
    private final WSServer wsServer = new WSServer(userDataAccess, gameDataAccess, authDataAccess);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", wsServer);

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res){
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerResult = userService.register(registerRequest);
        res.status(registerResult.statusNumber());
        registerResult = registerResult.removeStatusNumber();
        return new Gson().toJson(registerResult);
    }

    private Object clear(Request req, Response res){
        ClearResult clearResult = clearService.clear();
        System.out.println(clearResult.statusNumber());
        res.status(clearResult.statusNumber());
        clearResult = clearResult.removeStatusNumber();
        return new Gson().toJson(clearResult);
    }

    private Object login(Request req, Response res){
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        res.status(loginResult.statusNumber());
        loginResult = loginResult.removeStatusNumber();
        return new Gson().toJson(loginResult);
    }

    private Object logout(Request req, Response res){
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
        LogoutResult logoutResult = userService.logout(logoutRequest);
        res.status(logoutResult.statusNumber());
        logoutResult = logoutResult.removeStatusNumber();
        return new Gson().toJson(logoutResult);
    }

    private Object listGames(Request req, Response res){
        ListGamesRequest listGamesRequest = new ListGamesRequest(req.headers("authorization"));
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
        res.status(listGamesResult.statusNumber());
        listGamesResult = listGamesResult.removeStatusNumber();
        return new Gson().toJson(listGamesResult);
    }

    private Object createGame(Request req, Response res){
        CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
        createGameRequest = createGameRequest.addAuthToken(req.headers("authorization"));
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        res.status(createGameResult.statusNumber());
        createGameResult = createGameResult.removeStatusNumber();
        return new Gson().toJson(createGameResult);
    }

    private Object joinGame(Request req, Response res){
        JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
        joinGameRequest = joinGameRequest.addAuthToken(req.headers("authorization"));
        JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
        res.status(joinGameResult.statusNumber());
        joinGameResult = joinGameResult.removeStatusNumber();
        return new Gson().toJson(joinGameResult);
    }
}
