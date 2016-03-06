/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superkey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Tiago
 */
public class OperacoesCifra {

    private String key;
    private String keyChainFileName;

    public OperacoesCifra() {

        this.key = Configuracoes.key;
        this.keyChainFileName = Configuracoes.keyChainFileName;
    }

    public void cifrarFicheiro() throws FileNotFoundException {
        //
        try {

            
            SecretKeySpec secretKey = new SecretKeySpec(getKey().getBytes(), "AES");
            
            //Creation of Cipher objects  
            Cipher encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);

            // Open the Plaintext file  
            byte[] FileBytes = encrypt.doFinal(getFileBytes(keyChainFileName));

            FileOutputStream fos = new FileOutputStream(keyChainFileName);

            fos.write(FileBytes);
            fos.close();
        } catch (InvalidKeyException e) {
            System.out.println("Nao foi possivel efetuar a cifra do ficheiro. Chave invalida");
    

        } catch (IOException e) {
            System.out.println("Nao foi possivel escrever o ficheiro cifrado");
           
        } catch (Exception e) {
            System.out.println("Nao foi possivel cifrar o ficheiro. Algoritmo invalido");
           
        }

    }

    public void decifrarFicheiro() throws FileNotFoundException {

        try {
            SecretKeySpec secretKey = new SecretKeySpec(getKey().getBytes(), "AES");
            
            //Creation of Cipher objects  
            javax.crypto.Cipher decrypt = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");

            decrypt.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey);
            byte[] FileBytes = decrypt.doFinal(getFileBytes(keyChainFileName));

            FileOutputStream fos = new FileOutputStream(keyChainFileName);

            fos.write(FileBytes);
            fos.close();

        } catch (InvalidKeyException e) {
            System.out.println("Nao foi possivel efetuar a decifra do ficheiro. Chave invalida");
         

        } catch (IOException e) {
            System.out.println("Nao foi possivel escrever o ficheiro decifra");
          
        } catch (Exception e) {
            System.out.println("Nao foi possivel decifra o ficheiro. Algoritmo invalido");
            
        }

    }

    private byte[] getFileBytes(String fileName) throws IOException {
        File keyFile = new File(fileName);

        if (!keyFile.exists()) {
            System.out.print("o ficheiro nao existe" + fileName);
            System.exit(1);
        }

        FileInputStream finput = null;
        try {
            finput = new FileInputStream(keyFile);
        } catch (FileNotFoundException ex) {
        }
        byte fileContent[] = new byte[(int) keyFile.length()];
        finput.read(fileContent);

        return fileContent;

    }

    private String getKey() {

        int AESsize = 16;
        char FillChar = '0';
        int length = key.length();
        if (length > AESsize && length != AESsize) {
            key = key.substring(0, AESsize - 1);
        }
        if (length < AESsize && length != AESsize) {
            for (int i = 0; i < AESsize - length; i++) {
                key = key + FillChar;
            }
        }

        return key;
    }

}
