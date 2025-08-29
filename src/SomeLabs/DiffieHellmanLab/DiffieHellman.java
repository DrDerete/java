package SomeLabs.DiffieHellmanLab;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DiffieHellman {

    public static void main(String[] args) throws Exception {

        // Генерация параметров Диффи-Хеллмана Алисы
        KeyPairGenerator aKeyPairGen = KeyPairGenerator.getInstance("DH");
        aKeyPairGen.initialize(512);
        KeyPair aKeyPair = aKeyPairGen.generateKeyPair();

        // Алиса передает параметры Бобу
        // KeyAgreement - класс реализации обмена ключами (используя формулу и свойства остатка от деления получается одинаковый ключ)
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
        // Алиса считает общий ключ используя свои параметры
        aliceKeyAgree.init(aKeyPair.getPrivate());

        // Боб получает публичные параметры Алисы
        // KeyFactory - класс для преобразования ключей (так и получаем параметры)
        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(aKeyPair.getPublic().getEncoded());
        PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec);

        // Боб создает и передает параметры Алисе
        KeyPairGenerator bKeyPairGen = KeyPairGenerator.getInstance("DH");
        // используя то, что уже знает от Алисы
        bKeyPairGen.initialize(((DHPublicKey) alicePubKey).getParams());
        KeyPair bKeyPair = bKeyPairGen.generateKeyPair();
        // Боб считает общий ключ используя свои параметры
        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bKeyPair.getPrivate());

        // Алиса использует параметры полученные от Боба
        x509KeySpec = new X509EncodedKeySpec(bKeyPair.getPublic().getEncoded());
        PublicKey bobPubKey = bobKeyFac.generatePublic(x509KeySpec);

        // Вот тут происходит основной подсчет класса KeyAgreement по переданным друг другу параметрам
        // общий ключ сгенерирован
        aliceKeyAgree.doPhase(bobPubKey, true);
        bobKeyAgree.doPhase(alicePubKey, true);

        byte[] aliceSecret = aliceKeyAgree.generateSecret();
        byte[] bobSecret = bobKeyAgree.generateSecret();

        // Проверяем, совпали ли ключи
        if (MessageDigest.isEqual(aliceSecret, bobSecret)) {
            System.out.println("!!!Shared key are the same!!!");
        } else {
            System.out.println("!!!Shared key are different!!!");
        }

        // Вывод всех ключей
        System.out.println("Alice key: " + Base64.getEncoder().encodeToString(aKeyPair.getPrivate().getEncoded()));
        System.out.println("Bob key: " + Base64.getEncoder().encodeToString(bKeyPair.getPrivate().getEncoded()));
        System.out.println("Shared key: " + Base64.getEncoder().encodeToString(aliceSecret));
    }
}
