import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class FractalSaver
{
	public static void save(File file, AbstractFractalDrawer drawer, Dimension dim) throws Exception
	{
		BufferedImage img = new BufferedImage(
				dim.width,
				dim.height,
				BufferedImage.TYPE_INT_RGB);

		drawer.draw(img.createGraphics(), dim);

		// TODO: rename file if existing(and maybe move this code in another class)
		ImageIO.write(img, "jpg", file);
	}
}
