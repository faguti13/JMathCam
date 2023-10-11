import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.HighGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Cliente {
    private static boolean captureRequested = false;
    private static String serverAddress = "localhost"; // Dirección IP del servidor
    private static int serverPort = 12345; // Puerto del servidor

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture camera = new VideoCapture(0); // Abre la cámara web (puede variar según la cámara)

        if (!camera.isOpened()) {
            System.out.println("Error al abrir la cámara.");
            return;
        }

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Users\\FabianGJ\\Downloads\\Tess4J\\tessdata"); // Ruta al directorio tessdata

        Mat destination = new Mat();
        Mat source = new Mat();

        // Crear la ventana de la aplicación
        JFrame frame = new JFrame("JMathCam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 1024);

        frame.setLocationRelativeTo(null);

        // Crear un VideoPanel personalizado para mostrar el cuadro de video
        VideoPanel videoPanel = new VideoPanel();
        frame.add(videoPanel); // Agregar VideoPanel al JFrame directamente

        // Crear un panel para los botones y usar un FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        frame.add(buttonPanel, BorderLayout.SOUTH); // Agregar el panel de botones en la parte inferior del JFrame

        // Crear un botón para tomar una foto
        JButton captureButton = new JButton("Tomar Foto");
        buttonPanel.add(captureButton); // Agregar el botón al panel de botones

        // Crear un botón para escribir texto
        JButton writeTextButton = new JButton("Escribir Texto");
        buttonPanel.add(writeTextButton); // Agregar el botón al panel de botones

        // Manejar el evento del botón para tomar una foto
        captureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                captureRequested = true;
            }
        });

        // Manejar el evento del botón para escribir texto
        writeTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar una ventana emergente para ingresar texto
                String userInput = JOptionPane.showInputDialog(frame, "Ingrese el texto:");

                // Procesar el texto ingresado (puedes realizar cualquier procesamiento que desees aquí)
                if (userInput != null) {
                    System.out.println("Texto ingresado: " + userInput);

                    // Enviar el texto al servidor y recibir el resultado
                    String resultadoFromServer = sendTextToServer(userInput);
                    System.out.println("Resultado del servidor: " + resultadoFromServer);

                    // Mostrar el resultado en una ventana emergente
                    receiveTextFromServerAndShowPopup(resultadoFromServer);
                }
            }
        });

        // Agregar un KeyListener al JFrame para detectar la tecla Esc y salir de la aplicación
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // No es necesario implementar este método
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0); // Sale de la aplicación al presionar Esc
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No es necesario implementar este método
            }
        });

        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);

        while (true) {
            if (camera.read(source)) {
                if (captureRequested) {
                    // Aplicar el mismo procesamiento de imagen que hiciste en la imagen estática a la foto capturada
                    for (int i = 0; i < 4; i++) {
                        destination = new Mat(source.rows(), source.cols(), source.type());
                        Imgproc.GaussianBlur(source, destination, new Size(101, 101), 10);
                        Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
                        source = destination;
                    }

                    Mat resultMat = new Mat();
                    Imgproc.threshold(destination, resultMat, 55, 255, Imgproc.THRESH_BINARY);

                    // Guardar la imagen preprocesada (opcional)
                    Imgcodecs.imwrite("imagen_preprocesada.jpg", resultMat);

                    // Utilizar Tesseract OCR para extraer texto
                    try {
                        String texto = tesseract.doOCR(new File("imagen_preprocesada.jpg"));
                        System.out.println("Texto extraído: " + texto);

                        // Enviar el texto al servidor
                        sendTextToServer(texto);

                        String resultadoFromServer = receiveTextFromServer();
                        System.out.println("Resultado del servidor: " + resultadoFromServer);

                    } catch (TesseractException ex) {
                        ex.printStackTrace();
                    }

                    captureRequested = false;
                }

                // Mostrar el cuadro de video en el VideoPanel
                videoPanel.updateImage((BufferedImage) HighGui.toBufferedImage(source));

                // Espera un breve período para evitar bloqueos en la interfaz de usuario
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // VideoPanel personalizado para mostrar el cuadro de video
    static class VideoPanel extends JPanel {
        private Image image;

        public void updateImage(BufferedImage newImage) {
            image = newImage;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null); // Centrar la imagen
            }
        }
    }

    // Método para enviar texto al servidor
    private static String sendTextToServer(String text) {
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            // Enviar el texto al servidor
            out.println(text);

            // Recibir el resultado del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al recibir el resultado del servidor.";
        }
    }

    // Método para recibir texto del servidor
    private static String receiveTextFromServer() {
        try (Socket socket = new Socket(serverAddress, serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            // Leer el texto del servidor
            return in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error al recibir el resultado del servidor.";
        }
    }

    private static void receiveTextFromServerAndShowPopup(String resultado) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Mostrar el resultado en una ventana emergente
                JOptionPane.showMessageDialog(null, "Resultado del servidor: " + resultado, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
