import java.awt.image.BufferedImage;

public class ImageManip {
	public static void main(String[] args) {
		
		String inName = "in.jpg";
		String outName = "out.jpg";

		ImageGlitcher glitch = new ImageGlitcher();	
		ResourceGenerator gen = new ResourceGenerator();

		// Import image
		System.err.println("Reading image...");
		BufferedImage in = glitch.load(inName);

		// Generate derivative arrays
		System.err.println("Generating arrays...");
		int[][] rgb = glitch.generateRGB(in);
		int[][] lum = glitch.generateLum(rgb);

		// Sort arrays
		System.err.println("Sorting...");
		glitch.dependentColQuicksort(rgb, lum);

		// Block glitch image
		System.err.println("Block glitching...");
		for (int i = 500; i <= 1500; i += 500) {
			glitch.blockFlip(rgb, i, i);
		}
		rgb = glitch.generateInverse(rgb);
		
		lum = gen.generateCheckerboard(lum[0].length, lum.length, 15);
		glitch.dependentRowQuicksort(rgb, lum);
		for (int i = 500; i <= 1500; i += 500) {
			glitch.blockFlip(rgb, i, i);
		}

		glitch.rewriteImg(in, rgb);



		// Save manipulated image
		System.err.println("Saving final image...");
		glitch.save(in, outName);

		System.err.println("Done!");

	}
}
