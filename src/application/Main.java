package application;
	

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
	ListePieces innerList90 = new ListePieces();
	ListePieces innerList180= new ListePieces();
	ListePieces innerList270= new ListePieces();

	
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
	                        			finalList.ResolveBorder(hm,direction,pieceCorners,pieceBorders,directions,expectedSize);
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
	                        			    return;
	                        			} catch (StackOverflowError error) {
	                        			    errorMessage = "Erreur : débordement de pile (stack overflow).";
	                        			    ouvrirFenetrePuzzle(null,null,errorMessage);
	                        			    return;
	                        			}
	                        

	                        		

	                        		
	                        	
	                        			ouvrirFenetrePuzzle(puzzle,tentatives,null);
		                }
	                     else {
	                    	listView.getItems().add("Aucun fichier trouvé !");
	                    }
		            }
		            	else if(nomDossier.endsWith("_rotate")) {
		            		
		            		


		    				File[] fichiers = dossier.listFiles();
		    		                String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp"};
		    		                if (fichiers != null) {
		    		                	
		    	                        for (File fichier : fichiers) {
		    	                        	String nomFichier = fichier.getName().toLowerCase();
		    	                        	for (String ext : imageExtensions) {
		    	                        		
		    	                                if (nomFichier.endsWith(ext)) {
		    	                                	try {
		    	                                		String parent = fichier.getParent(); 
		    	                                		String name = fichier.getName();     
		    	                                		String baseName = name.substring(0, name.lastIndexOf(".")); 
		    	                                		String extension = name.substring(name.lastIndexOf("."));   
		    	                                		
		    											Piece piece = new Piece(fichier,fichier.getName());
		    											Piece piece90 = new Piece(fichier,fichier.getName());
		    											File f90= new File(parent, baseName + "90" + extension);
		    											piece90.rotate90(fichier,f90,f90.getName());
		    		    								Piece piece180 = new Piece(fichier,fichier.getName());
		    		    								File f180= new File(parent, baseName + "180" + extension);
		    		    								piece180.rotate90(f90, f180,f180.getName());
		    		    								Piece piece270 = new Piece(fichier,fichier.getName());
		    		    								File f270= new File(parent, baseName + "270" + extension);
		    		    								piece270.rotate90(f180,f270,f270.getName());
		    		    								
		    											innerList.addPiece(piece);

		    											innerList90.addPiece(piece90);

		    											innerList180.addPiece(piece180);

		    											innerList270.addPiece(piece270);

		    										
		    										
		    											
		    										} catch (Exception e1) {
		    											
		    											e1.printStackTrace();
		    										}
		    	                                	listView.getItems().add(fichier.getName());
		    	                                }
		    	                        	}
		    	                        }
		    	                        
		    	                        		ListePieces pieceCorners=new ListePieces();
		    	                        		ListePieces pieceBorders=new ListePieces();
		    	                        		ListePieces pieceCorners90=new ListePieces();
		    	                        		ListePieces pieceBorders90=new ListePieces();
		    	                        		ListePieces pieceCorners180=new ListePieces();
		    	                        		ListePieces pieceBorders180=new ListePieces();
		    	                        		ListePieces pieceCorners270=new ListePieces();
		    	                        		ListePieces pieceBorders270=new ListePieces();
		    	                        		

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
		    	                        		for (Piece p : innerList90.getPieces()) {
		    	                        			
		    	                        			
		    	                        			if(p.getCount()==1) {
		    	                        				pieceBorders90.addPiece(p);
		    	                        				innerList90.removePiece(p);
		    	                        			}
		    	                        			else if(p.getCount()==2) {
		    	                        				pieceCorners90.addPiece(p);
		    	                        				innerList90.removePiece(p);
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
		    	                        		for (Piece p : innerList180.getPieces()) {
		    	                        			
		    	                        			
		    	                        			if(p.getCount()==1) {
		    	                        				pieceBorders180.addPiece(p);
		    	                        				innerList180.removePiece(p);
		    	                        			}
		    	                        			else if(p.getCount()==2) {
		    	                        				pieceCorners180.addPiece(p);
		    	                        				innerList180.removePiece(p);
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
		    	                        		for (Piece p : innerList270.getPieces()) {
		    	                        			
		    	                        			
		    	                        			if(p.getCount()==1) {
		    	                        				pieceBorders270.addPiece(p);
		    	                        				innerList270.removePiece(p);
		    	                        			}
		    	                        			else if(p.getCount()==2) {
		    	                        				pieceCorners270.addPiece(p);
		    	                        				innerList270.removePiece(p);
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
		    	                        		
		    	                        		ListePieces allOuterPieces = mergeListePieces(pieceCorners,pieceCorners90,pieceCorners180,pieceCorners270,pieceBorders,pieceBorders90,pieceBorders180,pieceBorders270);
		    	                        		ListePieces allInnerPieces = mergeListePieces(innerList,innerList90,innerList180,innerList270);
		    	                        		
		    	                        		HashMap<String, ListePieces> innerMap= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: innerList.getPieces()) {
		    	                        			innerMap.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
		    	                        		}
		    	                        		
		    	                        		HashMap<String, ListePieces> innerMap90= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: innerList90.getPieces()) {
		    	                        			innerMap90.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap90.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap90.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap90.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
		    	                        		}
		    	                        		
		    	                        		HashMap<String, ListePieces> innerMap180= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: innerList180.getPieces()) {
		    	                        			innerMap180.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap180.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap180.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap180.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
		    	                        		}
		    	                        		
		    	                        		HashMap<String, ListePieces> innerMap270= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: innerList270.getPieces()) {
		    	                        			innerMap270.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap270.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap270.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			innerMap270.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
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
		    	                        		
		    	                        		HashMap<String, ListePieces> hm90= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: pieceBorders90.getPieces()) {
		    	                        			hm90.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm90.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm90.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm90.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
		    	                        		}
		    	                        		
		    	                        		for(Piece p: pieceCorners90.getPieces()) {
		    	                        			hm90.computeIfAbsent(p.getTopSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm90.computeIfAbsent(p.getRightSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm90.computeIfAbsent(p.getBottomSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm90.computeIfAbsent(p.getLeftSignature(),k-> new ListePieces()).addPiece(p);
		    	                  

		    	                        		}
		    	                        		HashMap<String, ListePieces> hm180= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: pieceBorders180.getPieces()) {
		    	                        			hm180.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm180.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm180.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm180.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
		    	                        		}
		    	                        		
		    	                        		for(Piece p: pieceCorners180.getPieces()) {
		    	                        			hm180.computeIfAbsent(p.getTopSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm180.computeIfAbsent(p.getRightSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm180.computeIfAbsent(p.getBottomSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm180.computeIfAbsent(p.getLeftSignature(),k-> new ListePieces()).addPiece(p);

		    	                        		}
		    	                        		
		    	                        		HashMap<String, ListePieces> hm270= new HashMap<String, ListePieces>();
		    	                        		for (Piece p: pieceBorders270.getPieces()) {
		    	                        			hm270.computeIfAbsent(p.getTopSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm270.computeIfAbsent(p.getRightSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm270.computeIfAbsent(p.getBottomSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			hm270.computeIfAbsent(p.getLeftSignature(), k -> new ListePieces()).addPiece(p);
		    	                        			
		    	                        		}
		    	                        		
		    	                        		for(Piece p: pieceCorners270.getPieces()) {
		    	                        			hm270.computeIfAbsent(p.getTopSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm270.computeIfAbsent(p.getRightSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm270.computeIfAbsent(p.getBottomSignature(),k-> new ListePieces()).addPiece(p);
		    	                        			hm270.computeIfAbsent(p.getLeftSignature(),k-> new ListePieces()).addPiece(p);

		    	                        		}
		    	                        	
		    	                        		
		    	                        		HashMap<String, ListePieces> hmfusion = mergeHashMaps(hm,hm90,hm180,hm270);
		    	                        		HashMap<String, ListePieces> hmInnerMerge = mergeHashMaps(innerMap,innerMap90,innerMap180,innerMap270); 
		    	                        		Set<String> visitedStates1 = new HashSet<>();
		    	                        		Set<String> visitedStates2 = new HashSet<>();


		    	                        		
		    	                    
		    	                        		
		    	                        		
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
		    	                        		ResolveBorder(hmfusion,finalList,count,direction,allOuterPieces,pieceCorners,pieceCorners90,pieceCorners180,pieceCorners270,pieceBorders,puzzle,directions,visitedStates1);
		    	                        		
		    	                        		int piecesNumber=puzzle.getl()*puzzle.getL();
		    	                        		int totalPieces=innerList.getPieces().size()+pieceBorders.getPieces().size()+pieceCorners.getPieces().size();
		    	                        
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
		    	                        			
		    	                        			
		    	                       
		    	                        			
		    	                        			int w=0;
		    	                        			for (int i=0;i<puzzle.getl();i++) {
		    	                        				
		    	                        				puzzle.setCase(finalList.getPieces().get(w),i,0);
		    	                        				w++;
		    	                        			}

		    	                        			for (int j=1;j<puzzle.getL();j++) {
		    	                        				puzzle.setCase(finalList.getPieces().get(w), puzzle.getl()-1, j);
		    	                        				w++;
		    	                        			}

		    	                        			for (int k=puzzle.getl()-2;k>=0;k--) {
		    	                        				puzzle.setCase(finalList.getPieces().get(w), k, puzzle.getL()-1);
		    	                        				w++;
		    	                        			}

		    	                        			for (int n=puzzle.getL()-2;n>0;n--) {
		    	                        				puzzle.setCase(finalList.getPieces().get(w), 0,n);
		    	                        				w++;
		    	                        			}

		    	                        			
		    	                        			
		    	                        		resolveInner(puzzle,hmInnerMerge,allInnerPieces,1,1,puzzle.getL()-1,puzzle.getl()-1,tentatives,visitedStates2);
		    	                        			
		    	                       
		    	                        		}
		    	                        		
		    	        
		    	                        		
		    	                       ArrayList<String> tentatives1 = new ArrayList<>();	
		    	                       ouvrirFenetrePuzzle(puzzle,tentatives1,null);
		    		                }
		    	                     else {
		    	                    	listView.getItems().add("Aucun fichier trouvé !");
		    	                    }
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
		            		
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
	
	
	
	private Boolean ResolveBorder(HashMap<String, ListePieces> hmfusion, ListePieces finalList, int count, String direction,ListePieces allOuterPieces, ListePieces pieceCorners,ListePieces pieceCorners90,ListePieces pieceCorners180,ListePieces pieceCorners270,ListePieces pieceBorders, Puzzle dimImg,ArrayList<String> directions,Set<String> visitedStates1) {
		Piece p1=finalList.getPieces().getLast();
		String cleEtat = construireCleEtat(finalList, direction);
		if (visitedStates1.contains(cleEtat)) {
		    return false;
		}
		visitedStates1.add(cleEtat);
    		String Signature = null;
    		String oppositeDirection=p1.oppositeDirection(direction);
    		if(pieceCorners.containsPiece(p1)||pieceCorners90.containsPiece(p1)||pieceCorners180.containsPiece(p1)||pieceCorners270.containsPiece(p1)) {
    			count++;
    			if(dimImg.getl()==0 || dimImg.getl()==1 || dimImg.getl()==2) {
    				dimImg.setl(count);
    			}
    			else if(dimImg.getL()==0) {
    				dimImg.setL(count);
    			}
    			count=0;
    			direction=changeDirection(oppositeDirection,p1);
    			directions.add(direction);
    			oppositeDirection=p1.oppositeDirection(direction);
    		}
    		
    		
    		
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
    		
    		ListePieces candidats=hmfusion.get(Signature);
    		
     		ListePieces candidats2=filterByUsed(candidats);
     		
    		if(candidats2.isEmpty()) {
    			
    			if(finalList.getPieces().size()== pieceCorners.getPieces().size() + pieceBorders.getPieces().size()) {
    				
    				
    		        Piece first = finalList.getPieces().getFirst();
    		        Piece last = finalList.getPieces().getLast();
    		        
    		        String sigLast = null;
    		        String sigFirst = null;
    		        String lastDirection=directions.getLast();
    		        
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
    			count++;
    			p.setState(true);
    			finalList.addPiece(p);
    			String prefix = p.getNom().substring(0, Math.min(7, p.getNom().length()));
    			marquageWithPrefix(allOuterPieces,prefix);
    			
    			Boolean success=ResolveBorder(hmfusion,finalList,count,direction,allOuterPieces,pieceCorners,pieceCorners90,pieceCorners180,pieceCorners270,pieceBorders,dimImg,directions,visitedStates1);
    			if (success) { return true;}
    			else {
    				p.setState(false);
    				finalList.removePiece(p);
    			}
    		}
    		return false;		
		
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
	public static ListePieces mergeListePieces(ListePieces... listes) {
	    ListePieces fusion = new ListePieces();
	    Set<String> nameAlreadyIn = new HashSet<>();

	    for (ListePieces lp : listes) {
	        if (lp != null) {
	            for (Piece p : lp.getPieces()) {
	                String nom = p.getNom();
	                if (!nameAlreadyIn .contains(nom)) {
	                    fusion.addPiece(p);
	                    nameAlreadyIn .add(nom);
	                }
	            }
	        }
	    }

	    return fusion;
	}
	@SafeVarargs
	public static HashMap<String, ListePieces> mergeHashMaps(HashMap<String, ListePieces>... maps) {
		
		    HashMap<String, ListePieces> fusion = new HashMap<>();
		    for (HashMap<String, ListePieces> map : maps) {
		        for (Map.Entry<String, ListePieces> entry : map.entrySet()) {
		            String key = entry.getKey();
		            ListePieces pieces = entry.getValue();

		            fusion.computeIfAbsent(key, k -> new ListePieces()).addAll(pieces); 
		        }
		    }

		    return fusion;
		}


	public void marquageWithPrefix(ListePieces toutesLesPieces, String prefix) {
	    for (Piece p : toutesLesPieces.getPieces()) {
	        if (p.getNom().startsWith(prefix)) {
	            p.setState(true);
	        }
	    }
	}
	private String construireCleEtat(ListePieces finalList, String direction) {
	    return direction + "|" + finalList.getPieces().stream()
	        .map(Piece::getNom)
	        .collect(Collectors.joining(","));
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
	
	private Boolean resolveInner(Puzzle puzzle, HashMap<String, ListePieces> hmInnerMerge, ListePieces allInnerPieces, int y, int x, int ymax, int xmax,ArrayList<String> tentatives,Set<String> visitedStates2) {
		
		
		if (x >= xmax) {
	        return true; 
	    }
		
		if(y>=ymax) {
			return resolveInner(puzzle,hmInnerMerge,allInnerPieces,1,x+1,ymax,xmax,tentatives,visitedStates2);
		}
		String stateKey = generateMemoKey(puzzle, x, y);
	    if (visitedStates2.contains(stateKey)) {
	        return false;
	    }
	    visitedStates2.add(stateKey);
	    
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
		 ListePieces candidats=getCandidatsBySignature(hmInnerMerge,leftSig,topSig,bottomSig,rightSig);
		 if (candidats.isEmpty()) return false;
		 
		 ListePieces filteredCandidates=filterByUsed(candidats);
		 if (filteredCandidates.isEmpty()) return false;
		 
		 for (Piece p:filteredCandidates.getPieces()) {
			 p.setState(true);
			 puzzle.setCase(p, x, y);
			 String prefix = p.getNom().substring(0, Math.min(7, p.getNom().length()));
 			 marquageWithPrefix(allInnerPieces,prefix);
			 
			 if (resolveInner(puzzle, hmInnerMerge,allInnerPieces, y + 1, x, ymax, xmax,tentatives,visitedStates2)) {
		            return true; 
		        }


		        p.setState(false);
		        puzzle.setCase(null,x, y);
		 }
		 
		tentatives.add("tentative#"+x+y); 
		return false;
	}
	
	
	private ListePieces getCandidatsBySignature(HashMap<String, ListePieces> hm, String leftSig, String topSig, String bottomSig, String rightSig) {
	    Set<Piece> candidats = null;

	    
	    if (leftSig != null) {
	        Set<Piece> leftSet = new HashSet<>();
	        for (Piece p : hm.getOrDefault(leftSig, new ListePieces()).getPieces()) {
	            if (leftSig.equals(p.getLeftSignature())) leftSet.add(p);
	        }
	        candidats = leftSet;
	    }

	    if (topSig != null) {
	        Set<Piece> topSet = new HashSet<>();
	        for (Piece p : hm.getOrDefault(topSig, new ListePieces()).getPieces()) {
	            if (topSig.equals(p.getTopSignature())) topSet.add(p);
	        }
	        if (candidats == null) candidats = topSet;
	        else candidats.retainAll(topSet);
	        if (candidats.isEmpty()) return new ListePieces(); 
	    }

	    if (bottomSig != null) {
	        Set<Piece> bottomSet = new HashSet<>();
	        for (Piece p : hm.getOrDefault(bottomSig, new ListePieces()).getPieces()) {
	            if (bottomSig.equals(p.getBottomSignature())) bottomSet.add(p);
	        }
	        if (candidats == null) candidats = bottomSet;
	        else candidats.retainAll(bottomSet);
	        if (candidats.isEmpty()) return new ListePieces();
	    }

	    if (rightSig != null) {
	        Set<Piece> rightSet = new HashSet<>();
	        for (Piece p : hm.getOrDefault(rightSig, new ListePieces()).getPieces()) {
	            if (rightSig.equals(p.getRightSignature())) rightSet.add(p);
	        }
	        if (candidats == null) candidats = rightSet;
	        else candidats.retainAll(rightSet);
	        if (candidats.isEmpty()) return new ListePieces();
	    }

	    ListePieces result = new ListePieces();
	    if (candidats != null) {
	        for (Piece p : candidats) {
	            result.addPiece(p);
	        }
	    }
	    return result;
	
	}
	private String generateMemoKey(Puzzle puzzle, int x, int y) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(x).append(":").append(y).append("|");

	    for (int i = 0; i < puzzle.getl(); i++) {
	        for (int j = 0; j < puzzle.getL(); j++) {
	            Piece p = puzzle.getCase(i, j);
	            if (p != null) {
	                sb.append(i).append("-").append(j).append("=").append(p.getNom()).append(",");
	            }
	        }
	    }

	    return sb.toString();
	}


	
	
	public static void main(String[] args) {
		launch(args);
	}
}