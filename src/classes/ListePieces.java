package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Classe qui gère une collection de pièces de puzzle
 */
public class ListePieces {
    
    /** La liste des pièces de puzzle */
    private List<Piece> pieces;
    private Set<Piece> pieceSet = new HashSet<>();
    
    /**
     * Constructeur de la classe ListePieces
     * Initialise une nouvelle liste vide de pièces
     */
    public ListePieces() {
        this.pieces = new ArrayList<>();
    }
    
    /**
     * Ajoute une pièce à la liste
     * 
     * @param p La pièce à ajouter
     */
    public void addPiece(Piece p) {
        if (!pieceSet.contains(p)) {
            pieces.add(p);
            pieceSet.add(p);
        }
    }
    
    /**
     * Supprime une pièce de la liste
     * 
     * @param p La pièce à supprimer
     */
    public void removePiece(Piece p) {
        if (pieceSet.remove(p)) {
            pieces.remove(p);
        }
    }
    /**
     * permet d'ajouter à la liste une liste complète
     * @param autres
     */
    public void addAll(ListePieces autres) {
        for (Piece p : autres.getPieces()) {
            addPiece(p); 
        }
    }
    
    /**
     * Vérifie si une pièce existe dans la liste
     * 
     * @param piece La pièce à vérifier
     * @return true si la pièce existe, false sinon
     */
    public boolean containsPiece(Piece piece) {
        return pieceSet.contains(piece);
    }
    
    /**
     * Récupère la liste complète des pièces
     * Retourne une copie de la liste pour éviter les modifications externes
     * 
     * @return Une copie de la liste des pièces
     */
    public List<Piece> getPieces() {
        return new ArrayList<>(pieces);
    }
    
    /**
     * Récupère une pièce spécifique par son nom
     * 
     * @param name Le nom de la pièce à rechercher
     * @return La pièce correspondante ou null si non trouvée
     */
    public Piece getPieceByName(String name) {
        for (Piece piece : pieces) {
            if (piece.getNom().equals(name)) {
                return piece;
            }
        }
        return null;
    }
    
    /**
     * Récupère toutes les pièces qui correspondent à une signature donnée
     * 
     * @param signature La signature à rechercher
     * @return Une liste des pièces correspondantes
     */
    public List<Piece> getPiecesBySignature(String signature) {
        List<Piece> matchingPieces = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getTopSignature().equals(signature) ||
                piece.getRightSignature().equals(signature) ||
                piece.getBottomSignature().equals(signature) ||
                piece.getLeftSignature().equals(signature)) {
                matchingPieces.add(piece);
            }
        }
        return matchingPieces;
    }
    
    /**
     * Récupère le nombre total de pièces dans la liste
     * 
     * @return Le nombre de pièces
     */
    public int getNumberOfPieces() {
        return pieces.size();
    }
    
    /**
     * Vérifie si la liste est vide
     * 
     * @return true si la liste est vide, false sinon
     */
    public boolean isEmpty() {
        return pieces.isEmpty();
    }

    
    /**
     * Trie la liste des candidats en fonction de leur score de pixel avec la pièce de référence
     * @param p1 La pièce de référence à considérer
     * @param direction la direction dans laquelle on se dirige pour remplir le bord
     */
    public void sortByPixelScoreDifference(Piece p1, String direction) {
        TreeMap<Double, List<Piece>> scoreMap = new TreeMap<>();

        for (Piece other : this.pieces) {
            double diff = scoreDifference(p1, other, direction);
            

            diff = Math.round(diff * 100.0) / 100.0;


            scoreMap.computeIfAbsent(diff, k -> new ArrayList<>()).add(other);
        }


        List<Piece> sortedList = new ArrayList<>();
        for (List<Piece> group : scoreMap.values()) {
            sortedList.addAll(group);
        }

        this.pieces = sortedList;
    }

    /**
     * Calcule le score de pixels entre la pièce p1 et la pièce other
     * @param p1
     * @param other
     * @param direction
     * @return un double, qui est le score de pixel avec la pièce de référence
     */
    private double scoreDifference(Piece p1, Piece other, String direction) {
        switch (direction) {
            case "top":
                ArrayList<int[]> tab1=p1.getTopScore();
                ArrayList<int[]> tab2=other.getBottomScore();
                double score=0;
                int size = Math.min(tab1.size(), tab2.size()); 

                for (int i = 0; i < size; i++) {
                    int[] rgb1 = tab1.get(i); 
                    int[] rgb2 = tab2.get(i); 

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                    
                
                    score += diffR + diffG + diffB;
                }
                return score;
		case "bottom":
            	ArrayList<int[]> tab1B=p1.getBottomScore();
                ArrayList<int[]> tab2B=other.getTopScore();
                double scoreB=0;
                int sizeB = Math.min(tab1B.size(), tab2B.size());

                for (int i = 0; i < sizeB; i++) {
                    int[] rgb1 = tab1B.get(i);
                    int[] rgb2 = tab2B.get(i);

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                
                    scoreB += diffR + diffG + diffB;
                }
                return scoreB;
            case "left":
            	ArrayList<int[]> tab1L=p1.getLeftScore();
                ArrayList<int[]> tab2L=other.getRightScore();
                double scoreL=0;
                int sizeL = Math.min(tab1L.size(), tab2L.size()); 

                for (int i = 0; i < sizeL; i++) {
                    int[] rgb1 = tab1L.get(i); 
                    int[] rgb2 = tab2L.get(i); 

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                
                    scoreL += diffR + diffG + diffB;
                }
                return scoreL;
            case "right":
            	ArrayList<int[]> tab1R=p1.getRightScore();
                ArrayList<int[]> tab2R=other.getLeftScore();
                double scoreR=0;
                int sizeR = Math.min(tab1R.size(), tab2R.size());

                for (int i = 0; i < sizeR; i++) {
                    int[] rgb1 = tab1R.get(i);
                    int[] rgb2 = tab2R.get(i);

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                
                    scoreR += diffR + diffG + diffB;
                }
                return scoreR;
            default:
                throw new IllegalArgumentException("Direction inconnue : " + direction);
        }
    }
