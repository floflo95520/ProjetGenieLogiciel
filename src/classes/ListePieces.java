package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	
}
