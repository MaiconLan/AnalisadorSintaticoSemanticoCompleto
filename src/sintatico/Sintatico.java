package sintatico;

import util.Pilha;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Sintatico {

    private FileReader file = null;
    private BufferedReader reader;
    private String line;

    public Sintatico(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
    }

    private void readLine() throws IOException {
        line = reader.readLine();
    }

    public String scanAll(Hashtable words, String resultadoSemantico) throws IOException, AnalisadorSintaticoException, AnalisadorSemanticoException {
        StringBuilder resultado = new StringBuilder();
        Pilha simbolos = novaPilha();
        Pilha entrada = new Pilha();
        Semantico semantico = new Semantico(words);

        readLine();
        String[] arrayEntrada = line.split(" ");
        //preenche a pilha de entrada de trás pra frente
        for (int i = arrayEntrada.length - 1; i >= 0; i--) {
            entrada.empilhar(Integer.valueOf(arrayEntrada[i]));
        }

        do {
            //pega o simbolo do topo  pilha
            int simboloTopoPilha = simbolos.exibeUltimoValor();

            while (simboloTopoPilha == Constants.EPSILON) {
                simbolos.desempilhar();
                simboloTopoPilha = simbolos.exibeUltimoValor();
            }

            //verifica se o topo da pilha de simbolos é terminal
            if (isTerminal(simboloTopoPilha) || simbolos.pilhaVazia()) {
                //verifica se o topo da pilha de simbolos é igual ao topo da pilha de entrada
                if (simboloTopoPilha == entrada.exibeUltimoValor()) {
                    resultado.append("Desempilhando: " + simbolos.desempilhar() + "-" + entrada.desempilhar() + "\n");

                } else {
                    // se for terminal e o topo dos simbolos for diferente do topo da pilha de entrada, gera erro
                    lancarErro(simboloTopoPilha, entrada.exibeUltimoValor());
                }

                //verifica se o topo da pilha de simbolos é não terminal
            } else if (isNaoTerminal(simboloTopoPilha)) {

                //verifica se o não terminal está na matriz de parse
                if (estaNaMatrizParse(simboloTopoPilha, entrada.exibeUltimoValor())) {
                    simbolos.desempilhar();
                    int idMatrizParse = obterMatrizParse(simboloTopoPilha, entrada.exibeUltimoValor());

                    // obtem as regras de produção de acordo com o valor retornado da matriz de parse
                    int[] regrasProducao = obterRegrasProducao(idMatrizParse);

                    // empilha de trás pra frente na pilha de símbolos os tokens
                    for (int i = regrasProducao.length - 1; i >= 0; i--) {
                        simbolos.empilhar(regrasProducao[i]);
                    }

                } else {

                    // aqui está só colocando os simbolos para mostrar na tela quando tiver erro
                    for (int i = 0; i < simbolos.tamanho(); i++) {
                        resultado.append(simbolos.pilha[i]);
                    }
                    lancarErro(simboloTopoPilha, entrada.exibeUltimoValor());
                }

            } else {
                semantico.tratarSemantico(simboloTopoPilha, entrada.exibePenultimoValor(), entrada.exibeAntepenultimoValor());
                resultadoSemantico += semantico.getResultadoSemantico();
                simbolos.desempilhar();
            }

        } while (!simbolos.pilhaVazia());

        resultado.append("\nSimbolos");
        for (Integer integer : simbolos.pilha) {
            if (integer != null)
                resultado.append(integer + " ");
        }

        resultado.append("\nEntrada");
        for (Integer integer : entrada.pilha) {
            if (integer != null)
                resultado.append(integer + " ");
        }

        return resultado.toString();
    }

    private void lancarErro(int simboloEsperado, int simboloRecebido) throws AnalisadorSintaticoException {
        throw new AnalisadorSintaticoException(ParserConstants.PARSER_ERROR[simboloEsperado - ParserConstants.FIRST_SEMANTIC_ACTION - ParserConstants.FIRST_NON_TERMINAL] + ", era esperado " + simboloEsperado + " porém foi recebido  " + simboloRecebido);
    }

    private int[] obterRegrasProducao(int idMatrizParse) {
        return ParserConstants.PRODUCTIONS[idMatrizParse];
    }

    //primeiro parâmetro: tabela de símbolos, segundo valor pilha de entrada
    private boolean estaNaMatrizParse(int X, int a) {
        return obterMatrizParse(X, a) >= 0;
    }

    //primeiro parâmetro: tabela de símbolos, segundo valor pilha de entrada
    private int obterMatrizParse(int X, int a) {
        return Constants.PARSER_TABLE[X - ParserConstants.START_SYMBOL][a - 1];
    }


    //Inicia a pilha de simbolos com
    private Pilha novaPilha() {
        Pilha pilha = new Pilha();
        pilha.empilhar(Constants.DOLLAR);
        pilha.empilhar(ParserConstants.START_SYMBOL);
        pilha.empilhar(Constants.EPSILON);
        return pilha;
    }

    private boolean isTerminal(int token) {
        return token > Constants.DOLLAR && token < Constants.FIRST_NON_TERMINAL;
    }

    private boolean isNaoTerminal(int token) {
        return token >= Constants.FIRST_NON_TERMINAL && token < Constants.FIRST_SEMANTIC_ACTION;
    }
}
