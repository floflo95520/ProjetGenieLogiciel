package application;
	

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import classes.Puzzle;
import classes.ListePieces;
import classes.Piece;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;



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
		                System.out.println("Dossier sélectionné : " + dossier.getAbsolutePath());
		                File[] fichiers = dossier.listFiles();
		                String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
		                if (fichiers != null) {
		                	int l=0;
	                        for (File fichier : fichiers) {
	                        	String nomFichier = fichier.getName().toLowerCase();
	                        	for (String ext : imageExtensions) {
	                        		
	                                if (nomFichier.endsWith(ext) && (l==0 || l==1 || l==2 || l==3)) {
	                                	try {
	                                	//	l++;
	                                	String parent = fichier.getParent(); // "/home/user/images"
	                                		String name = fichier.getName();     // "photo.png"
	                                		String baseName = name.substring(0, name.lastIndexOf(".")); // "photo"
	                                		String extension = name.substring(name.lastIndexOf("."));   // ".png"
	                                		
											Piece piece = new Piece(fichier,fichier.getName());

											
											
											innerList.addPiece(piece);
										
										
											
										} catch (Exception e1) {
											
											e1.printStackTrace();
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
	                        		
	                        		


	                        		
	                    /*    	for (Map.Entry<String, ListePieces> entry : hm.entrySet()) {
	                        			if(entry.getValue().getNumberOfPieces()>2) 
	                        				
	                        			
	                        		    System.out.println("Signature: " + entry.getKey());
	                        		    System.out.println("Taille de la liste associée : " + entry.getValue().getNumberOfPieces());
	                        		    for(Piece p: entry.getValue().getPieces()) {
	                        		    	System.out.println(p.getNom());
	                        		    }
	                        		    System.out.println("Taille totale de la HashMap : " + hm.size());
	                        		    System.out.println();
	                        			
	                        		}*/
	                        		
	                        		
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
	                        		ResolveBorder(hm,finalList,direction,pieceCorners,pieceBorders,directions);
	                        		puzzle.setl(0);
	                        		puzzle.setL(0);
	                        		for (Piece n : finalList.getPieces()) {
	                        			System.out.println(n.getNom());
	                        			if(pieceCorners.containsPiece(n)) {
	                        				System.out.println(n.getNom());
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
	                        		System.out.println(puzzle.getl());
	                        		System.out.println(puzzle.getL());
	                        		System.out.println(innerList.getPieces().size());
	                        		System.out.println(pieceBorders.getPieces().size());
	                        		System.out.println(pieceCorners.getPieces().size());
	                        		System.out.println(totalPieces);
	                        		if(piecesNumber!=totalPieces){
	                        			listView.getItems().add("Problème lors de l'assemblage des pièces détecté.");
	                        		}
	                        		
	                        		else {
	                        	
	                        		
	                        		
	                        			puzzle=new Puzzle(puzzle.getl(),puzzle.getL());
	                        			
	                        			
	                        			HashMap<String, ListePieces> signatureMap = new HashMap<>();
	                        			
	                        			for (Piece p:innerList.getPieces()) {
	                        				signatureMap.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
	                        				signatureMap.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
	                        				signatureMap.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
	                        				signatureMap.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
	                        			}
	                        			
	                        			
	                        		/*	for (Map.Entry<String, ListePieces> entry : signatureMap.entrySet()) {
	                        			    String signature = entry.getKey();
	                        			    ListePieces liste = entry.getValue();

	                        			    System.out.print("Signature : " + signature + " -> [");

	                        			    List<Piece> pieces = liste.getPieces();
	                        			    for (int i = 0; i < pieces.size(); i++) {
	                        			        System.out.print(pieces.get(i).getNom());
	                        			        if (i < pieces.size() - 1) {
	                        			            System.out.print(", ");
	                        			        }
	                        			    }

	                        			    System.out.println("]");
	                        			}*/

	                        	//		System.out.println(signatureMap.size());
	                        			
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

	                        			
	                        			
	                        			resolveInner(puzzle,signatureMap,1,1,puzzle.getL()-1,puzzle.getl()-1,tentatives);
	                    			
	                        /*		for (int y = 0; y < puzzle.getL(); y++) {
	                        			    for (int x = 0; x < puzzle.getl(); x++) {
	                        			    	System.out.println(x);
	                        			    	System.out.println(y);
	                        			        Piece p = puzzle.getCase(x, y);
	                        			        if (p != null) {
	                        			            System.out.println("[" + x + "," + y + "] = " + p.getNom());
	                        			        } else {
	                        			            System.out.println("[" + x + "," + y + "] = null");
	                        			        }
	                        			    }
	                        			}
	                        		*/
	                        		
	                        		for(String s: tentatives) {
	                        			System.out.println(s.toString());
	                        		}

	                        		ouvrirFenetrePuzzle(puzzle,tentatives);

	                        		}
	                        	
	                       
		                }
	                     else {
	                    	listView.getItems().add("Aucun fichier trouvé !");
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
	
	
	private Boolean ResolveBorder(HashMap<String, ListePieces> hm, ListePieces finalList, String direction, ListePieces pieceCorners,ListePieces pieceBorders,ArrayList<String> directions) {
		Piece p1=finalList.getPieces().getLast();
    		String Signature = null;
    		String oppositeDirection=p1.oppositeDirection(direction);
    		if(pieceCorners.containsPiece(p1)) {
    			direction=changeDirection(oppositeDirection,p1);
    			directions.add(direction);
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
    				System.out.println("Direction non reconnue");
    				break;
    		}
    		
    		ListePieces candidats=hm.get(Signature);
    		ListePieces candidats1=filterByRightSide(candidats, oppositeDirection1, Signature);
    		ListePieces candidats2=filterByUsed(candidats1);
    		System.out.println("Candidats pour :"+p1.getNom());
    		for(Piece p: candidats.getPieces()) {
    			System.out.println(p.getNom());
    		}
    		
    	if(candidats2.getPieces().size()>1) {
    			TreeMap<Double, Piece> scoreMap = new TreeMap<>();
    			candidats2.sortByPixelScoreDifference(p1,direction);
    		}
    		
    		
    		if(candidats2.isEmpty()) {
    			System.out.println(finalList.getPieces().size());
    			System.out.println(pieceCorners.getPieces().size());
    			System.out.println(pieceBorders.getPieces().size());
    			if(finalList.getPieces().size()== pieceCorners.getPieces().size() + pieceBorders.getPieces().size()) {
    				// Vérifie que la dernière pièce connecte bien à la première
    		        Piece first = finalList.getPieces().getFirst();
    		        Piece last = finalList.getPieces().getLast();
    		        
    		        String sigLast = null;
    		        String sigFirst = null;
    		        String lastDirection=directions.getLast();
    		        // direction ici est la direction dans laquelle tu AVANÇAIS
    		        // donc il faut que la direction opposée de last corresponde à celle de first
    		        switch (lastDirection) {
    		            case "right":
    		                sigLast = last.getRightSignature();
    		                sigFirst = first.getLeftSignature();
    		                break;
    		            case "left":
    		                sigLast = last.getLeftSignature();
    		                sigFirst = first.getRightSignature();
    		                break;
    		            case "top":
    		                sigLast = last.getTopSignature();
    		                sigFirst = first.getBottomSignature();
    		                break;
    		            case "bottom":
    		                sigLast = last.getBottomSignature();
    		                sigFirst = first.getTopSignature();
    		                break;
    		        }

    		        if (sigLast != null && sigLast.equals(sigFirst)) {
    		            return true;
    		        } else {
    		            return false; 
    		        }
    			}
    			else {
    				return false;
    			}
    		}
    		
    		for (Piece p:candidats2.getPieces()) {
    			p.setState(true);
    			finalList.addPiece(p);
    			Boolean success=ResolveBorder(hm,finalList,direction,pieceCorners,pieceBorders,directions);
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
			String[] directions=p1.directionCorners();
			for (String s: directions) {
				if(s!=direction) {
					return s;
				}
			}
		}
		return null;
	}
	
	
	private Boolean resolveInner(Puzzle puzzle, HashMap<String, ListePieces> hm, int y, int x, int ymax, int xmax,ArrayList<String> tentatives) {
		
		
		if (x >= xmax) {
	        return true; // succès
	    }
		
		if(y>=ymax) {
			return resolveInner(puzzle,hm,1,x+1,ymax,xmax,tentatives);
		}
		 String leftSig = puzzle.getCase(x-1, y).getRightSignature();
		 String topSig = puzzle.getCase(x, y-1).getBottomSignature();
		 String bottomSig=null;
		 String rightSig=null;
		 if(y+1==ymax) {
			 bottomSig =puzzle.getCase(x,y+1).getTopSignature();
		 }
		 if(x+1==xmax) {
			 rightSig=puzzle.getCase(x+1, y).getLeftSignature();
		 }
		 ListePieces candidats=getCandidatsBySignature(hm,leftSig,topSig,bottomSig,rightSig);
		 ListePieces filteredCandidates=filterByUsed(candidats);
		// filteredCandidates.sortByPixelScoreInner(puzzle.getCase(x-1, y),puzzle.getCase(x, y-1),puzzle.getCase(x, y+1),puzzle.getCase(x+1, y));
		 
		 for (Piece p:filteredCandidates.getPieces()) {
			 p.setState(true);
			 puzzle.setCase(p, x, y);
			 
			 if (resolveInner(puzzle, hm, y + 1, x, ymax, xmax,tentatives)) {
		            return true; // Succès, on remonte
		        }


		        p.setState(false);
		        puzzle.setCase(null,y, x);
		 }
		 
		tentatives.add("tentative#"+x+y); 
		return false;
	}
	
	
	private ListePieces getCandidatsBySignature(HashMap<String, ListePieces> hm, String leftSig, String topSig,
			String bottomSig,String rightSig) {
		 	ListePieces left = hm.getOrDefault(leftSig, new ListePieces());
		    ListePieces top = hm.getOrDefault(topSig, new ListePieces());
		    ListePieces bottom = bottomSig != null ? hm.getOrDefault(bottomSig, new ListePieces()) : null;
		    ListePieces right = rightSig != null ? hm.getOrDefault(rightSig, new ListePieces()) : null;

		    Set<Piece> filteredLeft = new HashSet<>();
		    for (Piece p : left.getPieces()) {
		        if (p.getLeftSignature() != null && p.getLeftSignature().equals(leftSig)) {
		            filteredLeft.add(p);
		        }
		    }

		    Set<Piece> filteredTop = new HashSet<>();
		    for (Piece p : top.getPieces()) {
		        if (p.getTopSignature() != null && p.getTopSignature().equals(topSig)) {
		            filteredTop.add(p);
		        }
		    }

		    Set<Piece> filteredBottom = null;
		    if (bottom != null) {
		        filteredBottom = new HashSet<>();
		        for (Piece p : bottom.getPieces()) {
		            if (p.getBottomSignature() != null && p.getBottomSignature().equals(bottomSig)) {
		                filteredBottom.add(p);
		            }
		        }
		    }

		    Set<Piece> filteredRight = null;
		    if (right != null) {
		        filteredRight = new HashSet<>();
		        for (Piece p : right.getPieces()) {
		            if (p.getRightSignature() != null && p.getRightSignature().equals(rightSig)) {
		                filteredRight.add(p);
		            }
		        }
		    }
		    Set<Piece> intersection = new HashSet<>(filteredLeft);
		    intersection.retainAll(filteredTop);
		    if (filteredRight != null) intersection.retainAll(filteredRight);
		    if (filteredBottom != null) intersection.retainAll(filteredBottom);

		    // On retourne une ListePieces contenant les candidats valides
		    ListePieces result = new ListePieces();
		    for (Piece p : intersection) {
		        result.addPiece(p);
		    }

		    return result;
	}


	private void ouvrirFenetrePuzzle(Puzzle puzzle, ArrayList<String> tentatives) {
	    Stage puzzleStage = new Stage();

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

	    double yplus = 0;
	    int x = 0, y = 0;

	    for (int i = 0; i < puzzle.getL(); i++) {
	        for (int j = 0; j < puzzle.getl(); j++) {
	            Piece piece = puzzle.getCase(j, i);
	            if (piece == null || piece.getImg() == null) continue;

	            try {
	                BufferedImage bufferedImage = piece.getImg();
	                WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
	                ImageView imageView = new ImageView(fxImage);

	                imageView.setFitWidth(fxImage.getWidth() * ratio);
	                imageView.setFitHeight(fxImage.getHeight() * ratio);
	                imageView.setPreserveRatio(false);

	                int[][] tab = Piece.corners(bufferedImage);
	                int x1 = tab[0][0];
	                int x2 = tab[1][0];
	                int y1 = tab[0][1];
	                int y2 = tab[2][1];

	                imageView.setLayoutX(x - (x1 * ratio));
	                imageView.setLayoutY(y - (y1 * ratio));

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

	    // Liste des tentatives avec label
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

	    // Nombre de tentatives
	    Label nbTentativesLabel = new Label("Nombre de tentatives : " + tentatives.size());
	    nbTentativesLabel.setStyle("-fx-padding: 10px; -fx-font-size: 14px;");

	    // Bouton pour enregistrer l'image
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

	    // Mise en page droite
	    VBox rightPane = new VBox(10, dimensionLabel, tentativeLabel, tentativeList, nbTentativesLabel, saveButton);
	    rightPane.setPadding(new Insets(10));

	    // ScrollPane pour le puzzle si grand
	    ScrollPane scrollPane = new ScrollPane(puzzlePane);
	    scrollPane.setFitToWidth(true);
	    scrollPane.setFitToHeight(true);

	    root.setCenter(scrollPane);
	    root.setRight(rightPane);

	    Scene scene = new Scene(root, TailleX * ratio + 300, TailleY * ratio + 50);
	    puzzleStage.setScene(scene);
	    puzzleStage.setTitle("Puzzle Résolu");
	    puzzleStage.show();
	}


	
	
	public static void main(String[] args) {
		launch(args);
	}
}