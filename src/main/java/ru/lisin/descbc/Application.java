package ru.lisin.descbc;

import java.util.List;

public class Application {
    public static void main(String[] args) {
        String inputMessage = "Annoying hello world message with new words";
        Encoder encoder = new Encoder();
        List<List<Byte>> binaryBlocksToEncrypt = encoder.getBinaryBlocksToEncrypt(
                encoder.byteArrayToList(inputMessage.getBytes())
        );

        List<List<Byte>> lists = encoder.splitInputByteBlock(binaryBlocksToEncrypt.get(0));
        int i = 0;

//        List<Byte> bytes = binaryBlocksToEncrypt.get(4);
//        StringBuilder binaryMessage = new StringBuilder();
//        for (Byte b : bytes) {
//            String formattedString = String.format("%8s", Integer.toBinaryString(b)).replace(" ", "0");
//            binaryMessage.append(formattedString).append(" ");
//        }
//
//        bytes = binaryBlocksToEncrypt.get(5);
//        for (Byte b : bytes) {
//            String formattedString = String.format("%8s", Integer.toBinaryString(b)).replace(" ", "0");
//            binaryMessage.append(formattedString).append(" ");
//        }
//
//        System.out.println(binaryMessage);

//        StringBuilder binaryMessage = new StringBuilder();
//
//        for (byte b : inputMessage.getBytes()) {
//            String formattedString = String.format("%8s", Integer.toBinaryString(b)).replace(" ", "0");
//            binaryMessage.append(formattedString).append(" ");
//        }
//
//        System.out.println(binaryMessage);
    }



}
