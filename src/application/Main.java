package application;
	
import java.io.File;
import classes.Piece;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
	                       // ouvrirFenetrePuzzle(fichiers);
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
	
	private void ouvrirFenetrePuzzle(File[] fichiers) {
	    Stage puzzleStage = new Stage();
	    Pane puzzlePane = new Pane();
	    final double TAILLE_CASE = 100;
	    int i = 0;
	    int x = 0, y=0;
	    for (File fichier : fichiers) {
	        String nomFichier = fichier.getName().toLowerCase();
	        if (nomFichier.endsWith(".png")) {
	            try {
	                // Image et ImageView
	                Image image = new Image(fichier.toURI().toString());
	                ImageView imageView = new ImageView(image);
	                
	                
	                //imageView.setScaleX(0.2);
	                //imageView.setScaleY(0.2);
	                
	                imageView.setFitHeight(100);
	                //imageView.setPreserveRatio(true);
	                
	                imageView.setFitWidth(100);
	                //imageView.setPreserveRatio(true);
					
	                
	                imageView.setLayoutX(x);
	                imageView.setLayoutY(0);

	                x += 80;

	                puzzlePane.getChildren().add(imageView);
	                i++;

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    Scene scene = new Scene(puzzlePane, 2000, 1000);
	    puzzleStage.setTitle("Puzzle Assemblé");
	    puzzleStage.setScene(scene);
	    puzzleStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}