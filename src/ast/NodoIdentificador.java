package ast;

public class NodoIdentificador extends NodoBase {

    private String nombre;
    private String tipo;

    public NodoIdentificador(String nombre) {
        super();
        this.nombre = nombre;
    }

    public NodoIdentificador() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
