import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;

public class ImageGlitcher {

	private Random rand = new Random();
	
	public static final int EDGE_PAD = 0;
	public static final int EDGE_REFLECT = 1;
	public static final int EDGE_WRAP = 2;
	
	public static final int CHANNEL_RED = 16;
	public static final int CHANNEL_GREEN = 8;
	public static final int CHANNEL_BLUE = 0;

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
	public void dependentRowSelectionSort(int[][] rgb, int[][] lume) {
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
	public void dependentColSelectionSort(int[][] rgb, int[][] lume) {
		int[][] intermediateRGB = new int[rgb[0].length][rgb.length];
		int[][] intermediateLum = new int[rgb[0].length][rgb.length];
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length - 1; x++) {
				intermediateRGB[x][y] = rgb[y][x];
				intermediateLum[x][y] = lume[y][x];
			}
		}
		this.dependentRowSelectionSort(intermediateRGB, intermediateLum);
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
	 * selects a random subimage from the input and returns it for manipulation. 
	 * The subimage top left is located somewhere in the center 7/8 of the image, can extend as far as the right edge of the image, and is less than or equal to 1/16 * the height of the image
	 * @param in - a buffered image object from which you would like to make a sub image
	 * @return a BufferedImage object
	 */
	public BufferedImage generateRandomSubImg(BufferedImage in) {
		int x = (int) (rand.nextDouble() * (7 * (in.getWidth() / 8))) + (in.getWidth() / 16);
		int y = (int) (rand.nextDouble() * (7 * (in.getHeight() / 8))) + (in.getHeight() / 16);
		int w = (int) (rand.nextDouble() * (in.getWidth() - x));
		int h = (int) Math.min(((rand.nextDouble() * (in.getHeight() - y)) / 2) + 1, (in.getHeight() / 16));
		BufferedImage sub = in.getSubimage(x, y, w, h);
		return sub;
	}

	/**
	 * mirrors an image across x = y
	 * @param in - a BufferedImage obj
	 */
	public void blockFlip(BufferedImage in) {

		int rh = in.getHeight();
		int rw = in.getWidth();
		int temp;

		for (int y = 0; y < rh / 2; y++) {
			for (int x = 0; x < rw; x++) { 
				temp = in.getRGB(x, y);
				in.setRGB(x, y,  in.getRGB((rw - 1) - x, (rh - 1) - y));
				in.setRGB((rw - 1) - x, (rh - 1) - y, temp);
			}
		}

	}

	/**
	 * mirrors an rgb array across x = y
	 * @param rgb - an array of rgb values
	 */
	public int[][] blockFlip(int[][] a) {
		int temp;
		int[][] rgb = a;
		int rw = a[0].length;
		int rh = a.length;

		for (int y = 0; y < rh /2; y++) {
			for (int x = 0; x < rw; x++) { 

				temp = rgb[y][x];
				rgb[y][x] = rgb[(rh - 1) - y][(rw - 1) - x];
				rgb[(rh - 1) - y][(rw - 1) - x] = temp;
			}
		}
		return rgb;
	}

	/**
	 * shifts all values in a matrix right the specified number of cells
	 * @param a - 2d array to translate
	 * @param distance - number of pixels to shift right
	 * @param edgeMode - what the left edge of the image should look like (use constants in ImageGlitcher class)
	 * @throws IndexOutOfBoundsException
	 */
	public void translateX(int[][] a, int distance, int edgeMode) throws IndexOutOfBoundsException {
		int[][] orig =  a.clone();
		switch (edgeMode) {
		case 0: // pad duplicates the leftmost row in the newly vacated space
			for (int y = 0; y < a.length; y++) {
				for (int x = a[y].length - 1; x >= distance; x--) {
					a[y][x] = orig[y][x - distance];
				}
				for (int x = distance - 1; x >= 0; x--) {
					a[y][x] = orig[y][distance];
				}
			}
			break;
		case 1: // reflect reflects the leftmost columns across the transform line		
			for (int y = 0; y < a.length; y++) {
				for (int x = a[y].length - 1; x >= distance; x--) {
					a[y][x] = orig[y][x - distance];
				}
				for (int x = distance - 1; x >= 0; x--) {
					a[y][x] = orig[y][distance + (distance - x)];
				}
			}
			break;
		case 2: // wrap moves the cropped portion of the image to the newly vacated columns
			// TODO: Currently wraps twice. I assumed using array orig would fix this... it didn't.
			for (int y = 0; y < a.length; y++) {
				for (int x = 0; x < distance; x++) {
					a[y][x] = orig[y][(orig[0].length - distance) + x];
				}
				for (int x = a[y].length - 1; x >= distance; x--) {
					a[y][x] = orig[y][x - distance];
				}
			}
			break;
		}
	}
	
	/**
	 * extracts the given color channel from an rgb array
	 * @param rgb - array to extract from
	 * @param channel - channel to extract (use constants in ImageGlitcher class)
	 * @return a color array (formatted like a lume array)
	 */
	public int[][] extractChannel(int[][] rgb, int channel) {
		int[][] color = new int[rgb.length][rgb[0].length];
		for (int y = 0; y < rgb.length; y++) {
			for (int x = 0; x < rgb[0].length; x++) {
				color[y][x] = rgb[y][x] >> channel & 0xFF;
			}
		}
		return color;
	}
	
	/**
	 * blends three color channels into a single rgb array
	 * @param r - red channel
	 * @param g - green channel
	 * @param b - blue channel
	 * @return an rgb int array
	 */
	public int[][] blendChannels(int[][] r, int[][] g, int[][] b) {
		int[][] rgb = new int[r.length][r[0].length];
		for (int y = 0; y < r.length; y++) {
			for (int x = 0; x < r[0].length; x++) {
				rgb [y][x] = (r[y][x] << 16) + (g[y][x] << 8) + (b[y][x]);
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
