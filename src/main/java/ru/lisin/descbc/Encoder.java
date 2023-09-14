package ru.lisin.descbc;

import java.util.ArrayList;
import java.util.List;

public class Encoder {
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

    public List<Byte> byteArrayToList(byte[] byteBlock) {
        List<Byte> byteList = new ArrayList<Byte>();
        for (byte b : byteBlock) {
            byteList.add(b);
        }
        return byteList;
    }

    public List<List<Byte>> splitInputByteBlock(List<Byte> inputByteBlock) {
        List<Byte> leftByteBlock = new ArrayList<>();
        List<Byte> rightByteBlock = new ArrayList<>();
        int inputByteBlockHalf = inputByteBlock.size() / 2;

        for (int i = 0; i < inputByteBlockHalf; ++i) {
            leftByteBlock.add(inputByteBlock.get(i));
        }

        for (int i = inputByteBlockHalf; i < inputByteBlock.size(); ++i) {
            rightByteBlock.add(inputByteBlock.get(i));
        }

        return new ArrayList<>() {{
            add(leftByteBlock);
            add(rightByteBlock);
        }};
    }

    public List<List<Byte>> encrypt(List<List<Byte>> binaryBlocksToEncrypt) {
        List<List<Byte>> completedEncryptedBlock = new ArrayList<>();

        for (int i = 0; i < binaryBlocksToEncrypt.size(); ++i) {
            List<Byte> encryptedBlockWithoutKey = new ArrayList<>();

            if (i == 0) {
                encryptedBlockWithoutKey.addAll(applyXORToBlocks(binaryBlocksToEncrypt.get(i), initializationVector));
                completedEncryptedBlock.add(applyFFunction(encryptedBlockWithoutKey));
            } else {
                encryptedBlockWithoutKey.addAll(
                        applyXORToBlocks(binaryBlocksToEncrypt.get(i), completedEncryptedBlock.get(i - 1))
                );
                completedEncryptedBlock.add(applyFFunction(encryptedBlockWithoutKey));
            }
        }

        return completedEncryptedBlock;
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
