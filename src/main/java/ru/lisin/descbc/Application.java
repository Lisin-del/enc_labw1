package ru.lisin.descbc;

import java.util.Collections;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        String inputMessage = "This is a new test message to test my new approach of secret key generation";
        Encoder encoder = new Encoder();
        List<List<Byte>> binaryBlocksToEncrypt = encoder.getBinaryBlocksToEncrypt(
                encoder.byteArrayToList(inputMessage.getBytes())
        );
        List<List<Byte>> encryptedBlocks = encoder.encrypt(binaryBlocksToEncrypt);

        StringBuilder unEncryptedMessageBuilder = new StringBuilder();

        for (List<Byte> block : binaryBlocksToEncrypt) {
            byte[] array = new byte[block.size()];
            int i = 0;
            for (Byte b : block) {
                array[i] = b;
                ++i;
            }
            unEncryptedMessageBuilder.append(new String(array).replace("0", ""));
        }
        System.out.println(unEncryptedMessageBuilder);
        System.out.println("------------------------------");

        StringBuilder encryptedMessageBuilder = new StringBuilder();

        for (List<Byte> block : encryptedBlocks) {
            byte[] array = new byte[block.size()];
            int i = 0;
            for (Byte b : block) {
                array[i] = b;
                ++i;
            }
            encryptedMessageBuilder.append(new String(array));
        }
        System.out.println(encryptedMessageBuilder);
        System.out.println("------------------------------");

        Decoder decoder = new Decoder();
        List<List<Byte>> decryptedBlocks = decoder.decrypt(encryptedBlocks);
        Collections.reverse(decryptedBlocks);

        StringBuilder decryptedMessageBuilder = new StringBuilder();

        for (List<Byte> block : decryptedBlocks) {
            byte[] array = new byte[block.size()];
            int i = 0;
            for (Byte b : block) {
                array[i] = b;
                ++i;
            }
            decryptedMessageBuilder.append(new String(array).replace("0", ""));
        }

        System.out.println(decryptedMessageBuilder);
    }
}
