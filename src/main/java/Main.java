
import java.util.Stack;
public class Main {
    public static void main(String[] args) {
        String expresionPostfija = "23*5*";
        Arbol<Character> arbol = new Arbol<>(expresionPostfija);
        int resultado = arbol.evaluar();
        System.out.println("Resultado de la expresión: " + resultado);
    }
}
