
import java.util.ArrayList;
import java.util.List;

public class Registro {

    String palavra;
    int quantidade;
    int chave;
    List<Long> indice = new ArrayList();

    public Registro(String palavra, long offset, int chave) {
        indice.add(offset);
        this.palavra = palavra;
        this.quantidade = 0;
        this.chave = chave;
    }
}
