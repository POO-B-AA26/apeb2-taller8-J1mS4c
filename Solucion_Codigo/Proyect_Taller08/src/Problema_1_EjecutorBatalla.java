import java.util.Random;

/*
Problema 1 .- En un juego de rol, se desea implementar un sistema de combate en el que participen diferentes tipos de personajes: guerreros, magos y arqueros.
Cada personaje tiene atributos y habilidades únicas, así como diferentes métodos de ataque y defensa.

El objetivo del juego es enfrentar a los personajes en batallas y determinar el ganador en función de sus habilidades, estrategias y atributos. 
Los guerreros se destacan por su fuerza y habilidades cuerpo a cuerpo, los magos por sus hechizos y poderes mágicos, y los arqueros por su precisión y habilidades a distancia.

El sistema debe permitir crear nuevos personajes de cada tipo, asignarles atributos iniciales, como puntos de vida y nivel de experiencia, 
y permitirles subir de nivel a medida que ganan batallas. Además, se debe implementar un algoritmo de combate que evalúe las habilidades de cada personaje 
y determine el resultado de la batalla.

Utilizando programación orientada a objetos, herencia y polimorfismo, implementa el sistema de combate y las clases necesarias para representar a los diferentes tipos de personajes. 
Asegúrate de que cada tipo de personaje tenga sus propias habilidades y métodos de ataque y defensa, y que puedan interactuar entre sí en las batallas.
 */

/**
 * @author Joan Salinas
 * @version 1.0
 */
abstract class Personaje{
    public String nombre;
    public int ptsVida;
    public int vidaMax;
    public int lvlExp;
    public int nivel;
    public int batallasGanadas;
    public int expSubirLvl = 3;

    // Un solo Random compartido por todos los personajes, en vez de uno por cada instancia.
    Random ALE = new Random();
    
    public Personaje(String nombre, int ptsVida) {
        this.nombre = nombre;
        this.ptsVida = ptsVida;
        this.vidaMax = ptsVida;
        this.nivel = 1;
    }
    
    public abstract int ataque(Personaje personaje);
    public abstract int defensa(int danioRecibido);
    
    public boolean estaVivo(){
        return ptsVida > 0;
    }
    
    public void recibirDanio(int danioBase){
        int danio = defensa(danioBase);
        ptsVida -= danio;
        if(ptsVida < 0){
            ptsVida = 0;
        }
    }
    
    public void contarBatallaGanada(){
        batallasGanadas++;
        lvlExp+=2;
        subirNivel();
    }
    
    public void contarBatallaPerdida(){
        lvlExp+=1;
        subirNivel();
    }
    
    public void subirNivel(){
        while(lvlExp >= nivel * expSubirLvl){
            nivel++;
            vidaMax +=2;
            ptsVida = vidaMax;
            System.out.println(nombre+" subio a nivel "+nivel);
        }
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Personaje{" + "nombre=" + nombre + ", ptsVida=" + ptsVida + ", vidaMax=" + vidaMax + ", lvlExp=" + lvlExp + ", nivel=" + nivel + 
                ", batallasGanadas=" + batallasGanadas + ", expSubirLvl=" + expSubirLvl + '}';
    }
    
}

class Guerrero extends Personaje{
    public int fuerza;//1..10
    public int armadura;
    
    public Guerrero(String nombre, int fuerza,  int ptsVida) {
        super(nombre, ptsVida);
        this.fuerza = fuerza;
        this.armadura = 2;
    }
    
    @Override
    public int ataque(Personaje personaje) {
        int danio = fuerza + ALE.nextInt(5);
        System.out.println(nombre + " ataca cuerpo a cuerpo causando " + danio + " de danio.");
        personaje.recibirDanio(danio);
        return danio;
    }
    
    @Override
    public int defensa(int danioRecibido){
        int danioFinal = danioRecibido - armadura;
        if(danioFinal < 0){
            danioFinal=0;
        }
        System.out.println(nombre+" bloquea "+armadura+" de danio");
        return danioFinal;
    }

    @Override
    public String toString() {
        return "Guerrero{" + "fuerza=" + fuerza + ", armadura=" + armadura + '}'+super.toString();
    }
    
}

class Mago extends Personaje{
    public String hechizo;//Avadakedabra, expelliarmus
    public int mana;
    public int poderMagico;
    public int manaMax;

