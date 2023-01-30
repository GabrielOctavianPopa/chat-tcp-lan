package ServerApuestas.datos;
public class Caballo {
    private int distancia;
    private int crash;
    private int numeroCaballo;
    public Caballo(int numeroCaballo) {
        this.numeroCaballo = numeroCaballo;
    }

    public void run() {
        int pasos = (int)(Math.random() * 100);
        distancia += pasos;
        distancia += crash;
    }
    public void reiniciar() {
        distancia = 0;
        crash = (int)((Math.random() * 10) - 5);
    }

    public int getDistancia() {
        return distancia;
    }

    public double getFinalCarrera() {
        return (double) distancia / (double) Carrera.DISTANCIA_MAXIMA;
    }

    public int getNumeroCaballo() {
        return numeroCaballo;
    }
}
