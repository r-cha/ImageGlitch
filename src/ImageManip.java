import java.awt.image.BufferedImage;

public class ImageManip {
	public static void main(String[] args) {
		
		ImageGlitcher glitch = new ImageGlitcher();
		// ResourceGenerator gen = new ResourceGenerator();

		// arg0 = inName
		// arg1 to arg n-2 = alterations
		// argn-1 = outName
		
		String inName = args[0];
		BufferedImage in = glitch.load(inName);
		int[][] inRGB = glitch.generateRGB(in);
		
		for (int i = 1; i < args.length - 1; i++) {
			
			if (args[i].equals("jitterX")) {
				
				glitch.jitterX(inRGB, Integer.parseInt(args[i+1]));
				i++;
				glitch.rewriteImg(in, inRGB);
				
			} else if (args[i].equals("jitterY")) {
				
				glitch.jitterY(inRGB, Integer.parseInt(args[i+1]));
				i++;
				glitch.rewriteImg(in, inRGB);
				
			}
			
		}
		
		String outName = args[args.length-1];
		glitch.save(in, outName);

	}
}
