/**
 * Esta clase representa una estructura de datos de pila genérica. La pila se implementa utilizando nodos
 * y permite operaciones como empujar (push), sacar (pop), inspeccionar (peek) y verificar si está vacía.
 *
 * @param <T> El tipo de datos que se almacena en la pila.
 * @author Adrián Muñoz Alvarado
 */
public class Pila<T> {
    private Nodo<T> top;

    /**
     * Agrega un nuevo elemento en la parte superior de la pila.
     *
     * @param data El elemento a agregar en la pila.
     */
    public void push(T data) {
        Nodo<T> newNode = new Nodo<>(data);
        newNode.right = top;
        top = newNode;
    }

    /**
     * Elimina y devuelve el elemento en la parte superior de la pila.
     *
     * @return El elemento eliminado de la pila.
     * @throws IllegalStateException Si la pila está vacía.
     */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T data = top.getData();
        top = top.getRight();
        return data;
    }

    /**
     * Inspecciona el elemento en la parte superior de la pila sin eliminarlo.
     *
     * @return El elemento en la parte superior de la pila.
     * @throws IllegalStateException Si la pila está vacía.
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return top.getData();
    }

    /**
     * Verifica si la pila está vacía.
     *
     * @return true si la pila está vacía, false de lo contrario.
     */
    public boolean isEmpty() {
        return top == null;
    }
}
