import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MultiUserTCPClient {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public void start(String serverIp, int serverPort) throws IOException {
        System.out.println("[C1] Conectando com servidor " + serverIp + ":" + serverPort);
        socket = new Socket(serverIp, serverPort);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        System.out.println("[C2] Conexão estabelecida, eu sou o cliente: " + socket.getLocalSocketAddress());

        // Thread para receber mensagens do servidor
        new ReceiveThread().start();

        // Loop para enviar mensagens
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Digite uma mensagem: ");
            String msg = scanner.nextLine();
            output.writeUTF(msg);
        }
    }

    public static void main(String[] args) {
        String serverIp = "127.0.0.1"; // Coloque o endereço IP do servidor aqui
        int serverPort = 6666;
        try {
            MultiUserTCPClient client = new MultiUserTCPClient();
            client.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    String response = input.readUTF();
                    System.out.println("[C5] Mensagem recebida: " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
