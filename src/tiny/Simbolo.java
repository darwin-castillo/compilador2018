package tiny;

import ast.NodoBase;

public class Simbolo {

    private String identificador;
    private int NumLinea;
    private int DireccionMemoria;
    private String tipo;
    private int numArgs;
    private int dimArray;
    private NodoBase contenido;
    private NodoBase args;
    private int longitud;
    private boolean isItDeclarated;

    public Simbolo(String identificador, int numLinea, int direccionMemoria) {
        super();
        this.identificador = identificador;
        NumLinea = numLinea;
        DireccionMemoria = direccionMemoria;
        isItDeclarated = false;
    }

    public Simbolo(String identificador, int NumLinea, int DireccionMemoria, String tipo, int numArgs, int dimArray, NodoBase contenido, NodoBase args) {
        this.identificador = identificador;
        this.NumLinea = NumLinea;
        this.DireccionMemoria = DireccionMemoria;
        this.tipo = tipo;
        this.numArgs = numArgs;
        this.dimArray = dimArray;
        this.contenido = contenido;
        this.args = args;
        this.isItDeclarated = false;
    }

    public String getIdentificador() {
        return identificador;
    }

    public int getNumLinea() {
        return NumLinea;
    }

    public int getDireccionMemoria() {
        return DireccionMemoria;
    }

    public void setDireccionMemoria(int direccionMemoria) {
        DireccionMemoria = direccionMemoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public NodoBase getContenido() {
        return contenido;
    }

    public void setContenido(NodoBase contenido) {
        this.contenido = contenido;
    }

    public NodoBase getArgs() {
        return args;
    }

    public void setArgs(NodoBase args) {
        this.args = args;
    }

    public boolean isIsItDeclarated() {
        return isItDeclarated;
    }

    public void setIsItDeclarated(boolean isItDeclarated) {
        this.isItDeclarated = isItDeclarated;
    }
    
}
