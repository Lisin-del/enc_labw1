package ru.lisin.descbc;

import java.util.Collections;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        String inputMessage = "Usual hello world message!!!";
        Encoder encoder = new Encoder();
        List<List<Byte>> binaryBlocksToEncrypt = encoder.getBinaryBlocksToEncrypt(
                encoder.byteArrayToList(inputMessage.getBytes())
        );
        List<List<Byte>> encryptedBlocks = encoder.encrypt(binaryBlocksToEncrypt);

        StringBuilder stringBuilder = new StringBuilder();

        for (List<Byte> block : binaryBlocksToEncrypt) {
            byte[] array = new byte[block.size()];
            int i = 0;
            for (Byte b : block) {
                array[i] = b;
                ++i;
            }
            stringBuilder.append(new String(array));
        }
        System.out.println(stringBuilder);
        System.out.println("------------------------------");

        StringBuilder stringBuilder1 = new StringBuilder();

        for (List<Byte> block : encryptedBlocks) {
            byte[] array = new byte[block.size()];
            int i = 0;
            for (Byte b : block) {
                array[i] = b;
                ++i;
            }
            stringBuilder1.append(new String(array));
        }
        System.out.println(stringBuilder1);
        System.out.println("------------------------------");

        Decoder decoder = new Decoder();
        List<List<Byte>> decryptedBlocks = decoder.decrypt(encryptedBlocks);
        Collections.reverse(decryptedBlocks);

        StringBuilder stringBuilder2 = new StringBuilder();

        for (List<Byte> block : decryptedBlocks) {
            byte[] array = new byte[block.size()];
            int i = 0;
            for (Byte b : block) {
                array[i] = b;
                ++i;
            }
            stringBuilder2.append(new String(array));
        }

        System.out.println(stringBuilder2);
    }
}
