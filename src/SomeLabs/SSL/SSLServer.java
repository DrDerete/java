package SomeLabs.SSL;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;

public class SSLServer {
    public static void main(String[] args) {
        int port = 8443;
        try {
            // Загрузка ключей и сертификатов
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("src/SomeLabs/SSL/keystore.jks"), "H-KEY".toCharArray());

            // Инициализация KeyManager
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, "prv".toCharArray());

            // Создание SSL контекста
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(kmf.getKeyManagers(), null, null);

            // Настройка серверного сокета
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);

            // Установка протоколов и шифров
            serverSocket.setEnabledProtocols(new String[] {"TLSv1.2"});
            serverSocket.setEnabledCipherSuites(serverSocket.getSupportedCipherSuites());

            System.out.println("Сервер слушает на порту " + port);

            while (true) {
                try (SSLSocket clientSocket = (SSLSocket) serverSocket.accept()) {
                    System.out.println("Клиент подключен: " + clientSocket.getRemoteSocketAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
//                      Сервер принял
                        System.out.println("Получено: " + inputLine);
//                      Ответ сервера
                        out.println("Держи ответ, НА");
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка обработки клиента:");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка инициализации сервера:");
            e.printStackTrace();
        }
    }
}