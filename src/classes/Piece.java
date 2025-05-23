package classes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
/**
 * Cette classe représente une pièce du puzzle.
 * Elle contient des informations à propos de l'image, du fichier à partir de laquelle elle est crée, ainsi que des informations
 * sur chaque bord comme la signature, la séquence de déplacement d'une matrice 2*2 qui sert à la signature, ou encore une liste de triplets rgb
 * des pixels sur la bordure
 * 
 */
public class Piece {

	/**	 img stockée dans la classe BufferedImage */
	private BufferedImage img;

	/** fichier servant à la création de l'image */
	private File fichier;

	/** nom de la piece*/
	private String nom;
	/** signature, sequence et liste de tableaux d'entier contenant des triplets rgb pour le côté droit */
    private String top;
    private String seqTop;
    private ArrayList<int[]> topScore;
    /** idem pour le bas */
    private String bottom;
    private String seqBottom;
    private ArrayList<int[]> bottomScore;
    /** idem pour la gauche */
    private String left;
    private String seqLeft;
    private ArrayList<int[]> leftScore;
    /** idem pour la droite */
    private String right;
    private String seqRight;
    private ArrayList<int[]> rightScore;
    /** attribut qui indique si la pièce est déjà utilisée ou non dans la résolution du puzzle */
    private boolean used;
    /** entier servant à compter le nombre de bords (côtés entièrement plats) de la pièce de puzzle*/
    private int count;
    /** entier indiquant la rotation actuelle de la pièce*/
    private int rotation;
    /** tableau d'entiers 4*2 qui stocke les coins "réels" de la pièce */
    private int[][] corners;
   
    /**
     * Renvoie le nom de la pièce
     * @return l'attribut nom
     */
	public String getNom() {
        return nom;
    }

