import java.io.*;
import java.net.*;
import java.util.Stack;

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
                System.out.println("Expresión recibida del cliente: " + expresionInfija);

                // Convierte la expresión infija a postfija
                String expresionPostfija = convertirInfijoAPostfijo(expresionInfija);
                System.out.println("Expresion post fija: " + expresionPostfija);

                // Construye y evalúa el árbol de expresiones con notación postfija
                Arbol<Character> arbol = new Arbol<>(expresionPostfija);
                int resultado = arbol.evaluar();
                System.out.println("Resultado de expresion: " + resultado);

                // Envia el resultado al cliente
                salidaCliente.println("Resultado: " + resultado);

                // Cierra la conexión con el cliente
                clienteSocket.close();
                System.out.println("Cliente desconectado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implementa la función para convertir de notación infija a postfija aquí
    private static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
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
                return 2;
        }
        return 0;
    }

    // Función para convertir una expresión infija a postfija
    public static String convertirInfijoAPostfijo(String expresionInfija) {
        StringBuilder expresionPostfija = new StringBuilder();
        Stack<Character> pila = new Stack<>();

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