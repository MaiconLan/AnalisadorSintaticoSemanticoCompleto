package sintatico;

import hashweiss.Hashweiss;

public class Semantico {

    Hashweiss tabelaSimbolos = new Hashweiss();

    public void executaAcaoSemantica(Integer token) {

    }

    public void tratarSemantico(int simboloTopoPilha, Integer penultimoValor, Integer antepenultimoValor) {
        tabelaSimbolos.buscar(simboloTopoPilha);
    }
}
