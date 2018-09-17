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
public class NodoReturn extends NodoBase {
    
    private NodoBase expresion;

    public NodoReturn(NodoBase expresion) {
        this.expresion = expresion;
    }

    public NodoBase getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoBase expresion) {
        this.expresion = expresion;
    }
    
    
}
