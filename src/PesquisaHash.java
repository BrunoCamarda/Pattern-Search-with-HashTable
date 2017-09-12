/*
---------------------------------------------------------
|           Algoritmos e Estruturas de Dados II         |
|                                                       |
|           Bruno Araujo Camarda - 14.1.8092            |
|                                                       |
|              Implementacao tabela hash                |
|        com encadeamento interno e area de colisao     |
|                                                       |
---------------------------------------------------------
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


/* 
Padrao de escrita no arquivo: 
{chave} palavra #quantidade_ocorrencia [posicao_byte1, posicaobyte2,...]

Onde: 
{chave} = chave equivalente na tabela hash
palavra = palavra em questao contida na tabela e no texto
#quantidade_ocorrencia = numero de ocorrencias no texto
[posicao_byte] = byte offset do inicio da palavra 
 */
public class PesquisaHash {

    public static void main(String[] args) throws IOException {
        Hash h = new Hash(); //instancia da minha clase Hash, onde sao feitas as operacoes
        h.CriaHash("texto.txt"); //inicia a criacao da hash com dados do arquivo "texto.txt"
        String palavra;
        Scanner in = new Scanner(System.in);
        Registro x;
        FileWriter arquivo = new FileWriter("saida.txt", false); //abre arquivo de saida saida.txt que eh limpo a cada execucao
        BufferedWriter escritor = new BufferedWriter(arquivo); //buffer para escrita no arquivo
        /* 
        menu de exibicao e decisao do usuario
        */
        System.out.println("TABELA HASH");
        System.out.println("Como gostaria de pesquisar na tabela? ");
        System.out.println("1. Passar palavras contidas em um arquivo de texto");
        System.out.println("2. Digitar palavra");
        System.out.println("0. SAIR");
        System.out.print("\n\n Informe a opcao: ");
        int opcao = in.nextInt();
        switch (opcao) {
            case 1:
                System.out.println("Lendo arquivo...");
                FileReader arq = new FileReader("entrada.txt");
                BufferedReader leitor = new BufferedReader(arq);
                while (leitor.ready()) {
                    palavra = leitor.readLine().toLowerCase();
                    x = h.PesquisaHash(palavra);
                    if (x != null) {
                        escritor.flush();
                        escritor.write("" + x.palavra);
                        escritor.write(" #" + x.quantidade);
                        escritor.newLine();
                    } else {
                        escritor.write("\npalavra " + palavra + " nao encontrada!");
                        escritor.newLine();
                    }
                }
                escritor.close();
                leitor.close();
                System.out.println("Arquivo de saida gerado com sucesso!");
                break;
            case 2:
                in.nextLine();
                FileWriter arquivo_saida = new FileWriter("saida2.txt", false);
                escritor = new BufferedWriter(arquivo_saida);
                do {
                    System.out.println("Digite 123 para parar de pesquisar palavras!!");
                    System.out.println("Informe a palavra a ser pesquisada: ");
                    palavra = in.nextLine().toLowerCase();
                    if (palavra.equals("123")) {
                        break;
                    }
                    x = h.PesquisaHash(palavra);
                    if (x != null) {
                        escritor.flush();
                        escritor.write("{" + x.chave + "}");
                        escritor.write(" " + x.palavra);
                        escritor.write(" #" + x.quantidade);
                        escritor.write(" " + x.indice);
                        escritor.newLine();
                    } else {
                        escritor.write("\npalavra " + palavra + " nao encontrada!");
                        escritor.newLine();
                    }
                } while (!palavra.equals("123"));
                escritor.close();
                break;

            default:
                break;
        }
    }
}
