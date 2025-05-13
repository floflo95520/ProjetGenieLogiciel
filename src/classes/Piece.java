package classes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import javax.imageio.ImageIO;

public class Piece {
	private BufferedImage img;
	private String nom;
	private Boolean used;
	private String left;
	private String right;
	private String top;
	private String bottom;
	
	public Piece(String imagePath,String nom) throws Exception {
        try {
            this.img = ImageIO.read(new File(imagePath));  // Charge l'image à partir d'un fichier
            int[][] tab=largestRectangle();
            this.nom=nom;
            System.out.println(this.nom);
            for (int i = 0; i < tab.length; i++) {
                System.out.println("Point " + (i + 1) + " : (" + tab[i][0] + ", " + tab[i][1] + ")");
            }
            
            setSide(left,tab[0][0],tab[0][1]);
            setSide(right,tab[3][0],tab[3][1]);
            setSide(top,tab[0][0],tab[0][1]);
            setSide(bottom,tab[3][0],tab[3][1]);
            used=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public int[][] largestRectangle() {
	    int width = img.getWidth();
	    int height = img.getHeight();
	    int[][] binaryMatrix = imageToMatrix(img);

	    int[] heights = new int[width];
	    int maxArea = 0;
	    int maxLeft = 0, maxRight = 0, maxTop = 0, maxBottom = 0;

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            heights[x] = (binaryMatrix[y][x] == 1) ? heights[x] + 1 : 0;
	        }

	        int[] result = largestRectangleInHistogram(heights);
	        int area = result[0];
	        int left = result[1];
	        int right = result[2];
	        int rectHeight = result[3];

	        if (area > maxArea) {
	            maxArea = area;
	            maxLeft = left;
	            maxRight = right - 1;
	            maxBottom = y;
	            maxTop = y - rectHeight + 1;
	        }
	    }

	    return new int[][] {
	        {maxLeft, maxTop},     // Coin haut gauche
	        {maxRight, maxTop},    // Coin haut droit
	        {maxLeft, maxBottom},  // Coin bas gauche
	        {maxRight, maxBottom}  // Coin bas droit
	    };
	}


	
	private int[] largestRectangleInHistogram(int[] heights) {
	    int n = heights.length;
	    Stack<Integer> stack = new Stack<>();
	    int maxArea = 0;
	    int left = 0, right = 0, height = 0;

	    for (int i = 0; i <= n; i++) {
	        int h = (i == n) ? 0 : heights[i];
	        while (!stack.isEmpty() && h < heights[stack.peek()]) {
	            int top = stack.pop();
	            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
	            int area = heights[top] * width;
	            if (area > maxArea) {
	                maxArea = area;
	                left = stack.isEmpty() ? 0 : stack.peek() + 1;
	                right = i;
	                height = heights[top];
	            }
	        }
	        stack.push(i);
	    }

	    return new int[]{maxArea, left, right, height};
	}
	
	public void setSide(String side, int x, int y) throws Exception {
			if(side!=null) {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				int[][] imageMatrix=imageToMatrix(img);
				switch(side) {
					case "top":
						int x1=x;
						int y1=y;
						while(x>0 && ((img.getRGB(x-1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y-1) >> 24) & 0xff)>0 ) {
							y--;
						}
						while(x1<img.getWidth() && ((img.getRGB(x1+1, y)>>24)& 0xff)>0) {
							x1++;
						}
						while(y1>0 && ((img.getRGB(x,y1-1)>>24)& 0xff)>0) {
							y1--;
						}
						
						int[][] matrix = new int[2][2];
						matrix[0][0]=0;
						matrix[0][1]=0;
						matrix[1][0]=0;
						matrix[1][1]=imageMatrix[y][x];
						while(x<x1) {
							if((matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==0 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==1 && matrix[1][0]==1 && matrix[1][1]==1 && matrix[0][1]==0)) {
								digest.update("R".getBytes(StandardCharsets.UTF_8));
								matrix[0][0]=matrix[0][1];
								matrix[1][0]=matrix[1][1];
								matrix[1][1]=imageMatrix[y][x+1];
								if(y!=0) {
									matrix[0][1]=imageMatrix[y-1][x+1];
								}
								else {
									matrix[0][1]=0;
								}
								x++;
							}
							else if((matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==0 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[0][1]==1 && matrix[1][0]==1 && matrix[1][1]==1) || (matrix[0][0]==0 && matrix[1][0]==0 && matrix[1][1]==0 && matrix[0][1]==1)  ) {
								digest.update("U".getBytes(StandardCharsets.UTF_8));
								matrix[1][0]=matrix[0][0];
								matrix[0][1]=matrix[1][1];
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
								digest.update("L".getBytes(StandardCharsets.UTF_8));
								matrix[0][1]=matrix[0][0];
								matrix[1][1]=matrix[1][0];
								if(x>2) {
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
						this.top=String.toString();
						break;
					case "left":
						while(x>0 && ((img.getRGB(x-1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y-1) >> 24) & 0xff)>0 ) {
							y--;
						}
						break;
					case "right":
						while(x>0 && ((img.getRGB(x+1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y+1) >> 24) & 0xff)>0 ) {
							y--;
						}
						break;
					case "bottom":
						while(x>0 && ((img.getRGB(x+1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y+1) >> 24) & 0xff)>0 ) {
							y--;
						}
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
