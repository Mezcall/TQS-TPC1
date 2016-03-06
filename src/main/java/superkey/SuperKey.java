package superkey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A text-oriented keychain to keep a list of passwords in a file
 *
 * @author tqs student
 */
public class SuperKey {

    final static Scanner scanner = new Scanner(System.in,"UTF-8");
    
    final static OperacoesCifra operacoesCifra = new OperacoesCifra();

    public static void main(String[] args) throws Exception {
        scanner.useDelimiter("\\n");

   
     
      
        int op;
        do {
            System.out.println("1- Criar nova entrada");
            System.out.println("2- Listar keychain");
            System.out.println("3- Pesquisar credenciais p/ aplicação");

            System.out.println("0- Sair ");
            System.out.print("Opção? ");
            op = scanner.nextInt();
            System.out.println();

            switch (op) {
                case 1:
                    criarNovaEntrada();
                    break;

                case 2:
                    listarFicheiro( );
                    break;

                case 3:
                    pesquisarCredenciais();
                    break;

                case 0:
                    break;
                default:
                    System.out.println("Nao existe tal opcao");
            }
        } while (op != 0);

    }

    public static void criarNovaEntrada() {
        File keyFile = new File(Configuracoes.keyChainFileName);
   
        System.out.println("OPCAO 1");
        String line = "";

        if (!keyFile.exists()) {
            try {
                if (!keyFile.createNewFile()) {
                    System.out.println("Não foi possivel criar o ficheiro " + keyFile.getName());
                    return;
                }
            } catch (IOException ex) {
                
                System.out.println("Não foi possivel criar o ficheiro " + keyFile.getName());
                return;
            }
        }

        FileWriter fw = null;
        try {
            operacoesCifra.decifrarFicheiro();

            fw = new FileWriter(keyFile, true);

        } catch (IOException ex) {

            System.out.println("O ficheiro " + keyFile.getName() + " não pode ser alterado!");
            return;
        } 

        System.out.println("Aplicação/categoria? ");
        String platform = scanner.next();
        line += platform + ",";
        System.out.println("Utilizador? ");
        String user = scanner.next();
        line += user + ",";
        System.out.print("Gerar password? (y/n)");
        String yn = scanner.next();

        boolean gerarPassword = "y".equals(yn) || "Y".equals(yn);
        boolean naoGerarPassword = "n".equals(yn) || "N".equals(yn);

        if (gerarPassword) {
            String generatedPass = nextSessionPass();
            System.out.println("Nova Pass > " + generatedPass);
            line += generatedPass;

        } else if (naoGerarPassword) {
            System.out.println("Digite Pass");
            String pass = scanner.next();
            line += pass;

        } else {
            System.out.println("opcao incorrecta");
            return;
        }

        
        try {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line);
            bw.newLine();
            bw.close();
            fw.close();
            operacoesCifra.cifrarFicheiro();
        } catch (IOException ex) {
            System.out.println("Aconteceu um erro ao tentar escrever no ficheiro ");
        } 
        

        System.out.println("Utilizador guardado.");
        System.out.println();
    }

    public static void listarFicheiro() {
        Scanner scFile;
        File keyFile = new File(Configuracoes.keyChainFileName);
        try {
            operacoesCifra.decifrarFicheiro();

            scFile = new Scanner(keyFile);
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro não existe! Adicione registos na opção 1");
            return;
        }
        System.out.println("Aplicação User Password");
        System.out.println("-------------------------------------");
        while (scFile.hasNextLine()) {
            String entry = scFile.nextLine();
            String[] words = entry.trim().split(",");
            System.out.printf("%-10s  %8s  %15s", words[0], words[1], words[2]);
            System.out.println();

        }
        scFile.close();

        try {
            operacoesCifra.cifrarFicheiro();
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro não existe! Adicione registos na opção 1");
            return;
        }
        System.out.println();
    }

    public static void pesquisarCredenciais() {
        
        File keyFile = new File(Configuracoes.keyChainFileName);
         Scanner scFile;
        System.out.println("OPCAO 3");
        System.out.println("Aplicação a procurar? ");
        String search = scanner.next();

        try {
            operacoesCifra.decifrarFicheiro();

            scFile = new Scanner(keyFile);
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro não existe! Adicione registos na opção 1");
            return;
        }
        String[] palavras;
        System.out.println("Platform       User          Password");
        System.out.println("-------------------------------------");

        while (scFile.hasNextLine()) {
            palavras = scFile.nextLine().split(",");
            if (palavras[0].startsWith(search)) {
                System.out.printf("%-10s  %8s  %15s", palavras[0], palavras[1], palavras[2]);
                System.out.println();
            }

        }

        scFile.close();
        try {
            operacoesCifra.cifrarFicheiro();
        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro não existe! Adicione registos na opção 1");
            return;
        }
        System.out.println();
    }

    // Criar uma password complexa Alphanumerica
    public static String nextSessionPass() {
        Random rand = new Random();
        int nBits = 90;
        int radix = 32;
        return (new BigInteger( nBits, rand).toString(radix));
    }
}
