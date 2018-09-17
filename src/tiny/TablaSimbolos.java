package tiny;

import java.util.*;

import ast.NodoAsignacion;
import ast.NodoBase;
import ast.NodoCall;
import ast.NodoDeclaracion;
import ast.NodoEscribir;
import ast.NodoFuncion;
import ast.NodoIdentificador;
import ast.NodoIf;
import ast.NodoMain;
import ast.NodoOperacion;
import ast.NodoRepeat;
import ast.NodoReturn;
import ast.NodoTipo;
import ast.NodoTipoFunc;
import ast.NodoValor;
import ast.NodoVector;

public class TablaSimbolos {

    private HashMap<String, Simbolo> tabla;
    private int direccion;  //Contador de las localidades de memoria asignadas a la tabla

    public TablaSimbolos() {
        super();
        tabla = new HashMap<String, Simbolo>();
        direccion = 0;
    }

    public void cargarTabla(NodoBase raiz) {
        while (raiz != null) {
            if (raiz instanceof NodoIdentificador) {
                InsertarSimbolo(((NodoIdentificador) raiz).getNombre(), -1, ((NodoIdentificador) raiz).getTipo(), 0, 0, null, null);
                //TODO: Anadir el numero de linea y localidad de memoria correcta
            }

            /* Hago el recorrido recursivo */
            if (raiz instanceof NodoIf) {
                cargarTabla(((NodoIf) raiz).getPrueba());
                cargarTabla(((NodoIf) raiz).getParteThen());
                if (((NodoIf) raiz).getParteElse() != null) {
                    cargarTabla(((NodoIf) raiz).getParteElse());
                }
            } else if (raiz instanceof NodoMain) {
                cargarTabla(((NodoMain) raiz).getFuncion());
                cargarTabla(((NodoMain) raiz).getContenido());
            } else if (raiz instanceof NodoRepeat) {
                cargarTabla(((NodoRepeat) raiz).getCuerpo());
                cargarTabla(((NodoRepeat) raiz).getPrueba());
            } else if (raiz instanceof NodoFuncion) {
                /*
                    Se agregar el sufijo 'Funcion' al Id de la insercion del simbolo, para no confundirlo con el Id del
                    CallFuncion. De esta forma, cuando se vaya a buscar el Id de la funcion, me traerá todos los atributos
                    de la funcion y no del CallFuncion
                */
                InsertarSimbolo(((NodoFuncion) raiz).getId()+"Funcion", -1, ((NodoFuncion) raiz).getTipo(), 0, 0, ((NodoFuncion) raiz).getContenido(), ((NodoFuncion) raiz).getArgs());
                cargarTabla(((NodoFuncion) raiz).getArgs());
                cargarTabla(((NodoFuncion) raiz).getContenido());
            } else if (raiz instanceof NodoReturn) {
                cargarTabla(((NodoReturn) raiz).getExpresion());

            } else if (raiz instanceof NodoCall) {
                InsertarSimbolo(((NodoCall) raiz).getId(), -1, "", 0, 0, null, ((NodoCall) raiz).getParams());
                if(((NodoCall) raiz).getParams() != null){
                    cargarTabla(((NodoCall) raiz).getParams());
                }
            } else if (raiz instanceof NodoDeclaracion) {
                InsertarSimbolo(((NodoDeclaracion) raiz).getVariable(), -1, ((NodoDeclaracion) raiz).getTipo(), 0, 0, ((NodoDeclaracion) raiz).getVect(), null);
                
                if(((NodoDeclaracion) raiz).getVect() != null){
                    NodoVector vector = (NodoVector) ((NodoDeclaracion) raiz).getVect();
                    ((NodoDeclaracion) raiz).setVect(vector);

                    cargarTabla(((NodoDeclaracion) raiz).getVect());
                }
            } else if (raiz instanceof NodoTipo) {
                if (((NodoTipo) raiz).getHermanoDerecha() != null) {
                    cargarTabla(((NodoTipo) raiz).getHermanoDerecha());
                }
            } else if (raiz instanceof NodoTipoFunc) {
                cargarTabla(((NodoTipoFunc) raiz).getType());
            } else if (raiz instanceof NodoAsignacion) {
                InsertarSimbolo(((NodoAsignacion) raiz).getIdentificador(), -1, "", 0, 0, ((NodoAsignacion) raiz).getExpresion(), null);
                
                if(((NodoAsignacion) raiz).getVct() != null){
                    NodoVector vector = (NodoVector) ((NodoAsignacion) raiz).getVct();
                    ((NodoAsignacion) raiz).setVct(vector);
                    cargarTabla(((NodoAsignacion) raiz).getVct());
                }
                cargarTabla(((NodoAsignacion) raiz).getExpresion());
            } else if (raiz instanceof NodoEscribir) {
                cargarTabla(((NodoEscribir) raiz).getExpresion());
            } else if (raiz instanceof NodoOperacion) {
                cargarTabla(((NodoOperacion) raiz).getOpIzquierdo());
                cargarTabla(((NodoOperacion) raiz).getOpDerecho());
            }else if (raiz instanceof NodoVector){
                InsertarSimbolo(((NodoVector) raiz).getId(), -1, "", 0, ((NodoVector) raiz).getTam(), null, null);
                ((NodoVector) raiz).setDimension(new NodoValor(((NodoVector) raiz).getTam()));
                cargarTabla(((NodoVector) raiz).getDimension());
            }else if (raiz instanceof NodoValor) {
                InsertarSimbolo(String.valueOf(((NodoValor) raiz).getValor()), -1, "", 0, 0, null, null);
            }
            if (raiz.getHermanoDerecha() != null) {
                cargarTabla(raiz.getHermanoDerecha());
            }
            raiz = raiz.getHermanoDerecha();
        }
    }

    //true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
    public boolean InsertarSimbolo(String identificador, int numLinea, String tipo, int numArgs, int dimArray, NodoBase contenido, NodoBase args) {
        Simbolo simbolo;
        if (tabla.containsKey(identificador)) {
            return false;
        } else {
            simbolo = new Simbolo(identificador, numLinea, direccion++, tipo, numArgs, dimArray, contenido, args);
            tabla.put(identificador, simbolo);
            return true;
        }
    }

    public Simbolo BuscarSimbolo(String identificador) {
        Simbolo simbolo = (Simbolo) tabla.get(identificador);
        return simbolo;
    }

    public void ImprimirClaves() {
        System.out.println("*** Tabla de Simbolos ***");
        for (Iterator<String> it = tabla.keySet().iterator(); it.hasNext();) {
            String s = (String) it.next();
            System.out.println("Consegui Key: " + s + " con direccion: " + BuscarSimbolo(s).getDireccionMemoria());
        }
    }

    public int getDireccion(String Clave) {
        return BuscarSimbolo(Clave).getDireccionMemoria();
    }

    /*
	 * TODO:
	 * 1. Crear lista con las lineas de codigo donde la variable es usada.
     */
}
