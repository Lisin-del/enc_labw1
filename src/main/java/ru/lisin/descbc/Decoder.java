package ru.lisin.descbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Decoder {
    private final String secretKey = "dfj34m.#";
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
    private List<String> secretKeys = new ArrayList<>();

    private void generateSecretKeys(int blockQuantity) {
        for (int i = 0; i < blockQuantity; ++i) {
            byte[] mainSecretKeyBytes = secretKey.getBytes();
            List<Byte> newIntermediateGeneratedKey = new ArrayList<>();

            for (int j = 0; j < mainSecretKeyBytes.length; ++j) {
                byte movedByte = (byte) (mainSecretKeyBytes[j] << (j + i));
                newIntermediateGeneratedKey.add((byte) (movedByte ^ (1457 * i)));
            }

            byte[] generatedKey = new byte[newIntermediateGeneratedKey.size()];
            for (int j = 0; j < newIntermediateGeneratedKey.size(); ++j) {
                generatedKey[j] = newIntermediateGeneratedKey.get(j);
            }

            secretKeys.add(new String(generatedKey));
        }
    }

    /**
     * Decrypts an input binary blocks.
     * @param binaryBlocksToDecrypt are input binary blocks.
     * @return a list of decrypted binary blocks.
     */
    public List<List<Byte>> decrypt(List<List<Byte>> binaryBlocksToDecrypt) {
        generateSecretKeys(binaryBlocksToDecrypt.size());
        Collections.reverse(secretKeys);
        Collections.reverse(binaryBlocksToDecrypt);
        List<List<Byte>> completedDecryptedBlock = new ArrayList<>();

        for (int i = 0; i < binaryBlocksToDecrypt.size(); ++i) {
            List<Byte> decryptedWithKey = applyFFunction(binaryBlocksToDecrypt.get(i), secretKeys.get(i));
            if ((i + 1) < binaryBlocksToDecrypt.size()) {
                completedDecryptedBlock.add(applyXORToBlocks(decryptedWithKey, binaryBlocksToDecrypt.get(i + 1)));
            } else {
                completedDecryptedBlock.add(applyXORToBlocks(decryptedWithKey, initializationVector));
            }
        }

        return completedDecryptedBlock;
    }

    /**
     * Applies the F function (XOR with the secret key) to an input encrypted byte block.
     * @param inputEncryptedByteBlock is an input encrypted byte block.
     * @return a result decrypted list of bytes with the applied secret key with (XOR).
     */
    public List<Byte> applyFFunction(List<Byte> inputEncryptedByteBlock, String inputSecretKey) {
        List<Byte> completedEncryptedBlock = new ArrayList<>();

        for (int i = 0; i < inputEncryptedByteBlock.size(); ++i) {
            int inputByteValue = inputEncryptedByteBlock.get(i).intValue();
            byte encryptedByte = (byte) (inputByteValue ^ inputSecretKey.getBytes()[i]);
            completedEncryptedBlock.add(encryptedByte);
        }

        return completedEncryptedBlock;
    }

    /**
     * Applies the XOR operation for 2 byte blocks.
     * @param encryptedByteBlock0 is an encrypted byte block.
     * @param encryptedByteBlock1 is an encrypted byte block.
     * @return a result list of decrypted bytes.
     */
    public List<Byte> applyXORToBlocks(List<Byte> encryptedByteBlock0, List<Byte> encryptedByteBlock1) {
        if (encryptedByteBlock0.size() == encryptedByteBlock1.size()) {
            List<Byte> readyEncryptedBlock = new ArrayList<>();

            for (int i = 0; i < encryptedByteBlock0.size(); ++i) {
                int openByteIntValue = encryptedByteBlock0.get(i).intValue();
                int encryptedByteIntValue = encryptedByteBlock1.get(i).intValue();

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
