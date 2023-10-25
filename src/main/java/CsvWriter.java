import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class CsvWriter {
    private String filePath;

    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeCsv(String[] entries) {
        try {
            File file = new File(filePath);
            boolean fileExists = file.exists();
            FileWriter writer = new FileWriter(filePath, true);

            // Concatena los elementos en una sola cadena separada por comas
            String entryLine = String.join(",", entries);

            writer.write(entryLine + "\n");

            System.out.println("Información añadida al archivo CSV.");
            writer.close(); // Cierra el archivo después de escribir
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
