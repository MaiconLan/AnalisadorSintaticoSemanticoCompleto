package hipotetica;

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar a área de
 * instruções.
 * Esta classe, bem como as classes "Tipos", "AreaLiterais"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
public class InstructionArea {

    public static final int RETU = 1;
    public static final int CRVL = 2;
    public static final int CRCT = 3;
    public static final int ARMZ = 4;
    public static final int SOMA = 5;
    public static final int SUBT = 6;
    public static final int MULT = 7;
    public static final int DIVI = 8;
    public static final int INVR = 9;
    public static final int NEGA = 10;
    public static final int CONJ = 11;
    public static final int DISJ = 12;
    public static final int CMME = 13;
    public static final int CMMA = 14;
    public static final int CMIG = 15;
    public static final int CMDF = 16;
    public static final int CMEI = 17;
    public static final int CMAI = 18;
    public static final int DSVS = 19;
    public static final int DSVF = 20;
    public static final int LEIT = 21;
    public static final int IMPR = 22;
    public static final int IMPRL = 23;
    public static final int AMEM = 24;
    public static final int CALL = 25;
    public static final int PARA = 26;
    public static final int COPY = 28;
    public static final int DSVT = 29;

    public Instruction instructions[] = new Instruction[1000];

    public int LC;

    /**
     * Construtor sem parâmetros.
     * Todos os atributos são inicializados com valores padrões.
     */
    public InstructionArea() {
        for (int i = 0; i < 1000; i++) {
            instructions[i] = new Instruction();
        }

        this.LC = 0;
    }
}