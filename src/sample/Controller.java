package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lexico.Lexico;
import sintatico.AnalisadorLexicoException;
import sintatico.AnalisadorSemanticoException;
import sintatico.AnalisadorSintaticoException;
import sintatico.Sintatico;
import util.Util;

import java.io.*;

public class Controller {

    private File f;

    @FXML
    private TextArea resultadoLexico;

    @FXML
    private TextArea resultadoSintatico;

    @FXML
    private TextArea resultadoSemantico;

    @FXML
    private TextArea codigo;

    @FXML
    private Text arquivo;

    @FXML
    public void analisar() {
        String resultadoLexico = "";
        String resultadoSintatico = "";
        String resultadoSemantico = "";
        try {
            if(f == null) {
                arquivo.setText("Selecione o Arquivo!!!!!!!!!");
                return;
            }
            FileReader fileReader = new FileReader(f);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linha = bufferedReader.readLine();
            String codigo = bufferedReader.readLine();
            while (linha != null) {
                codigo += linha + "\n";
                linha = bufferedReader.readLine();
            }
            this.codigo.setText(codigo);
            fileReader.close();

            Lexico lexico = new Lexico(new FileReader(f));
            resultadoLexico = lexico.scanAll();
            Sintatico sintatico = new Sintatico(Util.RESULTADO_COMPILADOR_LEXICO);
            resultadoSintatico = sintatico.scanAll(lexico.getWords(), resultadoSemantico);

            this.resultadoLexico.setText(resultadoLexico);
            this.resultadoSintatico.setText(resultadoSintatico);
            this.resultadoSemantico.setText(resultadoSemantico);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (AnalisadorLexicoException e) {
            this.resultadoLexico.setText(resultadoLexico + "\n" + e.getMessage());

        } catch (AnalisadorSintaticoException e) {
            this.resultadoLexico.setText(resultadoLexico);
            this.resultadoSintatico.setText(resultadoSintatico + "\n" + e.getMessage());
        } catch (AnalisadorSemanticoException e) {
            this.resultadoLexico.setText(resultadoLexico);
            this.resultadoSintatico.setText(resultadoSintatico);
            this.resultadoSemantico.setText(resultadoSemantico + "\n" + e.getMessage());
        }
    }

    @FXML
    public void abreArquivo() {
        f = selecionaImagem();
        if (f != null)
            arquivo.setText(f.getName());
        else
            arquivo.setText("Arquivo não selecionado!");
    }

    private File selecionaImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new
                FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        fileChooser.setInitialDirectory(new File(
                "C:\\"));
        File arquivo = fileChooser.showOpenDialog(null);

        try {
            if (arquivo != null) {
                return arquivo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