/**
 * Sert à trier la liste des candidats par leur score de pixel avec les pièces voisines
 * @param left pièce de gauche
 * @param top pièce du haut
 * @param bottom pièce du bas
 * @param right pièce de droite
 */
    public void sortByPixelScoreInner(Piece left, Piece top, Piece bottom, Piece right) {
        TreeMap<Double, List<Piece>> scoreMap = new TreeMap<>();

        for (Piece other : this.pieces) {
            double diff = 0;
            diff += scoreDifference(top, other, "bottom");
            diff += scoreDifference(left, other, "right");
            if (bottom != null) {
                diff += scoreDifference(bottom, other, "top");
            }
            if (right != null) {
                diff += scoreDifference(right, other, "left");
            }

            diff = Math.round(diff * 100.0) / 100.0;

            scoreMap.computeIfAbsent(diff, k -> new ArrayList<>()).add(other);
        }


        List<Piece> sortedList = new ArrayList<>();
        for (List<Piece> group : scoreMap.values()) {
            sortedList.addAll(group);
        }

        this.pieces = sortedList;
    }

    /**
     * Fonction qui sert à remplir le bord du puzzle
     * @param hm contient des signatures auxquelles sont associées de listes de pièces
     * @param direction indique la direction dans laquelle on remplit le bord
     * @param pieceCorners liste des coins
     * @param pieceBorders liste des bords
     * @param directions liste des directions enregistrées lors du remplissage
     * @param expectedSize taille de la liste des pièces assemblées attendues à la fin du remplissage
     * @return boolean pour indiquer si le remplissage est correct au fur et à mesure
     */
    public Boolean ResolveBorder(HashMap<String, ListePieces> hm, String direction, ListePieces pieceCorners,ListePieces pieceBorders,ArrayList<String> directions,int expectedSize) {
		Piece p1=this.getPieces().getLast();
		
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

    		
    	if(candidats2.getPieces().size()>1) {
    			TreeMap<Double, Piece> scoreMap = new TreeMap<>();
    			candidats2.sortByPixelScoreDifference(p1,direction);
    		}
    		
    		
    		if(candidats2.isEmpty()) {
    			if(this.getPieces().size()== expectedSize) {

    		        Piece first = this.getPieces().getFirst();
    		        Piece last = this.getPieces().getLast();
    		        
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
    			p.setState(true);
    			this.addPiece(p);
    			Boolean success=ResolveBorder(hm,direction,pieceCorners,pieceBorders,directions,expectedSize);
    			if (success) { return true;}
    			else {
    				p.setState(false);
    				this.removePiece(p);
    			}
    		}
    		return false;		
		
	}

	/**
	 * fonction qui filtre les candidats selon s'ils ont la signature sur le bon côté de la pièce
	 * @param candidats liste des candidats à considérer
	 * @param oppositeDirection direction opposée pour regarder la signature sur les candidats
	 * @param signature celle considérée pour filtrer les candidats
	 * @return result la liste des pieces filtrées
	 */
    
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
		                continue; 
		        }
		        if (candidateSignature != null && !candidateSignature.isEmpty() && candidateSignature.equals(signature)) {
		            result.addPiece(p);
		        }
	} return result;
	}
	
	/**
	 * fonction qui sert à filtrer la liste des candidats selon s'ils ont été déjà utilisés dans le remplissage ou non
	 * @param candidats liste à considérer
	 * @return result la liste filtrée
	 */
	public ListePieces filterByUsed(ListePieces candidats) {
		ListePieces result = new ListePieces();
		
		for (Piece p: candidats.getPieces()) {
			if(!p.getState()) {
				result.addPiece(p);
			}
		}
		return result;
	}
	/**
	 * fonction qui lorsqu'on arrive sur un coin, la direction change et devient l'autre direction possible du coin
	 * @param direction celle actuelle
	 * @param p1 le coin à considérer
	 * @return s nouvelle direction
	 */
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
    
	
}
