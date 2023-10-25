
import java.util.regex.*;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce una operación matemática: ");
        String operacion = scanner.nextLine();

        String expresionModificada = reemplazarDobleAsterisco(operacion);
        System.out.println(expresionModificada);
        String UnDig = procesarOperacion(expresionModificada);
        String aPOS = convertirInfijoAPostfijo(UnDig);
        System.out.println("Resultado: " + aPOS);
        Arbol<Integer> arbol = new Arbol<>(aPOS);
            float resultado = arbol.evaluar();
        System.out.println("Resultado: " + resultado);
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