package Game1;

//Interface to allow polymorphism for objects with attached light sources
public interface LightSource {

	public double getSourceX();
	public double getSourceY();
	public Light getSource();
	public int getCurrentMap();
}
