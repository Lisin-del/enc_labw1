package ru.lisin.descbc;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the encryption logic.
 */
public class Encoder {
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
     * Split an input byte block into equal blocks of 8 bytes.
     * @param byteBlock is an input byte block.
     * @return a list of blocks of 8 bytes.
     */
    public List<List<Byte>> getBinaryBlocksToEncrypt(List<Byte> byteBlock) {
        int byteQuantity = 8; // because a block size is 64 bits
        List<List<Byte>> byteListToEncrypt = new ArrayList<>();

        int remainder = byteBlock.size() % 8;
        int cycleQuantity = byteBlock.size() / byteQuantity;
        int necessaryNumberOfNullBytes = byteQuantity - remainder;

        if (remainder != 0) {
            for (int i = 0; i < cycleQuantity; ++i) {
                List<Byte> inputBlock = new ArrayList<>();

                for (int j = 0; j < byteQuantity; ++j) {
                    inputBlock.add(byteBlock.get(0));
                    byteBlock.remove(0);
                }

                byteListToEncrypt.add(inputBlock);
            }

            List<Byte> inputBlock = new ArrayList<>();

            for (Byte b : byteBlock) {
                inputBlock.add(b);
            }

            for (int i = 0; i < necessaryNumberOfNullBytes; ++i) {
                byte b = 48; // 48 code is 0 by the ASCII table
                inputBlock.add(b);
            }

            byteListToEncrypt.add(inputBlock);
        } else {
            for (int i = 0; i < cycleQuantity; ++i) {
                List<Byte> inputBlock = new ArrayList<>();

                for (int j = 0; j < byteQuantity; ++j) {
                    inputBlock.add(byteBlock.get(0));
                    byteBlock.remove(0);
                }

                byteListToEncrypt.add(inputBlock);
            }
        }

        return byteListToEncrypt;
    }

    /**
     * Converts a byte array to a list of bytes.
     * @param byteBlock is an input array of bytes.
     * @return a list of bytes.
     */
    public List<Byte> byteArrayToList(byte[] byteBlock) {
        List<Byte> byteList = new ArrayList<Byte>();
        for (byte b : byteBlock) {
            byteList.add(b);
        }
        return byteList;
    }

    /**
     * Encrypts a list of binary blocks.
     * @param binaryBlocksToEncrypt is a list of binary blocks to encrypt.
     * @return an encrypted list of binary blocks.
     */
    public List<List<Byte>> encrypt(List<List<Byte>> binaryBlocksToEncrypt) {
        List<List<Byte>> completedEncryptedBlock = new ArrayList<>();

        generateSecretKeys(binaryBlocksToEncrypt.size());

        for (int i = 0; i < binaryBlocksToEncrypt.size(); ++i) {
            List<Byte> encryptedBlockWithoutKey = new ArrayList<>();

            if (i == 0) {
                encryptedBlockWithoutKey.addAll(applyXORToBlocks(binaryBlocksToEncrypt.get(i), initializationVector));
                completedEncryptedBlock.add(applyFFunction(encryptedBlockWithoutKey, secretKeys.get(i)));
            } else {
                encryptedBlockWithoutKey.addAll(
                        applyXORToBlocks(binaryBlocksToEncrypt.get(i), completedEncryptedBlock.get(i - 1))
                );
                completedEncryptedBlock.add(applyFFunction(encryptedBlockWithoutKey, secretKeys.get(i)));
            }
        }

        return completedEncryptedBlock;
    }

    /**
     * Applies the F function (XOR with the secret key) to an input encrypted byte block.
     * @param inputEncryptedByteBlock is an input encrypted byte block.
     * @return a result encrypted list of bytes with the applied secret key with (XOR).
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
     * @param openByteBlock is an unencrypted byte block.
     * @param encryptedByteBlock is an encrypted byte block.
     * @return a result list of encrypted bytes.
     */
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
