package org.openjfx.precificacao;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

	  private static Scene scene;

	    @Override
	    public void start(Stage stage) throws IOException {
	        scene = new Scene(loadFXML("DashBoard"));
	        stage.setScene(scene);

			stage.setMinHeight(600);
			stage.setMinWidth(800);
			stage.setMaxHeight(Screen.getPrimary().getBounds().getHeight());
			stage.setMaxWidth(Screen.getPrimary().getBounds().getWidth());

			stage.setTitle("Precificação");
			Image image = new Image("images/ico.png");
			stage.getIcons().add(image);
			stage.setMaximized(true);
	        stage.show();
	    }

	    static void setRoot(String fxml) throws IOException {
	        scene.setRoot(loadFXML(fxml));
	    }

	    private static Parent loadFXML(String fxml) throws IOException {
	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/openjfx/precificacao/" + fxml + ".fxml"));
	        return fxmlLoader.load();
	    }
	    
	    public static void mudarTela(String outraTela) {
	        String telaFxml = null;  // Nome do arquivo FXML correspondente à tela
	        switch (outraTela) {
	            case "DashBoard":
	                telaFxml = "DashBoard";
	                break;
	            case "Clientes":
	                telaFxml = "Clientes";
	                break;
				case "Profissionais":
					telaFxml = "Profissionais";
					break;
				case "Projeto":
					telaFxml = "Projeto";
					break;
				case "Custos":
					telaFxml = "Custos";
					break;
				case "DetalhamentoProjeto":
					telaFxml = "DetalhamentoProjeto";
					break;
				case "Precificacao":
					telaFxml = "Precificacao";
					break;
				case "Atividade":
					telaFxml = "Atividade";
					break;
				case "Etapas":
					telaFxml = "Etapas";
					break;
				case "CustosVariaveis":
					telaFxml = "CustosVariaveis";
					break;
				case "qnfrCustosVariaveis":
					telaFxml = "qnfrCustosVariaveis";
					break;
				case "ClientesDetalhes":
					telaFxml = "ClientesDetalhes";
					break;
				case "GraficosProjeto":
					telaFxml = "GraficosProjeto";
					break;
				case "Impostos":
					telaFxml = "Impostos";
					break;
				default:
	                System.err.println("Tela não reconhecida: " + outraTela);
	                return;  // Encerra o método se o nome da tela não for reconhecido
	        }

	        try {
	            scene.setRoot(loadFXML(telaFxml));
	        } catch (IOException e) {
	            System.err.println("Erro ao carregar a tela: " + telaFxml);
	            e.printStackTrace();

				System.err.println("Erro ao carregar a tela: " + telaFxml);
				e.printStackTrace();
				mostrarAlertaErro("Erro ao carregar a tela: " + telaFxml);
	            // Aqui você poderia adicionar mais lógica, como mostrar uma mensagem de erro ao usuário
	        }

	    }

	private static void mostrarAlertaErro(String mensagemErro) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Erro");
		alert.setHeaderText(null);
		alert.setContentText(mensagemErro);

		alert.showAndWait();
	}


	    public static void main(String[] args) {
	        launch();
	    }

}