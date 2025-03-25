package client;

import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;
import result.Result;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {
    String url;
    public ServerFacade(String url){
        this.url = url;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws Exception{
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

    private Result makeRequest(String method, String path, Object request, Class<Result> responseClass) {
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
            Result result;
            if(statusCode/100 == 2){
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    result = new Gson().fromJson(inputStreamReader, responseClass);
                }
            }
            else{
                try (InputStream respError = http.getErrorStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respError);
                    result = new Gson().fromJson(inputStreamReader, responseClass);
                }
            }
            result.addStatusNumber(statusCode);
            return result;
        }
        catch (Exception e){
            return null;
        }
    }

}
