package client;

import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Objects;

public class ServerFacade {
    String url;
    public ServerFacade(String url){
        this.url = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException{
        return makeRequest("POST", "/user", registerRequest, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException{
        return makeRequest("POST", "/session", loginRequest, LoginResult.class, null);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws ResponseException{
        String authToken = logoutRequest.authToken();
        return makeRequest("DELETE", "/session", logoutRequest, LogoutResult.class, authToken);
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException{
        String authToken = listGamesRequest.authToken();
        return makeRequest("GET", "/game", listGamesRequest, ListGamesResult.class, authToken);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException{
        String authToken = createGameRequest.authToken();
        return makeRequest("POST", "/game", createGameRequest, CreateGameResult.class, authToken);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ResponseException{
        String authToken = joinGameRequest.authToken();
        return makeRequest("PUT", "/game", joinGameRequest, JoinGameResult.class, authToken);
    }

    private  <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URI uri = new URI(url + path);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.addRequestProperty("Content-Type", "application/json");
            if(authToken!= null){
                http.addRequestProperty("authorization", authToken);
            }
            if(!Objects.equals(method, "GET")){
                http.setDoOutput(true);
                String reqData = new Gson().toJson(request);
                try (OutputStream reqBody = http.getOutputStream()){
                    reqBody.write(reqData.getBytes());
                }
            }
            http.connect();
            int statusCode = http.getResponseCode();
            if(statusCode/100 == 2){
                T result = null;
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    result = new Gson().fromJson(inputStreamReader, responseClass);
                }
                return result;
            }
            else{
                try (InputStream respError = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respError);
                    ErrorHolder error = new Gson().fromJson(inputStreamReader, ErrorHolder.class);
                    throw new ResponseException(error.message());
                }
            }
        }
        catch (ResponseException e){
            throw e;
        } catch (Exception e) {
            throw new ResponseException("Unknown Error.");
        }
    }

}
