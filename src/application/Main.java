package application;
	

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import classes.Puzzle;
import classes.ListePieces;
import classes.Piece;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;


/**
 * Classe principale avec affichage JavaFX
 */
public class Main extends Application {
	
	ListePieces innerList = new ListePieces();

	
	@Override
	public void start(Stage primaryStage) throws Exception {

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
		            	listView.getItems().add("Dossier sélectionné : " + dossier.getAbsolutePath());
		            	String nomDossier = dossier.getName(); 
		            	if(!nomDossier.endsWith("_rotate")) {
		            	listView.getItems().add("Fichiers trouvés et traités :");
		                File[] fichiers = dossier.listFiles();
		                String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
		                if (fichiers != null) {
		     
	                        for (File fichier : fichiers) {
	                        	String nomFichier = fichier.getName().toLowerCase();
	                        	for (String ext : imageExtensions) {
	                        		
	                                if (nomFichier.endsWith(ext)) {
	                                	try {

	                                		
											Piece piece = new Piece(fichier,fichier.getName());

											
											
											innerList.addPiece(piece);
										
										
											
										} catch (Exception e1) {
											String errorMessage = "Erreur lors de la création des signatures d'une pièce.";
	                        				ouvrirFenetrePuzzle(null,null,errorMessage);
	                        				return ;
											
										}
	                                	listView.getItems().add(fichier.getName());
	                                }
	                        	}
	                        }
	                        
	                        		ListePieces pieceCorners=new ListePieces();
	                        		ListePieces pieceBorders=new ListePieces();

	                        		for (Piece p : innerList.getPieces()) {
	                        			
	                        			
	                        			if(p.getCount()==1) {
	                        				pieceBorders.addPiece(p);
	                        				innerList.removePiece(p);
	                        			}
	                        			else if(p.getCount()==2) {
	                        				pieceCorners.addPiece(p);
	                        				innerList.removePiece(p);
	                        			}
	                        			else if(p.getCount()>2) {
	                        				String messageErreur = "Erreur de lecture d'une ou plusieurs pièces ou bien le dossier sélectionné est incorrect.";
	                        				ouvrirFenetrePuzzle(null,null,messageErreur);
	                        				return;
	                        			}
	                        			
	                        		}
	                        		
	                        		
	                        		HashMap<String, ListePieces> hm= new HashMap<String, ListePieces>();
	                        		for (Piece p: pieceBorders.getPieces()) {
	                        			hm.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
	                        			hm.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
	                        			hm.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
	                        			hm.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
	                        			
	                        		}
	                        		
	                        		for(Piece p: pieceCorners.getPieces()) {
	                        			hm.computeIfAbsent(p.getTopSignature(),k-> new ListePieces()).addPiece(p);
	                        			hm.computeIfAbsent(p.getRightSignature(),k-> new ListePieces()).addPiece(p);
	                        			hm.computeIfAbsent(p.getBottomSignature(),k-> new ListePieces()).addPiece(p);
	                        			hm.computeIfAbsent(p.getLeftSignature(),k-> new ListePieces()).addPiece(p);

	                        		}
	                        		
	                        	
	                        		
	                        		Set<String> visitedStates1 = new HashSet<>();
	                        		ListePieces finalList= new ListePieces();
	                        		ArrayList<String> tentatives = new ArrayList<>();
	                        		ArrayList<String> directions = new ArrayList<>();
	                        		Piece p1=pieceCorners.getPieces().getFirst();
	                        		p1.setState(true);
	                        		finalList.addPiece(p1);
	                        		String direction=p1.directionCorners()[0];
	                        		directions.add(direction);
	                        		int count=1;
	                        		Puzzle puzzle=new Puzzle();
	                        		int expectedSize = pieceCorners.getPieces().size() + pieceBorders.getPieces().size();
	                        		try {
	                        			finalList.ResolveBorder(hm,direction,pieceCorners,pieceBorders,directions,expectedSize,visitedStates1);
	                        		}
	                        		catch (Exception e2) {
	                        			String errorMessage="Erreur lors de la résolution du bord du puzzle.";
	                        			ouvrirFenetrePuzzle(null,null,errorMessage);
	                        			return;
	                        		}
	                        		
	                        		puzzle.setl(0);
	                        		puzzle.setL(0);
	                        		for (Piece n : finalList.getPieces()) {
	                        			
	                        			if(pieceCorners.containsPiece(n)) {
	                        				
	                        				if(puzzle.getl()==0 && count!=1) {
	                        					puzzle.setl(count);
	                        				}
	                        				else if(puzzle.getL()==0 && count!=1) {
	                        					puzzle.setL(count);
	                        				}
	                        				count=1;
	                        			}
	                        			count++;
	                        		}
	                        		int piecesNumber=puzzle.getl()*puzzle.getL();
	                        		int totalPieces=innerList.getPieces().size()+pieceBorders.getPieces().size()+pieceCorners.getPieces().size();
	                        		if(piecesNumber!=totalPieces){
	                        			String errorMessage="Avertissement. Les dimensions trouvées ne correspondent pas au nombre total de pièces du puzzle.";
	                        			ouvrirFenetrePuzzle(null,null,errorMessage);
	                        			try {
											Thread.sleep(3000);
										} catch (InterruptedException e1) {
											ouvrirFenetrePuzzle(null,null,e1.getMessage());
											return;
										}
	                        		}
	                        		
	                        		
	                        	
	                        		
	                        		
	                        			puzzle=new Puzzle(puzzle.getl(),puzzle.getL());
	                        			
	                        			
	                        			HashMap<String, ListePieces> signatureMap = new HashMap<>();
	                        			
	                        			for (Piece p:innerList.getPieces()) {
	                        				signatureMap.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
	                        				signatureMap.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
	                        				signatureMap.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
	                        				signatureMap.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
	                        			}
	                        			
	                        			
	                        			int w=0;
	                        			for (int i=0;i<puzzle.getl();i++) {
	                        				
	                        				if(w<finalList.getPieces().size())puzzle.setCase(finalList.getPieces().get(w),i,0);
	                        				w++;
	                        			}

	                        			for (int j=1;j<puzzle.getL();j++) {
	                        				if(w<finalList.getPieces().size())puzzle.setCase(finalList.getPieces().get(w), puzzle.getl()-1, j);
	                        				w++;
	                        			}

	                        			for (int k=puzzle.getl()-2;k>=0;k--) {
	                        				if(w<finalList.getPieces().size())puzzle.setCase(finalList.getPieces().get(w), k, puzzle.getL()-1);
	                        				w++;
	                        			}

	                        			for (int n=puzzle.getL()-2;n>0;n--) {
	                        				if(w<finalList.getPieces().size())puzzle.setCase(finalList.getPieces().get(w), 0,n);
	                        				w++;
	                        			}
	                        			String errorMessage=null;
	                        			try {
	                        			    puzzle.resolveInnerIteratif(puzzle, signatureMap, puzzle.getL() - 1, puzzle.getl() - 1,tentatives);
	                        			} catch (Exception error) {
	                        			    errorMessage = error.getMessage();
	                        			    ouvrirFenetrePuzzle(puzzle,tentatives,errorMessage);
	                        			} catch (StackOverflowError error) {
	                        			    errorMessage = "Erreur : débordement de pile (stack overflow).";
	                        			    ouvrirFenetrePuzzle(null,null,errorMessage);
	                        			}
	                        

	                        		

	                        		
	                        	
	                        			ouvrirFenetrePuzzle(puzzle,tentatives,null);
		                }
	                     else {
	                    	listView.getItems().add("Aucun fichier trouvé !");
	                    }
		            }
		            	else if(nomDossier.endsWith("_rotate")) {
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            	}
		            } else {
		            	listView.getItems().add("Aucun dossier sélectionné.");
		            }
		            	
		            
		        });
			root.setTop(D);
			root.setCenter(listView);
			Scene scene = new Scene(root,400,400);

			primaryStage.setScene(scene);
			primaryStage.show();
		    
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	


	/**
	 * Afficher le puzzle résolu (ou pas) ainsi que différentes informations et options sur une fenêtre graphique
	 * @param puzzle
	 * @param tentatives
	 * @param message
	 */
	public void ouvrirFenetrePuzzle(Puzzle puzzle, ArrayList<String> tentatives, String message) {

	    Stage puzzleStage = new Stage();

	    if (message != null && !message.isEmpty() && !message.equals("Arrêt forcé : trop d'itérations, boucle infinie détectée.")) {
	        
	        VBox messageBox = new VBox();
	        messageBox.setPadding(new Insets(20));
	        messageBox.setAlignment(Pos.CENTER);

	        Label messageLabel = new Label(message);
	        messageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	        Button closeButton = new Button("Fermer");
	        closeButton.setOnAction(e -> puzzleStage.close());

	        messageBox.getChildren().addAll(messageLabel, closeButton);

	        Scene messageScene = new Scene(messageBox, 400, 400);
	        puzzleStage.setScene(messageScene);
	        puzzleStage.setTitle("Message");
	        puzzleStage.show();
	        return; 
	    }

	    BorderPane root = new BorderPane();
	    Pane puzzlePane = new Pane();

	    int[][] tab2 = Piece.corners(puzzle.getCase(0, 0).getImg());
	    int x3 = tab2[0][0];
	    int x4 = tab2[1][0];
	    int y3 = tab2[0][1];
	    int y4 = tab2[2][1];

	    int TailleX = (x4 - x3) * puzzle.getl();
	    int TailleY = (y4 - y3) * puzzle.getL();

	    double ratio = (TailleX > TailleY) ? 1000.0 / TailleX : 1000.0 / TailleY;

	    
	    double puzzleWidth = TailleX * ratio;
	    double puzzleHeight = TailleY * ratio;

	    
	    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	    double maxWidth = screenBounds.getWidth() * 0.9;  
	    double maxHeight = screenBounds.getHeight() * 0.9; 

	    
	    double rightPaneWidth = 300;

	    
	    double windowWidth = Math.min(puzzleWidth + rightPaneWidth, maxWidth);
	    double windowHeight = Math.min(puzzleHeight + 50, maxHeight);

	    
	    if (puzzleWidth + rightPaneWidth > maxWidth) {
	        double availableWidthForPuzzle = maxWidth - rightPaneWidth;
	        double scaleX = availableWidthForPuzzle / puzzleWidth;
	        ratio *= scaleX;
	        puzzleWidth = TailleX * ratio;
	        puzzleHeight = TailleY * ratio;
	    }

	    if (puzzleHeight + 50 > maxHeight) {
	        double scaleY = (maxHeight - 50) / puzzleHeight;
	        ratio *= scaleY;
	        puzzleWidth = TailleX * ratio;
	        puzzleHeight = TailleY * ratio;
	    }

	    
	    windowWidth = Math.min(puzzleWidth + rightPaneWidth, maxWidth);
	    windowHeight = Math.min(puzzleHeight + 50, maxHeight);

	    
	    double yplus = 0;
	    int x = 0, y = 0;

	    puzzlePane.getChildren().clear();

	    for (int i = 0; i < puzzle.getL(); i++) {
	        for (int j = 0; j < puzzle.getl(); j++) {
	            Piece piece = puzzle.getCase(j, i);
	            if (piece == null || piece.getImg() == null) continue;
	            try {
	                BufferedImage bufferedImage = piece.getImg();
	                WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
	                ImageView imageView = new ImageView(fxImage);

	                imageView.setFitWidth(Math.round(fxImage.getWidth() * ratio));
	                imageView.setFitHeight(Math.round(fxImage.getHeight() * ratio));
	                imageView.setPreserveRatio(false);

	                int[][] tab = Piece.corners(bufferedImage);
	                int x1 = tab[0][0];
	                int x2 = tab[1][0];
	                int y1 = tab[0][1];
	                int y2 = tab[2][1];

	                imageView.setLayoutX(Math.round(x - (x1 * ratio)));
	                imageView.setLayoutY(Math.round(y - (y1 * ratio)));

	                if (j == 0) {
	                    yplus = (y2 - y1) * ratio;
	                }

	                x += (x2 - x1) * ratio;

	                if (j == puzzle.getl() - 1) {
	                    x = 0;
	                    y += yplus;
	                }

	                puzzlePane.getChildren().add(imageView);

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    
	    Label tentativeLabel = new Label("Tentatives");
	    tentativeLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");

	    ListView<String> tentativeList = new ListView<>();
	    if (tentatives.isEmpty()) {
	        tentativeList.getItems().add("Le puzzle a été réussi du premier coup");
	    } else {
	        tentativeList.getItems().addAll(tentatives);
	    }
	    tentativeList.setPrefWidth(250);
	    tentativeList.setPrefHeight(400);

	    
	    Label nbTentativesLabel = new Label("Nombre de tentatives : " + tentatives.size());
	    nbTentativesLabel.setStyle("-fx-padding: 10px; -fx-font-size: 14px;");

	   
	    Button saveButton = new Button("Enregistrer l'image");
	    saveButton.setOnAction(e -> {
	        WritableImage snapshot = puzzlePane.snapshot(new SnapshotParameters(), null);

	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Enregistrer l'image recomposée");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image PNG", "*.png"));
	        File file = fileChooser.showSaveDialog(puzzleStage);

	        if (file != null) {
	            try {
	                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    });

	    Label dimensionLabel = new Label(
	        "Dimensions du puzzle : " + puzzle.getl() + " x " + puzzle.getL() +
	        "\nNombre total de pièces : " + (puzzle.getl() * puzzle.getL())
	    );
	    dimensionLabel.setStyle("-fx-padding: 5; -fx-font-size: 14px; -fx-font-weight: bold;");

	    
	    VBox rightPane = new VBox(10, dimensionLabel, tentativeLabel, tentativeList, nbTentativesLabel, saveButton);
	    rightPane.setPadding(new Insets(10));
	    
	    VBox topPane = new VBox();
	    topPane.setPadding(new Insets(10));
	    topPane.setSpacing(10);

	    if (message != null && !message.isEmpty()) {
	        Label errorLabel = new Label(message);
	        errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14px;");
	        topPane.getChildren().add(errorLabel);
	    }

	   
	    ScrollPane scrollPane = new ScrollPane(puzzlePane);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setFitToHeight(true);

	    topPane.getChildren().add(scrollPane);
	    root.setCenter(topPane);
	    root.setRight(rightPane);

	    Scene scene = new Scene(root, windowWidth, windowHeight);
	    puzzleStage.setScene(scene);
	    puzzleStage.setTitle("Puzzle Résolu");
	    puzzleStage.show();
	}


	
	
	public static void main(String[] args) {
		launch(args);
	}
}