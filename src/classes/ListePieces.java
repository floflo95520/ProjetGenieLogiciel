package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Classe qui gère une collection de pièces de puzzle
 */
public class ListePieces {
    
    /** La liste des pièces de puzzle */
    private List<Piece> pieces;
    
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
     * @param piece La pièce à ajouter
     */
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }
    
    /**
     * Supprime une pièce de la liste
     * 
     * @param piece La pièce à supprimer
     */
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }
    
    /**
     * Vérifie si une pièce existe dans la liste
     * 
     * @param piece La pièce à vérifier
     * @return true si la pièce existe, false sinon
     */
    public boolean containsPiece(Piece piece) {
        return pieces.contains(piece);
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

    public void sortByPixelScoreDifference(Piece p1, String direction) {
        TreeMap<Double, Piece> scoreMap = new TreeMap<>();

        for (Piece other : this.pieces) {
           double diff = scoreDifference(p1, other, direction);
           System.out.println("score entre : "+p1.getNom()+ " et " + other.getNom());
            System.out.println(diff);
            // arrondir la différence à 2 décimales pour éviter les doublons imprécis
            diff = Math.round(diff * 100.0) / 100.0;
            // Si plusieurs pièces ont la même diff, garde la première rencontrée
            scoreMap.putIfAbsent(diff, other);
        }

        this.pieces = new ArrayList<>(scoreMap.values());
    }
    
    private double scoreDifference(Piece p1, Piece other, String direction) {
        switch (direction) {
            case "top":
                ArrayList<int[]> tab1=p1.getTopScore();
                ArrayList<int[]> tab2=other.getBottomScore();
                double score=0;
                int size = Math.min(tab1.size(), tab2.size()); // Pour éviter IndexOutOfBounds

                for (int i = 0; i < size; i++) {
                    int[] rgb1 = tab1.get(i); // [r, g, b]
                    int[] rgb2 = tab2.get(i); // [r1, g1, b1]

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                    
                
                    score += diffR + diffG + diffB;
                }
                return score;
		case "bottom":
            	ArrayList<int[]> tab1B=p1.getTopScore();
                ArrayList<int[]> tab2B=other.getBottomScore();
                double scoreB=0;
                int sizeB = Math.min(tab1B.size(), tab2B.size()); // Pour éviter IndexOutOfBounds

                for (int i = 0; i < sizeB; i++) {
                    int[] rgb1 = tab1B.get(i); // [r, g, b]
                    int[] rgb2 = tab2B.get(i); // [r1, g1, b1]

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                
                    scoreB += diffR + diffG + diffB;
                }
                return scoreB;
            case "left":
            	ArrayList<int[]> tab1L=p1.getTopScore();
                ArrayList<int[]> tab2L=other.getBottomScore();
                double scoreL=0;
                int sizeL = Math.min(tab1L.size(), tab2L.size()); // Pour éviter IndexOutOfBounds

                for (int i = 0; i < sizeL; i++) {
                    int[] rgb1 = tab1L.get(i); // [r, g, b]
                    int[] rgb2 = tab2L.get(i); // [r1, g1, b1]

                    int diffR = Math.abs(rgb1[0] - rgb2[0]);
                    int diffG = Math.abs(rgb1[1] - rgb2[1]);
                    int diffB = Math.abs(rgb1[2] - rgb2[2]);
                
                    scoreL += diffR + diffG + diffB;
                }
                return scoreL;
            case "right":
            	ArrayList<int[]> tab1R=p1.getTopScore();
                ArrayList<int[]> tab2R=other.getBottomScore();
                double scoreR=0;
                int sizeR = Math.min(tab1R.size(), tab2R.size()); // Pour éviter IndexOutOfBounds

                for (int i = 0; i < sizeR; i++) {
                    int[] rgb1 = tab1R.get(i); // [r, g, b]
                    int[] rgb2 = tab2R.get(i); // [r1, g1, b1]

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

	
}
