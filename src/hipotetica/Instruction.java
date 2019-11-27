package hipotetica;

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar as informações
 * de uma instrução.
 * Esta classe, bem como as classes "AreaInstrucoes", "AreaLiterais"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
public class Instruction {

    public int codigo;
    public int op1;
    public int op2;

    /**
     * Construtor sem parâmetros.
     * Todos os atributos são inicializados com valores padrões.
     */
    public Instruction() {
        codigo = 0;
        op1 = 0;
        op2 = 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public Instruction setCodigo(int codigo) {
        this.codigo = codigo;
        return this;
    }

    public int getOp1() {
        return op1;
    }

    public Instruction setOp1(int op1) {
        this.op1 = op1;
        return this;
    }

    public int getOp2() {
        return op2;
    }

    public Instruction setOp2(int op2) {
        this.op2 = op2;
        return this;
    }

    public String getNomeCodigo() {
        switch (this.codigo) {
            case InstructionArea.RETU:
                return "RETU";
            case InstructionArea.CRVL:
                return "CRVL";
            case InstructionArea.CRCT:
                return "CRCT";
            case InstructionArea.ARMZ:
                return "ARMZ";
            case InstructionArea.SOMA:
                return "SOMA";
            case InstructionArea.SUBT:
                return "SUBT";
            case InstructionArea.MULT:
                return "MULT";
            case InstructionArea.DIVI:
                return "DIVI";
            case InstructionArea.INVR:
                return "INVR";
            case InstructionArea.NEGA:
                return "NEGA";
            case InstructionArea.CONJ:
                return "CONJ";
            case InstructionArea.DISJ:
                return "DISJ";
            case InstructionArea.CMME:
                return "CMME";
            case InstructionArea.CMMA:
                return "CMMA";
            case InstructionArea.CMIG:
                return "CMIG";
            case InstructionArea.CMDF:
                return "CMDF";
            case InstructionArea.CMEI:
                return "CMEI";
            case InstructionArea.CMAI:
                return "CMAI";
            case InstructionArea.DSVS:
                return "DSVS";
            case InstructionArea.DSVF:
                return "DSVF";
            case InstructionArea.LEIT:
                return "LEIT";
            case InstructionArea.IMPR:
                return "IMPR";
            case InstructionArea.IMPRL:
                return "IMPRL";
            case InstructionArea.AMEM:
                return "AMEM";
            case InstructionArea.CALL:
                return "CALL";
            case InstructionArea.PARA:
                return "PARA";
            case InstructionArea.COPY:
                return "COPY";
            case InstructionArea.DSVT:
                return "DSVT";
        }
        return String.valueOf(this.codigo);
    }
}