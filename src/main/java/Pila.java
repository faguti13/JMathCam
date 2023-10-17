public class Pila<T> {
    private Nodo<T> top;

    public void push(T data) {
        Nodo<T> newNode = new Nodo<>(data);
        newNode.right = top;
        top = newNode;
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T data = top.getData();
        top = top.getRight();
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return top.getData();
    }

    public boolean isEmpty() {
        return top == null;
    }
}