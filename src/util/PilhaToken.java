package util;

import lexico.Token;

public class PilhaToken {

    public Token[] pilha;
    public int posicaoPilha;

    public PilhaToken() {
        this.posicaoPilha = -1;
        this.pilha = new Token[10000];
    }

    public boolean pilhaVazia() {
        return this.posicaoPilha == -1;
    }

    public int tamanho() {
        if (this.pilhaVazia())
            return 0;

        return this.posicaoPilha + 1;
    }

    public Token exibeUltimoValor() {
        if (this.pilhaVazia())
            return null;

        return this.pilha[this.posicaoPilha];
    }


    public Token exibePenultimoValor() {
        return this.pilha[this.posicaoPilha - 1];
    }


    public Token exibeAntepenultimoValor() {
        return this.pilha[this.posicaoPilha - 2];
    }

    public Object desempilhar() {
        //pop
        if (pilhaVazia()) {
            return null;
        }
        return this.pilha[this.posicaoPilha--];
    }

    public void empilhar(Token valor) {
        if (this.posicaoPilha < this.pilha.length - 1)
            this.pilha[++posicaoPilha] = valor;
    }

}