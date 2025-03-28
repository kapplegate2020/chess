# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Phase 2 URL: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIcDcujheT576V5LD99A7FiChT5iWw63O+46tl8LxvMskGLpgIFAUUaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatJvqMJpEuGFoctyvIGoKwowKK4qutKSaXqh5QsdojrOrxTryjAVDAMgWiZFU36wbCMFLLMRraIRcrEQSpEsh6uqZLG-rTiGXHmpGHLRjA+nxupnZIeUOE8tmuaIecBY8ShJRXK+I6jH+NZBrOzbwW2gGdtkPYwP2g69F54E+UFXzTgF87xUunCrt4fiBF4KDoHuB6+Mwx7pJkmBhRe7nXtIACiu5VfUVXNC0D6qE+3SJY26Dtshtmln07Vzi2-XoACLkdrKfEwBh9gFdh+W+nhGJqahGpaaSMDkmA+kBv5HVoLRTJugxpQbTAhnaEKYRDWgoZ0YdblEaUZ3yEJiaabd2lGCg3B6UG21xqpN0HdxhSWp932GE9wAXadO1zi9xHJqNaY4QVjkIHmSOdoBVwAVeIKnGVYB9gOQ6pSungZRukK2ru0IwAA4qOrJFaepXnsw40eZU9N1Y19ijm1sOdSFiNAqBfVC8l5RXSNYvIQ9k3QozoyqNhSujgtBHwyt71rRtW1XftZoRiDHIwAAZjytqQ9DV2A8bWPqdLQZWctJG6+660UsrKC07EsJG-RpnlJbvL86MDrGRGnPguto6MhUjTa25PV+2APtoxjcuO9Q4vh2oLYVC4xeNLjuf44UhPE1F-T56ohfFy4pdmGlFProE2A+FA2DcPAumGD7KTFWeOQcyJ161A0fMC8EksvvnAByEETv+XVsj1-RXYNkuzPndwpc52cx2Jnp6j7sJwP3GcoGii3J29QN6xSBuS4Hd2myHVsw-98i25L9tB2Pmhb+wZBJER1o-T2p9Mjn3zkZVaJt2TSxtHHCOgko451EsA-OABJaQydRapnKJfL0MCNZqCckhTBoFcHSEggEMuxw3JVwiiTXotD6Fk3Su3AIlgvoYWSDAAAUhAHkDNRyBB0AgUADY2ajzZNjSoVRKR3haPnQWdZdpDh7sAPhUA4AQAwrAFeu9Rx4M4WvQhwJPJb0gldWYOi9EGKMfFUxoxzHlAYYfVMCinYwAAFaiLQOfERPJr63y1uA92kDyKbV+obAB78kEWy-jbNidsMH3T8ZDe+MgEGxNgWY6Qb9gbJJOnA86bFaGJO4kAsk8cwHWTqTAEAqQUAgAbAoCS2xdDcCqNIvRCkBmUGcdANxKAQxRIfsbUogSwmjlhBU+QJSTIf3YtgaShgllQyqUUghwEkblFCcE8hOZ0beNcpzUCjCxoE3ZtXUmLdyZrkygELwuiuxelgMAbAPdCDxESEPVmhNfHl3KBUaqtV6qNWMJYg5cscYXLGiJWOIBuB4ADrkqO5Q0VfMxZktZHAvoUi2Q056BLklEvBuxe06CEFHTBiSkBrssE2UOZ8vAmckUVwqmmHoNyeUsMio85cmAgA
