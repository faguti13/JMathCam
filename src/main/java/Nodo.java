
public class Nodo<T>  {
    private T data;
    protected Nodo<T> left;
    protected Nodo<T> right;


    public Nodo(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }


    public Nodo(T data, Nodo<T> left, Nodo<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Nodo<T> getLeft() {
        return left;
    }

    public void setLeft(Nodo<T> left) {
        this.left = left;
    }

    public Nodo<T> getRight() {
        return right;
    }

    public void setRight(Nodo<T> right) {
        this.right = right;
    }
}