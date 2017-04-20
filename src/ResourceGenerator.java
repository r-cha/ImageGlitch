
public class ResourceGenerator {

	/**
	 * generated a 2D noise field of the desired height
	 * @param width - width of desired noise array
	 * @param height - height of desired noise array
	 * @return an int array of brightness values
	 */
	public int[][] generateNoise(int width, int height) {
		// TODO: make this Perlin instead of true noise
		int[][] perlin = new int[height][width];
		double[][] noise = new double[height][width];
		// generate noise
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				noise[y][x] = Math.random();
			}
		}

		// Map noise values to brightness values
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				noise[y][x] = noise[y][x] * 255;
			}
		}

		// populate "Perlin" array (which is not yet actually perlin) with brightness values
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				perlin[y][x] = (int) noise[y][x];
			}
		}

		return perlin;
	}

	public int[][] generateCheckerboard(int width, int height, int tilesAcross) {
		int[][] checker = new int[height][width];
		int tileWidth = width / tilesAcross;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((x / tileWidth) % 2 == 0) {
					if ((y / tileWidth) % 2 == 0)
						checker[y][x] = 0xFFFFFF;
					else
						checker[y][x] = 0x0;						
				}
				else { 
					if ((y / tileWidth) % 2 == 0)
						checker[y][x] = 0x0;
					else
						checker[y][x] = 0xFFFFFF;
				}
			}
		}
		return checker;
	}

}
