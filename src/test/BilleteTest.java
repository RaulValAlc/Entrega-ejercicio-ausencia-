package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import trayecto.Billete;
import trayecto.Ciudad;
import java.time.LocalDate;

/**
 * Tests de caja negra para Billete.
 * "hoy - X" del Excel → fechaViaje = LocalDate.now().plusDays(X)
 * "hoy + X" del Excel → fechaViaje = LocalDate.now().minusDays(X)  [inválido]
 *
 * Trayectos y precios base:
 *   SNT ↔ MAD : 100 €
 *   MAD ↔ BCN : 120 €
 *   SNT ↔ BCN : 150 €
 */
public class BilleteTest {

    @Test
    void tc1_pairwise_SNT_SNT_20_CE13() {
        // origen == destino → IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.SANTANDER,
                    LocalDate.now().plusDays(3), 20);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc2_pairwise_SNT_MAD_60_CE14() {
        // base=100, edad 60→0%, antelación 20d→15% → max=15% → 85.00€
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID,
                LocalDate.now().plusDays(20), 60);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc3_pairwise_SNT_BCN_70_CE15() {
        // base=150, edad 70→40%, antelación 40d→25% → max=40% → 90.00€
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.BARCELONA,
                LocalDate.now().plusDays(40), 70);
        assertEquals(90.0, b.calcularPrecioFinal());
    }

    @Test
    void tc4_pairwise_MAD_SNT_60_CE15() {
        // base=100, edad 60→0%, antelación 40d→25% → max=25% → 75.00€
        Billete b = new Billete(Ciudad.MADRID, Ciudad.SANTANDER,
                LocalDate.now().plusDays(40), 60);
        assertEquals(75.0, b.calcularPrecioFinal());
    }

    @Test
    void tc5_pairwise_MAD_MAD_70_CE13() {
        // origen == destino → IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.MADRID, Ciudad.MADRID,
                    LocalDate.now().plusDays(3), 70);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc6_pairwise_MAD_BCN_20_CE14() {
        // base=120, edad 20→30%, antelación 20d→15% → max=30% → 84.00€
        Billete b = new Billete(Ciudad.MADRID, Ciudad.BARCELONA,
                LocalDate.now().plusDays(20), 20);
        assertEquals(84.0, b.calcularPrecioFinal());
    }

    @Test
    void tc7_pairwise_BCN_SNT_70_CE14() {
        // base=150, edad 70→40%, antelación 20d→15% → max=40% → 90.00€
        Billete b = new Billete(Ciudad.BARCELONA, Ciudad.SANTANDER,
                LocalDate.now().plusDays(20), 70);
        assertEquals(90.0, b.calcularPrecioFinal());
    }

    @Test
    void tc8_pairwise_BCN_MAD_20_CE15() {
        // base=120, edad 20→30%, antelación 40d→25% → max=30% → 84.00€
        Billete b = new Billete(Ciudad.BARCELONA, Ciudad.MADRID,
                LocalDate.now().plusDays(40), 20);
        assertEquals(84.0, b.calcularPrecioFinal());
    }

    @Test
    void tc9_pairwise_BCN_BCN_60_CE13() {
        // origen == destino → IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.BARCELONA, Ciudad.BARCELONA,
                    LocalDate.now().plusDays(3), 60);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc10_pairwise_origenInvalido_CE4() {
        // CE4: ciudad origen inválida (ZGZ no existe en el enum → se usa null como proxy)
        // obtenerPrecioBase() no encuentra trayecto → IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(null, Ciudad.SANTANDER,
                    LocalDate.now().plusDays(3), 20);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc11_pairwise_destinoInvalido_CE8() {
        // CE8: ciudad destino inválida (ZGZ no existe en el enum → se usa null como proxy)
        // obtenerPrecioBase() no encuentra trayecto → IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, null,
                    LocalDate.now().plusDays(3), 20);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc12_pairwise_edadNegativa() {
        // CE12: edad=-10 → IllegalArgumentException en constructor
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID,
                    LocalDate.now().plusDays(3), -10);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc13_pairwise_fechaPasada() {
        // CE16: fecha pasada (now-5) → IllegalArgumentException en constructor
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID,
                    LocalDate.now().minusDays(5), 20);
            b.calcularPrecioFinal();
        });
    }

    // Tests valores límite, hecho con método cartesiano (80 casos)
    @Test
    void tc14_VL1_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc15_VL1_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc16_VL1_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc17_VL1_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc18_VL1_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc19_VL1_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 0);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc20_VL1_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc21_VL1_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc22_VL1_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 0);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc23_VL2_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc24_VL2_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc25_VL2_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc26_VL2_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc27_VL2_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc28_VL2_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc29_VL2_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc30_VL2_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc31_VL2_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 1);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc32_VL3_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc33_VL3_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc34_VL3_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc35_VL3_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc36_VL3_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc37_VL3_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 24);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc38_VL3_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc39_VL3_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc40_VL3_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 24);
        assertEquals(70.0, b.calcularPrecioFinal());
    }

    @Test
    void tc41_VL4_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 25);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc42_VL4_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 25);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc43_VL4_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 25);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc44_VL4_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 25);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc45_VL4_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 25);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc46_VL4_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 25);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc47_VL4_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 25);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc48_VL4_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 25);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc49_VL4_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 25);
        assertEquals(75.0, b.calcularPrecioFinal());
    }

    @Test
    void tc50_VL5_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 26);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc51_VL5_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 26);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc52_VL5_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 26);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc53_VL5_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 26);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc54_VL5_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 26);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc55_VL5_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 26);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc56_VL5_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 26);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc57_VL5_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 26);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc58_VL5_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 26);
        assertEquals(75.0, b.calcularPrecioFinal());
    }

    @Test
    void tc59_VL6_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 64);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc60_VL6_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 64);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc61_VL6_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 64);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc62_VL6_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 64);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc63_VL6_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 64);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc64_VL6_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 64);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc65_VL6_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 64);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc66_VL6_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 64);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc67_VL6_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 64);
        assertEquals(75.0, b.calcularPrecioFinal());
    }

    @Test
    void tc68_VL7_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 65);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc69_VL7_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 65);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc70_VL7_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 65);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc71_VL7_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 65);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc72_VL7_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 65);
        assertEquals(100.0, b.calcularPrecioFinal());
    }

    @Test
    void tc73_VL7_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 65);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc74_VL7_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 65);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc75_VL7_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 65);
        assertEquals(85.0, b.calcularPrecioFinal());
    }

    @Test
    void tc76_VL7_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 65);
        assertEquals(75.0, b.calcularPrecioFinal());
    }

    @Test
    void tc77_VL8_VL10() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc78_VL8_VL11() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc79_VL8_VL12() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc80_VL8_VL13() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc81_VL8_VL14() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc82_VL8_VL15() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(-1), 66);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc83_VL8_VL16() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc84_VL8_VL17() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc85_VL8_VL18() {
        Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), 66);
        assertEquals(60.0, b.calcularPrecioFinal());
    }

    @Test
    void tc86_VL9_VL10() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(6), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc87_VL9_VL11() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(7), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc88_VL9_VL12() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(8), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc89_VL9_VL13() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(1), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc90_VL9_VL14() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(0), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc91_VL9_VL16() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(29), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc92_VL9_VL17() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(30), -1);
            b.calcularPrecioFinal();
        });
    }

    @Test
    void tc93_VL9_VL18() {
        assertThrows(IllegalArgumentException.class, () -> {
            Billete b = new Billete(Ciudad.SANTANDER, Ciudad.MADRID, LocalDate.now().plusDays(31), -1);
            b.calcularPrecioFinal();
        });
    }
}
