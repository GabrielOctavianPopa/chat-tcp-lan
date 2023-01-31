package ServerChat;

import java.util.ArrayList;

public class HiloOnline implements Runnable {
    String[] lista= new String[20];
    String usuario;
    int i =0;
public String[] getLista(){
    return lista;
}
    @Override
    public void run() {
    synchronized (this){
        while(true){
        if(lista[i]!=null){
            i++;
        }else{
            try {
                wait(2000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        }
    }
    }

    public HiloOnline(String[] usuarios){
        lista=usuarios;
    }
    public HiloOnline(){

    };
public void setnuevousuario(String nombreusuario){
    lista[i]=nombreusuario;
    i++;
}
}
