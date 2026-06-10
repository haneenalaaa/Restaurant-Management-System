package advancedproj;

import java.io.*;
import java.net.*;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ChatService {
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private TextArea chatArea;
    private volatile boolean running = true; 

    public ChatService(TextArea chatArea) {
        this.chatArea = chatArea;
    }

    public void startServer(int port) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                Platform.runLater(() -> chatArea.appendText("System: Server started. Waiting...\n"));
                socket = serverSocket.accept(); 
                setupStreams();
            } catch (IOException e) {
                if(running) Platform.runLater(() -> chatArea.appendText("Error: " + e.getMessage() + "\n"));
            }
        }).start();
    }

   public void connectToServer(String ip, int port) {
    new Thread(() -> {
        int attempts = 0;
        while (attempts < 3) { 
            try {
                socket = new Socket(ip, port);
                setupStreams();
                return; 
            } catch (IOException e) {
                attempts++;
                try { Thread.sleep(1000); } catch (InterruptedException ie) {} 
            }
        }
        Platform.runLater(() -> chatArea.appendText("System: Could not connect to Admin. Make sure Admin Chat is open first.\n"));
    }).start();
}

    private void setupStreams() throws IOException {
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Platform.runLater(() -> chatArea.appendText("System: Connected!\n"));

        while (running) {
            try {
                if (in.ready()) { 
                    String message = in.readLine();
                    if (message == null) break;
                    Platform.runLater(() -> chatArea.appendText(message + "\n"));
                }
                Thread.sleep(100); 
            } catch (Exception e) {
                break;
            }
        }
    }

    public void sendMessage(String user, String msg) {
        if (out != null && !msg.trim().isEmpty()) {
            out.println(user + ": " + msg);
            chatArea.appendText("You: " + msg + "\n");
        }
    }

    public void stop() {
        running = false;
        try {
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            // ignore
        }
    }
}