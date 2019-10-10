package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lexico.Lexico;
import sintatico.AnalisadorLexicoException;
import sintatico.AnalisadorSintaticoException;
import sintatico.Sintatico;
import util.Util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Controller {

    private File f;

    @FXML
    private TextArea resultado;

    @FXML
    private Text arquivo;

    @FXML
    public void analisar() {
        String resultado = "";
        try {
            Lexico lexico = new Lexico(new FileReader(f));
            resultado = resultado.concat(lexico.scanAll());
            Sintatico sintatico = new Sintatico(Util.RESULTADO_COMPILADOR_LEXICO);
            resultado = resultado.concat(sintatico.scanAll());

            this.resultado.setText(resultado);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (AnalisadorLexicoException e) {
            this.resultado.setText(resultado + "\n" + e.getMessage());

        } catch (AnalisadorSintaticoException e) {
            this.resultado.setText(resultado + "\n" + e.getMessage());
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