	/**
	 * Constructeur de la classe pièce afin d'initialiser tous les attributs et de calculer les signatures
	 * ainsi que toutes les informations relatives aux côtés
	 * @param fichier,nom
	 */
	public Piece(File fichier,String nom) throws Exception {
        try {

        	this.fichier=fichier;
            this.img = ImageIO.read(new File(fichier.getPath()));  // Charge l'image à partir d'un fichier

            topScore=new ArrayList<>();
            rightScore=new ArrayList<>();
            bottomScore=new ArrayList<>();
            leftScore=new ArrayList<>();

            int[][] tab=corners(this.img);


 

            this.nom=nom;
            this.corners=tab;
           setSide("top",tab[0][0],tab[0][1],tab[1][0],tab[1][1]);
           setSide("right",tab[1][0],tab[1][1],tab[3][0],tab[3][1]);
           setSide("bottom",tab[2][0],tab[2][1],tab[3][0],tab[3][1]);
           setSide("left",tab[0][0],tab[0][1],tab[2][0],tab[2][1]); 
           count=0;
           rotation=0;
           if(isSingleCharRepeatedUntilUnderscore(seqTop)) {
        	   count++;
        	   top=null;
           }
           if(isSingleCharRepeatedUntilUnderscore(seqRight)) {
        	   count++;
        	   right=null;
           }
           if(isSingleCharRepeatedUntilUnderscore(seqBottom)) {
        	   count++;
        	   bottom=null;
           }
           if(isSingleCharRepeatedUntilUnderscore(seqLeft)) {
        	   count++;
        	   left=null;
           }
           
            this.used = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Calcule les coins réels de l'image
	 * @param img, l'image à considérer
	 * @return un tableau 4*2 contenant ces coins
	 */
	public static int[][] corners(BufferedImage img){
		int height =img.getHeight();
		int width=img.getWidth();
		int[][] tab= new int[4][2];
		int i=0,j=0;
		// Coin en haut à gauche
		while(i<img.getWidth() && j<img.getHeight()&& ((img.getRGB(i, j)>>24) & 0xff) == 0) {
			i++;j++;
		}
		while(i>0 && ((img.getRGB(i-1, j)>>24) & 0xff) >0){
			i--;
		}
		while(j>0 && ((img.getRGB(i, j-1)>>24) & 0xff) >0){
			j--;
		}
		tab[0][0]=i;
		tab[0][1]=j;
		i=width-1;
		j=0;
		while(i>0 && j<img.getHeight()&& ((img.getRGB(i, j)>>24) & 0xff) ==0) {
			i--;j++;
		}
		while(i+1<img.getWidth() && ((img.getRGB(i+1, j)>>24) & 0xff) >0){
			i++;
		}
		while(j>0 && ((img.getRGB(i, j-1)>>24) & 0xff) >0){
			j--;
		}
		tab[1][0]=i;
		tab[1][1]=j;
		i=0;
		j=height-1;
		while(i<width && j>0&& ((img.getRGB(i, j)>>24) & 0xff) ==0) {
			i++;j--;
		}
		while(i>0 && ((img.getRGB(i-1, j)>>24) & 0xff) >0){
			i--;
		}
		while(j+1<height && (((img.getRGB(i, j+1)>>24) & 0xff) >0)){
			j++;
		}
		tab[2][0]=i;
		tab[2][1]=j;
		i=width-1;
		j=height-1;
		while(i>0 && j>0 && ((img.getRGB(i, j)>>24) & 0xff) ==0) {
			i--;j--;
		}
		while(i+1<width && ((img.getRGB(i+1, j)>>24) & 0xff) >0){
			i++;
		}
		while(j+1<height && ((img.getRGB(i, j+1)>>24) & 0xff) >0){
			j++;
		}
		tab[3][0]=i;
		tab[3][1]=j;
		return tab;
		
	}
	
	/*
	 * Permet de calculer les signatures et d'enregistrer les informations sur la couleur de chaque côté
	 * 
	 * @param une chaine de caractère indiquant le côté à considérer
	 * @param les coordonnées du coin duquel on part ainsi que les coordonnées du coin jusqu'où on doit aller
	 */
	public void setSide(String side, int x, int y, int x1, int y1) throws Exception {
			if(side!="") {
				int[][] matrix = new int[2][2];
				int[][] imageMatrix=imageToMatrix(img);
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				switch(side) {
					case "top":
						seqTop=null;
						String t="";
						StringBuilder sTop=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=0;
						matrix[1][0]=0;
						matrix[1][1]=imageMatrix[y][x];
						
						
						while(x<x1) {
							if(matrix[1][1]>0) {
								int rgb=img.getRGB(x, y);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(topScore, r, g, b)) {
							        topScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[1][0]>0) {
								int rgb=img.getRGB(x-1, y);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(topScore, r, g, b)) {
							        topScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][1]>0) {
								int rgb=img.getRGB(x, y-1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(topScore, r, g, b)) {
							        topScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][0]>0){
								int rgb=img.getRGB(x-1, y-1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(topScore, r, g, b)) {
							        topScore.add(new int[] { r, g, b });
							    }
							}
							if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)) {
								sTop.append("R");
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								matrix[1][1]=imageMatrix[y][x+1];
								if(y>0 && x+1<img.getWidth()) {
									matrix[0][1]=imageMatrix[y-1][x+1];
								}
								else {
									matrix[0][1]=0;
								}
								x++;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[1][1]==0 && matrix[0][1]==1)  ) {
								if(t.equals("")) {
									t="1";
								}
								
								sTop.append("U");
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[1][1]=matrix[0][1];
								if(y>1) {
									matrix[0][0]=imageMatrix[y-2][x-1];
									matrix[0][1]=imageMatrix[y-2][x];
								}
								else {
									matrix[0][0]=0;
									matrix[0][1]=0;
								}
								y--;
							}
							else if((matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][1]==1 && matrix[1][0]==0) || (matrix[0][0]==1 && matrix [0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==0)) {
								
								sTop.append("L");
								digest.update("L".getBytes(StandardCharsets.UTF_8));

								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								if(x>1 && y>0) {
									matrix[0][0]=imageMatrix[y-1][x-2];
									matrix[1][0]=imageMatrix[y][x-2];
								}
								else {
									matrix[0][0]=0;
									matrix[1][0]=0;
								}
								x--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==0 && matrix[1][0]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[0][1]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==0)) {
								if(t.equals("")) {
									t="0";
								}
								
								sTop.append("B");
								digest.update("B".getBytes(StandardCharsets.UTF_8));

								matrix[0][0]=matrix[1][0];
								matrix[0][1]=matrix[1][1];
								matrix[1][0]=imageMatrix[y+1][x-1];
								matrix[1][1]=imageMatrix[y+1][x];
								y++;
							}
						} 
						byte[] hash = digest.digest();
						StringBuilder String = new StringBuilder();
						for (byte b : hash) {
						    String hex = Integer.toHexString(0xff & b);
						    if (hex.length() == 1) String.append('0');
						    String.append(hex);
						}
						if(!t.equals("")) sTop.append("_"+t);
						this.top=String.toString();
						this.seqTop=sTop.toString();
						break;
					case "right":
						seqRight="";
						String t1="";
						StringBuilder sRight=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=0;
						matrix[1][0]=imageMatrix[y][x];
						matrix[1][1]=0;
						
