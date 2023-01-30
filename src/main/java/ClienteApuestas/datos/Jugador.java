package ClienteApuestas.datos;

import ServerApuestas.datos.Carrera;

public class Jugador {
    private static final int DINERO_INICIAL = 100;
    private int dineroActual = DINERO_INICIAL;
    JugadorListener chivato;
    private int caballoApostado = -1;

    public Jugador() {
    }
    public void apostarPorCaballo(int numeroCaballo) {
        dineroActual--;
        caballoApostado = numeroCaballo;
        chivato.notifyCambioDeJugador();
    }

    public void notifyGanador(int numeroCaballo) {
        if(caballoApostado != -1) {
            if(numeroCaballo == caballoApostado) {
                dineroActual += Carrera.NUMERO_CORREDORES;
            }
            caballoApostado = -1;
            chivato.notifyCambioDeJugador();
        }
    }

    public int getDineroActual() {
        return dineroActual;
    }

    public void setChivato(JugadorListener chivato) {
        this.chivato = chivato;
    }

    public int getCaballoApostado() {
        return caballoApostado;
    }
}
