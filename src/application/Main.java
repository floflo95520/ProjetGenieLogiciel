package application;
	

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
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
	
	private List<Piece> Liste2Pieces = new ArrayList<>();
	
	private HashMap<String, List<Integer>> signatureMap = new HashMap<>();
	
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
		                	int index = 0;
	                        for (File fichier : fichiers) {
	                        	String nomFichier = fichier.getName().toLowerCase();
	                        	for (String ext : imageExtensions) {
	                        		
	                                if (nomFichier.endsWith(ext)) {
	                                	try {
											Piece piece = new Piece(fichier.getPath(),fichier.getName());
											Liste2Pieces.add(piece);
											
											signatureMap.computeIfAbsent(piece.getTopSignature(), k -> new ArrayList<>()).add(index);
											signatureMap.computeIfAbsent(piece.getLeftSignature(), k -> new ArrayList<>()).add(index);
											signatureMap.computeIfAbsent(piece.getRightSignature(), k -> new ArrayList<>()).add(index);
											signatureMap.computeIfAbsent(piece.getBottomSignature(), k -> new ArrayList<>()).add(index);
											
											index++;
											
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
	                                	listView.getItems().add(fichier.getName());
	                                }
	                        	}
	                        }
	                        	System.out.println("Signatures enregistrées :");
	                        	for (String key : signatureMap.keySet()) {
	                        		System.out.println(key + " -> Pièce n°" + signatureMap.get(key));}
	                        	{
	                        		System.out.println("Nombre total de signatures enregistrées : " + signatureMap.size());
	                        		
	                        		/*HashMap<String, Integer> borduresMap = new HashMap<>();
	                        		HashMap<String, Integer> interieursMap = new HashMap<>();

	                        		for (Map.Entry<String, Integer> entry : signatureMap.entrySet()) {
	                        		    String signature = entry.getKey();
	                        		    Integer pieceIndex = entry.getValue();

	                        		    if (signature.endsWith("_0") || signature.endsWith("_1")) {
	                        		        interieursMap.put(signature, pieceIndex);
	                        		    } else {
	                        		        borduresMap.put(signature, pieceIndex);
	                        		    }
                        		    for (Map.Entry<String, Integer> poulet : borduresMap.entrySet()) {
                        		    	String signa = poulet.getKey();
                        		    	Integer pieceInd = poulet.getValue();
                        		    	
	                        		}*/
	                        }
	                        	
	                       // ouvrirFenetrePuzzle(fichiers);

		                }
	                     else {
	                    	listView.getItems().add("Aucun fichier trouvé !");
	                    }
		            } else {
		            	listView.getItems().add("Aucun dossier sélectionné.");
		                /*System.out.println("Aucun dossier sélectionné.");*/
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
	                
	                imageView.setFitHeight(10);
	                //imageView.setPreserveRatio(true);
	                
	                imageView.setFitWidth(10);
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

	    Scene scene = new Scene(puzzlePane, 200, 100);
	    puzzleStage.setTitle("Puzzle Assemblé");
	    puzzleStage.setScene(scene);
	    puzzleStage.show();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}