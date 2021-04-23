import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Project06F_MultiChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            Scanner scanner = new Scanner(System.in);
            System.out.println("name");
            String name = scanner.nextLine();
            Thread sender = new Thread(new ClientSender(socket,name));
            Thread receiver = new Thread(new ClientReceiver(socket));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // inner class
    static class ClientSender extends Thread{
        Socket socket;
        DataOutputStream out;
        String name;
        ClientSender(Socket socket, String name){
            this.socket = socket;
            this.name = name;
            try {
                out = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                if (out != null)
                    out.writeUTF(name);
                while (out !=null){
                    String message = scanner.nextLine();
                    if (message.equals("quit"))
                        break;
                    out.writeUTF("["+name+"]"+message);
                }
                out.close();
                socket.close();

            } catch (Exception e) {

            }
        }
    }
    static class ClientReceiver extends Thread{
        Socket socket; DataInputStream in;
        ClientReceiver(Socket socket){
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (in != null){
                try {
                    System.out.println(in.readUTF());
                } catch (Exception e) {
                        e.printStackTrace();
                        break;
                }
            }
            try {
                    in.close();
                    socket.close();
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }
}
