package Game1;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

	private ArrayList<Frame> frames;
	private int index;
	private long timeInFrame;
	
	public Animation(){
		index = 0;
		timeInFrame = 0;
		frames = new ArrayList<Frame>();
	}
	
	//Adds a frame to the animation, returns the object so that you can chain the method (eg a.addFrame().addFrame())
	public Animation addFrame(long time, BufferedImage img){
		frames.add(new Frame(time, img));
		return this;
	}
	
	//Updates the frame of the animation, advances the frame if necessary
	public void update(long timePassed){
		timeInFrame += timePassed;
		while (timeInFrame > frames.get(index).getTime()){
			timeInFrame -= frames.get(index).getTime();
			index++;
				if (index >= frames.size()){
					index = 0;
				}
		}
	}
	
	//Resets the animation to the beginning
	public void resetAnimation(){
		index = 0;
		timeInFrame = 0;
	}
	
	//Returns the image of the current frame
	public BufferedImage getImage(){
		return frames.get(index).getImage();
	}
	public int getCurrentFrame(){
		return index;
	}
	
	//Class that holds an image and a length of time, essentially read only struct
	private class Frame{
		private long time;
		private BufferedImage image;
		
		public Frame(long length, BufferedImage sImage){
			time = length;
			image = sImage;
		}
		
		public long getTime(){
			return time;
		}
		
		public BufferedImage getImage(){
			return image;
		}
	}
	
	
}
