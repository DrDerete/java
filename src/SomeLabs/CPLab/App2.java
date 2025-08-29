package SomeLabs.CPLab;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class App2 {
    public static void main(String[] args) throws Exception {
        // Чтение данных от приложения 1 через стандартный ввод
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String signedDocument = reader.readLine();
        String encodedSignature = reader.readLine();
        String publicKeyString = reader.readLine();

        // Десериализация открытого ключа
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        // Проверка подписи
        Signature dsa = Signature.getInstance("SHA1withDSA");
        dsa.initVerify(publicKey);
        byte[] documentBytes = Base64.getDecoder().decode(signedDocument);
        byte[] signatureBytes = Base64.getDecoder().decode(encodedSignature);
        dsa.update(documentBytes);

        boolean isValid = dsa.verify(signatureBytes);
        System.out.println("Signature is valid: " + isValid);

    }
}
