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
public class NodoVector  extends NodoBase{
    
    private int tam;
    private String id;
    private NodoBase dimension;
    private NodoBase identificador;
    
    public NodoVector( String id, int tam) {
        this.tam = tam;
        this.id = id;
    }

    public int getTam() {
        return tam;
    }

    public void setTam(int tam) {
        this.tam = tam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodoBase getDimension() {
        return dimension;
    }

    public void setDimension(NodoBase dimension) {
        this.dimension = dimension;
    }

    public NodoBase getIdentificador() {
        return identificador;
    }

    public void setIdentificador(NodoBase identificador) {
        this.identificador = identificador;
    }
}
