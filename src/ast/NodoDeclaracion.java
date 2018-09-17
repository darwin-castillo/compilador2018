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
public class NodoDeclaracion extends NodoBase {

    String tipo;
    NodoBase vect;
    String variable;

    public NodoDeclaracion(String id, String tipo) {
        this.tipo = tipo;
        this.variable = id;
    }

    public NodoDeclaracion(NodoBase vect, String tipo) {
        this.vect = vect;
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public NodoBase getVect() {
        return vect;
    }

    public void setVect(NodoBase vect) {
        this.vect = vect;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

}
