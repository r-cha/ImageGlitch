import java.awt.image.BufferedImage;

public class ImageManip {
	public static void main(String[] args) {

		String inName = "in.jpg";
		String outName = "out.jpg";
		final int glitches = 30;



		ImageGlitcher glitch = new ImageGlitcher();	
		//ResourceGenerator gen = new ResourceGenerator();

		// Import image
		BufferedImage in = glitch.load(inName);

		// GenerateSubImg testing!
		for (int i = 0; i < glitches; i++) {
			BufferedImage sub = glitch.generateRandomSubImg(in);
			int[][] rgb = glitch.generateRGB(sub);
			int[][] lum = glitch.generateLum(rgb);

			glitch.dependentRowSelectionSort(rgb, lum);
			glitch.rewriteImg(sub, rgb);
		}


		// Save manipulated image
		glitch.save(in, outName);

		System.err.println("Done!");

	}
}
