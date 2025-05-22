package classes;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*
public class compareImages {
		
	public static int pixelScore(Piece p1, Piece p2, String direction) {
		BufferedImage img1=p1.getImg();
		BufferedImage img2=p2.getImg();
		ArrayList<Integer> p1List= new ArrayList<>();
		ArrayList<Integer> p2List= new ArrayList<>();
		switch(direction) {
			case "top":
				int xt=p1.getCorners()[0][0];
				int yt=p1.getCorners()[0][1];
				int xtmax=p1.getCorners()[1][0];
				int ytmax=p1.getCorners()[1][1];
				int[][] matrixP1=new int[2][2];
				int[][] matrixP2=new int[2][2];
				int xb=p2.getCorners()[2][0];
				int yb=p2.getCorners()[2][1];
				int xbmax=p2.getCorners()[3][0];
				int ybmax=p2.getCorners()[3][1];
				
				matrixP1[0][0]=0;
				matrixP1[0][1]=0;
				matrixP1[1][0]=0;
				matrixP1[1][1]=img1.getRGB(xt, yt);
				
				while(xt<xtmax) {

					if((matrixP1[0][0]==0 && matrixP1[0][1]==0 && matrixP1[1][0]==0 && matrixP1[1][1]==1) || (matrixP1[0][0]==0 && matrixP1[0][1]==0 && matrixP1[1][0]==1 && matrixP1[1][1]==1) || (matrixP1[0][0]==1 && matrixP1[1][0]==1 && matrixP1[1][1]==1 && matrixP1[0][1]==0)) {
						p1List.add()
						matrixP1[0][0]=matrixP1[0][1];
						matrixP1[1][0]=matrixP1[1][1];
						matrixP1[1][1]=img1.getRGB(xt, yt+1);
						if(y>0 && x+1<img.getWidth()) {
							matrixP1[0][1]=imagematrixP1[y-1][x+1];
						}
						else {
							matrixP1[0][1]=0;
						}
						x++;
					}
					else if((matrixP1[0][0]==0 && matrixP1[0][1]==1 && matrixP1[1][0]==0 && matrixP1[1][1]==1) || (matrixP1[0][0]==0 && matrixP1[0][1]==1 && matrixP1[1][0]==1 && matrixP1[1][1]==1) || (matrixP1[0][0]==0 && matrixP1[1][0]==0 && matrixP1[1][1]==0 && matrixP1[0][1]==1)  ) {
						
						sTop.append("U");
						digest.update("U".getBytes(StandardCharsets.UTF_8));
						matrixP1[1][0]=matrixP1[0][0];
						matrixP1[1][1]=matrixP1[0][1];
						if(y>1) {
							matrixP1[0][0]=imagematrixP1[y-2][x-1];
							matrixP1[0][1]=imagematrixP1[y-2][x];
						}
						else {
							matrixP1[0][0]=0;
							matrixP1[0][1]=0;
						}
						y--;
					}
					else if((matrixP1[0][0]==1 && matrixP1[0][1]==1 && matrixP1[1][1]==1 && matrixP1[1][0]==0) || (matrixP1[0][0]==1 && matrixP1 [0][1]==1 && matrixP1[1][0]==0 && matrixP1[1][1]==0) || (matrixP1[0][0]==1 && matrixP1[0][1]==0 && matrixP1[1][0]==0 && matrixP1[1][1]==0)) {
						
						sTop.append("L");
						digest.update("L".getBytes(StandardCharsets.UTF_8));

						matrixP1[0][1]=matrixP1[0][0];
						matrixP1[1][1]=matrixP1[1][0];
						if(x>1 && y>0) {
							matrixP1[0][0]=imagematrixP1[y-1][x-2];
							matrixP1[1][0]=imagematrixP1[y][x-2];
						}
						else {
							matrixP1[0][0]=0;
							matrixP1[1][0]=0;
						}
						x--;
					}
					else if((matrixP1[0][0]==0 && matrixP1[0][1]==0 && matrixP1[1][1]==0 && matrixP1[1][0]==1) || (matrixP1[0][0]==1 && matrixP1[1][0]==1 && matrixP1[0][1]==0 && matrixP1[1][1]==0) || (matrixP1[0][0]==1 && matrixP1[0][1]==1 && matrixP1[1][0]==1 && matrixP1[1][1]==0)) {
						if(t.equals("")) {
							t="0";
						}
						
						sTop.append("B");
						digest.update("B".getBytes(StandardCharsets.UTF_8));

						matrixP1[0][0]=matrixP1[1][0];
						matrixP1[0][1]=matrixP1[1][1];
						matrixP1[1][0]=imagematrixP1[y+1][x-1];
						matrixP1[1][1]=imagematrixP1[y+1][x];
						y++;
					}
				} 
				
		}
	}
}*/
