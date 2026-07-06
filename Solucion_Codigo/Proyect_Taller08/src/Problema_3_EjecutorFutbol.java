
import java.util.ArrayList;

/*
Se desea realizar una aplicación que permita a un periodista deportivo llevar las estadísticas de los jugadores de un equipo de fútbol para poder valorar su actuación en el partido.

Cada jugador se identifica por su nombre, número de dorsal y Rut

Los jugadores se dividen en tres categorías:

Atacantes
Defensores
Porteros
Para todos los jugadores se desea contabilizar el número de goles marcados, además en el caso de los jugadores de campo se contabilizan los pases realizados con éxito y el número de balones recuperados. En el caso de los porteros se contabilizan las atajadas realizadas.

Valoración del jugador
Cálculo base para todos los jugadores:

valor_goles = goles * 30
Valor adicional según tipo de jugador:

Atacantes

valor += recuperaciones * 3
Defensores

valor += recuperaciones * 4
Porteros

valor += atajadas * 5
 */

/**
 * @author Joan Salinas
 * @version 1.0
 */

abstract class Jugador {
    public String nombre;
    public int dorsal;
    public int goles;
 
    public Jugador(String nombre, int dorsal) {
        this.nombre = nombre;
        this.dorsal = dorsal;
    }
 
    public abstract double calcularValoracion();
 
    public double calcularValorBase() {
        return goles * 30;
    }
 
    public void registrarGol() {
        goles++;
    }
 
    @Override
    public String toString() {
        return String.format("%s [dorsal=%d, goles=%d, valoracion=%.2f]",
                nombre, dorsal,goles, calcularValoracion());
    }
}
 
abstract class JugadorDeCampo extends Jugador {
    public int pasesExitosos;
    public int recuperaciones;
 
    public JugadorDeCampo(String nombre, int dorsal) {
        super(nombre, dorsal);
    }
 
    public void registrarPaseExitoso() {
        pasesExitosos++;
    }
 
    public void registrarRecuperacion() {
        recuperaciones++;
    }
}
 
class Atacante extends JugadorDeCampo {
 
    public Atacante(String nombre, int dorsal) {
        super(nombre, dorsal);
    }
 
    @Override
    public double calcularValoracion() {
        return calcularValorBase() + recuperaciones * 3;
    }
 
    @Override
    public String toString() {
        return "Atacante{" + "pasesExitosos=" + pasesExitosos + ", recuperaciones=" + recuperaciones
                + "} " + super.toString();
    }
}
 
class Defensor extends JugadorDeCampo {
 
    public Defensor(String nombre, int dorsal) {
        super(nombre, dorsal);
    }
 
    @Override
    public double calcularValoracion() {
        return calcularValorBase() + recuperaciones * 4;
    }
 
    @Override
    public String toString() {
        return "Defensor{" + "pasesExitosos=" + pasesExitosos + ", recuperaciones=" + recuperaciones
                + "} " + super.toString();
    }
}
 
class Portero extends Jugador {
    public int atajadas;
 
    public Portero(String nombre, int dorsal) {
        super(nombre, dorsal);
    }
 
    public void registrarAtajada() {
        atajadas++;
    }
 
    @Override
    public double calcularValoracion() {
        return calcularValorBase() + atajadas * 5;
    }
 
    @Override
    public String toString() {
        return "Portero{" + "atajadas=" + atajadas + "} " + super.toString();
    }
}

class Equipo {
    public String nombreEquipo;
    public ArrayList<Jugador> jugadores;
 
    public Equipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
        this.jugadores = new ArrayList<>();
    }
 
    public void agregarJugador(Jugador jugador) {
        jugadores.add(jugador);
    }
 
    @Override
    public String toString() {
        String detalleJugadores = "";
        for (Jugador jugador : jugadores) {
            detalleJugadores += jugador + "\n";
        }
        return "Equipo{" + "nombreEquipo=" + nombreEquipo + "}\n" + "jugadores=\n" + detalleJugadores;
    }
}
public class Problema_3_EjecutorFutbol {
    public static void main(String[] args) {
        Equipo equipo = new Equipo("Seleccion Ecuatoriana");
 
        Atacante atacante = new Atacante("Kendry Paez", 10);
        atacante.registrarGol();
        atacante.registrarGol();
        atacante.registrarPaseExitoso();
        atacante.registrarRecuperacion();
        atacante.registrarRecuperacion();
 
        Defensor defensor = new Defensor("William Pacho", 51);
        defensor.registrarGol();
        defensor.registrarPaseExitoso();
        defensor.registrarPaseExitoso();
        defensor.registrarRecuperacion();
        defensor.registrarRecuperacion();
        defensor.registrarRecuperacion();
 
        Portero portero = new Portero("Hernan Galindez", 1);
        portero.registrarAtajada();
        portero.registrarAtajada();
        portero.registrarAtajada();
 
        equipo.agregarJugador(atacante);
        equipo.agregarJugador(defensor);
        equipo.agregarJugador(portero);
 
        System.out.println(equipo);
    }
}
/**
run-single:
Equipo{nombreEquipo=Seleccion Ecuatoriana}
jugadores=
Atacante{pasesExitosos=1, recuperaciones=2} Kendry Paez [dorsal=10, goles=2, valoracion=66.00]
Defensor{pasesExitosos=2, recuperaciones=3} William Pacho [dorsal=51, goles=1, valoracion=42.00]
Portero{atajadas=3} Hernan Galindez [dorsal=1, goles=0, valoracion=15.00]

BUILD SUCCESSFUL (total time: 0 seconds)


 */