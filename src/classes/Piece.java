package classes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.imageio.ImageIO;

public class Piece {
	private static BufferedImage img;
	private String nom;

    private String top;
    private String seqTop;
    private String bottom;
    private String seqBottom;
    private String left;
    private String seqLeft;
    private String right;
    private String seqRight;
    private boolean used;

	public String getNom() {
        return nom;
    }

	
	public Piece(String imagePath,String nom) throws Exception {
        try {
            this.img = ImageIO.read(new File(imagePath));  // Charge l'image à partir d'un fichier
            int[][] tab=corners(this.img);
            this.nom=nom;
            System.out.println(nom);
           setSide("top",tab[0][0],tab[0][1],tab[1][0],tab[1][1]);
           setSide("right",tab[1][0],tab[1][1],tab[3][0],tab[3][1]);
           setSide("bottom",tab[3][0],tab[3][1],tab[2][0],tab[2][1]);
           setSide("left",tab[2][0],tab[2][1],tab[0][0],tab[0][1]); 
           
           System.out.println("top= "+top);
           System.out.println("right= "+right);
           System.out.println("bottom= "+bottom);
           System.out.println("left= "+left);

            this.used = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
	public void setSide(String side, int x, int y, int x1, int y1) throws Exception {
			if(side!="") {
				int[][] matrix = new int[2][2];
				int[][] imageMatrix=imageToMatrix(img);
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				switch(side) {
					case "top":
						
						String t="";
						StringBuilder sTop=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=0;
						matrix[1][0]=0;
						matrix[1][1]=imageMatrix[y][x];
						
						
						while(x<x1) {
							System.out.println("x =" +x);
							System.out.println("y=" +y);
							for(int i=0;i<2;i++) {
								for (int j=0;j<2;j++) {
									System.out.println(matrix[i][j]);
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
								if(y>2) {
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
								if(x>2 && y>0) {
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
						String t1="";
						StringBuilder sRight=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=0;
						matrix[1][0]=imageMatrix[y][x];
						matrix[1][1]=0;
						
						while(y<y1) {

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
								if(y>2 && x+1<img.getWidth()) {
									matrix[0][0]=imageMatrix[y-2][x];
									matrix[0][1]=imageMatrix[y-2][x+1];
								}
								else {
									matrix[0][0]=0;
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
								if(x>1) {
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
								if(x+1>=img.getWidth()) matrix[1][1]=0;
								else matrix[1][1]=imageMatrix[y+1][x+1];
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
						String t2="";
						StringBuilder sBottom=new StringBuilder();
						matrix[0][0]=imageMatrix[y][x];
						matrix[0][1]=0;
						matrix[1][0]=0;
						matrix[1][1]=0;
						while(x1<x) {
							if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)) {
								sBottom.append("R");
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								matrix[1][1]=imageMatrix[y+1][x+2];
								if(y!=0) {
									matrix[0][1]=imageMatrix[y][x+2];
								}
								else {
									matrix[0][1]=0;
								}
								x++;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[1][1]==0 && matrix[0][1]==1)  ) {
								if(t2.equals("")) {
									t2="0";
								}
								sBottom.append("U");
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[1][1]=matrix[0][1];
								if(y>2) {
									matrix[0][0]=imageMatrix[y-1][x];
									matrix[0][1]=imageMatrix[y-1][x+1];
								}
								else {
									matrix[0][0]=0;
									matrix[0][1]=0;
								}
								y--;
							}
							else if((matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][1]==1 && matrix[1][0]==0) || (matrix[0][0]==1 && matrix [0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==0)) {
								sBottom.append("L");
								digest.update("L".getBytes(StandardCharsets.UTF_8));
								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								matrix[0][0]=imageMatrix[y][x-1];
								if(y+1<img.getHeight()) {
									matrix[1][0]=imageMatrix[y+1][x-1];
								}
								else {
									matrix[1][0]=0;
								}
								x--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==0 && matrix[1][0]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[0][1]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==0)) {
								if(t2.equals("")) {
									t2="1";
								}
								sBottom.append("B");
								digest.update("B".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[1][0];
								matrix[0][1]=matrix[1][1];
								if(y+2<img.getHeight() && x+1<img.getWidth()) {
									matrix[1][0]=imageMatrix[y+2][x];
									matrix[1][1]=imageMatrix[y+2][x+1];
								}
								matrix[1][0]=0;
								matrix[1][1]=0;
								
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
				        String t3="";
						StringBuilder sLeft=new StringBuilder();
						matrix[0][0]=0;
						matrix[0][1]=imageMatrix[y][x];
						matrix[1][0]=0;
						matrix[1][1]=0;
						
						while(y1<y) {
							if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)) {
								if(t3.equals("")) {
									t3="0";
								}
								sLeft.append("R");
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								matrix[1][1]=imageMatrix[y+1][x+1];
								if(y!=0) {
									matrix[0][1]=imageMatrix[y][x+1];
								}
								else {
									matrix[0][1]=0;
								}
								x++;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[1][1]==0 && matrix[0][1]==1)  ) {
								sLeft.append("U");
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[1][1]=matrix[0][1];
								matrix[0][1]=imageMatrix[y-1][x];
								if(x>0 && y>0) {
									matrix[0][0]=imageMatrix[y-1][x-1];	
								}
								else {
									matrix[0][0]=0;
								}
								y--;
							}
							else if((matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][1]==1 && matrix[1][0]==0) || (matrix[0][0]==1 && matrix [0][1]==1 && matrix[1][0]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==0)) {
								if(t3.equals("")) {
									t3="1";
								}
								sLeft.append("L");
								digest.update("L".getBytes(StandardCharsets.UTF_8));
								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								if(x>2) {
									matrix[0][0]=imageMatrix[y][x-2];
									matrix[1][0]=imageMatrix[y+1][x-2];
								}
								else {
									matrix[0][0]=0;
									matrix[1][0]=0;
								}
								x--;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][1]==0 && matrix[1][0]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[0][1]==0 && matrix[1][1]==0) || (matrix[0][0]==1 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==0)) {
								sLeft.append("B");
								digest.update("B".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[1][0];
								matrix[0][1]=matrix[1][1];
								if(x!=0) {
									matrix[1][0]=imageMatrix[y+2][x-1];
								}
								else {
									matrix[1][0]=0;
								}
								matrix[1][1]=imageMatrix[y+2][x];
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
	
	
	public Boolean getState() {
		return this.used;
	}
	public void setState(Boolean state) {
		this.used=state;
	}
	public String getTopSignature() {
		return this.top; 
	}
	public String getLeftSignature() {
		return this.left; 
	}
	public String getRightSignature() {
		return this.right; 
	}
	public String getBottomSignature() {
		return this.bottom; 
	}
	
	public String getSeqTop() {
		return seqTop;
	}
	public String getSeqRight() {
		return seqRight;
	}
	public String getSeqLeft() {
		return seqLeft;
	}
	public String getSeqBottom() {
		return seqBottom;
	}
	

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
}
