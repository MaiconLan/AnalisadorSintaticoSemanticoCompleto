package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lexico.Lexico;
import lexico.Token;
import lexico.AnalisadorLexicoException;
import semantico.AnalisadorSemanticoException;
import sintatico.AnalisadorSintaticoException;
import sintatico.Sintatico;

import java.io.*;
import java.util.List;

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

            String codigo = bufferedReader.readLine() + "\n";
            String linha = bufferedReader.readLine();
            while (linha != null) {
                codigo += linha + "\n";
                linha = bufferedReader.readLine();
            }
            this.codigo.setText(codigo);
            fileReader.close();

            Lexico lexico = new Lexico(new FileReader(f));
            resultadoLexico = lexico.scanAll();
            List<Token> tokens = lexico.getTokens();
            Sintatico sintatico = new Sintatico(tokens);
            resultadoSintatico = sintatico.scanAll();

            this.resultadoLexico.setText(resultadoLexico);
            this.resultadoSintatico.setText(resultadoSintatico);
            this.resultadoSemantico.setText(sintatico.getResultadoSemantico());

        } catch (IOException e) {
            e.printStackTrace();

        } catch (AnalisadorLexicoException e) {
            this.resultadoLexico.setText(resultadoLexico + "\n" + e.getMessage());
            this.resultadoSintatico.setText("");
            this.resultadoSemantico.setText("");
        } catch (AnalisadorSintaticoException e) {
            this.resultadoLexico.setText(resultadoLexico);
            this.resultadoSintatico.setText(resultadoSintatico + "\n" + e.getMessage());
            this.resultadoSemantico.setText("");
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
