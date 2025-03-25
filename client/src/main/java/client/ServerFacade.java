package client;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

public class ServerFacade {
    String url;
    public ServerFacade(String url){
        this.url = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException{
        RegisterResult result = new RegisterResult(null, null, null, null);
        return makeRequest("POST", "/user", registerRequest, RegisterResult.class);
//        HttpURLConnection http = (HttpURLConnection) new URI(url+"/user").toURL().openConnection();
//        http.setRequestMethod("POST");
//        http.setDoOutput(true);
//        http.addRequestProperty("Content-Type", "application/json");
//        String reqData = new Gson().toJson(registerRequest);
//        try (OutputStream reqBody = http.getOutputStream()){
//            reqBody.write(reqData.getBytes());
//        }
//        http.connect();
//        int statusCode = http.getResponseCode();
//        RegisterResult registerResult;
//        try (InputStream respBody = http.getInputStream()) {
//            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
//            registerResult = new Gson().fromJson(inputStreamReader, RegisterResult.class);
//        }
//        return registerResult;
    }

    private  <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URI uri = new URI(url + path);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
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
            throw new ResponseException("Unknown Error");
        }
    }

}
