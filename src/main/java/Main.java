public class Main {
    public static void main(String[] args) {

        String expresionPostfija = "45^";


        Arbol<Integer> arbol = new Arbol<>(expresionPostfija);


        int resultado = arbol.evaluar();
        System.out.println("Resultado: " + resultado);
    }
}