package ui;

import java.util.Scanner;

public class Repl {
    String serverURL;
    private Client client;
    boolean quit = false;
    String promptMessage = "[LOGGED OUT]";

    public Repl(String serverURL){
        this.serverURL = serverURL;
        client = new LoggedOutClient(serverURL, this);
    }

    public void run(){
        System.out.println("Welcome to Chess! Login to start.");
        System.out.println(client.help());
        Scanner scanner = new Scanner(System.in);
        while(!quit){
            System.out.print(promptMessage+" >>> ");
            String line = scanner.nextLine();
            try{
                String result = client.eval(line);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void login(String authToken){
        client = new LoggedInClient(serverURL, this, authToken);
        promptMessage = "[LOGGED IN]";
    }

    public void logout(){
        client = new LoggedOutClient(serverURL, this);
        promptMessage = "[LOGGED OUT]";
    }

    public void quit(){
        quit = true;
    }
}
