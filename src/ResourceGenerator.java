
public class ResourceGenerator {

	/**
	 * generated a 2D noise field of the desired height
	 * @param width - width of desired noise array
	 * @param height - height of desired noise array
	 * @return an int array of brightness values
	 */
	public int[][] generateNoise(int width, int height) {
		
		int[][] noise = new int[height][width];
		// generate noise
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				noise[y][x] = (int) Math.random() * 255;
			}
		}
		return noise;
	}
	
	/**
	 * generates a checkerboard of black and white
	 * @param width - width in pixels of desired board
	 * @param height - width in height of desired board
	 * @param tilesAcross - number of tiles across desired
	 * @return an int array of rgb values
	 */
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
