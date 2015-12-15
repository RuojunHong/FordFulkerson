import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class Image {
	BufferedImage img;
	int[][] intensityMatrix;
	int lamda = 0;

	public Image() {

	}

	public Image(String fileName, int lamda)
			throws IOException {
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
		this.lamda = lamda;
	}

	public void writeToFile(String filename) throws IOException {
		PrintWriter pw = new PrintWriter(filename);
		for (int i = 0; i < this.intensityMatrix.length; i++) {
			for (int j = 0; j < this.intensityMatrix[0].length; j++) {
				pw.println("s "
						+ i
						+ "_"
						+ j
						+ " "
						+ Math.abs(
								this.intensityMatrix[i][j] - 255)
								);
				pw.println(i
						+ "_"
						+ j
						+ " t "
						+ Math.abs(
								this.intensityMatrix[i][j] - 0));
			}
		}

		for (int i = 1; i < this.intensityMatrix.length - 1; i++) {
			for (int j = 1; j < this.intensityMatrix[0].length - 1; j++) {

				pw.println(i + "_" + j + " " + (i + 1) + "_" + j + " " + lamda);

				pw.println(i + "_" + j + " " + (i - 1) + "_" + j + " " + lamda);

				pw.println(i + "_" + j + " " + i + "_" + (j + 1) + " " + lamda);

				pw.println(i + "_" + j + " " + i + "_" + (j - 1) + " " + lamda);
			}
		}
		pw.close();
	}

	public void drawBinaryImage(String fileName) throws IOException {

		boolean[][] matrix = new boolean[this.img.getWidth()][this.img
				.getHeight()];
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				String[] str = sCurrentLine.split(" ");
				try {
					if (Integer.parseInt(str[2]) == 0)
						matrix[Integer.parseInt(str[0])][Integer
								.parseInt(str[1])] = true;
					else
						matrix[Integer.parseInt(str[1])][Integer
								.parseInt(str[1])] = false;
				} catch (java.lang.ArrayIndexOutOfBoundsException aiobe) {

				}
			}

		} catch (IOException e) {
			System.out.println("No such file.");
			return;
		}
		BufferedImage newImg = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < newImg.getWidth(); i++) {
			for (int j = 0; j < newImg.getHeight(); j++) {

				int rgb = this.img.getRGB(i, j);

				if (matrix[i][j]) {
					newImg.setRGB(i, j, Color.white.getRGB());
				} else {
					newImg.setRGB(i, j, Color.black.getRGB());
				}

			}
		}
		File outputfile = new File("imageFinished.jpg");
		ImageIO.write(newImg, "jpg", outputfile);
	}

	public static void main(String[] args) throws Exception {
		Image i = new Image(args[0], Integer.parseInt(args[1]));
		i.writeToFile("graph.txt");
		FordFulkerson f = new FordFulkerson("graph.txt");
		f.run("s", "t");
		f.flagNodesToFile("output.txt");
		i.drawBinaryImage("output.txt");
	}
}
