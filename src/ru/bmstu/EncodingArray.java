/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.bmstu;

import com.sun.source.tree.Tree;
import java.util.Random;
import java.util.Scanner;
import java.util.Base64;
import java.security.Key;
import java.security.SecureRandom;
import java.security.*;
import java.util.Collections;
import javax.crypto.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Set;

/**
 *
 * @author alenk
 */
public class EncodingArray {

    //encode xor
    public static void encodeArray(int[] Array, int magicNumber) {

        for (int i = 0; i < Array.length; i++) {
            Array[i] = Array[i] ^ magicNumber;
        }
    }

    //encode for b64
    public static byte[] encodeArray(String str) {

        Base64.Encoder enc = Base64.getEncoder();
        byte[] encbytes = enc.encode(str.getBytes());
        return encbytes;
    }

    //decoder for b64
    public static byte[] decodeArray(byte[] encbytes, int[] array) {
        Base64.Decoder dec = Base64.getDecoder();
        byte[] decbytes = dec.decode(encbytes);
        return decbytes;
    }

    //encode for BlowFish
    public static byte[] encodeBlowFish(int[] array, Key secretkey) {
        byte[] encrypted = null;
        Cipher cipher = null;
        //Set<String> str = Security.getAlgorithms("Cipher");

        try {
            cipher = Cipher.getInstance("BLOWFISH");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("No such Algorithm");
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
        } catch (InvalidKeyException e) {
            System.out.println("Invalid key");
        }
        String inputText = intArrayToString(array);
        try {
            encrypted = cipher.doFinal(inputText.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("IllegalBlockSize");
        }
        return encrypted;
    }

    
    //decode forBlowFish
    public static byte[] decodeBlowFish(byte[] encryptedData, Key key) {
        // SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF-8"), "Blowfish");
        byte[] decrypted = null;
         Cipher cipher = null;

        try {
            cipher = Cipher.getInstance("BLOWFISH");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("No such Algorithm");
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            System.out.println("Invalid key");
        }
        try {
            decrypted = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("IllegalBlockSize");
        }
        
    
        return decrypted;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Test encode array");
        int[] array = new int[10];
        int methodEncode;

        //заполним массив случайными числами
        fillRandom(array);
        //выведем  массив на консоль
        System.out.print("Generate array");
        printArray(array);
        //кодируем числом 2774162085 в 16-ой системе
        //int magicNumber = 0xA55A5AA5;
        //encode random number
        Random r = new Random();
        int magicNumber = r.nextInt();
        // спросим у пользователя каким способом он хочет кодировать массив
        Scanner input = new Scanner(System.in);
        System.out.println("Enter\n1: Encode method XOR\n2: Encode method b64:\n3: Encode BlowFish:");
        methodEncode = input.nextInt();
        //закодируем массив
        switch (methodEncode) {
            case (1) -> {
                encodeArray(array, magicNumber);
                //выведем закодируем  массив на консоль
                System.out.println("Encode Array");
                printArray(array);
                //раскодируем  массив
                encodeArray(array, magicNumber);
                //выведем  массив на консоль (после расколировки массив должен быть первоначальным)
                System.out.println("Decode Array");
                printArray(array);
            }
            case (2) -> {
                String str = intArrayToString(array);
                byte[] encbytes = encodeArray(str);
                printArray(encbytes);
                //decoder for b64
                encbytes = decodeArray(encbytes, array);
                stringToIntArray(encbytes, array);
                //print array
                printArray(array);
            }
            case (3) -> {
                // create a key generator based upon the Blowfish cipher
                // KeyGenerator keygenerator = KeyGenerator.getInstance("RC2");
                KeyGenerator keygenerator = null;
                try {
                            keygenerator = KeyGenerator.getInstance("BLOWFISH");
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("NoSuch");
                }

                //Creating a SecureRandom object
             //   SecureRandom secRandom = new SecureRandom();
                //Initializing the KeyGenerator
                keygenerator.init(128);
                // create a key
                Key secretkey = keygenerator.generateKey();
                       
                byte[] encbytes = encodeBlowFish(array, secretkey);
                printArray(encbytes);
                byte[] decbytes = decodeBlowFish(encbytes, secretkey);
                stringToIntArray(decbytes, array);
                printArray(array);

                            }

            default ->
                System.out.println("Not correct methods");
        }

    }

    private static void fillRandom(int[] array) {

        try {
            Random r = new Random();
            if (r == null) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            //заполнение массива случайными целыми числами
            for (int i = 0; i < array.length; i++) {
                array[i] = r.nextInt();
            }
        } catch (UnsupportedOperationException e) {
            System.err.println("Cant generate number");
        }
    }

    private static void printArray(int[] array) {
        try {
            if (array == null) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            //вывод массива на консоль
            for (int i = 0; i < array.length; i++) {
                System.out.print("\n" + Integer.toString(array[i]));
            }
            System.out.println("\n");
        } catch (UnsupportedOperationException e) {
            System.out.println("Array not create");
        }
    }

    //print for b64 
    private static void printArray(byte[] encbytes) {
        try {
            if (encbytes == null) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            //вывод массива на консоль
            for (int i = 0; i < encbytes.length; i++) {
                System.out.printf("%c", (char) encbytes[i]);
                if (i != 0 && i % 4 == 0) {
                    System.out.print(' ');
                }
            }
            System.out.println();
        } catch (UnsupportedOperationException e) {
            System.out.println("Array not create");
        }
    }

    private static String intArrayToString(int[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str += Integer.toString(array[i]) + " ";
        }
        return str;
    }

    private static void stringToIntArray(byte[] decbytes, int[] array) {
        //преобразуем decode в строку
        //String str = "";
        String str = new String(decbytes);
        String[] str1 = str.split(" ");

        for (int i = 0; i < array.length; i++) {

            array[i] = Integer.parseInt(str1[i]);
        }
    }
}
