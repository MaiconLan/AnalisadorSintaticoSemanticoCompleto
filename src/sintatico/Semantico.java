package sintatico;

import hashweiss.Hashweiss;
import lexico.Token;
import simbolos.Simbolo;
import util.HashweissException;
import util.Pilha;

import java.util.Hashtable;
import java.util.Map;

public class Semantico {

    private Hashtable<String, Token> words;

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

    private String resultadoSemantico;

    public Semantico(Hashtable words) {
        this.words = words;
    }

    public void executaAcaoSemantica(int acaoSemantica, Integer penultimoValor, Integer antepenultimoValor) throws AnalisadorSemanticoException {
        switch (acaoSemantica) {
            // #100  Inicialização de variáveis de controle utilizadas
            //durante toda a análise semântica...
            case 100:
                acaoSemantica100(acaoSemantica);
                break;

            // #104  Insere nome na tabela de símbolos...
            case 104:
                acaoSemantica104(acaoSemantica, penultimoValor, antepenultimoValor);
                break;

            // #107  Seta tipo de identificador = VARIÁVEL
            case 107:
                acaoSemantica107(acaoSemantica, penultimoValor, antepenultimoValor);
                break;

            default:
                lancarErro("Ação Semantica não mapeada: " + acaoSemantica + "\n");
        }
        resultadoSemantico += "Executada Ação " + acaoSemantica + "\n";
    }

    private void acaoSemantica104(int acaoSemantica, Integer penultimoValor, Integer antepenultimoValr) throws AnalisadorSemanticoException {
        try {
            if (this.tipoIdentificador.equals("VARIAVEL")) {
                Simbolo penultimoSimbolo = getSimbolo(penultimoValor);
                Simbolo simboloBusca = this.tabelaSimbolos.buscar(penultimoSimbolo);
                if (simboloBusca == null) {
                    Simbolo simbolo = new Simbolo(penultimoSimbolo.getNome(), "VARIAVEL", this.nivelAtual, this.deslocamento, 0);
                    this.tabelaSimbolos.adicionar(simbolo);
                    this.deslocamento += 1;
                    this.nivel += 1;
                } else {
                    lancarErro("Erro semântico\nVariável \"" + penultimoSimbolo.getNome() + "\" já foi declarada");
                }
            }
        } catch (HashweissException e) {
            lancarErro(e.getMessage());
        }
    }

    private void acaoSemantica107(int acaoSemantica, Integer penultimoValor, Integer antepenultimoValr) {
        this.tipoIdentificador = "VARIAVEL";
        this.nivel = 0;
    }

    private void acaoSemantica100(int acaoSemantica) {
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

    private Simbolo getSimbolo(int s) {
        for (Map.Entry<String, Token> token : words.entrySet()) {
            if (token.getValue().tag == s) {
                Simbolo simbolo = new Simbolo();
                simbolo.setNome(token.getKey());
                simbolo.setCategoria(token.getValue().descricao);
                return simbolo;
            }
        }
        return null;
    }

    private void lancarErro(String erro) throws AnalisadorSemanticoException {
        throw new AnalisadorSemanticoException(erro);
    }

    public String getResultadoSemantico() {
        return resultadoSemantico;
    }
}
