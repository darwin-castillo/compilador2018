/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author Darwin
 */
public class NodoCall extends NodoBase {
    
    private String id;
    private NodoBase params;
    private NodoBase funcion;

    public NodoCall(String id, NodoBase params) {
        this.id = id;
        this.params = params;
    }

    /**
     * Get the value of params
     *
     * @return the value of params
     */
    public NodoBase getParams() {
        return params;
    }

    /**
     * Set the value of params
     *
     * @param params new value of params
     */
    public void setParams(NodoBase params) {
        this.params = params;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    public NodoBase getFuncion() {
        return funcion;
    }

    public void setFuncion(NodoBase funcion) {
        this.funcion = funcion;
    }

}