    public Mago(String nombre, String hechizo, int poderMagico, int ptsVida) {
        super(nombre, ptsVida);
        this.hechizo = hechizo;
        this.poderMagico = poderMagico;
        this.manaMax = 10;
        this.mana = manaMax;
    }
    
    @Override
    public int ataque(Personaje personaje){
        if(mana < 3){
            System.out.println(nombre+" no tiene mana y golpea con su baston");
            int danioDebil = 1;
            personaje.recibirDanio(danioDebil);
            return danioDebil;
        }
        mana -= 3;
        int danioMagico = poderMagico + ALE.nextInt(6);
        System.out.println(nombre+" lanza "+hechizo+" causando " + danioMagico+" de danio magico, ahora le queda "+mana+" mana.");
        personaje.recibirDanio(danioMagico);
        return danioMagico;
    }
    
    @Override
    public int defensa(int danioRecibido) {
        if (mana >= 2) {
            mana -= 2;
            int danioFinal = danioRecibido / 2;
            System.out.println(nombre + " invoca un escudo magico y reduce el danio a la mitad.");
            return danioFinal;
        }
        return danioRecibido;
    }

    @Override
    public String toString() {
        return "Mago{" + "hechizo=" + hechizo + ", mana=" + mana + ", manaMax=" + manaMax + '}'+super.toString();
    }
}

class Arquero extends Personaje{
    public int precision;
    public int cantidadFlechas;

   public Arquero(String nombre, int precision, int cantidadFlechas, int ptsVida) {
        super(nombre, ptsVida);
        this.precision = precision;
        this.cantidadFlechas = cantidadFlechas;
    }
 
    @Override
    public int ataque(Personaje personaje){
        if (cantidadFlechas <= 0) {
            System.out.println(nombre + " se quedó sin flechas y golpea con el arco.");
            int danio = 1;
            personaje.recibirDanio(danio);
            return danio;
        }
        
        cantidadFlechas--;
        int probabilidad = ALE.nextInt(10)+1;
        if(probabilidad <= precision){
            int danioDistancia = ALE.nextInt(4) + precision;
            System.out.println(nombre+" dispara una flecha certera y causa "+danioDistancia+" de danio, le quedan "+cantidadFlechas+" flechas.");
            personaje.recibirDanio(danioDistancia);
            return danioDistancia;
        }else {
            System.out.println(nombre + " falla el disparo y le quedan " + cantidadFlechas + " flechas");
            return 0;
        }
    }
    
    @Override
    public int defensa(int danioRecibido){
        int esquive = ALE.nextInt(10)+1;
        if(esquive <= precision / 2){
            System.out.println(nombre+" esquiva el ataque");
            return 0;
        }
        return danioRecibido;
    }

    @Override
    public String toString() {
        return "Arquero{" + "precision=" + precision + ", cantidadFlechas=" + cantidadFlechas + '}'+super.toString();
    }    
}

class ComenzarBatalla{
    public Personaje batalla(Personaje p1, Personaje p2){
        System.out.println("Comienza la pelea entre "+p1.getNombre()+" y "+p2.getNombre());
        
        Personaje atacante = p1;
        Personaje defensor = p2;
        int turno = 1;
        
        while(p1.estaVivo() && p2.estaVivo()){
            System.out.println("\n Turno "+turno);
            atacante.ataque(defensor);
            if(!defensor.estaVivo()){
                break;
            }
            Personaje aux = atacante;
            atacante = defensor;
            defensor = aux;
            turno++;
        }
        
        Personaje ganador = p1.estaVivo() ? p1 : p2;
        Personaje perdedor = p1.estaVivo() ? p2 : p1;
        
        ganador.contarBatallaGanada();
        perdedor.contarBatallaPerdida();
        
        System.out.println(ganador.getNombre()+" ha salido Victorioso!");
        System.out.println("\nRESULTADO FINAL");
        System.out.println("\n"+ganador);
        System.out.println("\n"+perdedor);
        
        return ganador;
    }
}

public class Problema_1_EjecutorBatalla {
    public static void main(String[] args) {
        Personaje guerrero = new Guerrero("Thorfin", 7, 20);
        Personaje mago = new Mago("Harry", "Expelliarmus", 6, 16);
        
        ComenzarBatalla b1 = new ComenzarBatalla();
        b1.batalla(guerrero, mago);
       
    }
}