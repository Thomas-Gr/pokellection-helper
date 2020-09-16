package core;

import static java.util.Objects.requireNonNull;
import static org.imgscalr.Scalr.Method.ULTRA_QUALITY;
import static org.imgscalr.Scalr.Mode.FIT_EXACT;
import static org.imgscalr.Scalr.resize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResizeImage {

    public static void main(String [] args) throws IOException {
        resizeImage("icons", 40, false);
        resizeImage("cards", 250, true);
    }

    private static void resizeImage(String folderName, int newSize, boolean widthFirst) throws IOException {
        final File folder = new File(String.format("./out/images/%s/", folderName));

        for (File file : requireNonNull(folder.listFiles())) {
            if (file.getName().equals(".DS_Store")) continue;

            BufferedImage originalImage = ImageIO.read(file.getAbsoluteFile());
            int height = originalImage.getHeight();
            int width = originalImage.getWidth();

            int newWidth, newHeight;

            if (width > height || widthFirst) {
                newWidth = newSize;
                newHeight = height * newSize / width;
            } else {
                newWidth = height * newSize / width;
                newHeight = newSize;
            }

            BufferedImage scaledImage = resize(originalImage, ULTRA_QUALITY, FIT_EXACT, newWidth, newHeight);

            String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

            ImageIO.write(
                    scaledImage,
                    extension,
                    new File(String.format("./out/images/resized_%s/%s", folderName, file.getName())));
        }
    }
}
