import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.*;

public class BatalhaNavalController {

    @FXML
    private TextField endereco;

    @FXML
    private TextField posicao;

    @FXML
    private Button conectar;

    @FXML
    private Button aguardarJogador;

    @FXML
    private Button jogar;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public void conectar() {
        try {
            socket = new Socket(endereco.getText(), 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aguardarJogador() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                socket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                listenForMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void jogar() {
        String move = posicao.getText();
        out.println(move);
        posicao.clear();
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
              
                System.out.println("Received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
