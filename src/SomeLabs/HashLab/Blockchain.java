package SomeLabs.HashLab;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    // простоя реализация блокчейна
    public static List<Block> blockchain = new ArrayList<>();
    public static int difficulty = 5;

    public static void main(String[] args) {
        blockchain.add(new Block("0", "Genesis Block"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.getFirst().mineBlock(difficulty);

        blockchain.add(new Block(blockchain.getLast().calculateHash(), "Second Block"));
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block(blockchain.getLast().calculateHash(), "Third Block"));
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " + isChainValid());
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.calculateHash().equals(currentBlock.getHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }
}
