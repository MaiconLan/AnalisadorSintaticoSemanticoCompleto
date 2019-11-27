package semantico;

import hashweiss.Hashweiss;
import hipotetica.Hipotetica;
import hipotetica.InstructionArea;
import lexico.Token;
import simbolos.Simbolo;
import sintatico.ParserConstants;
import sun.awt.Symbol;
import util.HashweissException;
import util.Pilha;

public class Semantico {

    private Hashweiss tabelaSimbolos = new Hashweiss();

    private Simbolo procedureAtual;

    private Pilha ifs;
    private Pilha fors;
    private Pilha whiles;
    private Pilha parametros;
    private Pilha cases;
    private Pilha repeats;
    private Pilha procedures;

    private int nivel = 0;
    private int deslocamento = 0;
    private int nivelAtual = 0;
    private int variavel = 0;
    private int shift = 0;
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
        resultadoSemantico = "Executada Ação " + acaoSemantica + "\n";
    }

    private void acaoSemantica109(int numeroAcaoSemantica, Token penultimoValor, Token antepenultimoValor) {
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
            if (tipoIdentificador.equals(Simbolo.VARIAVEL)){
                Simbolo penultimoSimbolo = getSimbolo(penultimoValor);
                Simbolo simboloBusca = this.tabelaSimbolos.buscar(penultimoSimbolo);
                if (simboloBusca == null) {
                    Simbolo simbolo = new Simbolo(penultimoSimbolo.getNome(), "VARIAVEL", this.nivelAtual, this.deslocamento, 0);

                    tabelaSimbolos.adicionar(simbolo);
                    deslocamento += 1;
                    nivel += 1;
                } else {
                    lancarErro("Erro semântico\nVariável \"" + penultimoSimbolo.getNome() + "\" já foi declarada");
                }
            }
        } catch (HashweissException e) {
            lancarErro(e.getMessage());
        }
    }

    private void acaoSemantica107(int acaoSemantica, Token penultimoValor, Token antepenultimoValr) {
        this.tipoIdentificador = "VARIAVEL";
        this.nivel = 0;
    }

    private void acaoSemantica100() {
        ifs = new Pilha();
        fors = new Pilha();
        whiles = new Pilha();
        parametros = new Pilha();
        cases = new Pilha();
        repeats = new Pilha();
        procedures = new Pilha();
        hipotetica = new Hipotetica();
        nivel = 0;
        deslocamento = 0;
        nivelAtual = 0;
        variavel = 0;
        shift = 3;
    }

    public void tratarSemantico(Token simboloTopoPilha, Token penultimoValor, Token antepenultimoValor) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha, penultimoValor, antepenultimoValor);
    }

    public void tratarSemantico(int simboloTopoPilha, Integer penultimoValor, Integer antepenultimoValor) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha - ParserConstants.FIRST_SEMANTIC_ACTION, penultimoValor, antepenultimoValor);
    }

    private Simbolo getSimbolo(Token token) {
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
