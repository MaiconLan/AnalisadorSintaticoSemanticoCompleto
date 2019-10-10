package hashweiss;

import simbolos.Simbolo;
import util.HashweissException;

public class Hashweiss {

    /*
    PARA RODAR, BASTA EXECUTAR O MÉTODO MAIN
    Foram criados os métodos casoDeTeste() e casoDeTeste2() para realizar alguns testes;

    O método do trabalho é trabalho2(); que está sendo chamado no main!!!!
     */

    private static final int TAMANHO_TABELA = 25143;
    private static Simbolo[] simbolos = new Simbolo[TAMANHO_TABELA];  //Inicialização

    public static void main(String[] args) {
        try {
            trabalho2();
        } catch (HashweissException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void trabalho2() throws HashweissException {

        Simbolo simbolo = new Simbolo();
        simbolo.setNome("Token");

        Simbolo simbolo2 = new Simbolo();
        simbolo2.setNome("Token2");

        Simbolo simbolo3 = new Simbolo();
        simbolo3.setNome("Token3");

        Simbolo simbolo4 = new Simbolo();
        simbolo4.setNome("Token");

        Simbolo simbolo5 = new Simbolo();
        simbolo5.setNome("Token5");

        Simbolo simbolo6 = new Simbolo();
        simbolo6.setNome("Token");

        Simbolo simbolo7 = new Simbolo();
        simbolo7.setNome("Token7");

        Simbolo simbolo8 = new Simbolo();
        simbolo8.setNome("Token8");

        Simbolo simbolo9 = new Simbolo();
        simbolo9.setNome("Token9");

        Simbolo simbolo10 = new Simbolo();
        simbolo10.setNome("Token10");

        Simbolo simbolo11 = new Simbolo();
        simbolo11.setNome("Token11");

        //Inserir 10 elementos;
        adicionar(simbolo);
        adicionar(simbolo2);
        adicionar(simbolo3);
        adicionar(simbolo4);
        adicionar(simbolo5);
        adicionar(simbolo6);
        adicionar(simbolo7);
        adicionar(simbolo8);
        adicionar(simbolo9);
        adicionar(simbolo10);
        adicionar(simbolo11);

        //Mostrar conteúdo da tabela;
        mostraConteudoTabela();

        // Alterar dados de 5 elementos;
        Simbolo simboloAtualizado = new Simbolo();
        simboloAtualizado.setNome("Novo Simbolo");
        atualizar(simbolo, simboloAtualizado);
        atualizar(simbolo2, simboloAtualizado);
        atualizar(simbolo3, simboloAtualizado);
        atualizar(simbolo4, simboloAtualizado);
        atualizar(simbolo5, simboloAtualizado);

        //Mostrar conteúdo da tabela;
        mostraConteudoTabela();

        // Excluir 3 elementos;
        remover(simbolo6);
        remover(simbolo7);
        remover(simbolo8);

        //Mostrar conteúdo da tabela;
        mostraConteudoTabela();

        // Fazer uma busca por nome de 3 elementos que estão na tabela.
        // Mostrar os dados completos dos elementos encontrados.

        System.out.println("-----------------------------------------------------------------------");
        mostrarDadosCompletos(buscar(simbolo9));
        mostrarDadosCompletos(buscar(simbolo10));
        mostrarDadosCompletos(buscar(simbolo11));

        //  Fazer uma busca por 1 elemento inexistente na tabela.
        //  Mostrar mensagem informando que o elemento não foi encontrado;
        Simbolo simboloNaoExistente = new Simbolo();
        simboloNaoExistente.setNome("Novo símbo que não existe");
        buscar(simboloNaoExistente);
    }

    public static void mostrarDadosCompletos(Simbolo simbolo) {
        System.out.println("--------Símbolo: " + simbolo.getNome());
        System.out.println("--------Próximo: " + (possuiProximo(simbolo) ? simbolo.getProximo().getNome() : " null"));
        System.out.println("--------Nível: " + simbolo.getNivel());
        System.out.println("--------Categoria: " + simbolo.getCategoria());
        System.out.println("--------GeralA: " + simbolo.getGeralA());
        System.out.println("--------GeralB: " + simbolo.getGeralB());
        System.out.println("-----------------------------------------------------------------------");
    }

    public static void atualizar(Simbolo simbolo, Simbolo novoSimbolo) throws HashweissException {
        System.out.println("--------------Atualizando símbolo " + simbolo.getNome() + "--------------");
        int indexSimbolo = hash(simbolo.getNome(), TAMANHO_TABELA);

        Simbolo atual = simbolos[indexSimbolo];
        Simbolo proximo = atual.getProximo();

        if (proximo == null && atual.getNome().equals(simbolo.getNome())) {
            simbolos[indexSimbolo] = novoSimbolo;

        } else if (proximo == null && !atual.getNome().equals(simbolo.getNome())) {
            throw new HashweissException("Não foi encontrado o Símbolo \"" + simbolo.getNome() + "\" para atualizar!");

        } else {
            do {
                atual = proximo;
                proximo = atual.getProximo();
                if (proximo.getNome().equals(simbolo.getNome())) {
                    atual.setProximo(novoSimbolo);
                    novoSimbolo.setProximo(proximo);
                    return;
                }
            } while (possuiProximo(proximo));

            throw new HashweissException("Não foi encontrado o Símbolo \"" + simbolo.getNome() + "\" para atualizar!");
        }
    }

    public static void mostraConteudoTabela() {
        System.out.println("--------------Conteudo Tabela--------------");
        for (Simbolo simbolo : simbolos) {
            if (simbolo != null) {
                System.out.println(simbolo.getNome());

                Simbolo proximo = simbolo.getProximo();
                while (proximo != null) {
                    System.out.println(simbolo.getNome());
                    proximo = proximo.getProximo();
                }
            }
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
        System.out.println("-->Adicionando símbolo " + simbolo.getNome() + "<--");

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

    public static void remover(Simbolo simbolo) throws HashweissException {
        System.out.println("-->Removendo símbolo " + simbolo.getNome() + "<--");
        int indexSimbolo = hash(simbolo.getNome(), TAMANHO_TABELA);

        Simbolo atual = simbolos[indexSimbolo];
        Simbolo proximo = atual.getProximo();

        if (proximo == null && atual.getNome().equals(simbolo.getNome())) {
            simbolos[indexSimbolo] = null;

        } else if (proximo == null && !atual.getNome().equals(simbolo.getNome())) {
            throw new HashweissException("Não foi encontrado o Símbolo \"" + simbolo.getNome() + "\" para remover!");

        } else {
            do {
                atual = proximo;
                proximo = atual.getProximo();
                if (proximo.getNome().equals(simbolo.getNome())) {
                    atual.setProximo(proximo.getProximo());
                    return;
                }
            } while (possuiProximo(proximo));

            throw new HashweissException("Não foi encontrado o Símbolo \"" + simbolo.getNome() + "\" para remover!");
        }
    }

    public static Simbolo buscar(Simbolo simbolo) throws HashweissException {
        System.out.println("-->Buscando símbolo \"" + simbolo.getNome() + "\"<--");
        int indexSimbolo = hash(simbolo.getNome(), TAMANHO_TABELA);

        Simbolo busca = simbolos[indexSimbolo];

        if (busca == null) {
            throw new HashweissException("Não foi encontrado o Símbolo \"" + simbolo.getNome() + "\" para buscar!");
        }

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

    /*
        MÉTODOS DE TESTES DAS FUNCIONALIDADES!!!!!
     */
    public static void casoDeTeste2() throws HashweissException {
        Simbolo simbolo = new Simbolo();
        simbolo.setNome("Token");

        Simbolo simbolo2 = new Simbolo();
        simbolo2.setNome("Token 2");

        Simbolo editado = new Simbolo();
        editado.setNome("Editado");

        adicionar(simbolo);
        adicionar(simbolo2);

        atualizar(simbolo2, editado);

        mostraConteudoTabela();

        remover(simbolo);

        mostraConteudoTabela();
    }

    public static void casoDeTeste() throws HashweissException {

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
}
