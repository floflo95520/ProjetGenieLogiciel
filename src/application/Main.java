package application;
	

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import java.util.List;

import classes.ListePieces;
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
	
	private ListePieces pieceList = new ListePieces();
	
	private HashMap<String, List<Piece>> signatureMap = new HashMap<>();
	
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
											pieceList.addPiece(piece);
											
											signatureMap.computeIfAbsent(piece.getTopSignature(), k -> new ArrayList<>()).add(piece);
											signatureMap.computeIfAbsent(piece.getLeftSignature(), k -> new ArrayList<>()).add(piece);
											signatureMap.computeIfAbsent(piece.getRightSignature(), k -> new ArrayList<>()).add(piece);
											signatureMap.computeIfAbsent(piece.getBottomSignature(), k -> new ArrayList<>()).add(piece);
											
											index++;
											
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
	                                	listView.getItems().add(fichier.getName());
	                                }
	                        	}
	                        }
	                        
	                        		ListePieces pieceCorners=new ListePieces();
	                        		ListePieces pieceBorders=new ListePieces();

	                        		for (Piece p : pieceList.getPieces()) {
	                        			int count=0;
	                        			if(Piece.isSingleCharRepeatedUntilUnderscore(p.getSeqTop())) {
	                        				count++;
	                        			}
	                        			if(Piece.isSingleCharRepeatedUntilUnderscore(p.getSeqRight())) {
	                        				count++;
	                        			}
	                        			if(Piece.isSingleCharRepeatedUntilUnderscore(p.getSeqLeft())) {
	                        				count++;
	                        			}
	                        			if(Piece.isSingleCharRepeatedUntilUnderscore(p.getSeqBottom())) {
	                        				count++;
	                        			}
	                        			
	                        			
	                        			if(count==1) {
	                        				pieceBorders.addPiece(p);
	                        				pieceList.removePiece(p);
	                        			}
	                        			else if(count==2) {
	                        				pieceCorners.addPiece(p);
	                        				pieceList.removePiece(p);
	                        			}
	                        			else if(count>2) {
	                        				System.out.println(p.getNom());
	                        				System.out.println(p.getSeqTop());
	                        				System.out.println(p.getSeqRight());
	                        				System.out.println(p.getSeqLeft());
	                        				System.out.println(p.getSeqBottom());
	                        				System.out.println("Erreur. La pièce ne peut pas voir plus de deux côtés entièrement plats");
	                        				
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
	                        		
	                        		for (Map.Entry<String, ListePieces> entry : hm.entrySet()) {
	                        		    System.out.println("Signature: " + entry.getKey());
	                        		    System.out.println("Taille de la liste associée : " + entry.getValue().getNumberOfPieces());
	                        		    for(Piece p: entry.getValue().getPieces()) {
	                        		    	System.out.println(p.getNom());
	                        		    }
	                        		    System.out.println("Taille totale de la HashMap : " + hm.size());
	                        		    System.out.println();
	                        		}
	                        		
	                        		
	                        		ListePieces finalList= new ListePieces();
	                        		ArrayList<String> tentatives = new ArrayList<>();
	                        		Piece p1=pieceCorners.getPieces().getFirst();
	                        		p1.setState(true);
	                        		finalList.addPiece(p1);
	                        		String direction=p1.direction()[0];
	                        		int count=1;
	                        		int l=0,L=0;
	                        		ResolveBorder(hm,finalList,count,direction,pieceCorners,pieceBorders,l,L);
	                        		for (Piece n:finalList.getPieces()) {
	                        			System.out.println(n.getNom());
	                        		}
	                        		
	                        		
	                        	
	                       ouvrirFenetrePuzzle(fichiers);
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
	
	
	private Boolean ResolveBorder(HashMap<String, ListePieces> hm, ListePieces finalList, int count, String direction, ListePieces pieceCorners,ListePieces pieceBorders, int l, int L) {
		System.out.println(l);
		System.out.println(L);
		Piece p1=finalList.getPieces().getLast();
    		String Signature = null;
    		String oppositeDirection=p1.oppositeDirection(direction);
    		if(pieceCorners.containsPiece(p1)) {
    			count++;
    			if(l==0 || l==1 || l==2) {
    				l=count;
    			}
    			else if(L==0) {
    				L=count;
    			}
    			count=0;
    			direction=changeDirection(oppositeDirection,p1);
    		}
    		String oppositeDirection1=p1.oppositeDirection(direction);
    		switch(direction) {
    			case "right":
    				Signature=p1.getRightSignature();
    				break;
    			case "left":
    				Signature=p1.getLeftSignature();
    				break;
    			case "top":
    				Signature=p1.getTopSignature();
    				break;
    			case "bottom":
    				Signature=p1.getBottomSignature();
    				break;
    			default:
    				System.out.println("ntm");
    				break;
    		}
    		
    		ListePieces candidats=hm.get(Signature);
    		ListePieces candidats1=filterByRightSide(candidats,oppositeDirection1,Signature);
    		ListePieces candidats2=filterByUsed(candidats1);
    		
    		if(candidats2.isEmpty()) {
    			if(finalList.getPieces().size()== pieceCorners.getPieces().size() + pieceBorders.getPieces().size()) {

    				return true;
    			}
    			else {
    				return false;
    			}
    		}
    		
    		for (Piece p:candidats2.getPieces()) {
    			count++;
    			p.setState(true);
    			finalList.addPiece(p);
    			Boolean success=ResolveBorder(hm,finalList,count,direction,pieceCorners,pieceBorders,l,L);
    			if (success) { return true;}
    			else {
    				p.setState(false);
    				finalList.removePiece(p);
    			}
    		}
    		return false;		
		
	}
	
	private ListePieces filterByRightSide(ListePieces candidats, String oppositeDirection, String signature) {
		 ListePieces result = new ListePieces();

		    for (Piece p : candidats.getPieces()) {

		        String candidateSignature = null;

		        switch (oppositeDirection) {
		            case "left":
		                candidateSignature = p.getLeftSignature();
		                break;
		            case "right":
		                candidateSignature = p.getRightSignature();
		                break;
		            case "top":
		                candidateSignature = p.getTopSignature();
		                break;
		            case "bottom":
		                candidateSignature = p.getBottomSignature();
		                break;
		            default:
		                continue; // Direction non reconnue
		        }
		        if (candidateSignature != null && !candidateSignature.isEmpty() && candidateSignature.equals(signature)) {
		            result.addPiece(p);
		        }
	} return result;
	}
	
	private ListePieces filterByUsed(ListePieces candidats) {
		ListePieces result = new ListePieces();
		
		for (Piece p: candidats.getPieces()) {
			if(!p.getState()) {
				result.addPiece(p);
			}
		}
		return result;
	}
	
	private String changeDirection(String direction,Piece p1) {
		if(p1!=null) {
			String[] directions=p1.direction();
			for (String s: directions) {
				if(s!=direction) {
					return s;
				}
			}
		}
		return null;
	}
	
	private void ouvrirFenetrePuzzle(File[] fichiers) {
	    Stage puzzleStage = new Stage();
	    Pane puzzlePane = new Pane();
	    final double TAILLE_CASE = 100;
	    int i = 0;
	    int x = 0, y=0, x1, x2, y1=0, y2=0;
	    double yplus = 0;
	    int[][] tab;
	    for (File fichier : fichiers) {
	        String nomFichier = fichier.getName().toLowerCase();
	        if (nomFichier.endsWith(".png")) {
	            try {
	                // Image et ImageView
	     
	                
	                
	                double ratio = 0.3; // 30%

	                Image image = new Image(fichier.toURI().toString());
	                ImageView imageView = new ImageView(image);

	                imageView.setFitWidth(image.getWidth() * ratio);
	                imageView.setFitHeight(image.getHeight() * ratio);
	                imageView.setPreserveRatio(false); // car tu définis toi-même les 2
	                
	                BufferedImage bufferedImage = ImageIO.read(fichier);
	                tab = Piece.corners(bufferedImage);
	                x1 = tab[0][0];
	                x2 = tab[1][0];
	                
	                y1 = tab[0][1];
	                y2 = tab[2][1];
	                
	                imageView.setLayoutX(x - (x1 * ratio));
	               // System.out.println("valeur de x1: " + x1);
	                imageView.setLayoutY(y - (y1 * ratio));
	                
	                if(x == 0) {
	                	yplus = (y2 - y1) * ratio;
	                }
	                
	                x += (x2 - x1) * ratio;
	                
	                if(x + ((x2 - x1 ) * ratio ) >= 700) {
	                	x = 0;
	                	y += yplus;
	                }
	                
	                
	                
	                
	                
	              //  System.out.println("valeur de x" + x + " valeur de y : " + y);
	                
	                //System.out.println("valeur de x" + x + " valeur de y : " + y);
	                
	                puzzlePane.getChildren().add(imageView);
	               

	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	
	    Scene scene = new Scene(puzzlePane, 800, 800);
	    puzzleStage.setTitle("Puzzle Assemblé");
	    puzzleStage.setScene(scene);
	    puzzleStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}