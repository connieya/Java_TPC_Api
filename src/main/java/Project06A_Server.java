import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Project06A_Server {
    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(9999);
            System.out.println("Server ready ...");
        } catch (Exception e) {

        }
        while (true){
            try {
                Socket socket = ss.accept(); // 클라이언트의 접속을 대기하는 것
                System.out.println("client connect success!");
                InputStream in = socket.getInputStream();
                DataInputStream dis = new DataInputStream(in);
                String message = dis.readUTF();

                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                dos.writeUTF("[ECHO] : "+message+" from Server!");
                dos.close();
                dis.close();
                socket.close();
                System.out.println("client socket close...");
            } catch (Exception e) {

            }
        }
    }
}
