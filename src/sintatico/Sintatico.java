package sintatico;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import lexico.ConstantesTerminais;
import lexico.Token;
import util.Pilha;
import util.PilhaToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Sintatico {

    private FileReader file = null;
    private BufferedReader reader;
    private String line;
    private String resultadoSemantico;
    private List<Token> tokens;

    public Sintatico(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }
    }

    public Sintatico(List<Token> tokens) throws FileNotFoundException {
        this.tokens = tokens;
    }

    private void readLine() throws IOException {
        line = reader.readLine();
    }

    public String scanAll() throws IOException, AnalisadorSintaticoException, AnalisadorSemanticoException {
        StringBuilder resultado = new StringBuilder();
        PilhaToken simbolos = novaPilhaToken();
        PilhaToken entrada = new PilhaToken();
        List<Token> tokensDispostos = new ArrayList<>();

        //preenche a pilha de entrada de trás pra frente
        for (int i = tokens.size() - 1; i >= 0; i--) {
            entrada.empilhar(tokens.get(i));
            tokensDispostos.add(tokens.get(i));
        }

        Semantico semantico = new Semantico();

        do {
            //pega o simbolo do topo  pilha
            Token simboloTopoPilha = simbolos.exibeUltimoValor();

            while (simboloTopoPilha.tag == Constants.EPSILON) {
                simbolos.desempilhar();
                simboloTopoPilha = simbolos.exibeUltimoValor();
            }

            //verifica se o topo da pilha de simbolos é terminal
            if (isTerminal(simboloTopoPilha) || simbolos.pilhaVazia()) {
                //verifica se o topo da pilha de simbolos é igual ao topo da pilha de entrada
                if (simboloTopoPilha.tag == entrada.exibeUltimoValor().tag) {
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
                        simbolos.empilhar(new Token(regrasProducao[i]));
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
        for (Token token : simbolos.pilha) {
            if (token != null)
                resultado.append(token.toString() + " ");
        }

        resultado.append("\nEntrada");
        for (Token token : entrada.pilha) {
            if (token != null)
                resultado.append(token.toString() + " ");
        }

        return resultado.toString();
    }

    public String scanAll(Hashtable words, String resultadoSemantico) throws IOException, AnalisadorSintaticoException, AnalisadorSemanticoException {
        StringBuilder resultado = new StringBuilder();
        Pilha simbolos = novaPilha();
        Pilha entrada = new Pilha();
        Semantico semantico = new Semantico();

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

    private void lancarErro(Token simboloEsperado, Token simboloRecebido) throws AnalisadorSintaticoException {
        throw new AnalisadorSintaticoException(ParserConstants.PARSER_ERROR[simboloEsperado.tag - ParserConstants.FIRST_SEMANTIC_ACTION - ParserConstants.FIRST_NON_TERMINAL] + ", era esperado " + simboloEsperado.toString() + " porém foi recebido  " + simboloRecebido.toString());
    }

    private int[] obterRegrasProducao(int idMatrizParse) {
        return ParserConstants.PRODUCTIONS[idMatrizParse];
    }

    //primeiro parâmetro: tabela de símbolos, segundo valor pilha de entrada
    private boolean estaNaMatrizParse(int X, int a) {
        return obterMatrizParse(X, a) >= 0;
    }

    private boolean estaNaMatrizParse(Token X, Token a) {
        return obterMatrizParse(X, a) >= 0;
    }

    //primeiro parâmetro: tabela de símbolos, segundo valor pilha de entrada
    private int obterMatrizParse(int X, int a) {
        return Constants.PARSER_TABLE[X - ParserConstants.START_SYMBOL][a - 1];
    }

    private int obterMatrizParse(Token X, Token a) {
        return Constants.PARSER_TABLE[X.tag - ParserConstants.START_SYMBOL][a.tag - 1];
    }

    //Inicia a pilha de simbolos com
    private Pilha novaPilha() {
        Pilha pilha = new Pilha();
        pilha.empilhar(Constants.DOLLAR);
        pilha.empilhar(ParserConstants.START_SYMBOL);
        pilha.empilhar(Constants.EPSILON);
        return pilha;
    }

    private PilhaToken novaPilhaToken() {
        PilhaToken pilha = new PilhaToken();
        pilha.empilhar(ConstantesTerminais.FIM_ARQUIVO);
        pilha.empilhar(new Token(ParserConstants.START_SYMBOL));
        pilha.empilhar(new Token(Constants.EPSILON));
        return pilha;
    }

    private boolean isTerminal(int token) {
        return token > Constants.DOLLAR && token < Constants.FIRST_NON_TERMINAL;
    }

    private boolean isTerminal(Token token) {
        return token.tag > Constants.DOLLAR && token.tag < Constants.FIRST_NON_TERMINAL;
    }

    private boolean isNaoTerminal(int token) {
        return token >= Constants.FIRST_NON_TERMINAL && token < Constants.FIRST_SEMANTIC_ACTION;
    }

    private boolean isNaoTerminal(Token token) {
        return token.tag >= Constants.FIRST_NON_TERMINAL && token.tag < Constants.FIRST_SEMANTIC_ACTION;
    }
}