						while(y<y1) {
							if(matrix[1][1]>0) {
								int rgb=img.getRGB(x+1, y); 
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(rightScore, r, g, b)) {
							        rightScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[1][0]>0) {
								int rgb=img.getRGB(x, y);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(rightScore, r, g, b)) {
							        rightScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][1]>0) {
								int rgb=img.getRGB(x+1, y-1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(rightScore, r, g, b)) {
							        rightScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][0]>0) {
								int rgb=img.getRGB(x, y-1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(rightScore, r, g, b)) {
							        rightScore.add(new int[] { r, g, b });
							    }
							}
							if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)) {
								if(t1.equals("")) {
									t1="1";
								}
								sRight.append("R");
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								if(x+2<img.getWidth()) {
									matrix[1][1]=imageMatrix[y][x+2];
									matrix[0][1]=imageMatrix[y-1][x+2];
								}
								else {
									matrix[1][1]=0;
									matrix[0][1]=0;
								}
								x++;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[1][1]==0 && matrix[0][1]==1)  ) {
								
								sRight.append("U");
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[1][1]=matrix[0][1];
								matrix[0][0]=imageMatrix[y-2][x];
								if(y>1 && x+1<img.getWidth()) {
									matrix[0][1]=imageMatrix[y-2][x+1];
								}
								else {

									matrix[0][1]=0;
								}
								y--;
							}
							else if((matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][1]==1 && matrix[1][0]==0) || (matrix[0][0]==1 && matrix [0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==0)) {
								if(t1.equals("")) {
									t1="0";
								}
								sRight.append("L");
								digest.update("L".getBytes(StandardCharsets.UTF_8));
								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								if(x>0) {
									matrix[0][0]=imageMatrix[y-1][x-1];
									matrix[1][0]=imageMatrix[y][x-1];
								}
								else {
									matrix[0][0]=0;
									matrix[1][0]=0;
								}
								x--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==0 && matrix[1][0]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[0][1]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==0)) {
								
								sRight.append("B");
								digest.update("B".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[1][0];
								matrix[0][1]=matrix[1][1];
								matrix[1][0]=imageMatrix[y+1][x];
								if(x+1<img.getWidth()) { matrix[1][1]=imageMatrix[y+1][x+1];}
								else {matrix[1][1]=0;}
								y++;
							}
						} 
						byte[] hashR = digest.digest();
						StringBuilder StringR = new StringBuilder();
						for (byte b : hashR) {
						    String hex = Integer.toHexString(0xff & b);
						    if (hex.length() == 1) StringR.append('0');
						    StringR.append(hex);
						}
						if(!t1.equals("")) sRight.append("_"+t1);
						this.right=StringR.toString();
						this.seqRight=sRight.toString();
						break;
					case "bottom":
						seqBottom="";
						String t2="";
						StringBuilder sBottom=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=imageMatrix[y][x];
						matrix[1][0]=0;
						matrix[1][1]=0;
						while(x<x1) {
							if(matrix[1][1]==1) {
								int rgb=img.getRGB(x, y+1); 
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(bottomScore, r, g, b)) {
							        bottomScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[1][0]==1) {
								int rgb=img.getRGB(x-1, y+1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(bottomScore, r, g, b)) {
							        bottomScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][1]==1) {
								int rgb=img.getRGB(x, y);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(bottomScore, r, g, b)) {
							        bottomScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][0]==1) {
								int rgb=img.getRGB(x-1, y);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(bottomScore, r, g, b)) {
							        bottomScore.add(new int[] { r, g, b });
							    }
							}
							if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==0 && matrix[0][1]==1)) {
								sBottom.append("R");
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								matrix[0][1]=imageMatrix[y][x+1];
								if(y+1<img.getHeight()){
									matrix[1][1]=imageMatrix[y+1][x+1];
								}
								else {
									matrix[1][1]=0;
								}
								
								x++;
							}
							else if((matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)  ) {
								if(t2.equals("")) {
									t2="0";
								}
								sBottom.append("U");
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[1][1]=matrix[0][1];
								if(y>0) {
									matrix[0][0]=imageMatrix[y-1][x-1];
									matrix[0][1]=imageMatrix[y-1][x];
								}
								else {
									matrix[0][0]=0;
									matrix[0][1]=0;
								}
								y--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==0 && matrix[1][0]==1) || (matrix[0][0]==0 && matrix [0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1)) {
								sBottom.append("L");
								digest.update("L".getBytes(StandardCharsets.UTF_8));
								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								matrix[0][0]=imageMatrix[y][x-2];
								if(y+1<img.getHeight()) {
									matrix[1][0]=imageMatrix[y+1][x-2];
								}
								else {
									matrix[1][0]=0;
								}
								x--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==1 && matrix[1][0]==0) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[0][1]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1)) {
								if(t2.equals("")) {
									t2="1";
								}
								sBottom.append("B");
								digest.update("B".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[1][0];
								matrix[0][1]=matrix[1][1];
								if(y+2<img.getHeight()) {
									matrix[1][0]=imageMatrix[y+2][x-1];
									matrix[1][1]=imageMatrix[y+2][x];
								}
								else{
									matrix[1][0]=0;
									matrix[1][1]=0;
								}
								
								
								y++;
							}
						} 
						byte[] hashB = digest.digest();
						StringBuilder StringB = new StringBuilder();
						for (byte b : hashB) {
						    String hex = Integer.toHexString(0xff & b);
						    if (hex.length() == 1) StringB.append('0');
						    StringB.append(hex);
						}
						if(!t2.equals(""))sBottom.append("_"+t2);
						this.bottom=StringB.toString();
						this.seqBottom=sBottom.toString();
						break;
					case "left":
						seqLeft="";
				        String t3="";
						StringBuilder sLeft=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=0;
						matrix[1][0]=0;
						matrix[1][1]=imageMatrix[y][x];
						
						while(y<y1) {
							if(matrix[1][1]>0) {
								int rgb=img.getRGB(x, y); 
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(leftScore, r, g, b)) {
							        leftScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[1][0]>0) {
								int rgb=img.getRGB(x-1, y);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(leftScore, r, g, b)) {
							        leftScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][1]>0) {
								int rgb=img.getRGB(x, y-1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(leftScore, r, g, b)) {
							        leftScore.add(new int[] { r, g, b });
							    }
							}
							if(matrix[0][0]>0) {
								int rgb=img.getRGB(x-1, y-1);
								int r=(rgb >> 16) & 0xff;
								int g=(rgb >> 8) & 0xff;
								int b=(rgb) & 0xff;
								if (!containsRGB(leftScore, r, g, b)) {
							        leftScore.add(new int[] { r, g, b });
							    }
							}
							if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==0 && matrix[0][1]==1)) {
								if(t3.equals("")) {
									t3="0";
								}
								sLeft.append("R");
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								matrix[1][1]=imageMatrix[y][x+1];
								if(y>0) {
									matrix[0][1]=imageMatrix[y-1][x+1];
								}
								else {
									matrix[0][1]=0;
								}
								x++;
							}
							else if((matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)  ) {
								sLeft.append("U");
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[1][1]=matrix[0][1];
								matrix[0][1]=imageMatrix[y-2][x];
								if(x>0 && y>1) {
									matrix[0][0]=imageMatrix[y-2][x-1];	
								}
								else {
									matrix[0][0]=0;
								}
								y--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==0 && matrix[1][0]==1) || (matrix[0][0]==0 && matrix [0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1)) {
								if(t3.equals("")) {
									t3="1";
								}
								sLeft.append("L");
								digest.update("L".getBytes(StandardCharsets.UTF_8));
								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								if(x>1) {
									matrix[0][0]=imageMatrix[y-1][x-2];
									matrix[1][0]=imageMatrix[y][x-2];
								}
								else {
									matrix[0][0]=0;
									matrix[1][0]=0;
								}
								x--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==1 && matrix[1][0]==0) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[0][1]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1)) {
								sLeft.append("B");
								digest.update("B".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[1][0];
								matrix[0][1]=matrix[1][1];
								if(x>0) {
									matrix[1][0]=imageMatrix[y+1][x-1];
								}
								else {
									matrix[1][0]=0;
								}
								matrix[1][1]=imageMatrix[y+1][x];
								y++;
							}
						} 
						byte[] hashL = digest.digest();
						StringBuilder StringL = new StringBuilder();
						for (byte b : hashL) {
						    String hex = Integer.toHexString(0xff & b);
						    if (hex.length() == 1) StringL.append('0');
						    StringL.append(hex);
						}
						if(!t3.equals(""))sLeft.append("_"+t3);
						this.left=StringL.toString();
						this.seqLeft=sLeft.toString();
						break;
					default:
						throw new Exception("Erreur dans la configuration des pièces");
						
			} 
				
				
	}
}
	/**
	 * Permet d'accéder à la variable count pour compter le nombre de côtés plats
	 * @return l'attribut count
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * Sert à indiquer l'état de la pièce, si elle est déjà utilisée ou non dans la résolution du puzzle
	 * @return l'attribut used
	 */
	public Boolean getState() {
		return this.used;
	}
	/**
	 * Permet de modifier l'état de la pièce
	 * @param state une valeur booléenne, true ou false
	 */
	public void setState(Boolean state) {
		this.used=state;
	}
	/**
	 * indique si la pièce est une bordure
	 * @return true ou false
	 */
	public Boolean isABorder() {
		if(this.count==1) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * indique si la pièce est un coin
	 * @return true ou false
	 */
	public Boolean isACorner() {
		if(this.count==2) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * permet d'accéder à la signature du côté en haut
	 * @return la signature sous forme de chaine de caractères
	 */
	public String getTopSignature() {
		return this.top; 
	}
	/**
	 * permet d'accéder à la signature du côté gauche
	 * @return la signature sous forme de chaine de caractères
	 */
	public String getLeftSignature() {
		return this.left; 
	}
	/**
	 * permet d'accéder à la signature du côté droit
	 * @return la signature sous forme de chaine de caractères
	 */
	public String getRightSignature() {
		return this.right; 
	}
	/**
	 * permet d'accéder à la signature du côté en bas
	 * @return la signature sous forme de chaine de caractères
	 */
	public String getBottomSignature() {
		return this.bottom; 
	}
	/**
	 * permet d'accéder à la séquence de déplacement de la matrice 2*2 sur le bord en haut
	 * @return la séquence sous forme de chaine de caractères
	 */
	
	public String getSeqTop() {
		return seqTop;
	}
	/**
	 * permet d'accéder à la séquence de déplacement de la matrice 2*2 sur le bord droit
	 * @return la séquence sous forme de chaine de caractères
	 */
	public String getSeqRight() {
		return seqRight;
	}
	/**
	 * permet d'accéder à la séquence de déplacement de la matrice 2*2 sur le bord gauche
	 * @return la séquence sous forme de chaine de caractères
	 */
	public String getSeqLeft() {
		return seqLeft;
	}
	/**
	 * permet d'accéder à la séquence de déplacement de la matrice 2*2 sur le bord en bas
	 * @return la séquence sous forme de chaine de caractères
	 */
	public String getSeqBottom() {
		return seqBottom;
	}
	
	/**
	 * vérifie si la chaine de caractère contient toujours un seul et même caractère
	 * @param str, la chaine à considérer
	 * @return un booléan
	 */
	public static boolean isSingleCharRepeatedUntilUnderscore(String str) {
        if (str == null || str.isEmpty()) return false;

        int underscoreIndex = str.indexOf('_');
        String segment = (underscoreIndex == -1) ? str : str.substring(0, underscoreIndex);

        if (segment.isEmpty()) return false;

        char firstChar = segment.charAt(0);
        for (int i = 1; i < segment.length(); i++) {
            if (segment.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }
	
	
	/**
	 * sert à transformer l'image en matrice de pixels, avec 0 là où les pixels sont transparents et 1 là où ils ne le sont pas
	 * @param img
	 * @return la matrice représentant l'image
	 */
	private int[][] imageToMatrix(BufferedImage img){
		int[][] matrix= new int[img.getHeight()][img.getWidth()];
		for (int y=0; y<img.getHeight(); y++) {
			for (int x=0; x<img.getWidth();x++) {
				int argb = img.getRGB(x, y);
	            int alpha = (argb >> 24) & 0xff;
	            matrix[y][x] = (alpha != 0) ? 1 : 0;
			}
		}

		return matrix;
	}
	
	/**
	 * détermine les directions possible d'un coin
	 * @return un tableau de chaine de caractères de taille 2
	 */
	public String[] directionCorners() {
		String[] s=new String[2];
		if(!isSingleCharRepeatedUntilUnderscore(seqRight)) {
			s[0]="right";
		}
		if(!isSingleCharRepeatedUntilUnderscore(seqLeft)) {
			s[0]="left";
		}
		if(!isSingleCharRepeatedUntilUnderscore(seqTop)) {
			s[1]="top";
		}
		if(!isSingleCharRepeatedUntilUnderscore(seqBottom)) {
			s[1]="bottom";
		}
		return s;
	}
	/**
	 * 
	 * @param direction
	 * @return la direction opposée sous forme de chaîne de caractères
	 */
	public String oppositeDirection(String direction) {
		if(direction.equals("right")) {
			return "left";
		}
		if(direction.equals("left")) {
			return "right";
		}
		if(direction.equals("top")) {
			return "bottom";
		}
		if(direction.equals("bottom")) {
			return "top";
		}
		else {
			return null;
		}
	}
	
	/**
	 * Permet la rotation de la pièce et modifie les attributs concernés
	 * @param input
	 * @param output
	 * @param name
	 * @throws Exception
	 */
	public void rotate90(File input,File output,String name) throws Exception {
	    rotation = (rotation + 90) % 360;

	    if (this.img != null) {
	        this.img = rotate90CounterClockwise(input,output);
	        nom=name;
	        this.corners = corners(this.img);
	        for(int i=0;i<4;i++){
	        	for(int j=0;j<2;j++){
	        		System.out.println(corners[i][j]);// vérifie que corners() retourne bien [4][2]
	        	}
	        }
	        top=null;
	        right=null;
	        bottom=null;
	        left=null;
	        setSide("top",    corners[0][0], corners[0][1], corners[1][0], corners[1][1]);
	        setSide("right",  corners[1][0], corners[1][1], corners[3][0], corners[3][1]);
	        setSide("bottom", corners[2][0], corners[2][1], corners[3][0], corners[3][1]);
	        setSide("left",   corners[0][0], corners[0][1], corners[2][0], corners[2][1]);
	        if (isSingleCharRepeatedUntilUnderscore(seqTop))    top = null;
	        if (isSingleCharRepeatedUntilUnderscore(seqRight))  right = null;
	        if (isSingleCharRepeatedUntilUnderscore(seqBottom)) bottom = null;
	        if (isSingleCharRepeatedUntilUnderscore(seqLeft))   left = null;

	        // Nettoyage des signatures si bord vide

	    }
	}
	/**
	 * tourne l'image au niveau des pixels en transposant la grille de pixels de l'image
	 * @param input
	 * @param output
	 * @return la nouvelle image sous une instance de BufferedImage
	 * @throws IOException
	 */
	public static BufferedImage rotate90CounterClockwise(File input,File output) throws IOException {
		 // Chargement de l'image
        ImageInputStream iis = ImageIO.createImageInputStream(input);
        Iterator<ImageReader> iterator =ImageIO.getImageReaders(iis);
        ImageReader reader= iterator.next();
        String format = reader.getFormatName();
        
        BufferedImage image=ImageIO.read(iis);
        
        int width=image.getWidth();
        int height=image.getHeight();
        
        BufferedImage rotated = new BufferedImage(height,width,image.getType());

        for (int y=0; y<height;y++) {
        	for(int x=0;x<width;x++) {
        		rotated.setRGB(y, (width-1)-x, image.getRGB(x, y));
        	}
        }
        ImageIO.write(rotated, format, output);
		return rotated;
	}



/**
 * 
 * @return la rotation actuelle de l'image
 */
    public int getRotation() {
        return rotation;
    }


/**
 * 
 * @return le tableau contenant les coins de l'image
 */
	public int[][] getCorners() {
		return corners;
	}
/**
 * 
 * @return img, l'image contenue dans la classe BufferedImage
 */
	public BufferedImage getImg() {
		return img;
	}

/**
 * 
 * @return le fichier image
 */
	public File getFichier() {
		return fichier;
	}

/**
 * 
 * @return la liste de triplet rgb pour chaque pixel sur le bord haut
 */
	public ArrayList<int[]> getTopScore() {
		return topScore;
	}
	/**
	 * 
	 * @return la liste de triplet rgb pour chaque pixel sur le bord en bas
	 */

	public ArrayList<int[]> getBottomScore() {
		return bottomScore;
	}
	/**
	 * 
	 * @return la liste de triplet rgb pour chaque pixel sur le bord gauche
	 */

	public ArrayList<int[]> getLeftScore() {
		return leftScore;
	}
	/**
	 * 
	 * @return la liste de triplet rgb pour chaque pixel sur le bord droit
	 */

	public ArrayList<int[]> getRightScore() {
		return rightScore;
	}
	
	/**
	 * Vérifie si le triplet rgb est déjà enregistré dans le tableau
	 * @param list
	 * @param r
	 * @param g
	 * @param b
	 * @return true ou false
	 */
	private boolean containsRGB(ArrayList<int[]> list, int r, int g, int b) {
		if(list!=null) {
	    for (int[] rgb : list) {
	        if (rgb.length == 3 && rgb[0] == r && rgb[1] == g && rgb[2] == b) {
	            return true;
	        }
	    }}
	    return false;
	}

}
