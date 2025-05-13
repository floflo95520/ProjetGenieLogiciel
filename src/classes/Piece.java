package classes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Piece {
	private BufferedImage img;
	private String nom;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public int[][] largestRectangle() {
	    int width = img.getWidth();
	    int height = img.getHeight();
	    int[][] binaryMatrix = new int[height][width];

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            int argb = img.getRGB(x, y);
	            int alpha = (argb >> 24) & 0xff;
	            binaryMatrix[y][x] = (alpha != 0) ? 1 : 0;
	        }
	    }

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
	
	public String setSide(String side, int x, int y) throws Exception {
			if(side!=null) {
				StringBuilder SB = new StringBuilder();
				switch(side) {
					case "top":
						while(x>0 && ((img.getRGB(x-1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y-1) >> 24) & 0xff)>0 ) {
							y--;
						}
						while(x<img.getWidth()-1) {
							x++;
							for(int dy=-1; dy<=1;dy++) {
								int y1=y+dy;
								
							}
						}
					case "left":
						while(x>0 && ((img.getRGB(x-1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y-1) >> 24) & 0xff)>0 ) {
							y--;
						}
					case "right":
						while(x>0 && ((img.getRGB(x+1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y+1) >> 24) & 0xff)>0 ) {
							y--;
						}
					case "bottom":
						while(x>0 && ((img.getRGB(x+1, y) >> 24) & 0xff)>0 ) {
							x--;
						}
						while(y>0 && ((img.getRGB(x, y+1) >> 24) & 0xff)>0 ) {
							y--;
						}
					default:
						throw new Exception("Erreur dans la configuration des pièces");
						
			} 

				
				
	}
			return SB.toString();
}
	
}
