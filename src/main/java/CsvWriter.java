/**
 * Esta clase proporciona funcionalidad para escribir registros en un archivo CSV.
 * Permite agregar nuevos registros al archivo CSV especificado.
 *
 * @author Fabián Gutiérrez Jiménez
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class CsvWriter {
    private String filePath;

    /**
     * Crea una instancia de la clase CsvWriter para escribir registros en un archivo CSV.
     *
     * @param filePath La ruta del archivo CSV donde se agregarán los registros.
     */
    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Escribe un conjunto de entradas como un nuevo registro en el archivo CSV.
     *
     * @param entries Un arreglo de cadenas que representan las entradas del registro.
     */
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
