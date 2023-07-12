import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("서버 시작됨. 클라이언트 연결 대기 중...");

            clientSocket = serverSocket.accept();
            System.out.println("클라이언트 연결됨.");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 클라이언트로부터 메시지를 읽는 스레드 시작
            Thread readThread = new Thread(new ClientMessageReader());
            readThread.start();

            // 서버에서 입력받은 메시지를 클라이언트로 보내는 부분
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
            String message;
            while ((message = serverInput.readLine()) != null) {
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientMessageReader implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("클라이언트로부터 메시지: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(12345); // 포트 번호를 적절하게 설정해주세요.
    }
}