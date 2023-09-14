package ru.lisin.descbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Decoder {
    private final int secretKey = 90;
    private final List<Byte> initializationVector = new ArrayList<>() {{
        add((byte) 11);
        add((byte) 2);
        add((byte) 5);
        add((byte) 14);
        add((byte) 21);
        add((byte) 13);
        add((byte) 7);
        add((byte) 54);
    }};

    public List<List<Byte>> decrypt(List<List<Byte>> binaryBlocksToDecrypt) {
        Collections.reverse(binaryBlocksToDecrypt);
        List<List<Byte>> completedDecryptedBlock = new ArrayList<>();

        for (int i = 0; i < binaryBlocksToDecrypt.size(); ++i) {
            List<Byte> encryptedBlockWithoutKey = new ArrayList<>();
            List<Byte> decryptedWithKey = applyFFunction(binaryBlocksToDecrypt.get(i));
            if ((i + 1) < binaryBlocksToDecrypt.size()) {
                completedDecryptedBlock.add(applyXORToBlocks(decryptedWithKey, binaryBlocksToDecrypt.get(i + 1)));
            } else {
                completedDecryptedBlock.add(applyXORToBlocks(decryptedWithKey, initializationVector));
            }
        }

        return completedDecryptedBlock;
    }

    public List<Byte> applyFFunction(List<Byte> inputEncryptedByteBlock) {
        List<Byte> completedEncryptedBlock = new ArrayList<>();

        for (Byte b : inputEncryptedByteBlock) {
            int inputByteValue = b.intValue();
            byte encryptedByte = (byte) (inputByteValue ^ secretKey);
            completedEncryptedBlock.add(encryptedByte);
        }

        return completedEncryptedBlock;
    }

    public List<Byte> applyXORToBlocks(List<Byte> openByteBlock, List<Byte> encryptedByteBlock) {
        if (openByteBlock.size() == encryptedByteBlock.size()) {
            List<Byte> readyEncryptedBlock = new ArrayList<>();

            for (int i = 0; i < openByteBlock.size(); ++i) {
                int openByteIntValue = openByteBlock.get(i).intValue();
                int encryptedByteIntValue = encryptedByteBlock.get(i).intValue();

                byte finishingEncryptedByte = (byte) (openByteIntValue ^ encryptedByteIntValue);
                readyEncryptedBlock.add(finishingEncryptedByte);
            }

            return readyEncryptedBlock;
        } else {
            System.out.println("ERROR: " + this.getClass() + "the block size is wrong");
            return null;
        }
    }
}
