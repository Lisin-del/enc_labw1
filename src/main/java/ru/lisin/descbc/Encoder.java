package ru.lisin.descbc;

import java.util.ArrayList;
import java.util.List;

public class Encoder {

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
}
