package SomeLabs.SSL;

import javax.net.ssl.*;
import java.io.*;

public class SSLClient {
    public static void main(String[] args) {
        // Устанавливаем свойства хранилища доверия
        System.setProperty("javax.net.ssl.trustStore", "src/SomeLabs/SSL/keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "H-KEY");

        try {
            SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sf.createSocket("localhost", 8443);

            socket.setEnabledProtocols(new String[]{"TLSv1.2"});
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());

            System.out.println("Устанавливаем соединение...");
            socket.startHandshake();
            System.out.println("SSL соединение установлено");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Hello SSL Server!");
            String response = in.readLine();
            System.out.println("Ответ сервера: " + response);

            socket.close();

        } catch (Exception e) {
            System.err.println("Ошибка клиента:");
            e.printStackTrace();
        }
    }
}