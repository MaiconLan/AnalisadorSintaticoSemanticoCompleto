package sintatico;

import hashweiss.Hashweiss;
import lexico.Token;
import simbolos.Simbolo;
import util.HashweissException;
import util.Pilha;

import java.util.Hashtable;

public class Semantico {

    private Hashweiss tabelaSimbolos = new Hashweiss();

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

    private String tipoIdentificador;

    private String resultadoSemantico = "";

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

            default:
                lancarErro("Ação Semantica não mapeada: " + numeroAcaoSemantica + "\n");
        }
        resultadoSemantico = "Executada Ação " + acaoSemantica + "\n";
    }

    private void acaoSemantica102() {

    }

    private void acaoSemantica104(int acaoSemantica, Token penultimoValor, Token antepenultimoValr) throws AnalisadorSemanticoException {
        try {
            if (tipoIdentificador.equals("VARIAVEL")) {
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
        this.ifs = new Pilha();
        this.fors = new Pilha();
        this.whiles = new Pilha();
        this.parametros = new Pilha();
        this.cases = new Pilha();
        this.repeats = new Pilha();
        this.procedures = new Pilha();
        this.nivel = 0;
        this.deslocamento = 0;
        this.nivelAtual = 0;
    }

    public void tratarSemantico(Token simboloTopoPilha, Token penultimoValor, Token antepenultimoValor) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha, penultimoValor, antepenultimoValor);
    }

    public void tratarSemantico(int simboloTopoPilha, Integer penultimoValor, Integer antepenultimoValor) throws AnalisadorSemanticoException {
        executaAcaoSemantica(simboloTopoPilha - ParserConstants.FIRST_SEMANTIC_ACTION, penultimoValor, antepenultimoValor);

        //  if(simbolo == null ){
        //      throw new AnalisadorSemanticoException("Simbolo não encontrado!");
        //  }
        //  try {
        //      tabelaSimbolos.buscar(simbolo);
        //  } catch (HashweissException e) {
        //      throw new AnalisadorSemanticoException(e.getMessage());
        //  }
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
