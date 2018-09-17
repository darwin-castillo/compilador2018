package ast;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tiny.Generador;
import tiny.UtGen;

public class Util {

    static int sangria = 0;
     private static FileWriter fw  ;
	   private static BufferedWriter bw;

    //Imprimo en modo texto con sangrias el AST
    public static void imprimirAST(NodoBase raiz) {
        sangria += 2;
        while (raiz != null) {
            printSpaces();
            if (raiz instanceof NodoIf) {
                System.out.println("If");
            } else if (raiz instanceof NodoRepeat) {
                System.out.println("Repeat");
            } else if (raiz instanceof NodoAsignacion) {
                System.out.println("Asignacion a: " + ((NodoAsignacion) raiz).getIdentificador());
            } else if (raiz instanceof NodoLeer) {
                System.out.println("Lectura: " + ((NodoLeer) raiz).getIdentificador());
            } else if (raiz instanceof NodoEscribir) {
                System.out.println("Escribir");
            } else if (raiz instanceof NodoMain) {
                System.out.println("Main");
            } else if (raiz instanceof NodoOperacion
                    || raiz instanceof NodoValor
                    || raiz instanceof NodoIdentificador) {
                imprimirNodo(raiz);
            } else {
                System.out.println("Tipo de nodo desconocido");
            };

            /* Hago el recorrido recursivo */
            if (raiz instanceof NodoIf) {
                printSpaces();
                System.out.println("**Prueba IF**");
                imprimirAST(((NodoIf) raiz).getPrueba());
                printSpaces();
                System.out.println("**Then IF**");
                imprimirAST(((NodoIf) raiz).getParteThen());
                if (((NodoIf) raiz).getParteElse() != null) {
                    printSpaces();
                    System.out.println("**Else IF**");
                    imprimirAST(((NodoIf) raiz).getParteElse());
                }
            } else if (raiz instanceof NodoRepeat) {
                printSpaces();
                System.out.println("**Cuerpo REPEAT**");
                imprimirAST(((NodoRepeat) raiz).getCuerpo());
                printSpaces();
                System.out.println("**Prueba REPEAT**");
                imprimirAST(((NodoRepeat) raiz).getPrueba());
            } else if (raiz instanceof NodoAsignacion) {
                imprimirAST(((NodoAsignacion) raiz).getExpresion());
            } else if (raiz instanceof NodoEscribir) {
                imprimirAST(((NodoEscribir) raiz).getExpresion());
            } else if (raiz instanceof NodoOperacion) {
                printSpaces();
                System.out.println("**Expr Izquierda Operacion**");
                imprimirAST(((NodoOperacion) raiz).getOpIzquierdo());
                printSpaces();
                System.out.println("**Expr Derecha Operacion**");
                imprimirAST(((NodoOperacion) raiz).getOpDerecho());
            }
            raiz = raiz.getHermanoDerecha();
        }
        sangria -= 2;
    }

    /* Imprime espacios con sangria */
    static void printSpaces() {
        int i;
        for (i = 0; i < sangria; i++) {
            System.out.print(" ");
        }
    }

    /* Imprime informacion de los nodos */
    static void imprimirNodo(NodoBase raiz) {
        if (raiz instanceof NodoRepeat
                || raiz instanceof NodoLeer
                || raiz instanceof NodoEscribir) {
            System.out.println("palabra reservada: " + raiz.getClass().getName());
        }

        if (raiz instanceof NodoAsignacion) {
            System.out.println(":=");
        }

        if (raiz instanceof NodoOperacion) {
            tipoOp sel = ((NodoOperacion) raiz).getOperacion();
            if (sel == tipoOp.menor) {
                System.out.println("<");
            }
            if (sel == tipoOp.igual) {
                System.out.println("=");
            }
            if (sel == tipoOp.mas) {
                System.out.println("+");
            }
            if (sel == tipoOp.menos) {
                System.out.println("-");
            }
            if (sel == tipoOp.por) {
                System.out.println("*");
            }
            if (sel == tipoOp.entre) {
                System.out.println("/");
            }
        }

        if (raiz instanceof NodoValor) {
            System.out.println("NUM, val= " + ((NodoValor) raiz).getValor());
        }

        if (raiz instanceof NodoIdentificador) {
            System.out.println("ID, nombre= " + ((NodoIdentificador) raiz).getNombre());
        }
    }

    public static void imprimirExpresion(NodoBase raiz) {
         try {
            fw=new FileWriter("salida/archivo_salida.txt");
        } catch (IOException ex) {
            Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (raiz != null) {
            if (raiz instanceof NodoOperacion) {
                imprimirExpresion(((NodoOperacion) raiz).getOpIzquierdo());
                imprimirExpresion(((NodoOperacion) raiz).getOpDerecho());
                tipoOp sel = ((NodoOperacion) raiz).getOperacion();

                switch (sel) {
                    case mas:
                        UtGen.emitirRO("ADI", String.valueOf(tipoOp.mas),bw);
                        break;
                    case menos:
                        UtGen.emitirRO("SBI", String.valueOf(tipoOp.menos),bw);
                        break;
                    case por:
                        UtGen.emitirRO("MPI", String.valueOf(tipoOp.por),bw);
                        break;
                    case entre:
                        UtGen.emitirRO("DVI", String.valueOf(tipoOp.entre),bw);
                        break;
                    case menor:
                        UtGen.emitirRO("GRT", String.valueOf(tipoOp.menor),bw);
                        break;
                    default:
                        UtGen.emitirComentario("BUG: Error sintactico: Tipo de operacion desconocida");
                }/*
                if (sel == tipoOp.menor) {
                    UtGen.emitirRO("GRT", String.valueOf(tipoOp.menor));
                }
                if (sel == tipoOp.mas) {
                    UtGen.emitirRO("ADI", String.valueOf(tipoOp.mas));
                }
                if (sel == tipoOp.menos) {
                    UtGen.emitirRO("SBI", String.valueOf(tipoOp.menos));
                }
                if (sel == tipoOp.por) {
                    UtGen.emitirRO("MPI", String.valueOf(tipoOp.por));
                }
                if (sel == tipoOp.entre) {
                    UtGen.emitirRO("DVI", String.valueOf(tipoOp.entre));
                }*/
            }

            if (raiz instanceof NodoValor) {
                UtGen.emitirRO("LDC " + String.valueOf(((NodoValor) raiz).getValor()), "",bw);
            }

            if (raiz instanceof NodoIdentificador) {
                UtGen.emitirRO("LOD " + ((NodoIdentificador) raiz).getNombre(), "",bw);
            }
            raiz = raiz.getHermanoDerecha();
        }
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
