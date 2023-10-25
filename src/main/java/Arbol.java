/**
 * Esta clase representa un árbol que puede evaluar expresiones postfijas.
 *
 * @param <T> Tipo genérico para los nodos del árbol.
 * @author Adrian Muñoz Alvarado
 */
public class Arbol<T> {
    private Nodo<T> raiz;

    /**
     * Construye un árbol a partir de una expresión postfija dada.
     *
     * @param expresionPostfija La expresión postfija para construir el árbol.
     */
    public Arbol(String expresionPostfija) {
        construirArPostfijo(expresionPostfija);
    }

    private void construirArPostfijo(String expresionPostfija) {
        Pila<Nodo<T>> pila = new Pila<>();
        for (char c : expresionPostfija.toCharArray()) {
            if (esOperando(c)) {
                pila.push(new Nodo<T>((T) Character.valueOf(c)));
            } else if (c == '~') { // Si el operador es "~", saca un operando
                Nodo<T> derecho = pila.pop();
                pila.push(new Nodo<T>((T) Character.valueOf(c), null, derecho));
            } else {
                Nodo<T> derecho = pila.pop();
                Nodo<T> izquierdo = pila.pop();
                pila.push(new Nodo<T>((T) Character.valueOf(c), izquierdo, derecho));
            }
        }
        raiz = pila.pop();
    }

    /**
     * Evalúa el árbol y devuelve el resultado de la expresión.
     *
     * @return El resultado de la evaluación de la expresión.
     */
    public int evaluar() {
        return evaluarRecursivo(raiz);
    }

    private int evaluarRecursivo(Nodo<T> nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("El árbol está vacío o mal formado.");
        }

        if (esOperando((char) nodo.getData())) {
            return Character.getNumericValue((char) nodo.getData());
        } else {
            if (nodo.getLeft() == null) {
                int resultadoOperadorUnario = aplicarOperador((char) nodo.getData(), (nodo.getRight() != null) ? evaluarRecursivo(nodo.getRight()) : 0, 0);
                return resultadoOperadorUnario;
            }

            int izquierdo = evaluarRecursivo(nodo.getLeft());
            int derecho = (nodo.getRight() != null) ? evaluarRecursivo(nodo.getRight()) : 0;

            return aplicarOperador((char) nodo.getData(), izquierdo, derecho);
        }
    }

    private int aplicarOperador(char operador, int a, int b) {
        try {
            switch (operador) {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    if (b == 0) {
                        throw new ArithmeticException("División por cero");
                    }
                    return a / b;
                case '%':
                    if (b == 0) {
                        throw new ArithmeticException("Módulo por cero");
                    }
                    return a % b;
                case '&':
                    return a & b;
                case '^': // Caso para el operador XOR
                    return a ^ b;
                case '$': // Caso para el operador XOR
                    int x = (int) Math.pow(a, b);
                    return x;
                case '~':
                    return -a - 1;
                default:
                    throw new IllegalArgumentException("Operador no válido: " + operador);
            }
        } catch (Exception e) {
            return -a - 1;
        }
    }

    private boolean esOperando(char c) {
        return Character.isDigit(c);
    }
}
