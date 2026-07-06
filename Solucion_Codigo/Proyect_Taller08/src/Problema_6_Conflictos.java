import java.util.ArrayList;
import java.util.Random;

/*
La ONU le solicita desarrollar un simulador de conflictos bélicos mundiales en el lenguaje de alto nivel orientado a objetos Java, 
considerando sus cuatro pilares fundamentales: abstracción, encapsulamiento, herencia y polimorfismo, 
cumpliendo con los siguientes lineamientos:

Requisitos Funcionales
-De manera general, cada nación debe ser representada con la siguiente información: Nombre de la nación, Número de habitantes de la nación, 
 Cantidad de recursos económicos disponibles, Nivel de poder militar (valor entre 1 y 100), 
 su estado de conflicto que indica si la nación está actualmente en conflicto o no, y cualquier otra información que usted considere necesaria. No olvide implementar los métodos y/o constructores básicos para procesar esta información dados todos los requerimientos.

-A su vez se requiere la información de las naciones desarrolladas con alta tecnología militar, como: Si la nación dispone de tecnología avanzada. 
 Para estas naciones avanzadas, implementar el cálculo del impacto, el cual considera un bono de tecnología para el incremento de su poder militar 
 (no olvide que para este último la restricción es de 1-100, y en el caso de sobre pasar, asigne directamente 100).

-De igual forma se necesita conocer de las naciones en vías de desarrollo su nivel de recursos limitados (recursos económicos y poder militar 
 por cada N habitantes), así como la implementación del cálculo del impacto, el cual reduce el impacto en el conflicto debido a sus recursos limitados. 
 Queda a su criterio matemático y/o estadístico el planteamiento del modelo matemático (con las variables/parámetros que tenga a bien) para calcular este factor de impacto.

-Para las naciones desarrolladas o en vías de desarrollo, considere sus naciones aliadas, lo cual es decisivo para incrementar o decrementar su nivel de impacto directamente
 a su poder militar, pero solo si tiene aliados disponibles.

-El programa debe permitir declarar conflictos entre dos naciones seleccionadas con un proceso aleatorio/randomico.
-Calcular las consecuencias del conflicto utilizando polimorfismo y la implementación de cálculo de impacto.
-Consecuencias del conflicto:
    -Reducción del 5% de población por cada diferencia en los niveles de poder militar.
    -Reducción del 10% de recursos de la nación derrotada.
    -Si las naciones tienen el mismo nivel de poder militar, ambas pierden el 5% de recursos.
-Al finalizar el programa, debe mostrar un reporte con el estado actual de cada nación (población, recursos y estado de conflicto, etc), así como el total de conflictos que se simularon entre N naciones.
*/

/**
 * @author Joan Salinas
 * @version 1.0
 */
abstract class Nacion {
    public String nombre;
    public int habitantes;
    private double recursos;
    private int poder;
    public boolean enConflicto;
    public int conflictos;
    private ArrayList<Nacion> aliados;

    public Nacion(String nombre, int habitantes, double recursos, int poderInicial) {
        this.nombre = nombre;
        this.habitantes = habitantes;
        this.recursos = recursos;
        this.aliados = new ArrayList<>();
        setPoder(poderInicial);
    }

    public abstract double calcularImpacto();

    public double calcularBonoAliados() {
        if (aliados.isEmpty()) {
            return 0;
        }
        double sumaDif = 0;
        for (Nacion aliado : aliados) {
            sumaDif += (aliado.getPoder() - this.poder);
        }
        double promedioDif = sumaDif / aliados.size();
        return promedioDif * 0.10;
    }

    public void agregarAliado(Nacion aliado) {
        aliados.add(aliado);
    }

    public void registrarConflicto() {
        enConflicto = true;
        conflictos++;
    }

    public double getRecursos() {
        return recursos;
    }

    public void setRecursos(double recursos) {
        if (recursos < 0) {
            recursos = 0;
        }
        this.recursos = recursos;
    }

    public void setHabitantes(int habitantes) {
        if (habitantes < 0) {
            habitantes = 0;
        }
        this.habitantes = habitantes;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        if (poder > 100) {
            poder = 100;
        }
        if (poder < 1) {
            poder = 1;
        }
        this.poder = poder;
    }

