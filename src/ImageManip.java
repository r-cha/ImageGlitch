import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageManip {
	public static void main(String[] args) {

		String inName = "in.jpg";
		String outName = "out.jpg";
		//final int glitches = 30;
		//int task;

		//Random rand = new Random();



		ImageGlitcher glitch = new ImageGlitcher();	
		ResourceGenerator gen = new ResourceGenerator();

		// Import image
		BufferedImage in = glitch.load(inName);

		// Channel extraction testing
		int[][] rgb = glitch.generateRGB(in);
		int[][] sort = gen.generateCheckerboard(rgb[0].length, rgb.length, 10);
		int[][] r = glitch.extractChannel(rgb, ImageGlitcher.CHANNEL_RED);
		int[][] g = glitch.extractChannel(rgb, ImageGlitcher.CHANNEL_GREEN);
		int[][] b = glitch.extractChannel(rgb, ImageGlitcher.CHANNEL_BLUE);
	
		glitch.translateX(r, 600, ImageGlitcher.EDGE_REFLECT);
		glitch.dependentRowQuicksort(g, sort);
		glitch.blockFlip(b);
	
		rgb = glitch.blendChannels(r, g, b);

		glitch.rewriteImg(in, rgb);

		glitch.save(in, outName);


		System.err.println("Done!");

	}
}
