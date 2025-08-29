package SomeLabs.SymmetricLab;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Symmetric {

    public static String filePath = "src/SomeLabs/SymmetricLab/text.txt";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        String[] encrypt = {"DES/CBC/PKCS5Padding", "AES/CFB/PKCS5Padding", "Blowfish/OFB/PKCS5Padding"};
        String[] encryptAlg = {"DES", "AES", "Blowfish"};
        int[] keySize = {56, 128, 256};

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        for (int i = 0; i < 3; i++) {
            KeyGenerator keyGen = KeyGenerator.getInstance(encryptAlg[i]);
            keyGen.init(keySize[i]);
            SecretKey secretKey = keyGen.generateKey();

            // Генерация вектора инициализации (можно без него)
            // Генерируется при каждом шифровании и участвует в нем
            byte[] iv = new byte[(i % 2 == 0) ? 8 : keySize[i] / 8];
            SecureRandom.getInstanceStrong().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Шифрование текста
            Cipher cipher = Cipher.getInstance(encrypt[i]);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedText = cipher.doFinal(bytes);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptedText = cipher.doFinal(encryptedText);

            System.out.println("\nAlg: " + encryptAlg[i]);
            System.out.println("Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            System.out.println("Text: " + new String(bytes));
            System.out.println("Encrypt: " + Base64.getEncoder().encodeToString(encryptedText));
            System.out.println("Decrypt: " + new String(decryptedText) + "\n");

        }

    }

}
