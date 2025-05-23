package classes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Classe qui représente l'image finale, reconstituée avec toutes les pièces
 */
public class Puzzle {
		/**Matrice de pièces servant à placer les pièces correctement lors de la reconstitution */
		public Piece[][] puzzle;
		/** dimensions de l'image en terme de pièces, l*L*/
		private int l;
		private int L;
		/**
		 * Constructeur pour les dimensions
		 */
		public Puzzle() {
			this.l=0;
			this.L=0;
		}
		/**
		 * Constructeur complet de la classe puzzle
		 * @param l nombre de colonnes de la matrice
		 * @param L nombre de lignes de la matrice
		 */
		public Puzzle(int l, int L) {
			this.puzzle=new Piece[L][l];
			this.l=l;
			this.L=L;
		}
		/**
		 * accéder à l
		 * @return l, la longueur sur l'axe des abscisses
		 */
		public int getl() {
			return l;
		}
		/**
		 * Permet de modifier l
		 * @param l valeur que va prendre l'attribut
		 */
		public void setl(int l) {
			this.l = l;
		}
		/**
		 * accéder à L
		 * @return L, la longueur sur l'axe des ordonnées
		 */
		public int getL() {
			return L;
		}
		/**
		 * Permet de modifier L
		 * @param l valeur que va prendre l'attribut
		 */
		public void setL(int l) {
			this.L = l;
		}
		/**
		 * Récupère la case aux coordonnées l,L
		 * @param l colonne numéro l
		 * @param L ligne numéro L
		 * @return la pièce correspondante
		 */
		public Piece getCase(int l,int L) {
			return this.puzzle[L][l];
		}
		/**
		 * Permet de modifier la pièce à la case [L][l]
		 * @param p piece à insérer dans la matrice
		 * @param l nombre de colonnes 
		 * @param L nombre de lignes
		 */
		public void setCase(Piece p, int l, int L) {
			this.puzzle[L][l]=p;
		}
		
		/**
		 * fonction qui va remplir l'intérieur afin de compléter le puzzle
		 * @param puzzle le puzzle à remplir
		 * @param hm contient toutes les pieces de l'intérieur
		 * @param ymax y max
		 * @param xmax x max
		 * @param tentatives liste des tentatives stockées
		 * @return booléen afin d'indiquer si le remplissage est correct
		 * @throws Exception si on tombe dans une boucle infinie car le bord s'est mal rempli
		 */
		public Boolean resolveInnerIteratif(Puzzle puzzle, HashMap<String, ListePieces> hm, int ymax, int xmax,ArrayList<String> tentatives) throws Exception {
			int maxIterations = 1000000;
			int currentIterations = 0;
		    class State {
		        int x, y;
		        Iterator<Piece> candidates;
		        Piece current;

		        State(int x, int y, ListePieces candidates) {
		            this.x = x;
		            this.y = y;
		            this.candidates = candidates.getPieces().iterator();
		        }
		    }


		    Deque<State> stack = new ArrayDeque<>();
		    int x = 1, y = 1;

		    while (true) {
		    	currentIterations++;
		    	if (currentIterations > maxIterations) {
		    		throw new Exception("Arrêt forcé : trop d'itérations, boucle infinie détectée.");
		    	}
		        if (x >= xmax) return true;
		        if (y >= ymax) {
		            x++;
		            y = 1;
		            continue;
		        }

		        String leftSig = puzzle.getCase(x - 1, y).getRightSignature();
		        String topSig = puzzle.getCase(x, y - 1).getBottomSignature();
		        String bottomSig = (y + 1 == ymax) ? puzzle.getCase(x, y + 1).getTopSignature() : null;
		        String rightSig = (x + 1 == xmax) ? puzzle.getCase(x + 1, y).getLeftSignature() : null;

		        ListePieces candidats = getCandidatsBySignature(hm, leftSig, topSig, bottomSig, rightSig);
		        if (candidats.isEmpty()) {
		            if (stack.isEmpty()) return false;
		            State prev = stack.pop();
		            tentatives.add("Tentative#"+x+y);
		            prev.current.setState(false);
		            puzzle.setCase(null, prev.x, prev.y);
		            x = prev.x;
		            y = prev.y;
		            continue;
		        }

		        ListePieces filtered = candidats.filterByUsed(candidats);
		        
		        if (filtered.isEmpty()) {
		            if (stack.isEmpty()) return false;
		            State prev = stack.pop();
		            tentatives.add("Tentative#"+x+y);
		            prev.current.setState(false);
		            puzzle.setCase(null, prev.x, prev.y);
		            x = prev.x;
		            y = prev.y;
		            continue;
		        }

		        filtered.sortByPixelScoreInner(
		            puzzle.getCase(x - 1, y), puzzle.getCase(x, y - 1),
		            puzzle.getCase(x, y + 1), puzzle.getCase(x + 1, y)
		        );

		        State state = new State(x, y, filtered);
		        boolean placed = false;
		        while (state.candidates.hasNext()) {
		            Piece p = state.candidates.next();
		            
		            if (!p.getState()) {
		                p.setState(true);
		                puzzle.setCase(p, x, y);
		                state.current = p;
		                stack.push(state);
		                y++;
		                placed = true;
		                break;
		            }
		        }

		        if (!placed) {
		            if (stack.isEmpty()) return false;
		            State prev = stack.pop();
		            tentatives.add("Tentative#"+x+y);
		            prev.current.setState(false);
		            puzzle.setCase(null, prev.x, prev.y);
		            x = prev.x;
		            y = prev.y;
		        }
		    }
		}
		
		/**
		 * Récupère les candidats dont les signatures correspondent à ses voisins afin d'en éliminer au maximum
		 * @param hm contient toutes les signatures des pieces à l'intérieur du puzzle
		 * @param leftSig signature gauche recherchée
		 * @param topSig signature haut recherchée
		 * @param bottomSig signature bas recherchée
		 * @param rightSig signature droite recherchée
		 * @return result liste filtrée des pièces
		 */
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

			    ListePieces result = new ListePieces();
			    for (Piece p : intersection) {
			        result.addPiece(p);
			    }

			    return result;
		}
}
