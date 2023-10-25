import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class RecordNode {
    String[] data;
    RecordNode next;

    RecordNode(String[] data) {
        this.data = data;
        this.next = null;
    }
}

public class CsvViewer extends JFrame {
    private JTable table;

    public CsvViewer() {
        setTitle("Visor de Registros CSV");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Lee los registros desde el archivo CSV y almacénalos en una lista enlazada
        RecordNode records = readCsvFile("registro.csv");

        // Define las columnas para la tabla
        String[] columnNames = {"Expresión", "Resultado", "Fecha"};

        // Crea un modelo de tabla personalizado para mostrar los registros
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        // Agrega los registros a la tabla
        while (records != null) {
            tableModel.addRow(records.data);
            records = records.next;
        }

        // Crea la tabla Swing
        table = new JTable(tableModel);

        // Agrega la tabla a un JScrollPane para permitir el desplazamiento
        JScrollPane scrollPane = new JScrollPane(table);

        // Agrega el JScrollPane a la ventana
        add(scrollPane);

        // Muestra la ventana
        setVisible(true);
    }

    private RecordNode readCsvFile(String filePath) {
        RecordNode records = null;
        RecordNode current = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    RecordNode newNode = new RecordNode(fields);
                    if (records == null) {
                        records = newNode;
                        current = newNode;
                    } else {
                        current.next = newNode;
                        current = newNode;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }
}
