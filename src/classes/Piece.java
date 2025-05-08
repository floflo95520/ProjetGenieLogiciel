package classes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Piece {
	private BufferedImage img;
	private String gauche;
	private String droite;
	private String haut;
	private String bas;
	
	public Piece(String imagePath) {
        try {
            this.img = ImageIO.read(new File(imagePath));  // Charge l'image à partir d'un fichier
            setGauche();
           // setDroite();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public void setGauche() {
		int largeur=img.getWidth();
		int hauteur=img.getHeight();
		
		for(int x=0; x<largeur;x++) {
			
		}
	}
	
}
