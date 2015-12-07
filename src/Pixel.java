
public class Pixel {
	private int x;
	private int y;
	private int rgb;
	private int label;//binary label,0 or 1
	
	public Pixel(int x,int y, int rgb){
		this.x=x;
		this.y=y;
		this.rgb =rgb;
		if((rgb & 0x0ff)/255<0.5){
			this.label=1;
		}
		else{
			this.label=0;
		}
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getRgb(){
		return this.rgb;
	}
	public int getLabel(){
		return this.label;
	}
	public void switchLabel(){
		if(this.label==0){
			this.label=1;
		}
		else{
			this.label=0;
		}
	}
}