    @Override
    public String toString() {
        return String.format(
                "%s [habitantes=%d, recursos=$%.2f, poder=%d, enConflicto=%s, conflictos=%d]",
                nombre, habitantes, recursos, poder, enConflicto ? "Si" : "No", conflictos);
    }
}

class NacionDesarrollada extends Nacion {
    private boolean tieneTecnologia;
    private int bonoTec;

    public NacionDesarrollada(String nombre, int habitantes, double recursos,
                               int poderInicial, boolean tieneTecnologia, int bonoTec) {
        super(nombre, habitantes, recursos, poderInicial);
        this.tieneTecnologia = tieneTecnologia;
        this.bonoTec = bonoTec;
    }

    @Override
    public double calcularImpacto() {
        double bonoAliados = calcularBonoAliados();
        int nuevoPoder = getPoder();

        if (tieneTecnologia) {
            nuevoPoder += bonoTec;
        }
        nuevoPoder += (int) bonoAliados;

        setPoder(nuevoPoder);
        return getPoder();
    }

    @Override
    public String toString() {
        return "NacionDesarrollada{" + "tieneTecnologia=" + (tieneTecnologia ? "Si" : "No")
                + ", bonoTec=" + bonoTec + "} " + super.toString();
    }
}

class NacionEnDesarrollo extends Nacion {
    private double recursosRef = 1.0;
    private double factorMin = 0.30;

    public NacionEnDesarrollo(String nombre, int habitantes, double recursos, int poderInicial) {
        super(nombre, habitantes, recursos, poderInicial);
    }

    public double calcularFactorRecursos() {
        double recursosPorHabitante = getRecursos() / habitantes;

        double factor = recursosPorHabitante / recursosRef;
        if (factor > 1.0) {
            factor = 1.0;
        }
        if (factor < factorMin) {
            factor = factorMin;
        }
        return factor;
    }

    @Override
    public double calcularImpacto() {
        double factor = calcularFactorRecursos();
        double bonoAliados = calcularBonoAliados();

        int nuevoPoder = (int) (getPoder() * factor + bonoAliados);

        setPoder(nuevoPoder);
        return getPoder();
    }

    @Override
    public String toString() {
        return "NacionEnDesarrollo{" + "factorRecursos=" + String.format("%.2f", calcularFactorRecursos())
                + "} " + super.toString();
    }
}

class GestorConflictos {
    public ArrayList<Nacion> naciones = new ArrayList<>();
    public int totalConflictos;
    Random ale = new Random();

    public void agregarNacion(Nacion n) {
        naciones.add(n);
    }

    public void simularConflicto() {
        if (naciones.size() < 2) {
            System.out.println("Se necesitan al menos 2 naciones para simular un conflicto.");
            return;
        }
        int i1 = ale.nextInt(naciones.size());
        int i2;
        do {
            i2 = ale.nextInt(naciones.size());
        } while (i2 == i1);
        resolverConflicto(naciones.get(i1), naciones.get(i2));
    }

    public void resolverConflicto(Nacion n1, Nacion n2) {
        System.out.println("\nConflicto entre " + n1.nombre + " y " + n2.nombre);
        double impacto1 = n1.calcularImpacto();
        double impacto2 = n2.calcularImpacto();
        System.out.println(n1.nombre + " calcula un impacto de " + impacto1);
        System.out.println(n2.nombre + " calcula un impacto de " + impacto2);

        int diferencia = n1.getPoder() - n2.getPoder();
        if (diferencia < 0) {
            diferencia = -diferencia;
        }

        double perdidaPoblacion = diferencia * 0.05;
        if (perdidaPoblacion > 0.90) {
            perdidaPoblacion = 0.90;
        }

        n1.setHabitantes(n1.habitantes - (int) (n1.habitantes * perdidaPoblacion));
        n2.setHabitantes(n2.habitantes - (int) (n2.habitantes * perdidaPoblacion));
        System.out.println("Ambas pierden " + String.format("%.1f", perdidaPoblacion * 100) + "% de poblacion.");

        if (n1.getPoder() > n2.getPoder()) {
            n2.setRecursos(n2.getRecursos() * 0.90);
            System.out.println(n1.nombre + " ha derrotado a " + n2.nombre + " ha perdido el 10% de recursos.");
        } else if (n2.getPoder() > n1.getPoder()) {
            n1.setRecursos(n1.getRecursos() * 0.90);
            System.out.println(n2.nombre + " ha derrotado a " + n1.nombre + " ha perdido el 10% de recursos.");
        } else {
            n1.setRecursos(n1.getRecursos() * 0.95);
            n2.setRecursos(n2.getRecursos() * 0.95);
            System.out.println("Tregua de paz por igualdad en poder militar ambas pierden 5% de recursos.");
        }

        n1.registrarConflicto();
        n2.registrarConflicto();
        totalConflictos++;
    }

