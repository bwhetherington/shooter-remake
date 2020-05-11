import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpriteUtil {
	private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
	
	public static void preloadSpriteSheet(String id, BufferedImage image,
			int width, int height, int frames, Animation[] animations) {
		spriteSheets.put(id, new SpriteSheet(image, width, height, frames, animations));
	}

	public static SpriteSheet loadSpriteSheet(String id) {
		return spriteSheets.get(id);
	}
	
	public static BufferedImage generateImage(SpriteSheet sprite) {
		Dimension size = sprite.getSize();
		int rows = sprite.getRows(), cols = sprite.getCols(),
			width = cols * size.width, height = rows * size.height,
			frame;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				frame = row * cols + col;
				g.drawImage(sprite.getFrame(frame), col * size.width, row * size.height, null);
			}
		}
		return image;
	}
}	
