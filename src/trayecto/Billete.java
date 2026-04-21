package trayecto;

import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.time.LocalDate;

public class Billete {

    private Ciudad origen;
    private Ciudad destino;
    private LocalDate fechaViaje;
    private int edad;


    public Billete(Ciudad origen, Ciudad destino, LocalDate fechaViaje, int edad) {

        if (origen == destino) {
            throw new IllegalArgumentException("Origen y destino no pueden ser iguales.");
        }

        if (fechaViaje.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de viaje no puede ser anterior a la fecha de compra.");
        }

        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }

        this.origen = origen;
        this.destino = destino;
        this.fechaViaje = fechaViaje;
        this.edad = edad;
    }

    //  Precio base directo aquí (sin clase Trayecto)
    private double obtenerPrecioBase() {

        if ((origen == Ciudad.SANTANDER && destino == Ciudad.MADRID) || (origen == Ciudad.MADRID && destino == Ciudad.SANTANDER)) {
            return 100.0;
        }

        if ((origen == Ciudad.MADRID && destino == Ciudad.BARCELONA) || (origen == Ciudad.BARCELONA && destino == Ciudad.MADRID)) {
            return 120.0;
        }

        if ((origen == Ciudad.SANTANDER && destino == Ciudad.BARCELONA) || (origen == Ciudad.BARCELONA && destino == Ciudad.SANTANDER)) {
            return 150.0;
        }

        throw new IllegalArgumentException("Trayecto no válido.");
    }


    private double calcularDescuentoEdad() {
        if (edad < 25) {
            return 0.30;
        } else if (edad > 65) {
            return 0.40;
        }
        return 0.0;
    }

    private double calcularDescuentoAntelacion() {

        if (LocalDate.now().isBefore(fechaViaje.minusDays(30))) {
            return 0.25;
        } else if (LocalDate.now().isBefore(fechaViaje.minusDays(7))) {
            return 0.15;
        }
        return 0.0;
    }


    public double calcularPrecioFinal() {

        double precioBase = obtenerPrecioBase();

        double descuentoEdad = calcularDescuentoEdad();
        double descuentoAntelacion = calcularDescuentoAntelacion();

        double descuentoFinal = Math.max(descuentoEdad, descuentoAntelacion);

        return precioBase * (1 - descuentoFinal);
    }

    @Override
    public String toString() {
        return "Billete {" + "origen=" + origen + ", destino=" + destino + ", fechaCompra=" + LocalDate.now() + ", fechaViaje=" + fechaViaje + ", edad=" + edad + ", precioFinal=" + String.format("%.2f", calcularPrecioFinal()) + " €}";
    }


    public static void main(String[] args) {

        // Billete 1: joven con descuento por edad (30%)
        Billete billete1 = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.of(2026, 4, 20), 22);
        System.out.println(billete1.calcularPrecioFinal());


        // Billete 2: mayor con descuento por edad (40%) vs antelación
        Billete billete2 = new Billete(Ciudad.MADRID, Ciudad.BARCELONA, LocalDate.of(2026, 5, 15), 70);

        // Billete 3: adulto con descuento por antelación (15%)
        Billete billete3 = new Billete(Ciudad.SANTANDER, Ciudad.BARCELONA, LocalDate.of(2026, 4, 25), 45);


        // Resultados
        System.out.println("===== BILLETES =====\n");

        System.out.println(billete1);
        System.out.println(billete1.calcularPrecioFinal());
        System.out.println();
        System.out.println(billete2);
        System.out.println(billete2.calcularPrecioFinal());
        System.out.println();
        System.out.println(billete3);
        System.out.println(billete3.calcularPrecioFinal());
    }
}
