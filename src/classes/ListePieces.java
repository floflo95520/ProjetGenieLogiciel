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
        TreeMap<Double, List<Piece>> scoreMap = new TreeMap<>();

        for (Piece other : this.pieces) {
            double diff = scoreDifference(p1, other, direction);
            
            // arrondir la différence à 2 décimales
            diff = Math.round(diff * 100.0) / 100.0;

            // Ajouter la pièce à la liste associée au score
            scoreMap.computeIfAbsent(diff, k -> new ArrayList<>()).add(other);
        }

        // Fusionner toutes les listes dans l’ordre des scores
        List<Piece> sortedList = new ArrayList<>();
        for (List<Piece> group : scoreMap.values()) {
            sortedList.addAll(group);
        }

        this.pieces = sortedList;
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
            	ArrayList<int[]> tab1B=p1.getBottomScore();
                ArrayList<int[]> tab2B=other.getTopScore();
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
            	ArrayList<int[]> tab1L=p1.getLeftScore();
                ArrayList<int[]> tab2L=other.getRightScore();
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
            	ArrayList<int[]> tab1R=p1.getRightScore();
                ArrayList<int[]> tab2R=other.getLeftScore();
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

	
}