    public void generarReporte() {
        String detalle = "";
        for (Nacion n : naciones) {
            detalle += n + "\n";
        }
        System.out.println("\nREPORTE FINAL");
        System.out.println(detalle + "Total de conflictos simulados: " + totalConflictos);
    }
}

public class Problema_6_Conflictos {
    public static void main(String[] args) {
        GestorConflictos gestor = new GestorConflictos();

        NacionDesarrollada usa = new NacionDesarrollada("Estados Unidos", 500, 800.0, 95, true, 15);
        NacionDesarrollada rusia = new NacionDesarrollada("Rusia", 300, 400.0, 90, true, 10);
        NacionEnDesarrollo india = new NacionEnDesarrollo("India", 900, 200.0, 70);
        NacionEnDesarrollo nigeria = new NacionEnDesarrollo("Nigeria", 250, 90.0, 40);

        usa.agregarAliado(rusia);
        india.agregarAliado(nigeria);

        gestor.agregarNacion(usa);
        gestor.agregarNacion(rusia);
        gestor.agregarNacion(india);
        gestor.agregarNacion(nigeria);

        for (int i = 0; i < 5; i++) {
            gestor.simularConflicto();
        }

        gestor.generarReporte();
    }
}/**
 run:

Conflicto entre Estados Unidos y India
Estados Unidos calcula un impacto de 100.0
India calcula un impacto de 18.0
Ambas pierden 90.0% de poblacion.
Estados Unidos ha derrotado a India ha perdido el 10% de recursos.

Conflicto entre Estados Unidos y Rusia
Estados Unidos calcula un impacto de 100.0
Rusia calcula un impacto de 100.0
Ambas pierden 0.0% de poblacion.
Tregua de paz por igualdad en poder militar ambas pierden 5% de recursos.

Conflicto entre Rusia y India
Rusia calcula un impacto de 100.0
India calcula un impacto de 20.0
Ambas pierden 90.0% de poblacion.
Rusia ha derrotado a India ha perdido el 10% de recursos.

Conflicto entre Rusia y India
Rusia calcula un impacto de 100.0
India calcula un impacto de 22.0
Ambas pierden 90.0% de poblacion.
Rusia ha derrotado a India ha perdido el 10% de recursos.

Conflicto entre Nigeria y India
Nigeria calcula un impacto de 14.0
India calcula un impacto de 21.0
Ambas pierden 35.0% de poblacion.
India ha derrotado a Nigeria ha perdido el 10% de recursos.

REPORTE FINAL
NacionDesarrollada{tieneTecnologia=Si, bonoTec=15} Estados Unidos [habitantes=50, recursos=$760.00, poder=100, enConflicto=Si, conflictos=2]
NacionDesarrollada{tieneTecnologia=Si, bonoTec=10} Rusia [habitantes=3, recursos=$380.00, poder=100, enConflicto=Si, conflictos=3]
NacionEnDesarrollo{factorRecursos=1.00} India [habitantes=1, recursos=$145.80, poder=21, enConflicto=Si, conflictos=4]
NacionEnDesarrollo{factorRecursos=0.50} Nigeria [habitantes=163, recursos=$81.00, poder=14, enConflicto=Si, conflictos=1]
Total de conflictos simulados: 5
BUILD SUCCESSFUL (total time: 0 seconds)

 */