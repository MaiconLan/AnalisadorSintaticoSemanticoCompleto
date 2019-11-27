package semantico;

import hashweiss.Hashweiss;
import hipotetica.Hipotetica;
import hipotetica.InstructionArea;
import lexico.Token;
import simbolos.Simbolo;
import sintatico.ParserConstants;
import util.HashweissException;

import java.util.Stack;

public class Semantico {

    private Hashweiss tabelaSimbolos = new Hashweiss();

    private Simbolo procedureAtual;

    private Stack<Simbolo> parametros;
    private Stack<Desvio> desvios;

    private int shift = 0;
    private int variavel = 0;
    private int nivelAtual = 0;
    private int posicaoLivre = 0;
    private int numeroParametro = 0;
    private boolean temParametros;

    private String tipoIdentificador;
    private String resultadoSemantico = "";

    public Hipotetica hipotetica;

    public Semantico() {
    }

    public void executaAcaoSemantica(int acaoSemantica, int penultimoValor, int antepenultimoValor) throws AnalisadorSemanticoException {
    }

    public void executaAcaoSemantica(Token acaoSemantica, Token penultimoValor, Token antepenultimoValor) throws AnalisadorSemanticoException {
        int numeroAcaoSemantica = acaoSemantica.tag - ParserConstants.FIRST_SEMANTIC_ACTION;

        switch (numeroAcaoSemantica) {
            // #100  Inicialização de variáveis de controle utilizadas durante toda a análise semântica...
            case 100:
                acaoSemantica100();
                break;

            // Gera instrução AMEM utilizando como base o número de ações acumuladas na ação #104
            case 102:
                acaoSemantica102();
                break;

            // #104  Insere nome na tabela de símbolos...
            case 104:
                acaoSemantica104(numeroAcaoSemantica, penultimoValor, antepenultimoValor);
                break;

            // #107  Seta tipo de identificador = VARIÁVEL
            case 107:
                acaoSemantica107(numeroAcaoSemantica, penultimoValor, antepenultimoValor);
                break;

            // #108: Após nome de procedure, em declaração faz:
            // -categoria := proc
            // -inserção
            // -houve_parametros := false
            // -n_par := 0
            // -incrementa nível (Nível_atual:= nível_atual + 1)
            case 108:
                acaoSemantica108(numeroAcaoSemantica, penultimoValor, antepenultimoValor);
                break;

            case 109:
                acaoSemantica109(numeroAcaoSemantica, penultimoValor, antepenultimoValor);
                break;

            default:
                lancarErro("Ação Semantica não mapeada: " + numeroAcaoSemantica + "\n");
        }
        resultadoSemantico = "Executada Ação " + numeroAcaoSemantica + "\n";
    }

    private void acaoSemantica109(int numeroAcaoSemantica, Token penultimoValor, Token antepenultimoValor) {
        if (temParametros) {
            procedureAtual.setGeralB(numeroParametro);

            for (int i = 1; i <= numeroParametro; i++) {
                Simbolo simbolo = parametros.pop();
                simbolo.setGeralA(i * -1);
            }
        }

        hipotetica.addInstruction(InstructionArea.DSVS, -1, -1);
        Desvio desvio = new Desvio();
        desvio.ponteiro = hipotetica.intructionArea.LC;
        desvio.parametro = numeroParametro;
        desvios.add(desvio);
    }

    private void acaoSemantica108(int numeroAcaoSemantica, Token penultimoValor, Token antepenultimoValor) {
        //categoria := proc
        //inserção
        //houve_parametros := false
        //n_par := 0
        //incrementa nível (Nível_atual:= nível_atual + 1)

        Simbolo simbolo = new Simbolo(penultimoValor.toString(), Simbolo.PROCEDURE, this.nivelAtual, hipotetica.intructionArea.LC + 1, -1);
        tabelaSimbolos.adicionar(simbolo);

        procedureAtual = simbolo;
        temParametros = Boolean.FALSE;
        numeroParametro = 0;
        nivelAtual++;
        shift = 3;
    }

    private void acaoSemantica102() {
        shift = 3;
        hipotetica.addInstruction(InstructionArea.AMEM, -1, shift + variavel);
        variavel = 0;
    }

    private void acaoSemantica104(int acaoSemantica, Token penultimoValor, Token antepenultimoValr) throws AnalisadorSemanticoException {
        try {
            Simbolo penultimoSimbolo = tokenToSimbolo(penultimoValor);

            if (tipoIdentificador.equals(Simbolo.VARIAVEL)) {
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

            } else if (tipoIdentificador.equals(Simbolo.PARAMETRO)) {
                Simbolo simbolo = new Simbolo(penultimoSimbolo.getNome(), Simbolo.PARAMETRO, nivelAtual, -1, -1);
                tabelaSimbolos.adicionar(simbolo);
                numeroParametro++;
                parametros.add(simbolo);
            }
        } catch (HashweissException e) {
            lancarErro(e.getMessage());
        }
    }

    private void acaoSemantica107(int acaoSemantica, Token penultimoValor, Token antepenultimoValr) {
        this.tipoIdentificador = Simbolo.VARIAVEL;
    }

    private void acaoSemantica100() {
        parametros = new Stack<>();
        hipotetica = new Hipotetica();
        desvios = new Stack<>();

        shift = 3;
        variavel = 0;
        nivelAtual = 0;
        posicaoLivre = 1;
        numeroParametro = 0;
    }

    public void tratarSemantico(Token simboloTopoPilha, Token penultimoValor, Token antepenultimoValor) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha, penultimoValor, antepenultimoValor);
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
}
