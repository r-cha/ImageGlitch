import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

public class ImageGlitcher {

	/**
	 * creates an array of int values that represents the rgb value of each pixel of an image
	 * @param in - a BufferedImage object
	 * @return an array of int rgb values
	 */
	public int[][] generateRGB(BufferedImage in) {
		int[][] rgb = new int[in.getHeight()][in.getWidth()];
		for (int y = 0; y < in.getHeight(); y++) {
			for (int x = 0; x < in.getWidth(); x++) {
				rgb[y][x] = in.getRGB(x, y);
			}
		}
		return rgb;
	}

	/**
	 * creates an array of int values that represent the luminance
	 * uses (.299 * R + .598 * G + .114 * B) to calculate luminance
	 * @param in - a BufferedImage object
	 * @return an array of int luminance values
	 */
	public int[][] generateLum(BufferedImage in) {
		int[][] lume = new int[in.getHeight()][in.getWidth()];
		for (int y = 0; y < in.getHeight(); y++) {
			for (int x = 0; x < in.getWidth(); x++) {
				lume[y][x] = (int) Math.sqrt((.299 * (in.getRGB(x, y) >> 16 & 0xFF)) + (.587 * (in.getRGB(x, y) >> 8 & 0xFF)) + (.114 * (in.getRGB(x, y) >> 0 & 0xFF)));
			}
		}
		return lume;
	}

