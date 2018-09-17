package ast;

public class NodoAsignacion extends NodoBase {

    private String identificador;
    private NodoBase expresion;
    private NodoBase vct;

    public NodoAsignacion(String identificador) {
        super();
        this.identificador = identificador;
        this.expresion = null;
    }

    public NodoAsignacion(String identificador, NodoBase expresion) {
        super();
        this.identificador = identificador;
        this.expresion = expresion;
    }

    public NodoAsignacion(NodoBase vct, NodoBase expresion) {
        super();
        this.vct = vct;
        this.expresion = expresion;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public NodoBase getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoBase expresion) {
        this.expresion = expresion;
    }

    public NodoBase getVct() {
        return vct;
    }

    public void setVct(NodoBase vct) {
        this.vct = vct;
    }
}
