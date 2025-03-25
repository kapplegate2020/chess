package ui;

import client.ResponseException;
import client.ServerFacade;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

import java.util.Arrays;

public class LoggedOutClient implements Client{
    Repl repl;
    ServerFacade serverFacade;

    public LoggedOutClient(String serverURL, Repl repl){
        serverFacade = new ServerFacade(serverURL);
        this.repl = repl;
    }

    @Override
    public String eval(String input) throws ResponseException{
        String[] tokens = input.toLowerCase().split(" ");
        if(tokens.length==0){
            return help();
        }
        String command = tokens[0];
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        switch (command) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> quit(params);
            default -> help();
        }
    }

    @Override
    public String help(){
        return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - to close the application
                """;
    }

    private String register(String[] params) throws ResponseException {
        if(params.length<=1 || params.length>=4){
            return "Invalid Command.";
        }
        String username = params[0];
        String password = params[1];
        String email = null;
        if(params.length == 3){
            email = params[2];
        }
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult registerResult = serverFacade.register(registerRequest);
        repl.login(registerResult.authToken());
        return "Successfully registered as "+username;
    }

    private String login(String[] params) throws ResponseException {
        if(params.length != 2){
            return "Invalid Command.";
        }
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverFacade.login(loginRequest);
        repl.login(loginResult.authToken());
        return "Successfully registered as "+username;
    }

    private String quit(String[] params){
        if(params.length>0){
            return "Invalid Command.";
        }
        repl.quit();
        return "";
    }

}