	/**
	 * creates an array of int values that represent the luminance
	 * uses (.299 * R + .598 * G + .114 * B) to calculate luminance
	 * @param rgb - an array of int rgb values
	 * @return an array of int luminance values
	 */
	public int[][] generateLum(int[][] rgb) {
		int[][] lume = new int[rgb.length][rgb[0].length];
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length; x++) {
				lume[y][x] = (int) Math.sqrt((.299 * (rgb[y][x] >> 16 & 0xFF)) + (.587 * (rgb[y][x] >> 8 & 0xFF)) + (.114 * (rgb[y][x] >> 0 & 0xFF)));
			}
		}
		return lume;
	}
	
	/**
	 * generate the inverse of the input rgb array
	 * @param rgb - an int array of rgb values
	 * @return an int array of rgb values inverse to the input
	 */
	public int[][] generateInverse(int[][] rgb) {
		int[][] inverse = new int[rgb.length][rgb[0].length];
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length; x++) {
				inverse[y][x] = 0xFFFFFF - rgb[y][x];
			}
		}
		return inverse;
	}


	/**
	 * sets in to the brightness values found in lum
	 * @param lume - an array of luminance values based on in
	 * @return an int array of greysclae rgb values.
	 */
	public int[][] generateRGBbyBrightness(int[][] lume) {
		int[][] rgb = new int[lume.length][lume[0].length];
		for (int y = 0; y < lume.length; y++) {
			for (int x = 0; x < lume[0].length; x++) {
				rgb [y][x] = (lume[y][x] * 0x110000) + (lume[y][x] * 0x1100) + (lume[y][x] * 0x11);
			}
		}
		return rgb;
	}

	/**
	 * SLOW, UNPREDICTABLE
	 * sorts rgb array rows by luminance values
	 * @param rgb - an int arrray of rgb values
	 * @param lume - an int array of luminance values
	 */
	public void dependentSelectionRowSort(int[][] rgb, int[][] lume) {
		int min;
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length - 1; x++) {
				min = x;
				for (int i = 0; i < rgb[0].length; i++) {
					if (lume[y][i] < lume[y][x]) min = i;
				}
				doubleExchange(rgb[y], lume[y], x, min);
			}
		}
	}

	/**
	 * SLOW, UNPREDICTABLE
	 * sorts rgb array columns by luminance values
	 * @param rgb - an int arrray of rgb values
	 * @param lume - an int array of luminance values
	 */
	public void dependentSelectionColSort(int[][] rgb, int[][] lume) {
		int[][] intermediateRGB = new int[rgb[0].length][rgb.length];
		int[][] intermediateLum = new int[rgb[0].length][rgb.length];
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length - 1; x++) {
				intermediateRGB[x][y] = rgb[y][x];
				intermediateLum[x][y] = lume[y][x];
			}
		}
		this.dependentSelectionRowSort(intermediateRGB, intermediateLum);
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length - 1; x++) {
				rgb[y][x] = intermediateRGB[x][y];
				lume[y][x] = intermediateLum[x][y];
			}
		}
	}
	
	/**
	 * NOT SLOW, ACTUALLY DOES ITS JOB
	 * sorts rgb array rows by luminance values
	 * @param rgb - an int arrray of rgb values
	 * @param lume - an int array of luminance values
	 */
	public void dependentRowQuicksort(int[][] rgb, int[][]lume) {
		for (int y = 0; y < rgb.length; y++) {
			dependentQuicksort(rgb[y], lume[y], 0, rgb[y].length - 1);
		}
	}
	
	/**
	 * NOT SLOW, ACTUALLY DOES ITS JOB
	 * sorts rgb array colums by luminance values
	 * @param rgb - an int arrray of rgb values
	 * @param lume - an int array of luminance values
	 */
	public void dependentColQuicksort(int[][] rgb, int[][] lume) {
		int[][] intermediateRGB = new int[rgb[0].length][rgb.length];
		int[][] intermediateLum = new int[rgb[0].length][rgb.length];
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length - 1; x++) {
				intermediateRGB[x][y] = rgb[y][x];
				intermediateLum[x][y] = lume[y][x];
			}
		}
		this.dependentRowQuicksort(intermediateRGB, intermediateLum);
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length - 1; x++) {
				rgb[y][x] = intermediateRGB[x][y];
				lume[y][x] = intermediateLum[x][y];
			}
		}
	}

	/**
	 * sorts array a according to the values in b. assumes identical dimensions
	 * @param a
	 * @param b
	 */
	private static void dependentQuicksort(int[] a, int[] b, int low, int high) {
		if (high <= low) return;
		int j = partition(a, b, low, high);
		dependentQuicksort(a, b, low, j-1);
		dependentQuicksort(a, b, j+1, high);
	}

	/**
	 * partition helper function for dependent quicksort
	 * @param a
	 * @param b
	 * @param low
	 * @param high
	 * @return partition value
	 */
	private static int partition(int[] a, int[] b, int low, int high) {
		int i = low, j = high+1;
		int v = b[low];
		while (true) {
			while (b[++i] < v) if (i == high) break;
			while (v < b[--j]) if (j == low) break;
			if (i >= j) break;
			doubleExchange(a, b, i, j);
		}
		doubleExchange(a, b, low, j);
		return j;
	}

	/**
	 * exchanges the same values in two arrays
	 * @param a - an array
	 * @param b - an array
	 * @param i - an index to be exchanged
	 * @param j - an index to be exchanged
	 */
	private static void doubleExchange(int[] a, int[] b, int i, int j) {
		int temp = b[i];
		b[i] = b[j];
		b[j] = temp;
		temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	/**
	 * replaces each rgb value of in with the corresponding calue from rgb, assuming their dimensions match.
	 * @param in
	 * @param rgb
	 */
	public void rewriteImg(BufferedImage in, int[][] rgb) {
		for (int y = 0; y < in.getHeight(); y++) {
			for (int x = 0; x < in.getWidth(); x++) {
				in.setRGB(x, y, rgb[y][x]);
			}
		}
	}

	/**
	 * selects a random subimage from the input and returns it for manipulation
	 * @param in - a buffered image objec t from which you would like to make a sub image
	 * @return a BufferedImage object
	 */
	public BufferedImage generateRandomSubImg(BufferedImage in) {
		// TODO: make this work
		int x = (int) Math.random() * (in.getWidth() - (in.getWidth() / 8) + (in.getWidth() / 16));
		int y = (int) Math.random() * (in.getHeight() - (in.getHeight() / 8) + (in.getHeight() / 16));
		int w = (int) Math.random() * (250) + 250;
		int h = (int) Math.random() * (250) + 250;
		BufferedImage sub = in.getSubimage(x, y, w, h);
		return sub;
	}

	/**
	 * selects a subimage of in, then mirrors it across x = y
	 * @param in - a BufferedImage obj
	 */
	public void blockFlip(BufferedImage in, int width, int height) {
		int temp;
		int rx = in.getWidth() / 2 - (width / 2);
		int ry = in.getHeight() / 2 - (height / 2);
		int rw = width;
		int rh = height;

		for (int y = 0; y < rh /2; y++) {
			for (int x = 0; x < rw; x++) { 
				temp = in.getRGB(rx + x, ry + y);
				in.setRGB(rx + x, ry + y,  in.getRGB(rw + rx - x, ry + rh - y));
				in.setRGB(rw + rx - x, ry + rh - y, temp);
			}
		}

	}

	/**
	 * selects a subimage of in, then mirrors it across x = y
	 * @param rgb - an array of rgb values
	 */
	public int[][] blockFlip(int[][] a, int width, int height) {
		int temp;
		int[][] rgb = a;
		int rx = rgb[0].length / 2 - (width / 2);
		int ry = rgb.length / 2 - (height / 2);
		int rw = width;
		int rh = height;

		for (int y = 0; y < rh /2; y++) {
			for (int x = 0; x < rw; x++) { 

				temp = rgb[ry + y][rx + x];
				rgb[ry + y][rx + x] = rgb[ry + rh - y][rw + rx - x];
				rgb[ry + rh - y][rw + rx - x] = temp;
			}
		}
		return rgb;
	}
	
	/**
	 * loads a buffered image from a file name
	 * @param fileName - a string of the desired file name
	 * @return a BufferedImage object loaded from input
	 */
	public BufferedImage load(String fileName) {
		BufferedImage in = null;
		try{
			in = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}
	
	/**
	 * saves image in to file
	 * @param in
	 * @param fileName
	 */
	public void save(BufferedImage in, String fileName) {
		JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
		jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpegParams.setCompressionQuality(1f);
		
		try {
			final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
			// specifies where the jpg image has to be written
			writer.setOutput(new FileImageOutputStream(
					new File(fileName)));

			// writes the file with given compression level 
			// from your JPEGImageWriteParam instance
			writer.write(null, new IIOImage(in, null, null), jpegParams);
			writer.dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
