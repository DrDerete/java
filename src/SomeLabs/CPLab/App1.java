package SomeLabs.CPLab;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class App1 {

    public static String filePath = "src/SomeLabs/CPLab/Message.txt";
    public static String privateKeyPath = "src/SomeLabs/CPLab/private.key";
    public static String publicKeyPath = "src/SomeLabs/CPLab/public.key";
    public static String keyStorePath = "src/SomeLabs/CPLab/keystore.jks";

    private static final char[] keyStorePassword = "H-KEY".toCharArray();
    private static final char[] privateKeyPassword = "prv".toCharArray();

    public static void main(String[] args) throws Exception {

        PrivateKey prv;
        PublicKey pub;

        boolean noKeyStore = false;

//      Логика работы с ключами - без хранилища ключей или через него.
        if (noKeyStore) {

//          Генерация ключей для проверки разных подписей, без этого подписи получались одинаковые (либо я сошёл с ума, проверять это я конечно же не буду)
            if (!Files.exists(Paths.get(privateKeyPath)) || !Files.exists(Paths.get(publicKeyPath))) {
                generateKey();
            }

//          Загрузка ключей
            pub = loadPub();
            prv = loadPrv();

        } else {

//          По сути то же самое, но используя keyStore
            if (!Files.exists(Paths.get(keyStorePath))) {
                generateStore();
            }

//          Загрузка хранилища ключей из файла
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(keyStorePath)) {
                keyStore.load(fis, keyStorePassword);
            }

//          Загрузка ключей, приватный по имени и паролю
            prv = (PrivateKey) keyStore.getKey("prv", privateKeyPassword);
//            System.out.println("Private Key: " + prv);
//          Публичный из сертификата
            Certificate cert = keyStore.getCertificate("prv");
            pub = cert.getPublicKey();
//            System.out.println("Public Key: " + pub);

        }

//      Цифровая подпись с использование хеширование SHA1 и алгоритма шифрования DSA по стандарту.
        Signature dsa = Signature.getInstance("SHA1withDSA");

//      Инициализация signature
        dsa.initSign(prv);

//      Добавление данных для подписи
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        dsa.update(bytes);

//      Подпись
        byte[] sig = dsa.sign();

//      Данные для передачи: документ, подпись, публичный ключ
        String doc = Base64.getEncoder().encodeToString(bytes);
        String sign = Base64.getEncoder().encodeToString(sig);
        String pubKey = Base64.getEncoder().encodeToString(pub.getEncoded());

//      Передача через стандартный ввод, вывод
        System.out.println(doc);
        System.out.println(sign);
        System.out.println(pubKey);

    }

    public static void generateKey() throws IOException, NoSuchAlgorithmException {

//      Генератор ключей для цифровой подписи
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(1024, SecureRandom.getInstance("SHA1PRNG"));

//      Создание секретного и открытого ключа
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey prv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

//      Сохранение ключей в файлы
        try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
            fos.write(prv.getEncoded());
        }
        try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
            fos.write(pub.getEncoded());
        }

    }

    public static PrivateKey loadPrv() throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("DSA");
        return kf.generatePrivate(spec);

    }

    public static PublicKey loadPub() throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("DSA");
        return kf.generatePublic(spec);

    }

    public static void generateStore() throws Exception {

//      Генератор ключей для цифровой подписи
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        keyGen.initialize(1024, SecureRandom.getInstance("SHA1PRNG"));

//      Создание секретного и открытого ключа
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey prv = pair.getPrivate();
        PublicKey pub = pair.getPublic();

//      Создание и инит. пустого KeyStore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);

//      Создание самоподписанного сертификата
        X509Certificate cert = generateSelfSignedCertificate(pub, prv);

//      Сохранение ключей в KeyStore
        keyStore.setKeyEntry("prv", prv, privateKeyPassword, new Certificate[]{cert});
//      Вместе с ключом необходимо сохранять сертификат, содержащий данные о владельце(всё для идентификации)
//      Публичный ключ указан там же в сертификате и поэтому отдельно его сохранять не надо(да и нельзя, ошибку выдает при попытке)

//      Сохранение KeyStore в файл
        try (FileOutputStream fos = new FileOutputStream(keyStorePath)) {
            keyStore.store(fos, keyStorePassword);
        }

    }

//        вроде работает, но устарело
//    private static X509Certificate generateSelfSignedCertificate(PublicKey publicKey, PrivateKey privateKey) throws CertificateEncodingException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
//        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
//        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
//        certGen.setIssuerDN(new X500Principal("CN=SelfSigned"));
//        certGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24));
//        certGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10)));
//        certGen.setSubjectDN(new X500Principal("CN=SelfSigned"));
//        certGen.setPublicKey(publicKey);
//        certGen.setSignatureAlgorithm("SHA1withDSA");
//
//        return certGen.generate(privateKey);
//    }

    public static X509Certificate generateSelfSignedCertificate(PublicKey publicKey, PrivateKey privateKey)
            throws CertificateException, OperatorCreationException {

        // Устанавливаем провайдера Bouncy Castle
        Security.addProvider(new BouncyCastleProvider());

        // Информация для создания сертификата
        X500Name issuer = new X500Name("CN=SelfSigned");
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date notBefore = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
        Date notAfter = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10));
        X500Name subject = new X500Name("CN=SelfSigned");

        // Передаем настройки в сборщик сертификата
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                subject,
                SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())
        );

        // Подписываем и возвращаем сертификат
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA1withDSA").setProvider("BC").build(privateKey);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certBuilder.build(contentSigner));
    }

}
