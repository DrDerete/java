package SomeLabs.RASLab;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class RSA {

    public static String privateKeyPath = "src/SomeLabs/RASLab/private.key";
    public static String publicKeyPath = "src/SomeLabs/RASLab/public.key";

    private static PrivateKey prv;
    private static PublicKey pub;

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        generateKeys(1024);

        Cipher rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, pub);

//      Шифрование
        byte[] ciphered = rsa.doFinal("*Some specific place*".getBytes());
        System.out.println("Шифр: " + Base64.getEncoder().encodeToString(ciphered));

//      Дешифрование
        rsa.init(Cipher.DECRYPT_MODE, prv);
        byte[] deciphered = rsa.doFinal(ciphered);
        System.out.println("Расшифровка: " + new String(deciphered));

//      Текст
        String str = "110sdjald10выфр0111!01";
        System.out.println("Строка: " + str);
        System.out.println("Количество символов: " + str.length());
        System.out.println("Массив байт: " + str.getBytes().length);


//      Разные ключи
        int[] keySizes = {512, 1024, 2048};
        for (int size : keySizes) {
            generateKeys(size);
            rsa.init(Cipher.ENCRYPT_MODE, pub);
            ciphered = rsa.doFinal(str.getBytes());
            System.out.println("Ключ размера " + size + ": " + Base64.getEncoder().encodeToString(ciphered));
        }

    }

    public static void generateKeys(int keySize) throws IOException, NoSuchAlgorithmException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);
        KeyPair pair = keyGen.generateKeyPair();

        prv = pair.getPrivate();
        pub = pair.getPublic();

        try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
            fos.write(prv.getEncoded());
        }
        try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
            fos.write(pub.getEncoded());
        }

    }

}
