package ui;

import client.ResponseException;

public interface Client {
    public String eval(String input) throws ResponseException;
    public String help();
}
