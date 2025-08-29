package SomeLabs.MACLab;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Formatter;

public class mac {

    public static void main(String[] args) {
        String[] algorithms = {"HmacSHA1", "HmacSHA256", "HmacSHA384", "HmacSHA512"};
        String shortText1 = "Machine learning, deep learning, and AI come up in countless articles, often outside of technology-minded publications.";
        String shortText2 = "For a future or current practitioner of machine learning, it’s important to be able to recognize the signal in the noise so that you can tell world-changing developments from overhyped press releases.";

        for (String algorithm : algorithms) {
            System.out.println("Algorithm: " + algorithm);
            SecretKey key = generateKey(algorithm);
            Mac mac = getMacInstance(algorithm, key);

            // Одинаковые строки
            String macShortText1 = calculateMAC(mac, shortText1);
            String macShortText1Again = calculateMAC(mac, shortText1);
            System.out.println("MAC for short text 1: " + macShortText1);
            System.out.println("MAC for short text 1 (again): " + macShortText1Again);
            System.out.println("Are MACs equal for short text 1: " + macShortText1.equals(macShortText1Again));

            // Разные строки
            String macShortText2 = calculateMAC(mac, shortText2);
            System.out.println("MAC for short text 2: " + macShortText2);
            System.out.println("Are MACs equal for different texts: " + macShortText1.equals(macShortText2));

            // Строки с перестановкой слов
            String permutedWordsText = permuteWords(shortText1);
            String macPermutedWords = calculateMAC(mac, permutedWordsText);
            System.out.println("MAC for short text with permuted words: " + macPermutedWords);
            System.out.println("Are MACs equal for permuted words: " + macShortText1.equals(macPermutedWords));

            // Строки с перестановкой букв
            String permutedCharsText = permuteChars(shortText1);
            String macPermutedChars = calculateMAC(mac, permutedCharsText);
            System.out.println("MAC for short text with permuted chars: " + macPermutedChars);
            System.out.println("Are MACs equal for permuted chars: " + macShortText1.equals(macPermutedChars));

            System.out.println("Algorithm: " + mac.getAlgorithm());
            System.out.println("MAC length: " + mac.getMacLength());
            System.out.println();
        }
    }

    private static SecretKey generateKey(String algorithm) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            SecureRandom random = new SecureRandom();
            keyGen.init(random);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static Mac getMacInstance(String algorithm, SecretKey key) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);
            return mac;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static String calculateMAC(Mac mac, String data) {
        byte[] macBytes = mac.doFinal(data.getBytes());
        return bytesToHex(macBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return sb.toString();
    }

    private static String permuteWords(String text) {
        String[] words = text.split(" ");
        for (int i = 0; i < words.length - 1; i += 2) {
            String temp = words[i];
            words[i] = words[i + 1];
            words[i + 1] = temp;
        }
        return String.join(" ", words);
    }

    private static String permuteChars(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length - 1; i += 2) {
            char temp = chars[i];
            chars[i] = chars[i + 1];
            chars[i + 1] = temp;
        }
        return new String(chars);
    }
}