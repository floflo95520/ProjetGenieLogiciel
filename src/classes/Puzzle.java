package classes;

public class Puzzle {
		public Piece[][] puzzle;
		private int l;
		private int L;
		
		public Puzzle() {
			this.l=0;
			this.L=0;
		}
		public Puzzle(int l, int L) {
			this.puzzle=new Piece[L][l];
			this.l=l;
			this.L=L;
		}
		public int getl() {
			return l;
		}
		public void setl(int l) {
			this.l = l;
		}
		public int getL() {
			return L;
		}
		public void setL(int l) {
			L = l;
		}
		public Piece getCase(int l,int L) {
			return this.puzzle[L][l];
		}
		public void setCase(Piece p, int l, int L) {
			this.puzzle[L][l]=p;
		}
}
