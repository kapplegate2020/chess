package server;

import Request.RegisterRequest;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.MemoryAuthDataAccess;
import dataAccess.MemoryUserDataAccess;
import dataAccess.UserDataAccess;
import service.UserService;
import spark.*;

public class Server {
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    UserService userService = new UserService(userDataAccess, authDataAccess);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);

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
        return userService.register(registerRequest);
    }
}
