package hashweiss;

import simbolos.Simbolo;

public class Hashweiss {

    private static final int TAMANHO_TABELA = 25143;
    private static Simbolo[] simbolos = new Simbolo[TAMANHO_TABELA];

    public static void main(String[] args) {
        Simbolo simbolo = new Simbolo();
        simbolo.setNome("primeiro");
        simbolo.setNivel(1);
        adicionar(simbolo);

        Simbolo simbolo2 = new Simbolo();
        simbolo2.setNome("primeiro");
        simbolo2.setNivel(2);
        adicionar(simbolo2);

        Simbolo simbolo3 = new Simbolo();
        simbolo3.setNome("primeiro");
        simbolo3.setNivel(3);
        adicionar(simbolo3);

        Simbolo simbolo4 = new Simbolo();
        simbolo4.setNome("segundo");
        simbolo4.setNivel(4);
        adicionar(simbolo4);

        Simbolo simbolo5 = new Simbolo();
        simbolo5.setNome("terceiro");
        simbolo5.setNivel(5);
        adicionar(simbolo5);

        Simbolo simbolo6 = new Simbolo();
        simbolo6.setNome("quarto");
        simbolo6.setNivel(6);
        adicionar(simbolo6);

        remover(simbolo5);
        System.out.println("Busca: " + buscar(simbolo5).getNivel() + ": " + buscar(simbolo5).getNome());

        for (int i = 0; i < TAMANHO_TABELA; i++) {

            if (posicaoVazia(i))
                continue;

            Simbolo atual = simbolos[i];
            do {
                System.out.println(atual.getNome() + ": " + atual.getNivel());
                atual = atual.getProximo();
            } while (atual != null);
        }
    }

    public static int hash(String key, int tableSize) {
        int hashVal = 0;
        for (int i = 0; i < key.length(); i++)
            hashVal = 37 * hashVal + key.charAt(i);
        hashVal %= tableSize;
        if (hashVal < 0)
            hashVal += tableSize;
        return hashVal;
    }

    public static void adicionar(Simbolo simbolo) {
        int indexSimbolo = hash(simbolo.getNome(), TAMANHO_TABELA);

        if (posicaoVazia(indexSimbolo)) {
            simbolos[indexSimbolo] = simbolo;

        } else {
            Simbolo busca = simbolos[indexSimbolo];

            while (possuiProximo(busca)) {
                busca = busca.getProximo();
            }

            busca.setProximo(simbolo);
        }
    }

    public static boolean remover(Simbolo simbolo) {
        int indexSimbolo = hash(simbolo.getNome(), TAMANHO_TABELA);

        Simbolo atual = simbolos[indexSimbolo];
        Simbolo proximo = atual.getProximo();

        if (proximo == null && atual.getNome().equals(simbolo.getNome())) {
            simbolos[indexSimbolo] = null;
            return true;

        } else if (proximo == null && !atual.getNome().equals(simbolo.getNome())) {
            return false;

        } else {
            do {
                if (proximo.getNome().equals(simbolo.getNome())) {
                    atual.setProximo(proximo.getProximo());
                    return true;
                }
            } while (possuiProximo(proximo));

            return false;
        }
    }

    public static Simbolo buscar(Simbolo simbolo) {
        int indexSimbolo = hash(simbolo.getNome(), TAMANHO_TABELA);

        Simbolo busca = simbolos[indexSimbolo];

        if (busca.getNome().equals(simbolo.getNome())) {
            return busca;

        } else {
            do {
                busca = busca.getProximo();
            } while (possuiProximo(busca));

            return busca;
        }
    }

    private static boolean posicaoVazia(int index) {
        return simbolos[index] == null;
    }

    private static boolean possuiProximo(Simbolo simbolo) {
        return simbolo.getProximo() != null;
    }

}
