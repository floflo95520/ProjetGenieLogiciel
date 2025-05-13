package application;
	
import java.io.File;
import classes.Piece;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Button D= new Button("Sélectionner un dossier");
			ListView<String> listView = new ListView<>();
			 D.setOnAction(e -> {
		            DirectoryChooser chooser = new DirectoryChooser();
		            listView.getItems().clear();		            
		            chooser.setTitle("Sélectionnez un dossier");

		            File dossier = chooser.showDialog(primaryStage);
		            
		            

		            if (dossier != null) {
		            	listView.getItems().clear();
		                System.out.println("Dossier sélectionné : " + dossier.getAbsolutePath());
		                File[] fichiers = dossier.listFiles();
		                String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
		                if (fichiers != null) {
	                        for (File fichier : fichiers) {
	                        	String nomFichier = fichier.getName().toLowerCase();
	                        	for (String ext : imageExtensions) {
	                                if (nomFichier.endsWith(ext)) {
	                                	try {
											new Piece(fichier.getPath(),fichier.getName());
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
	                                	listView.getItems().add(fichier.getName());
	                                }
	                        }
	                        }
		                }
	                     else {
	                    	listView.getItems().add("Aucun fichier trouvé !");
	                    }
		            } else {
		            	listView.getItems().add("Aucun dossier sélectionné.");
		                System.out.println("Aucun dossier sélectionné.");
		            }
		        });
			root.setTop(D);
			root.setCenter(listView);
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}