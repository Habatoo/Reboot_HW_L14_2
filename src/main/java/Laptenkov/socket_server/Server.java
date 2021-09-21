package Laptenkov.socket_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  public static void main(String[] args) {
    try (ServerSocket server = new ServerSocket(10000)) {
      Socket client = server.accept();

      System.out.print("Connection accepted.");
      DataOutputStream out = new DataOutputStream(client.getOutputStream());
      System.out.println("DataOutputStream created");
      DataInputStream in = new DataInputStream(client.getInputStream());
      System.out.println("DataInputStream created");

      while (!client.isClosed()) {
        String entry = readFromClient(in);
        if (needQuit(out, entry)) break;
        answer(out, entry);
      }

      closeResources(client, out, in);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void answer(DataOutputStream out, String entry) throws InterruptedException, IOException {
    Thread.sleep(3000);
    out.writeUTF("Server answer: " + entry);
    out.flush();
    System.out.println("Server wrote message to client.");
  }

  private static void closeResources(Socket client, DataOutputStream out, DataInputStream in) throws IOException {
    System.out.println("Client disconnected");
    System.out.println("Closing connections & channels.");
    in.close();
    out.close();
    client.close();
    System.out.println("Finished");
  }

  private static boolean needQuit(DataOutputStream out, String entry) throws IOException, InterruptedException {
    if (entry.equalsIgnoreCase("quit")) {
      System.out.println("Client kill server ...");
      out.writeUTF("Server answer: " + entry);
      out.flush();
      Thread.sleep(3000);
      return true;
    }
    return false;
  }

  private static String readFromClient(DataInputStream in) throws IOException {
    System.out.println("Server reading from channel");
    String entry = in.readUTF();
    System.out.println("message from client - " + entry);
    System.out.println("Server try writing to channel");
    return entry;
  }
}
