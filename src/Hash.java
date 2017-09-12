
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.HashMap;

public class Hash {

    public final Map<Integer, Registro> tabela = new HashMap<>();

    /* HashMap eh uma collection de java semelhante a uma 
    ArrayList, mas que recebe um valor inteiro de (indice)chave 
    e associa um Registro a esse valor de chave. 
     */

 /* metodo que calcula e retorna um valor de Hash
    para insercao na tabela. O calculo eh feito usando
    a multiplicacao entre a primeira e a ultima letra
    da palavra, o tamanho da palavra. O resultado
    final eh o resto da divisao por 101 ( que eh o tamanho 
    da tabela ). 
     */
    public int HashCode(String r) {
        int chave;
        char pletra = r.charAt(0);
        char uletra = r.charAt(r.length() - 1);
        chave = ((pletra * uletra) * r.length()) % 101;
        return chave;
    }

    /*
    o metodo de inserir recebe um objeto do tipo Registro e 
    uma posicao. A posicao eh gerada pela funcao HashCode 
    e informa o indice da tabela onde o registro sera 
    adicionado. 
    Para tratamento de colisoes e encadeamento, caso 
    na posicao informada ja exista um registro, entao
    a funcao ira procurar a proxima posicao disponivel
    para inseri-lo
     */

    public void Inserir(Registro r, int posicao) {
        while (tabela.containsKey(posicao)) {
            posicao++;
                if (posicao > 100){ 
                    posicao = 0; 
                }
        }
        r.quantidade++;
        tabela.put(posicao, r);
    }

    /* O metodo PesquisaRegistro recebe uma String
e busca por ela na tabela hash. Caso ja exista essa palavra
ela retorna o registro ao qual ela esta associada. 
     */
    public Registro PesquisaRegistro(String p) {
        Registro r;
        for (Map.Entry<Integer, Registro> pair : tabela.entrySet()) {
            if (pair.getValue().palavra.equals(p)) {
                r = tabela.get(pair.getKey());
                return r;
            }
        }
        return null;
    }

    /* 
   O metodo CriaHash faz a leitura do arquivo de entrada
   dado pelo caminho passado como parametro (String entrada)
   Com base nos dados lidos do arquivo, gera a tabela Hash usando
   as outros metodos contidos nessa classe
     */
    public void CriaHash(String entrada) {
        char letra;
        int fim; //variavel de controle
        String palavra;
        long offset; //variavel onde sera armazenado o byte offset da palavra no arquivo txt
        Registro x; //objeto do tipo registro que contem os atributos
        try {
            RandomAccessFile arq = new RandomAccessFile(entrada, "r"); //abre arquivo para apenas leitura
            try {
                offset = arq.getFilePointer();
                fim = arq.read();
                while (fim != -1) {
                    palavra = "";
                    letra = (char) fim;
                    while (letra != ' ' && letra != '\n' && fim != -1) {
                        palavra += letra;
                        fim = arq.read();
                        if (fim == 13 || fim == 10) { //exclui caracteres especiais 
                            fim = arq.read(); //cujo valor em bits eh 10 e 13
                        }
                        letra = (char) fim;
                    }
                    palavra = palavra.toLowerCase(); //apenas letras minusculas
                    if (PesquisaRegistro(palavra) == null) { //caso nao exista um registro com essa palavra
                        x = new Registro(palavra, offset, HashCode(palavra));
                        Inserir(x, HashCode(palavra));
                        /* para o caso de nao haver registro com a palavra lida
                          entao sera criado um novo registro, passando a palavra 
                          como parametro e a posicao em bytes de seu 
                          inicio
                         */
                    } else {
                        /* 
                        caso ja exista um registro com a palavra passada
                        entao eh feita uma pesquisa para encontrar em qual 
                        registro ela esta, e entao ha uma atualizacao na quantidade
                        de vezes que a palavra apareceu e 
                        ainda a insercao da posicao em bytes na lista 
                        de posicoes
                         */
                        x = PesquisaRegistro(palavra);
                        x.quantidade++;
                        x.indice.add(offset);
                        tabela.replace(PesquisaHash(palavra).chave, x);
                    }
                    fim = arq.read();
                    offset = arq.getFilePointer();
                }

            } catch (EOFException ex) {
            }
            arq.close();
        } catch (IOException e) {
        }
        //exibe todos os registros na tabela hash
        //tabela.entrySet().forEach((pair) -> { 
         //   System.out.println(pair.getKey() + " " + pair.getValue().palavra + " x " + pair.getValue().quantidade
        //            + " x " + pair.getValue().indice);
        // });
    }

    //Metodo para pesquisar na chave pela chave
   public Registro PesquisaHash(String palavra){ 
        int chave = HashCode(palavra); //gera o hash para procurar na tabela
        if (tabela.get(chave) == null){  //nao existe nenhum registro nessa chave 
            return null;
        }else{ 
            if (tabela.get(chave).palavra.equals(palavra)){ //compara se a palavra eh a mesma que a do registro
                return tabela.get(chave);
            }
            else{ 
               return PesquisaRegistro(palavra); //pesquisa onde esta o registro com aquela palavra caso ele tenha colidido
               //com algum outro registro em uma posicao da tabela
                }
        } 
    }

}
    
