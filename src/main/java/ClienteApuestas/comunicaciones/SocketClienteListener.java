package ClienteApuestas.comunicaciones;

public interface SocketClienteListener {
    public void notifyEmpezarCarrera();
    public void notifyGanador(int ganador);
}
