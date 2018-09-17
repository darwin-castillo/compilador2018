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
public class NodoTipoFunc  extends NodoBase{
    NodoBase type;

    public NodoTipoFunc(NodoBase type) {
        this.type = type;
    }

    public NodoBase getType() {
        return type;
    }

    public void setType(NodoBase type) {
        this.type = type;
    }
    
    
    
}
