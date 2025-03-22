import com.google.gson.Gson;
import request.RegisterRequest;
import result.RegisterResult;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

public class ServerFacade {
    URI uri;
    public ServerFacade(URI uri){
        this.uri = uri;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws Exception{
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        String reqData = new Gson().toJson(registerRequest);
        try (OutputStream reqBody = http.getOutputStream()){
            reqBody.write(reqData.getBytes());
        }
        http.connect();
        int statusCode = http.getResponseCode();
        RegisterResult registerResult;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            registerResult = new Gson().fromJson(inputStreamReader, RegisterResult.class);
        }
        return registerResult;
    }

}
