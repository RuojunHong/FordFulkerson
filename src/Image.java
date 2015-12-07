import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class Image {
	BufferedImage img;
	int[][] intensityMatrix;
	int lamda = 0;
	int meanSource = 0;
	int meanSink = 0;
	
	public Image(){
		
	}
	public Image(String fileName,int lamda,int meanSource, int meanSink) throws IOException {
		try {
			img = ImageIO.read(new File(fileName));
			this.intensityMatrix = new int[img.getWidth()][img.getHeight()];

			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					int rgb = img.getRGB(i, j);
					int intensity = (rgb) & 0xff;
					this.intensityMatrix[i][j] = intensity;
					System.out.println("pixel: " + intensity);
				}
			}
		} catch (IOException e) {
			System.out.println(fileName + " doesn't exist.");
		}
		this.lamda=lamda;
		this.meanSource=meanSource;
		this.meanSink = meanSink;
	}

	public void writeToFile(String filename) throws IOException {
		PrintWriter pw = new PrintWriter(filename);
		for(int i = 0;i<this.intensityMatrix.length;i++){
			for(int j=0;j<this.intensityMatrix[0].length;j++){
					pw.println("s "+i+"_"+j+" "+Math.pow((this.intensityMatrix[i][j]-this.meanSource),2));
					pw.println(i+"_"+j+" t "+Math.pow((this.intensityMatrix[i][j]-this.meanSink),2));
			}
		}
		
		for(int i = 0;i<this.intensityMatrix.length;i++){
			for(int j = 0;j<this.intensityMatrix[0].length;j++){
				try{
				pw.println(i+"_"+j+" "+(i+1)+"_"+j+" "+lamda);
				}catch(ArrayIndexOutOfBoundsException e){	
				}
				try{
				pw.println(i+"_"+j+" "+(i-1)+"_"+j+" "+lamda);
				}catch(ArrayIndexOutOfBoundsException e){	
				}
				try{
				pw.println(i+"_"+j+" "+i+"_"+(j+1)+" "+lamda);
				}catch(ArrayIndexOutOfBoundsException e){	
				}
				try{
				pw.println(i+"_"+j+" "+i+"_"+(j-1)+" "+lamda);
				}catch(ArrayIndexOutOfBoundsException e){	
				}			}
		}
		pw.close();
	}

	public void drawImage(int[][] newIntensityMatrix) throws IOException {
		BufferedImage newImg = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < newImg.getWidth(); i++) {
			for (int j = 0; j < newImg.getHeight(); j++) {
				int rgb = ((newIntensityMatrix[i][j] & 0x0ff) << 16)
						| ((newIntensityMatrix[i][j] & 0x0ff) << 8)
						| (newIntensityMatrix[i][j] & 0x0ff);
				newImg.setRGB(newImg.getWidth()-i-1, j, rgb);
			}
		}
		File outputfile = new File("testFlipped.jpg");
		ImageIO.write(newImg, "jpg", outputfile);
	}
	
	
	public static void main(String[] args) throws Exception {
		Image i = new Image("test.jpg",20,10,250);
		i.drawImage(i.intensityMatrix);
		i.writeToFile("test.txt");
		FordFulkerson f = new FordFulkerson("test.txt");
		f.run("s", "t");
		//f.displayDotFile();
	}
}
