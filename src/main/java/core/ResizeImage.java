package core;

import static java.util.Objects.requireNonNull;
import static org.imgscalr.Scalr.Method.ULTRA_QUALITY;
import static org.imgscalr.Scalr.Mode.FIT_EXACT;
import static org.imgscalr.Scalr.resize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ResizeImage {

    public static void main(String [] args) throws IOException {
        //resizeImage("series", 40, false);
        resizeImage("cards", 300, true);
    }

    private static void resizeImage(String folderName, int newSize, boolean widthFirst) throws IOException {
        final File folder = new File("/Users/grillett/dev/card-assembler/website/images/full/");

        Arrays.stream(requireNonNull(folder.listFiles())).parallel().forEach(file -> {
            try {
                if (!file.getName().equals(".DS_Store")) {

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
                        new File(String.format("/Users/grillett/dev/card-assembler/website/images/min_m/%s", file.getName())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
