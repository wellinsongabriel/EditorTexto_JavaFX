package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller {

	@FXML
	private TextArea textArea;
	@FXML
	private MenuItem menuCopiar;
	@FXML
	private MenuItem menuSelecionarTudo;

	private File arquivoAtual;	
	private boolean arquivoAlterado = false;
	private Stage stage;
	private double tamanhoFonte = 8;
	private String modoAtual = "escuro";
	
	public void initialize() {
		textArea.textProperty().addListener((obs, valoraAntigo, valorNovo) -> observarAcoes());
	}
	
	private void observarAcoes() {

		menuSelecionarTudo.setDisable(textArea.getText().isEmpty());
		arquivoAlterado = true;
		definirNome(arquivoAtual, arquivoAlterado);
	}
	
	public void atualizarMenuCopiar() {
		menuCopiar.setDisable(textArea.getSelectedText().isEmpty());
	}

	public void salvarArquivo() {

		if (arquivoAtual != null) {
			escreverArquivo(arquivoAtual, textArea.getText());
		} else {
			salvarArquivoComo();
		}
	}

	private void escreverArquivo(File file, String conteudo) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(conteudo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salvarArquivoComo() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Salvar arquivo");

		fileChooser.getExtensionFilters().add(new ExtensionFilter("Arquivos de texto (*,.txt)", "*.txt"));

		fileChooser.setInitialFileName("nota.txt");

		arquivoAtual = fileChooser.showSaveDialog(textArea.getScene().getWindow());

		if (arquivoAtual != null)
			escreverArquivo(arquivoAtual, textArea.getText());

	}

	public void abrirArquivo() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Escolha um arquivo");

		fileChooser.getExtensionFilters().add(new ExtensionFilter("Arquivos de texto (*,.txt)", "*.txt"));

		arquivoAtual = fileChooser.showOpenDialog(textArea.getScene().getWindow());

		if (arquivoAtual != null) {

			String conteudoArquivo = lerConteudo(arquivoAtual);
			
			textArea.setText(conteudoArquivo);
			arquivoAlterado = false;
			definirNome(arquivoAtual, arquivoAlterado);
		}

	}

	private void definirNome(File arquivoAtual, boolean arquivoAlterado) {
		
		if(arquivoAtual!=null)
			stage.setTitle((arquivoAlterado ? "*" : "")+ arquivoAtual.getName());
		else
			stage.setTitle((arquivoAlterado ? "*" : "")+ "Sem título");
			
	}

	private String lerConteudo(File arquivoAtual) {
		StringBuilder conteudo = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(arquivoAtual))) {
			String linha;

			while ((linha = reader.readLine()) != null) {
				conteudo.append(linha).append("\n");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conteudo.toString();
	}

	public void novoArquivo() {

		if(arquivoAlterado) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Salvando");
			alert.setContentText("Deseja salvar as alterações em "+
			(arquivoAtual==null? "Sem título": arquivoAtual.getName()) + " ?");
			ButtonType btnSalvar = new ButtonType("Salvar");
			ButtonType btnNaoSalvar = new ButtonType("Não Salvar");
			alert.getButtonTypes().setAll(btnSalvar, btnNaoSalvar);
			
			alert.showAndWait().ifPresent(resposta ->{
				if(resposta == btnSalvar) {
					salvarArquivo();
					
					resetarInformacoes();
				}else if(resposta == btnNaoSalvar) {
					resetarInformacoes();
				}
			});
		}else {
			resetarInformacoes();
		}
	

	}
		
	public void onScroll(ScrollEvent evento) {
		
		if(evento.isControlDown()) {
			if(evento.getDeltaY() > 0) {
				tamanhoFonte += 2;
			}else {
				tamanhoFonte = Math.max(8, tamanhoFonte -2);
			}
			
			textArea.setStyle(getModoAtual() + "-fx-font-size: "+tamanhoFonte +" pt;");
			
			evento.consume();
				
		}
		
	}

	private String getModoAtual() {
		return "escuro".equals(modoAtual)? Tema.modoEscuro() : Tema.modoClaro();
	}

	private void resetarInformacoes() {
		arquivoAtual = null;
		textArea.setText("");
		arquivoAlterado = false;
		definirNome(arquivoAtual, arquivoAlterado);		
	}

	public void sair() {
		if(arquivoAlterado) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Saindo");
			alert.setContentText("Deseja salvar as alterações em "+
			(arquivoAtual==null? "Sem título": arquivoAtual.getName()) + " ?");
			ButtonType btnSalvar = new ButtonType("Salvar");
			ButtonType btnNaoSalvar = new ButtonType("Não Salvar");
			ButtonType btnCancelar = new ButtonType("Cancelar");
			alert.getButtonTypes().setAll(btnSalvar, btnNaoSalvar, btnCancelar);
			
			alert.showAndWait().ifPresent(resposta ->{
				if(resposta == btnSalvar) {
					salvarArquivo();
					stage.close();
				}else if(resposta == btnNaoSalvar) {
					stage.close();
				}
			});
		}else {
			stage.close();
		}
	}

	public void copiar() {
		String textoSelecionado = textArea.getSelectedText();
		
		if(!textoSelecionado.isEmpty()) {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent conteudo = new ClipboardContent();
			conteudo.putString(textoSelecionado);
			clipboard.setContent(conteudo);
		}

	}

	public void colar() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if(clipboard.hasString()) {
			String clipboardTexto = clipboard.getString();
			int caretPosition = textArea.getCaretPosition();
			textArea.insertText(caretPosition, clipboardTexto);
		}
	}

	public void selecionarTudo() {
		textArea.selectAll();
	}

	public void modoClaro() {
		modoAtual = "claro";
		textArea.setStyle(getModoAtual() + "-fx-font-size: "+tamanhoFonte +" pt;");
	}

	public void modoEscuro() {
		modoAtual = "escuro";
		textArea.setStyle(getModoAtual() + "-fx-font-size: "+tamanhoFonte +" pt;");

	}

	public void exibirInformacoes() {
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setTitle("Editor de texto");;
		alert.setContentText("Editor de texto Simples");
		alert.showAndWait();

	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setOnCloseRequest(this::fechar);
	}
	
	private void fechar(WindowEvent evento) {
		evento.consume();
		sair();
	}
	
	public void atalhos(KeyEvent evento) {
		
		if(evento.isControlDown() && evento.getCode() == KeyCode.O) {
			abrirArquivo();
		}
		if(evento.isControlDown() && evento.isShiftDown() && evento.getCode() == KeyCode.S) {
			salvarArquivoComo();
		}
		if(evento.isControlDown() && evento.isShiftDown() && evento.getCode() == KeyCode.S) {
			salvarArquivoComo();
		}
		if(evento.isControlDown() && evento.getCode() == KeyCode.N) {
			novoArquivo();
		}
		if(evento.isControlDown() && evento.getCode() == KeyCode.W) {
			sair();
		}
	}
	

}
