import request.RegisterRequest;
import result.RegisterResult;

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

    }

}
