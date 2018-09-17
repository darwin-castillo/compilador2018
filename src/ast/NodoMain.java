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
public class NodoMain extends NodoBase {

    private NodoBase funcion;
    private NodoBase contenido;
    private NodoBase block;
    private int ambito;

    public NodoMain(NodoBase funcion, NodoBase contenido, int ambito) {
        this.funcion = funcion;
        this.contenido = contenido;
        this.ambito = ambito;
    }

    public NodoBase getBlock() {
        return block;
    }

    public void setBlock(NodoBase block) {
        this.block = block;
    }

    public NodoBase getContenido() {
        return contenido;
    }

    public void setContenido(NodoBase contenido) {
        this.contenido = contenido;
    }

    public NodoBase getFuncion() {
        return funcion;
    }

    public void setFuncion(NodoBase funcion) {
        this.funcion = funcion;
    }

    public int getAmbito() {
        return ambito;
    }

    public void setAmbito(int ambito) {
        this.ambito = ambito;
    }

}
