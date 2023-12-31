import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTCPServer {
    private ServerSocket serverSocket;
    private List<Socket> clients;
    private List<DataOutputStream> clientOutputs;
    private ExecutorService threadPool;

    public void start(int port) throws IOException {
        System.out.println("[S1] Criando server socket para aguardar conexões de clientes em loop");
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        clientOutputs = new ArrayList<>();
        threadPool = Executors.newFixedThreadPool(10); // Número máximo de threads no pool

        while (serverSocket.isBound()) {
            System.out.println("[S2] Aguardando conexão em: " + serverSocket.getLocalSocketAddress());
            Socket socket = serverSocket.accept();
            System.out.println("[S3] Conexão estabelecida com cliente:" + socket.getRemoteSocketAddress());

            DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());
            clientOutputs.add(clientOutput);

            clients.add(socket);

            // Iniciar uma nova thread para lidar com o cliente
            threadPool.execute(new ClientHandler(socket, clients, clientOutputs));
        }
    }

    public static void main(String[] args) {
        int serverPort = 6666;
        try {
            ThreadPoolTCPServer server = new ThreadPoolTCPServer();
            server.start(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
