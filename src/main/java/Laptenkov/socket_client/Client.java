package Laptenkov.socket_client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Client {

  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", 10000);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        DataInputStream ois = new DataInputStream(socket.getInputStream());) {

      System.out.println("Client connected to socket");
      System.out.println("Client writing channel initialized");
      System.out.println("Client reading channel initialized");
      System.out.println();
      System.out.print("Enter message: ");

      while (!socket.isOutputShutdown()) {
        if (br.ready()) {
          String command = processInput(br, oos);
          if (needQuit(command, ois)) break;
          readData(ois);
        }
      }

      System.out.println("Close connections and channels on client side");

    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static String processInput(BufferedReader br, DataOutputStream oos)
      throws IOException {
    String command = br.readLine();
    System.out.println("User input:" + command);
    oos.writeUTF(command);
    oos.flush();
    System.out.println("Client sent message: " + command + " to server");

    return command;
  }

  private static boolean needQuit(String command, DataInputStream ois)
      throws IOException, InterruptedException {
    if (command.equalsIgnoreCase("quit")) {

      System.out.println("Client command: quit");
      System.out.println("Client kill connections");

      readDataFromSocket(ois);

      return true;
    }
    return false;
  }

  private static void readData(DataInputStream ois) throws InterruptedException, IOException {
    System.out.println("Client start waiting for data from server...");
    //System.out.println(new Date());

    readDataFromSocket(ois);
  }

  private static void readDataFromSocket(DataInputStream ois) throws IOException, InterruptedException {
    Thread.sleep(5000);

    System.out.println("Reading...");
    System.out.println(new Date());

    String in = ois.readUTF();
    System.out.println("Read data from socket: " + in);
  }
}



