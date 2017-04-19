import java.awt.image.BufferedImage;

public class ImageManip {
	public static void main(String[] args) {
		String inName = "in.jpg";
		String outName = "out.jpg";
		BufferedImage in = null;

		ImageGlitcher glitch = new ImageGlitcher();		

		// Import image
		System.err.println("Reading image...");
		in = glitch.load(in, inName);

		
		// generate noise
		int[][] noise = glitch.generateNoiseArray(in.getWidth(), in.getHeight());
		for (int y = 0; y < in.getHeight(); y++) {
			for (int x = 0; x < in.getWidth(); x++) {
				in.setRGB(x, y, (noise[y][x]));
			}
		}
		


		// Save manipulated image
		System.err.println("Saving final image...");
		glitch.save(in, outName);

		System.err.println("Done!");

	}
}
