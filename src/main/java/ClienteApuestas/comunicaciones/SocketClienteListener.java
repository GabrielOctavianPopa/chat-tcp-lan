package ClienteApuestas.comunicaciones;

/**
 * TODO: Migrar el esquema de conexion a uno parecido al del chat
 */

public interface SocketClienteListener {
    public void notifyEmpezarCarrera();
    public void notifyGanador(int ganador);
}
