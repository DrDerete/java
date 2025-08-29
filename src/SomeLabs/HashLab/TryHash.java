package SomeLabs.HashLab;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class TryHash {
    // тест хеш-функций
        public static void main(String[] args) {
            String[] texts = {"Hello, World!", "", "123", "123", "World! Hello,", "312"};
            String[] algorithms = {"MD2", "SHA-1", "SHA-256"};

            for (String alg : algorithms) {
                for (String t : texts) {
                    try {
                        String hash = calculateHash(t, alg);
                        System.out.println("Algorithm: " + alg + " --- Text: " + t +  " --- Hash: " + hash);
                    } catch (NoSuchAlgorithmException e) {
                        System.out.println("Algorithm " + alg + " is not available.");
                    }

                }
            }
        }

        public static String calculateHash(String input, String algorithm) throws NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = digest.digest(input.getBytes());
            return bytesToHex(hashBytes);
        }

        private static String bytesToHex(byte[] bytes) {
            StringBuilder hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
}
