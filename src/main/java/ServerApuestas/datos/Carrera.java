package ServerApuestas.datos;

import java.util.ArrayList;
import java.util.List;

public class Carrera {
    public static int DISTANCIA_MAXIMA = 10000;
    public static int NUMERO_CORREDORES = 5;
    private int TIEMPO_ESPERAR = 25;
    private List<Caballo> corredores = new ArrayList<Caballo>();
    public Carrera() {
        for(int i = 0; i< NUMERO_CORREDORES; i++) {
            corredores.add(new Caballo(i));
        }
    }

    public void caerse() {
        for(Caballo caballo : corredores) {
            caballo.caerse();
        }
    }

    public Caballo caballoCorredor() {
        Caballo ganador = getGanador();
        while(ganador == null) {
            try {
                Thread.sleep(TIEMPO_ESPERAR);
            } catch(InterruptedException e) {}
            for(Caballo runner : corredores) {
                runner.run();
            }
            ganador = getGanador();
        }
        return ganador;
    }

    public Caballo getGanador() {
        for(Caballo caballo : corredores) {
            if(caballo.getDistancia() > DISTANCIA_MAXIMA) {
                return caballo;
            }
        }
        return null;
    }

    public List<Caballo> getCorredores() {
        return corredores;
    }
}