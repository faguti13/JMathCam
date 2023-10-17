import java.util.Stack;

class Arbol<T> {
    private Nodo<T> raiz;

    public Arbol(String expresionPostfija) {
        construirArPostfijo(expresionPostfija);
    }

    private void construirArPostfijo(String expresionPostfija) {
        Stack<Nodo<T>> pila = new Stack<>();
        for (char c : expresionPostfija.toCharArray()) {
            if (esOperando(c)) {
                pila.push(new Nodo<T>((T) Character.valueOf(c)));
            } else {
                Nodo<T> derecho = pila.pop();
                Nodo<T> izquierdo = pila.pop();
                pila.push(new Nodo<T>((T) Character.valueOf(c), izquierdo, derecho));
            }
        }
        raiz = pila.pop();
    }

    public int evaluar() {
        return evaluarRecursivo(raiz);
    }

    private int evaluarRecursivo(Nodo<T> nodo) {
        if (esOperando((char) nodo.getData())) {
            return Character.getNumericValue((char) nodo.getData());
        } else {
            int izquierdo = evaluarRecursivo(nodo.getLeft());
            int derecho = evaluarRecursivo(nodo.getRight());
            return aplicarOperador((char) nodo.getData(), izquierdo, derecho);
        }
    }

    private int aplicarOperador(char operador, int a, int b) {
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
            default:
                throw new IllegalArgumentException("Operador no válido: " + operador);
        }
    }

    private boolean esOperando(char c) {
        return Character.isDigit(c);
    }
}