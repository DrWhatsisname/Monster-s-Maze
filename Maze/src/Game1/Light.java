package Game1;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//Represents a light source
public class Light {
	

	private double intensity;
	private Color color;
	
	private int[][] lightMap;
	
	//Calculates the light's intensity at all relevant pixels, stores in lightMap
	public Light(double inten, Color c){
		intensity = inten;
		lightMap = new int[(int) (intensity*2+1)][(int) (intensity*2+1)];
		color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 128);
		for (int x = 0; x <= intensity * 2; x++){
			for (int y = 0; y <= intensity * 2; y++){
				double distance = Math.sqrt(Math.pow(x-intensity, 2) + Math.pow(y-intensity, 2));
				double scaledDifference = 255 * (intensity - distance)/intensity;
				lightMap[x][y] = (int) scaledDifference;
			}
		}
	}
	
	public double getIntensity(){
		return intensity;
	}
	
	//Draws the light
	public void draw(BufferedImage shade, BufferedImage hue, double cx, double cy){
		//Draws the light's color
		Graphics2D gHue = hue.createGraphics();
		gHue.setColor(color);
		gHue.fillOval((int)(cx - intensity), (int)(cy - intensity), (int)(2 * intensity), (int)(2 * intensity));
		gHue.dispose();
		
		//Draws the light's shading
		for (int x = (int)(cx - intensity); x <= cx + intensity; x++){
			int bound = (int)(Math.sqrt(Math.pow(intensity, 2) - Math.pow(cx - x, 2)));
			for (int y = (int)cy-bound; y <= (int)cy+bound; y++){
				if (x < shade.getWidth() && y < shade.getHeight() && x > 0 && y > 0){
					int alpha = (shade.getRGB(x, y) >> 24) & 0xff;
					int scaledDifference = lightMap[(int) (x - cx + intensity)][(int)(y - cy + intensity)];
					int newShade = alpha - scaledDifference;
					if (newShade < 0){
						shade.setRGB(x, y, new Color(255, 255, 255, -newShade).getRGB());
					}
					else if (newShade <= 255){
						shade.setRGB(x, y, new Color(0,0,0, newShade).getRGB());
					}
				}
				
			}
			
		}
			
	}
	
	
}
