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

		// Generate derivative arrays
		System.err.println("Generating arrays...");
		int[][] rgb = glitch.makeRGB(in);
		int[][] lum = glitch.makeLum(rgb);

		// Sort arrays
		System.err.println("Sorting...");
		glitch.dependentQuicksort(rgb, lum);
		glitch.rewriteImg(in, rgb);

		// Block glitch image
		System.err.println("Block glitching...");
		for (int i = 500; i <= 1500; i += 500) {
			glitch.blockGlitch(rgb, i, i);
		}
		glitch.rewriteImg(in, rgb);

		// Save manipulated image
		System.err.println("Saving final image...");
		glitch.save(in, outName);

		System.err.println("Done!");

	}
}
