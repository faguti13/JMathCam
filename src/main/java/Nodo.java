/**
 * Esta clase representa un nodo de un árbol binario. Cada nodo contiene un valor de datos
 * y puede tener referencias a un hijo izquierdo y un hijo derecho.
 *
 * @param <T> El tipo de datos que se almacena en el nodo.
 * @author Adrián Muñoz Alvarado
 */
public class Nodo<T>  {
    private T data;
    protected Nodo<T> left;
    protected Nodo<T> right;

    /**
     * Crea un nodo con el valor de datos especificado y sin hijos.
     *
     * @param data El valor de datos almacenado en el nodo.
     */
    public Nodo(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    /**
     * Crea un nodo con el valor de datos especificado y con referencias a los hijos izquierdo y derecho.
     *
     * @param data El valor de datos almacenado en el nodo.
     * @param left El nodo hijo izquierdo.
     * @param right El nodo hijo derecho.
     */
    public Nodo(T data, Nodo<T> left, Nodo<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    /**
     * Obtiene el valor de datos almacenado en el nodo.
     *
     * @return El valor de datos del nodo.
     */
    public T getData() {
        return data;
    }

    /**
     * Establece el valor de datos del nodo.
     *
     * @param data El nuevo valor de datos a asignar al nodo.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene el nodo hijo izquierdo.
     *
     * @return El nodo hijo izquierdo.
     */
    public Nodo<T> getLeft() {
        return left;
    }

    /**
     * Establece el nodo hijo izquierdo.
     *
     * @param left El nuevo nodo hijo izquierdo a asignar.
     */
    public void setLeft(Nodo<T> left) {
        this.left = left;
    }

    /**
     * Obtiene el nodo hijo derecho.
     *
     * @return El nodo hijo derecho.
     */
    public Nodo<T> getRight() {
        return right;
    }

    /**
     * Establece el nodo hijo derecho.
     *
     * @param right El nuevo nodo hijo derecho a asignar.
     */
    public void setRight(Nodo<T> right) {
        this.right = right;
    }
}
