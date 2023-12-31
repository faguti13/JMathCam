/**
 * Esta clase representa un servidor que escucha a los clientes y procesa expresiones matemáticas.
 * El servidor recibe expresiones matemáticas en notación infija, las convierte a notación postfija
 * y luego evalúa su resultado. Los resultados y las expresiones se almacenan en un archivo CSV.
 *
 * @authors Fabián Gutiérrez Jiménez y Adrián Muñoz Alvarado
 */

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Servidor {
    public static void main(String[] args) {

        // Configura el servidor para escuchar en un puerto específico (por ejemplo, 12345)
        int puerto = 12345;
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexiones en el puerto " + puerto + "...");

            while (true) {
                // Acepta conexiones de clientes
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress().getHostAddress());

                // Crea flujos de entrada y salida para comunicarse con el cliente
                BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                PrintWriter salidaCliente = new PrintWriter(clienteSocket.getOutputStream(), true);

                // Lee la expresión infija del cliente
                String expresionInfija = entradaCliente.readLine();
                String expresionModificada = reemplazarDobleAsterisco(expresionInfija);
                System.out.println(expresionModificada);
                String UnDig = procesarOperacion(expresionModificada);
                System.out.println(UnDig);
                String aPOS = convertirInfijoAPostfijo(UnDig);
                System.out.println("Resultado: " + aPOS);



                Arbol<Integer> arbol;
                int resultado = 0;

                try {

                    arbol = new Arbol<>(aPOS);
                    resultado = arbol.evaluar();
                    salidaCliente.println(resultado);
                    String filePath = "registro.csv";
                    CsvWriter csvWriter = new CsvWriter(filePath);

                    // Obtén la fecha y hora actual
                    LocalDateTime currentDateTime = LocalDateTime.now();

                    // Define un formato personalizado para la fecha y hora
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // Convierte la fecha y hora actual en una cadena
                    String formattedDateTime = currentDateTime.format(formatter);

                    String[] entries = {
                            entradaCliente.readLine(), salidaCliente.toString(), formattedDateTime
                    };

                    csvWriter.writeCsv(entries);

                } catch (IllegalStateException e){
                    e.printStackTrace();
                    String error;
                    error = "Expresión inválida";
                    salidaCliente.println(error);

                }

                // Cierra la conexión con el cliente
                clienteSocket.close();
                System.out.println("Cliente desconectado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String reemplazarDobleAsterisco(String expresion) {
        StringBuilder resultado = new StringBuilder();
        int i = 0;

        while (i < expresion.length()) {
            if (i < expresion.length() - 1 && expresion.substring(i, i + 2).equals("**")) {
                resultado.append("$");
                i += 2;
            } else {
                resultado.append(expresion.charAt(i));
                i++;
            }
        }

        return resultado.toString();
    }

    public static String procesarOperacion(String operacion) {
        // Utilizamos una expresión regular para encontrar números de más de un dígito
        Pattern patron = Pattern.compile("-?\\d{2,}");
        Matcher matcher = patron.matcher(operacion);

        StringBuffer resultado = new StringBuffer();

        while (matcher.find()) {
            int numero = Integer.parseInt(matcher.group());

            // Calculamos la suma de nueves y el número restante
            int sumaDeNueves = Math.abs(numero) / 9;
            int numeroRestante = Math.abs(numero) % 9;

            // Creamos la cadena de reemplazo
            StringBuilder reemplazo = new StringBuilder();
            if (numero < 0) {
                reemplazo.append("-(");
            } else {
                reemplazo.append("(");
            }
            for (int i = 0; i < sumaDeNueves; i++) {
                reemplazo.append("9+");
            }
            reemplazo.append(numeroRestante);
            reemplazo.append(")");

            // Reemplazamos el número en la expresión original
            matcher.appendReplacement(resultado, reemplazo.toString());
        }

        // Agregamos el texto que queda después de procesar los números
        matcher.appendTail(resultado);

        return resultado.toString();
    }
    // Implementa la función para convertir de notación infija a postfija aquí
    private static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%'|| c == '$';
    }

    // Función para obtener la precedencia de un operador
    private static int obtenerPrecedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
            case '$':
                return 2;
        }
        return 0;
    }

    // Función para convertir una expresión infija a postfija
    public static String convertirInfijoAPostfijo(String expresionInfija) {
        StringBuilder expresionPostfija = new StringBuilder();
        Pila<Character> pila = new Pila<>();

        for (char c : expresionInfija.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                // Si es un operando, agregarlo a la expresión postfija
                expresionPostfija.append(c);
            } else if (c == '(') {
                // Si es un paréntesis de apertura, ponerlo en la pila
                pila.push(c);
            } else if (c == ')') {
                // Si es un paréntesis de cierre, desapilar operadores hasta encontrar el paréntesis de apertura
                while (!pila.isEmpty() && pila.peek() != '(') {
                    expresionPostfija.append(pila.pop());
                }
                pila.pop(); // Desapilar el paréntesis de apertura
            } else if (esOperador(c)) {
                // Si es un operador, desapilar operadores de mayor o igual precedencia y luego ponerlo en la pila
                while (!pila.isEmpty() && pila.peek() != '(' && obtenerPrecedencia(c) <= obtenerPrecedencia(pila.peek())) {
                    expresionPostfija.append(pila.pop());
                }
                pila.push(c);
            }
        }

        // Desapilar cualquier operador restante en la pila
        while (!pila.isEmpty()) {
            if (pila.peek() == '(') {
                return "Expresión infija no válida"; // Expresión infija no balanceada
            }
            expresionPostfija.append(pila.pop());
        }

        return expresionPostfija.toString();
    }

}