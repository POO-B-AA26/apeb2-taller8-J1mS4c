
import java.util.ArrayList;

/*
Problema 2 - Gestión de menus en un Restaurant
En un restaurant se tiene diferentes tipos de menú para ofrecer a los clientes. Una cuenta por pagar está compuesta por características como: nombre del cliente, listado de todos las cartas(menú) solicitados por el cliente, valor a cancelar total, subtotal, Iva.

Los tipos de menú del restaurant son:

Menú a la carta

nombre del plato
valor del menú
valor inicial del menú
valor de porción de guarnición
valor de bebida
porcentaje adicional por servicio en relación del valor inicial del menú
Menú del día

nombre del plato
valor del menú
valor inicial del menú
valor de postre
valor de bebida
Menú de niños

nombre del plato
valor del menú
valor inicial del menú
valor de porción de helado
valor de porción de pastel
Menú económico

nombre del plato
valor del menú
valor inicial del menú
porcentaje de descuento, en referencia al valor inicial del menú
*/
/**
 * @author Joan Salinas
 * @version 1.0
 */
abstract class Menu{
    public String nombrePlato;
    public double valorInicialMenu;
    public double valorMenu;

    public Menu(String nombrePlato, double valorInicialMenu) {
        this.nombrePlato = nombrePlato;
        this.valorInicialMenu = valorInicialMenu;
    }
    
    public abstract double calcularValor();

    @Override
    public String toString() {
        return String.format("%s [valorInicial=$%.2f, valorFinal=$%.2f]", nombrePlato, valorInicialMenu, valorMenu);
    }
    
}

class MenuCarta extends Menu {
    public double valorXGuarnicion;
    public double valorBebida;
    public double porcentajeServicio;

    public MenuCarta(double valorXGuarnicion, double valorBebida, double porcentajeServicio, String nombrePlato, double valorInicialMenu) {
        super(nombrePlato, valorInicialMenu);
        this.valorXGuarnicion = valorXGuarnicion;
        this.valorBebida = valorBebida;
        this.porcentajeServicio = porcentajeServicio;
    }
    
    @Override
    public double calcularValor(){
        double recargoServicio = valorInicialMenu * porcentajeServicio;
        valorMenu = valorInicialMenu+valorXGuarnicion+valorBebida+recargoServicio;
        return valorMenu;
    }
    
    @Override
    public String toString() {
        return "MenuALaCarta{" + "guarnicion=$" + valorXGuarnicion + ", bebida=$" + valorBebida
                + ", servicio=" + (porcentajeServicio * 100) + "%} " + super.toString();
    }
}

class MenuDia extends Menu{
    public double valorXPostre;
    public double valorBebida;

    public MenuDia(double valorXPostre, double valorBebida, String nombrePlato, double valorInicialMenu) {
        super(nombrePlato, valorInicialMenu);
        this.valorXPostre = valorXPostre;
        this.valorBebida = valorBebida;
    }
    
    @Override
    public double calcularValor(){
        valorMenu = valorInicialMenu + valorXPostre + valorBebida;
        return valorMenu;
    }

    @Override
    public String toString() {
        return "MenuDelDia{" + "postre=$" + valorXPostre + ", bebida=$" + valorBebida + "} " + super.toString();
    }
    
}

class MenuInfantil extends Menu{
    public double valorPorcionHelado;
    public double valorPorcionPastel;

    public MenuInfantil(double valorPorcionHelado, double valorPorcionPastel, String nombrePlato, double valorInicialMenu) {
        super(nombrePlato, valorInicialMenu);
        this.valorPorcionHelado = valorPorcionHelado;
        this.valorPorcionPastel = valorPorcionPastel;
    }
    
    @Override
    public double calcularValor(){
        return valorMenu =  valorInicialMenu + valorPorcionHelado + valorPorcionPastel;
    }

    @Override
    public String toString() {
        return "MenuInfantil{" + "helado=$" + valorPorcionHelado + ", pastel=$" + valorPorcionPastel + "} " + super.toString();
    }
    
}

class MenuEconomico extends Menu{
    public double porcentajeDescuento;

    public MenuEconomico(double porcentajeDescuento, String nombrePlato, double valorInicialMenu) {
        super(nombrePlato, valorInicialMenu);
        this.porcentajeDescuento = porcentajeDescuento;
    }
    
    @Override
    public double calcularValor(){
        double descuento = valorInicialMenu * porcentajeDescuento;
        return valorMenu = valorInicialMenu - descuento;
    }
    
    @Override
    public String toString() {
        return "MenuEconomico{" + "descuento=" + (porcentajeDescuento * 100) + "%} " + super.toString();
    }
    
}

class CuentaAPagar{
    public double porcentaje_iva = 0.15;
    public String nombreCliente;
    public ArrayList<Menu>menuSolicitados;
    public double subtotal;
    public double iva;
    public double total;

    public CuentaAPagar(String nombreCliente) {
        this.nombreCliente = nombreCliente;
        this.menuSolicitados = new ArrayList<>();
    }
    
    public void agregarMenu(Menu menu){
        menuSolicitados.add(menu);
    }
    
    public void calcularCuenta(){
        subtotal = 0;
        for(Menu menu: menuSolicitados){
            subtotal += menu.calcularValor();
        }
        iva = subtotal * porcentaje_iva;
        total = subtotal + iva;
    }
    
    @Override
    public String toString() {
        String detalleMenus = "";
        for (Menu menu : menuSolicitados) {
            detalleMenus += menu + "\n";
        }

        return "CuentaPorPagar{" + "nombreCliente=" + nombreCliente + "}\n"
                + "menusSolicitados=\n" + detalleMenus
                + String.format("subtotal=$%.2f, iva=$%.2f, totalPagar=$%.2f", subtotal, iva, total);
    }
}
public class Problema_2_EjecutorMenu {
        public static void main(String[] args) {
        CuentaAPagar cuenta = new CuentaAPagar("Maria Fernandez");
 
        Menu menu1 = new MenuCarta(1.20, 1.50, 0.10, "Lomo salteado", 8.50);
        Menu menu2 = new MenuDia(1.00, 1.00, "Seco de Pollo", 4.50);
        Menu menu3 = new MenuInfantil(0.75, 0.75, "Nuggets de Pollo", 3.00);
        Menu menu4 = new MenuEconomico(0.15, "Sopa de arroz", 5.00);
 
        cuenta.agregarMenu(menu1);
        cuenta.agregarMenu(menu2);
        cuenta.agregarMenu(menu3);
        cuenta.agregarMenu(menu4);
 
        cuenta.calcularCuenta();
 
        System.out.println(cuenta);
    }
}
/**
run-single:
CuentaPorPagar{nombreCliente=Maria Fernandez}
menusSolicitados=
MenuALaCarta{guarnicion=$1.2, bebida=$1.5, servicio=10.0%} Lomo salteado [valorInicial=$8.50, valorFinal=$12.05]
MenuDelDia{postre=$1.0, bebida=$1.0} Seco de Pollo [valorInicial=$4.50, valorFinal=$6.50]
MenuInfantil{helado=$0.75, pastel=$0.75} Nuggets de Pollo [valorInicial=$3.00, valorFinal=$4.50]
MenuEconomico{descuento=15.0%} Sopa de arroz [valorInicial=$5.00, valorFinal=$4.25]
subtotal=$27.30, iva=$4.10, totalPagar=$31.39
BUILD SUCCESSFUL (total time: 0 seconds)
 */