import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MultiUserTCPServer {
    private ServerSocket serverSocket;
    private List<Socket> clients;
    private List<DataOutputStream> clientOutputs;

    public void start(int port) throws IOException {
        System.out.println("[S1] Criando server socket para aguardar conexões de clientes em loop");
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        clientOutputs = new ArrayList<>();

        while (serverSocket.isBound()) {
            System.out.println("[S2] Aguardando conexão em: " + serverSocket.getLocalSocketAddress());
            Socket socket = serverSocket.accept();
            System.out.println("[S3] Conexão estabelecida com cliente:" + socket.getRemoteSocketAddress());

            DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());
            clientOutputs.add(clientOutput);

            clients.add(socket);
            // Iniciar uma nova thread para lidar com o cliente
            new ClientHandler(socket, clients, clientOutputs).start();
        }
    }

    public static void main(String[] args) {
        int serverPort = 6666;
        try {
            MultiUserTCPServer server = new MultiUserTCPServer();
            server.start(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
