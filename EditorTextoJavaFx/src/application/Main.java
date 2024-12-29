package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader((getClass().getResource("EditorTexto.fxml")));
			Scene scene = new Scene(loader.load());
			
			Controller controller = loader.getController();
			controller.setStage(primaryStage);
			primaryStage.setMinHeight(200);
			primaryStage.setMinWidth(300);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Sem título");
			primaryStage.getIcons().add(new Image("file:imagens/icone.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
