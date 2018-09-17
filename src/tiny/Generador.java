package tiny;

import ast.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Generador {

    /* Ilustracion de la disposicion de la memoria en
	 * este ambiente de ejecucion para el lenguaje Tiny
	 *
	 * |t1	|<- mp (Maxima posicion de memoria de la TM
	 * |t1	|<- desplazamientoTmp (tope actual)
	 * |free|
	 * |free|
	 * |...	|
	 * |x	|
	 * |y	|<- gp
	 * 
	 * */

 /* desplazamientoTmp es una variable inicializada en 0
	 * y empleada como el desplazamiento de la siguiente localidad
	 * temporal disponible desde la parte superior o tope de la memoria
	 * (la que apunta el registro MP).
	 * 
	 * - Se decrementa (desplazamientoTmp--) despues de cada almacenamiento y
	 * 
	 * - Se incrementa (desplazamientoTmp++) despues de cada eliminacion/carga en 
	 *   otra variable de un valor de la pila.
	 * 
	 * Pudiendose ver como el apuntador hacia el tope de la pila temporal
	 * y las llamadas a la funcion emitirRM corresponden a una inserccion 
	 * y extraccion de esta pila
     */
    private static int desplazamientoTmp = 0;
    private static TablaSimbolos tablaSimbolos = null;
    private static boolean ban_fun = false;
    private static String Nombre_ambito = "";
    private static int inicioMain = -1;
    private static HashMap<String, Integer> elemFuncion = new HashMap<String, Integer>();
     private static FileWriter fw  ;
	   private static BufferedWriter bw;

    public static void setTablaSimbolos(TablaSimbolos tabla) {
        tablaSimbolos = tabla;
    }

    public static void generarCodigoObjeto(NodoBase raiz) {
        System.out.println();
        System.out.println();
        System.out.println("------ CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
        System.out.println();
        System.out.println();
        try {
            fw=new FileWriter("salida/archivo_salida.txt");
        } catch (IOException ex) {
            Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
        }
		bw = new BufferedWriter(fw);
        generarPreludioEstandar();
        generar(raiz);
        /*Genero el codigo de finalizacion de ejecucion del codigo*/
//        UtGen.emitirComentario("Fin de la ejecucion.");
        UtGen.emitirRO("STP", "",bw);
        System.out.println();
        System.out.println();
        System.out.println("------ FIN DEL CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA EL CODIGO P ------");
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Generador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Funcion principal de generacion de codigo
    //prerequisito: Fijar la tabla de simbolos antes de generar el codigo objeto 
    private static void generar(NodoBase nodo) {
        if (tablaSimbolos != null) {
            if (nodo instanceof NodoIf) {
                generarIf(nodo);
            } else if (nodo instanceof NodoRepeat) {
                generarRepeat(nodo);
            } else if (nodo instanceof NodoAsignacion) {
                generarAsignacion(nodo);
            } else if (nodo instanceof NodoLeer) {
                generarLeer(nodo);
            } else if (nodo instanceof NodoEscribir) {
                generarEscribir(nodo);
            } else if (nodo instanceof NodoValor) {
                generarValor(nodo);
            } else if (nodo instanceof NodoIdentificador) {
                generarIdentificador(nodo);
            } else if (nodo instanceof NodoOperacion) {
                generarOperacion(nodo);
            } else if (nodo instanceof NodoFuncion) {
                generarFuncion(nodo);
            } else if (nodo instanceof NodoReturn) {
                generarReturn(nodo);
            } else if (nodo instanceof NodoCall) {
                generarCall(nodo);
            } else if (nodo instanceof NodoDeclaracion) {
                generarDeclaracion(nodo);
            } else if (nodo instanceof NodoVector) {
                generarVector(nodo);
            } else if (nodo instanceof NodoTipo) {
                generarTipo(nodo);
            } else if (nodo instanceof NodoTipoFunc) {
                generarTipoFunc(nodo);
            } else if (nodo instanceof NodoMain) {
                generarMain(nodo);
            } else {
                System.out.println("BUG: Tipo de nodo a generar desconocido");
            }
            /*Si el hijo de extrema izquierda tiene hermano a la derecha lo genero tambien*/
            if (nodo.TieneHermano()) {
                generar(nodo.getHermanoDerecha());
            }
        } else {
            System.out.println("ERROR: por favor fije la tabla de simbolos a usar antes de generar codigo objeto!!!");
        }
    }

    public static void generarMain(NodoBase nodo) {
        NodoMain n = (NodoMain) nodo;
        int posicionActual;
//        UtGen.emitirComentario("Inicio de un bloque");
        if (inicioMain == -1) {
            inicioMain = UtGen.emitirSalto(1);
        }
        if (n.getFuncion() != null) {
            generar(n.getFuncion());
            ban_fun = true;
        }
        if ("".equals(Nombre_ambito)) {
            posicionActual = UtGen.emitirSalto(0);
            Nombre_ambito = "main";
            UtGen.cargarRespaldo(inicioMain);
            UtGen.emitirRM_Abs("LDA", UtGen.PC, posicionActual, "Saltar las funciones y empezar con el main",bw);
            UtGen.restaurarRespaldo();
            //UtGen.emitirRM("LDC", UtGen.DM, posicionActual, UtGen.AC, "Cargar el inicio del main");
            Iterator iterador = elemFuncion.entrySet().iterator();
//            int direccion;
            Map.Entry funcion;
            while (iterador.hasNext()) {
                funcion = (Map.Entry) iterador.next();
                UtGen.emitirRM("LDC", funcion.getValue().toString(), "op: pop o cargo de la pila la posicion actual de la funcion",bw);
//                direccion = tablaSimbolos.getDireccion(funcion.getKey().toString());
                UtGen.emitirRM("STO", funcion.getKey().toString(), "Guarda en memoria la posicion actual de la funcion",bw);
                //producto = iterador.next(); Si se usase tambien la otra linea comentada.
            }
        }
        generar(n.getContenido());
        if (ban_fun) {
            UtGen.emitirRM("LDA", UtGen.PC, 0, UtGen.DM, "Salta hacia el main",bw);
            ban_fun = false;
        }
        Nombre_ambito = "";
        if (n.getBlock() != null) {
            generar(n.getBlock());
        }
    }

    private static void generarIf(NodoBase nodo) {
        NodoIf n = (NodoIf) nodo;
        int localidadSaltoElse, localidadSaltoEnd, localidadActual;

        generar(n.getPrueba());
        localidadSaltoElse = UtGen.emitirSalto(1);
        if (n.getParteElse() != null) {
            UtGen.emitirRM_Abs("FJP", "ELSE_L"+localidadSaltoElse,bw);
        }else{
            UtGen.emitirRM_Abs("FJP", "L"+localidadSaltoElse,bw);
        }
        /*Genero la parte THEN*/
        generar(n.getParteThen());
        localidadSaltoEnd = UtGen.emitirSalto(1);

        localidadActual = UtGen.emitirSalto(0);
        UtGen.cargarRespaldo(localidadSaltoElse);
//        UtGen.emitirRM_Abs("FJP", UtGen.AC, localidadActual, "if: jmp hacia else");
        UtGen.restaurarRespaldo();

        /*Genero la parte ELSE*/
        if (n.getParteElse() != null) {
            UtGen.emitirComentario("LAB ELSE_L"+localidadSaltoElse);
            generar(n.getParteElse());
            localidadActual = UtGen.emitirSalto(0);
            UtGen.cargarRespaldo(localidadSaltoEnd);
            UtGen.emitirRM_Abs("FJP", "ENDIF_L"+localidadSaltoEnd,bw);
            UtGen.restaurarRespaldo();
            UtGen.emitirComentario("LAB ENDIF_L"+localidadSaltoEnd);
        }else{
            UtGen.emitirComentario("LAB L"+localidadSaltoElse);
        }
    }

    private static void generarRepeat(NodoBase nodo) {
        NodoRepeat n = (NodoRepeat) nodo;
        int localidadSaltoInicio;

        localidadSaltoInicio = UtGen.emitirSalto(0);
        /* Genero el cuerpo del repeat */
        generar(n.getCuerpo());
        /* Genero el codigo de la prueba del repeat */
        generar(n.getPrueba());
        UtGen.emitirRM_Abs("FJP", UtGen.AC, localidadSaltoInicio, "repeat: jmp hacia el inicio del cuerpo",bw);
    }

    private static void generarAsignacion(NodoBase nodo) {
        NodoAsignacion n = (NodoAsignacion) nodo;

        Simbolo simbolo;
        try {
            simbolo = tablaSimbolos.BuscarSimbolo(n.getIdentificador());
            if (n.getVct() != null) {
                generar(n.getVct());
            } else {
                UtGen.emitirRM("LDA", String.valueOf(simbolo.getDireccionMemoria()), "Asigna memoria a la variable '" + n.getIdentificador() + "' con posicion de memoria " + simbolo.getDireccionMemoria(),bw);
            }

        } catch (Exception e) {
            System.out.println("Error semantico: La variabe '" + n.getIdentificador() + "' no ha sido declarada. Exception: " + e);
        }
        /* Genero el codigo para la expresion a la derecha de la asignacion */
        generar(n.getExpresion());
        /* Ahora almaceno el valor resultante */
        UtGen.emitirRM("STO", "", "",bw);
    }

    private static void generarLeer(NodoBase nodo) {
        NodoLeer n = (NodoLeer) nodo;
        int direccion;

        try {
            direccion = tablaSimbolos.getDireccion(n.getIdentificador());

            UtGen.emitirRM("LOD", String.valueOf(direccion), ";Cargo el valor de '" + n.getIdentificador() + "' con posicion de memoria " + direccion,bw);
            UtGen.emitirRO("RDI", ";Lee el valor entero '" + n.getIdentificador() + "' con posocion de memoria " + direccion,bw);
        } catch (Exception e) {
            System.out.println("Error semantico: La variabe '" + n.getIdentificador() + "' no ha sido declarada. Exception: " + e);
        }
    }

    private static void generarEscribir(NodoBase nodo) {
        NodoEscribir n = (NodoEscribir) nodo;

        /* Genero el codigo de la expresion que va a ser escrita en pantalla */
        generar(n.getExpresion());
        /* Ahora genero la salida */
        UtGen.emitirRO("WRI", "",bw);
    }

    private static void generarValor(NodoBase nodo) {
        NodoValor n = (NodoValor) nodo;
        int direccion;

        direccion = tablaSimbolos.getDireccion(String.valueOf(n.getValor()));
        UtGen.emitirRM("LDC", Integer.toString(n.getValor()), ";Cargo constante " + n.getValor() + " con posicion de memoria " + direccion,bw);
    }

    private static void generarIdentificador(NodoBase nodo) {
        NodoIdentificador n = (NodoIdentificador) nodo;
        Simbolo simbolo;

        try {
            simbolo = tablaSimbolos.BuscarSimbolo(n.getNombre());
//            if (!simbolo.isIsItDeclarated()) {
                UtGen.emitirRM("LOD", String.valueOf(simbolo.getDireccionMemoria()), ";Cargo valor del identificador '" + n.getNombre() + "' con posicion de memoria " + simbolo.getDireccionMemoria(),bw);
//                simbolo.setIsItDeclarated(true);
//            }
        } catch (Exception e) {
            System.out.println("Error semantico: La variabe '" + n.getNombre() + "' no ha sido declarada. Exception: " + e);
        }
    }

    private static void generarOperacion(NodoBase nodo) {
        NodoOperacion n = (NodoOperacion) nodo;

        /* Genero la expresion izquierda de la operacion */
        generar(n.getOpIzquierdo());

        /* Genero la expresion derecha de la operacion */
        generar(n.getOpDerecho());

        switch (n.getOperacion()) {
            case mas:
                UtGen.emitirRO("ADI", "",bw);
                break;
            case menos:
                UtGen.emitirRO("SBI", "",bw);
                break;
            case por:
                UtGen.emitirRO("MPI", "",bw);
                break;
            case entre:
                UtGen.emitirRO("DVI", "",bw);
                break;
            case menor:
                UtGen.emitirRO("GRT", "",bw);
                UtGen.emitirRM("JLT", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC<0)",bw);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)",bw);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)",bw);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)",bw);
                break;
            case igual:
                UtGen.emitirRO("EQU", "",bw);
                UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC==0)",bw);
                UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)",bw);
                UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)",bw);
                UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)",bw);
                break;
            default:
                UtGen.emitirComentario("BUG: tipo de operacion desconocida");
        }
    }

    private static void generarFuncion(NodoBase nodo) {
        NodoFuncion n = (NodoFuncion) nodo;
        int localidadSaltoElse, localidadSaltoEnd, localidadActual;

        UtGen.emitirRO("LAB  " + n.getId() + "Funcion", "",bw);
        /*Genero los argumentos*/
        generar(n.getArgs());
        /*Genero el contenido, el cuerpo de la funcion*/
        generar(n.getContenido());
        UtGen.emitirRM_Abs("FJP", n.getId(),bw);
    }

    private static void generarReturn(NodoBase nodo) {
        NodoReturn n = (NodoReturn) nodo;
        generar(n.getExpresion());
    }

    private static void generarVector(NodoBase nodo) {
        NodoVector n = (NodoVector) nodo;

        int direccion;
        try {
            direccion = tablaSimbolos.getDireccion(n.getId());

            UtGen.emitirRM("LDC", String.valueOf(n.getTam()), "",bw);
            n.setIdentificador(new NodoIdentificador(n.getId()));
            generar(n.getIdentificador());
        } catch (Exception e) {
            System.out.println("Error semantico: La variabe '" + n.getId() + "' no ha sido declarada. Exception: " + e);
        }
    }

    private static void generarCall(NodoBase nodo) {
        int localidadSaltoFunction, localidadActual, localidadSaltoEnd, direccion;
        Simbolo simbolo;
        NodoCall n = (NodoCall) nodo;

        generar(n.getParams());
        localidadSaltoFunction = UtGen.emitirSalto(1);
        /*
            Se agregar el sufijo 'Funcion' al Id de la busqueda del simbolo, para no confundirlo con el Id del
            CallFuncion. De esta forma, cuando se vaya a buscar el Id de la funcion, me traerá todos los atributos
            de la funcion y no del CallFuncion.
        
            simbolo = tablaSimbolos.BuscarSimbolo(n.getId() + "Funcion");
            ((NodoCall) nodo).setFuncion(new NodoFuncion(simbolo.getTipo(), simbolo.getIdentificador(), simbolo.getArgs(), simbolo.getContenido()));
            // generar(n.getFuncion());
         */
        localidadSaltoEnd = UtGen.emitirSalto(1);
        localidadActual = UtGen.emitirSalto(0);
        UtGen.cargarRespaldo(localidadSaltoFunction);
        UtGen.emitirRM_Abs("FJP", n.getId() + "Funcion",bw);
        UtGen.restaurarRespaldo();
        UtGen.emitirRO("LAB  " + n.getId(), "",bw);
    }

    private static void generarDeclaracion(NodoBase nodo) {
        NodoDeclaracion n = (NodoDeclaracion) nodo;

        Simbolo simbolo;
        try {
            simbolo = tablaSimbolos.BuscarSimbolo(n.getVariable());

            if (n.getVect() != null) {
                generar(n.getVect());
            } else {
                if (!simbolo.isIsItDeclarated()) {
                    UtGen.emitirRM("LDA", String.valueOf(simbolo.getDireccionMemoria()), "Asigna memoria a la variable '" + n.getVariable() + "' con posicion de memoria " + simbolo.getDireccionMemoria(),bw);
                    simbolo.setIsItDeclarated(true);
                }
            }
        } catch (Exception e) {
            System.out.println("Error semantico: La variabe '" + n.getVariable() + "' no ha sido declarada. Exception: " + e);
        }
    }

    private static void generarTipo(NodoBase nodo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void generarTipoFunc(NodoBase nodo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //TODO: enviar preludio a archivo de salida, obtener antes su nombre
    private static void generarPreludioEstandar() {
        UtGen.emitirComentario("Compilacion TINY para el codigo objeto TM");
        UtGen.emitirComentario("Archivo: " + "NOMBRE_ARREGLAR");
        /*Genero inicializaciones del preludio estandar*/
 /*Todos los registros en tiny comienzan en cero*/
        UtGen.emitirComentario("Preludio estandar:");
        UtGen.emitirRM("LD", UtGen.MP, 0, UtGen.AC, "cargar la maxima direccion desde la localidad 0",bw);
        UtGen.emitirRM("ST", UtGen.AC, 0, UtGen.AC, "limpio el registro de la localidad 0",bw);
    }
}
