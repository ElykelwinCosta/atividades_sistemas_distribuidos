import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private List<Socket> clients;
    private List<DataOutputStream> clientOutputs;

    public ClientHandler(Socket socket, List<Socket> clients, List<DataOutputStream> clientOutputs) throws IOException {
        this.socket = socket;
        this.clients = clients;
        this.clientOutputs = clientOutputs;
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = input.readUTF();
                System.out.println("[S4] Mensagem recebida de " + socket.getRemoteSocketAddress() + ": " + msg);

                // Encaminhar a mensagem para todos os clientes conectados
                for (DataOutputStream clientOutput : clientOutputs) {
                    clientOutput.writeUTF(msg);
                    clientOutput.flush(); // Garantir que a mensagem seja enviada imediatamente
                }
            }
        } catch (IOException e) {
            // Lidar com a desconexão do cliente
            System.out.println("Cliente " + socket.getRemoteSocketAddress() + " desconectado.");
            clients.remove(socket);
            clientOutputs.remove(output);
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
