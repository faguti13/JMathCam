import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.tess4j.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class trial {

    public static void main(String[] args) {
        System.load("C:\\Users\\FabianGJ\\Downloads\\opencv\\build\\java\\x64\\opencv_java480.dll");

        Mat destination = new Mat();
        Mat source = Imgcodecs.imread("C:\\Users\\FabianGJ\\Desktop\\test1.png");

        for (int i = 0; i < 4; i++) {
            destination = new Mat(source.rows(), source.cols(), source.type());
            Imgproc.GaussianBlur(source, destination, new Size(0, 0), 10);
            Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);

            Imgcodecs.imwrite("C:\\Users\\FabianGJ\\Desktop\\destinationImg.jpg", destination);
            source = destination;
        }

        Mat resultMat = new Mat();
        Imgproc.threshold(destination, resultMat, 55, 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite("C:\\Users\\FabianGJ\\Desktop\\resultImg.jpg", resultMat);

        String result = null;
        File imageFile = new File("C:\\Users\\FabianGJ\\Desktop\\test1.png");

        Tesseract instance = new Tesseract();
        instance.setDatapath("C:\\Users\\FabianGJ\\Downloads\\Tess4J\\tessdata");

        try {
            result = instance.doOCR(imageFile);
        } catch (TesseractException ex) {
            Logger.getLogger(trial.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(result);
    }
}
