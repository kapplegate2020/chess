package ui;

import java.util.Scanner;

public class Repl {
    String serverURL;
    private Client client;
    private LoggedInClient loggedInClient;
    private LoggedOutClient loggedOutClient;
    boolean quit = false;
    String promptMessage = "[LOGGED OUT]";

    public Repl(String serverURL){
        this.serverURL = serverURL;
        loggedOutClient = new LoggedOutClient(serverURL, this);
        loggedInClient = new LoggedInClient(serverURL, this);
        client = loggedOutClient;
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

    public void login(){
        client = loggedInClient;
        promptMessage = "[LOGGED IN]";
    }

    public void logout(){
        client = loggedOutClient;
        promptMessage = "[LOGGED OUT]";
    }

    public void quit(){
        quit = true;
    }
}
