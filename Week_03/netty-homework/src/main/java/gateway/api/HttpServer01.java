package gateway.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于单线程
 */
public class HttpServer01 {
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(8801);

    while (true) {
      try {
       // Listens for a connection to be made to this socket and accepts it.
        // The method blocks until a connection is made.
        Socket socket = serverSocket.accept();
        service(socket);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private static void service(Socket socket) {
    try {
      //模拟IO操作
      Thread.sleep(5);
      PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
      printWriter.println("HTTP/1.1 200 OK");
      printWriter.println("Content-Type:text/html;charset=utf-8");
      String body = "hello,nio1";
      //TODO 试试不加Content-Length会怎样
      printWriter.println("Content-Length:" + body.getBytes().length);
      printWriter.println();
      printWriter.write(body);
      printWriter.close();
      socket.close();
    } catch (IOException | InterruptedException e) { // | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
