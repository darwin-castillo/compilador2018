/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author Jeniffer
 */
public class NodoFuncion extends NodoBase{
    
    //NodoFuncion(tya,new NodoIdentificador(nomf.toString()),stsms,sds); 
    private String tipo; 
    private String id;
    private NodoBase args; 
    private NodoBase contenido;

    public NodoFuncion(NodoBase hermanoDerecha) {
        super(hermanoDerecha);
    }

    public NodoFuncion(String tipo, String id, NodoBase args, NodoBase contenido) {
        super();
        this.tipo = tipo;
        this.id = id;
        this.args = args;
        this.contenido = contenido;
    }
 
     public NodoFuncion(String tipo, String id,   NodoBase contenido) {
        super();
        this.tipo = tipo;
        this.id = id;
        this.contenido = contenido;
    }     
     
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodoBase getArgs() {
        return args;
    }

    public void setArgs(NodoBase args) {
        this.args = args;
    }

    public NodoBase getContenido() {
        return contenido;
    }

    public void setContenido(NodoBase contenido) {
        this.contenido = contenido;
    }
   
 
}
