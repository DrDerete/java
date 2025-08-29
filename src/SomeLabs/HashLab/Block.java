package SomeLabs.HashLab;

import java.security.NoSuchAlgorithmException;

public class Block {
    // инициализация полей блока
    private final String previousHash;
    private final String data;
    private final long timestamp;
    private int nonce;
    private String hash;

    public Block(String previousHash, String data) {
        this.previousHash = previousHash;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.nonce = 0;
        hash = calculateHash();
    }

    public String calculateHash() {
        // хэш формируется по входным данным блока
        try {
            String input = previousHash + data + timestamp + nonce;
            return TryHash.calculateHash(input, "SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        // реализация майнинга у блока, в данном случае подбор числа nonce такого, что в начале строки хэша будет 5 нулей
        String target = new String(new char[difficulty]).replace('\0','0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}
