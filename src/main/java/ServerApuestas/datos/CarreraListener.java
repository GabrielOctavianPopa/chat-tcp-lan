package ServerApuestas.datos;

/**
 * TODO: migrar a un esquema de conexion como la del chat
 */

@Deprecated
public interface CarreraListener {

    public void notifyProgresoCarrera();

    public void notifyEstado(String estado);
}