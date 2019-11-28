package semantico;

import hashweiss.Hashweiss;
import hipotetica.Hipotetica;
import hipotetica.InstructionArea;
import lexico.Token;
import simbolos.Simbolo;
import sintatico.ParserConstants;
import util.HashweissException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AnalisadorSemantico {

    private static final String CONTEXTO_EXPRESSAO = "CONTEXTO_EXPRESSAO";
    private static final String CONTEXTO_READLN = "CONTEXTO_READLN";

    public List<Token> tokensTerminais;
    private Hashweiss tabelaSimbolos = new Hashweiss();
    private Stack<Simbolo> parametros;
    private Stack<Desvio> desvios;
    private Stack<Integer> ifs;
    private Simbolo procedureAtual;
    private Simbolo parteEsquerda;

    private int shift = 0;
    private int variavel = 0;
    private int nivelAtual = 0;
    private int posicaoLivre = 0;
    private int numeroParametros = 0;
    private int numeroParametrosEfetivos = 0;
    private boolean temParametros;
    private String contexto;

    private Token ultimoToken;

    private String tipoIdentificador;
    private String resultadoSemantico = "";

    public Hipotetica hipotetica;

    public AnalisadorSemantico() {
        tokensTerminais = new ArrayList<>();
    }

    public void executaAcaoSemantica(int acaoSemantica, int penultimoValor, int antepenultimoValor) throws AnalisadorSemanticoException {
    }

    public void executaAcaoSemantica(Token acaoSemantica) throws AnalisadorSemanticoException {
        int numeroAcaoSemantica = acaoSemantica.tag - ParserConstants.FIRST_SEMANTIC_ACTION;

        switch (numeroAcaoSemantica) {
            // #100  Inicialização de variáveis de controle utilizadas durante toda a análise semântica...
            case 100:
                acaoSemantica100();
                break;

            // Gera instrução AMEM utilizando como base o número de ações acumuladas na ação #104
            case 101:
                acaoSemantica101();
                break;

            // Gera instrução AMEM utilizando como base o número de ações acumuladas na ação #104
            case 102:
                acaoSemantica102();
                break;

            // #104  Insere nome na tabela de símbolos...
            case 104:
                acaoSemantica104();
                break;

            // #107  Seta tipo de identificador = VARIÁVEL
            case 107:
                acaoSemantica107();
                break;

            // #108: Após nome de procedure, em declaração faz:
            // -categoria := proc
            // -inserção
            // -houve_parametros := false
            // -n_par := 0
            // -incrementa nível (Nível_atual:= nível_atual + 1)
            case 108:
                acaoSemantica108();
                break;

            case 109:
                acaoSemantica109();
                break;

            case 110:
                acaoSemantica110();
                break;

            case 114:
                acaoSemantica114();
                break;

            case 115:
                acaoSemantica115();
                break;

            case 116:
                acaoSemantica116();
                break;

            case 117:
                acaoSemantica117();
                break;

            case 118:
                acaoSemantica118();
                break;

            case 120:
                acaoSemantica120();
                break;

            case 121:
                acaoSemantica121();
                break;

            case 122:
                acaoSemantica122();
                break;

            case 128:
                acaoSemantica128();
                break;

            case 129:
                acaoSemantica129();
                break;

            case 130:
                acaoSemantica130();
                break;

            case 131:
                acaoSemantica131();
                break;

            case 141:
                acaoSemantica141();
                break;

            case 143:
                acaoSemantica143();
                break;

            case 148:
                acaoSemantica148();
                break;

            case 149:
                acaoSemantica149();
                break;
                
            case 156:
                acaoSemantica156();
                break;

            default:
                lancarErro("ação semântica não mapeada: " + numeroAcaoSemantica + "\n");
        }
        resultadoSemantico = "Executada Ação " + numeroAcaoSemantica + "\n";
    }

    private void acaoSemantica101() {
        this.hipotetica.addInstruction(InstructionArea.PARA, -1, -1);
    }

    private void acaoSemantica131() {
        hipotetica.addInstruction(InstructionArea.IMPR, -1, -1);
    }

    private void acaoSemantica130() {
        hipotetica.addLiteralArea(ultimoToken.descricao);
        hipotetica.addInstruction(InstructionArea.IMPRL, -1, hipotetica.instructionliteralarea.LIT - 1);
    }

    private void acaoSemantica143() {
        hipotetica.addInstruction(InstructionArea.CMMA, -1, -1);
    }

    private void acaoSemantica110() throws AnalisadorSemanticoException {
        Desvio desvio = desvios.pop();
        this.hipotetica.addInstruction(InstructionArea.RETU, -1, desvio.parametro);
        this.hipotetica.intructionArea.instructions[desvio.ponteiro - 1].op2 = this.hipotetica.intructionArea.LC;
        // deleta nomes do escopo do nível na TS;
        try {
            tabelaSimbolos.removerPorNivel(nivelAtual);
        } catch (HashweissException e) {
            e.printStackTrace();
            throw new AnalisadorSemanticoException(e.getMessage());
        }
        nivelAtual--;
    }

    private void acaoSemantica121() {
        hipotetica.alterInstruction(ifs.pop(), 0, hipotetica.intructionArea.LC + 1);
        ifs.add(hipotetica.intructionArea.LC);
    }

    private void acaoSemantica118() {
        numeroParametrosEfetivos++;
    }

    private void acaoSemantica117() throws AnalisadorSemanticoException {
        if (numeroParametrosEfetivos > 0 && procedureAtual.getGeralB() != numeroParametrosEfetivos) {
            throw new AnalisadorSemanticoException("Erro na ação semântica 117: Número de parâmetros diferente do número de parâmetros efetivos!");
        } else {
            hipotetica.addInstruction(InstructionArea.CALL, -1, procedureAtual.getGeralA());
            numeroParametrosEfetivos = 0;
        }
    }

    private void acaoSemantica116() throws AnalisadorSemanticoException {
        try {
            Simbolo simbolo = tabelaSimbolos.buscar(tokenToSimbolo(ultimoToken));
            if (Simbolo.PROCEDURE.equals(simbolo.getCategoria())) {
                procedureAtual = simbolo;
            } else {
                throw new AnalisadorSemanticoException("Erro na ação semântica 116: Símbolo " + simbolo.getNome() + " não é uma procedure!");
            }
        } catch (HashweissException e) {
            e.printStackTrace();
            throw new AnalisadorSemanticoException(e.getMessage());
        }
    }

    private void acaoSemantica149() {
        hipotetica.addInstruction(InstructionArea.SUBT, -1, -1);
    }

    private void acaoSemantica122() {
        hipotetica.alterInstruction(ifs.pop(), 0, hipotetica.intructionArea.LC + 1);
        hipotetica.addInstruction(InstructionArea.DSVS, 0, 0);
        ifs.add(hipotetica.intructionArea.LC - 1);
    }

    private void acaoSemantica115() {
        this.hipotetica.addInstruction(InstructionArea.ARMZ, nivelAtual - parteEsquerda.getNivel(), parteEsquerda.getGeralA());
    }

    private void acaoSemantica148() {
        hipotetica.addInstruction(InstructionArea.SOMA, -1, -1);
    }

    private void acaoSemantica114() throws AnalisadorSemanticoException {
        try {
            Simbolo simbolo = tabelaSimbolos.buscar(tokenToSimbolo(ultimoToken));

            if (simbolo == null) {
                throw new AnalisadorSemanticoException("Erro na ação semântica 114: O Símbolo " + ultimoToken.toString() + " não foi declarado!");
            }

            if (Simbolo.VARIAVEL.equals(simbolo.getCategoria())) {
                parteEsquerda = simbolo;
            } else {
                throw new AnalisadorSemanticoException("Erro na ação semântica 114: O Símbolo " + simbolo.getNome() + " é uma variável");
            }
        } catch (HashweissException e) {
            e.printStackTrace();
            throw new AnalisadorSemanticoException(e.getMessage());
        }
    }

    private void acaoSemantica120() {
        this.hipotetica.addInstruction(InstructionArea.DSVF, -1, -1);
        ifs.add(hipotetica.intructionArea.LC - 1);
    }

    private void acaoSemantica141() {
        hipotetica.addInstruction(InstructionArea.CMIG, -1, -1);
    }

    private void acaoSemantica156() {
        contexto = CONTEXTO_EXPRESSAO;
    }

    private void acaoSemantica129() throws AnalisadorSemanticoException {
        try {
            Simbolo simbolo = tabelaSimbolos.buscar(tokenToSimbolo(ultimoToken));

            if (CONTEXTO_READLN.equals(contexto)) {
                if (Simbolo.VARIAVEL.equals(simbolo.getCategoria())) {
                    this.hipotetica.addInstruction(InstructionArea.LEIT, -1, -1);
                    this.hipotetica.addInstruction(InstructionArea.ARMZ, nivelAtual - simbolo.getNivel(), simbolo.getGeralA());
                } else {
                    throw new AnalisadorSemanticoException("Erro na ação semântica 129: O Símbolo " + simbolo.getNome() + " não é uma variável!");
                }

            } else if (CONTEXTO_EXPRESSAO.equals(contexto)) {
                if (Simbolo.PROCEDURE.equals(simbolo.getCategoria())) {
                    throw new AnalisadorSemanticoException("Erro na ação semântica 129: Símbolo " + simbolo.getNome() + " é uma procedure");
                }
                if (Simbolo.CONSTANTE.equals(simbolo.getCategoria())) {
                    this.hipotetica.addInstruction(InstructionArea.CRCT, 0, simbolo.getGeralA());
                } else {
                    this.hipotetica.addInstruction(InstructionArea.CRVL, nivelAtual - simbolo.getNivel(), simbolo.getGeralA());
                }
            }
        } catch (HashweissException e) {
            e.printStackTrace();
            throw new AnalisadorSemanticoException(e.getMessage());
        }
    }

    private void acaoSemantica128() {
        contexto = CONTEXTO_READLN;
    }

    private void acaoSemantica109() {
        if (temParametros) {
            procedureAtual.setGeralB(numeroParametros);

            for (int i = 1; i <= numeroParametros; i++) {
                Simbolo simbolo = parametros.pop();
                simbolo.setGeralA(i * -1);
            }
        }

        hipotetica.addInstruction(InstructionArea.DSVS, -1, -1);
        Desvio desvio = new Desvio();
        desvio.ponteiro = hipotetica.intructionArea.LC;
        desvio.parametro = numeroParametros;
        desvios.add(desvio);
    }

    private void acaoSemantica108() {
        //categoria := proc
        //inserção
        //houve_parametros := false
        //n_par := 0
        //incrementa nível (Nível_atual:= nível_atual + 1)

        Simbolo simbolo = new Simbolo(ultimoToken.toString(), Simbolo.PROCEDURE, this.nivelAtual, hipotetica.intructionArea.LC + 1, -1);
        tabelaSimbolos.adicionar(simbolo);

        procedureAtual = simbolo;
        temParametros = Boolean.FALSE;
        numeroParametros = 0;
        nivelAtual++;
        shift = 3;
    }

    private void acaoSemantica102() {
        shift = 3;
        hipotetica.addInstruction(InstructionArea.AMEM, -1, shift + variavel);
        variavel = 0;
    }

    private void acaoSemantica104() throws AnalisadorSemanticoException {
        try {
            Simbolo penultimoSimbolo = tokenToSimbolo(ultimoToken);

            switch (tipoIdentificador) {
                case Simbolo.VARIAVEL:
                    Simbolo simboloBusca = tabelaSimbolos.buscar(penultimoSimbolo);

                    if (simboloBusca == null) {
                        Simbolo simbolo = new Simbolo(penultimoSimbolo.getNome(), Simbolo.VARIAVEL, nivelAtual, shift, 0);
                        simbolo.setGeralA(variavel + 2);

                        tabelaSimbolos.adicionar(simbolo);
                        shift++;
                        variavel++;

                    } else {
                        lancarErro("Erro semântico\nVariável \"" + penultimoSimbolo.getNome() + "\" já foi declarada");
                    }
                    break;
                case Simbolo.PARAMETRO:
                    Simbolo simbolo = new Simbolo(penultimoSimbolo.getNome(), Simbolo.PARAMETRO, nivelAtual, -1, -1);
                    tabelaSimbolos.adicionar(simbolo);
                    numeroParametros++;
                    parametros.add(simbolo);
                    break;

                default:
                    lancarErro("Ação semântica 104 possui simbolo não configurado!");
                    break;
            }
        } catch (HashweissException e) {
            lancarErro(e.getMessage());
        }
    }

    private void acaoSemantica107() {
        this.tipoIdentificador = Simbolo.VARIAVEL;
    }

    private void acaoSemantica100() {
        parametros = new Stack<>();
        hipotetica = new Hipotetica();
        desvios = new Stack<>();
        ifs = new Stack<>();
        tokensTerminais.clear();
        ultimoToken = null;
        procedureAtual = null;
        parteEsquerda = null;

        shift = 3;
        variavel = 0;
        nivelAtual = 0;
        posicaoLivre = 1;
        numeroParametros = 0;
        numeroParametrosEfetivos = 0;
    }

    public void tratarSemantico(Token simboloTopoPilha) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha);
    }

    @Deprecated
    public void tratarSemantico(int simboloTopoPilha, Integer penultimoValor, Integer antepenultimoValor) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha - ParserConstants.FIRST_SEMANTIC_ACTION, penultimoValor, antepenultimoValor);
    }

    private Simbolo tokenToSimbolo(Token token) {
        Simbolo simbolo = new Simbolo();
        simbolo.setNome(token.toString());
        simbolo.setCategoria(token.descricao);
        return simbolo;
    }

    private void lancarErro(String erro) throws AnalisadorSemanticoException {
        throw new AnalisadorSemanticoException(erro);
    }

    public String getResultadoSemantico() {
        return resultadoSemantico;
    }

    public void setUltimoToken(Token ultimoToken) {
        this.ultimoToken = ultimoToken;
        System.err.println("Token: " + ultimoToken.toString());
    }
}
