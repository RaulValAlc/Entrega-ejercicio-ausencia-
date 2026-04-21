package test;
import calculadora.Calculadora;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraTest {
    Calculadora calculadora;

    @BeforeAll
    static void initAll() {
        System.out.println("Antes de todos los tests");
    }

    @BeforeEach
    void init() {
        calculadora = new Calculadora();
        System.out.println("Antes de cada test");
    }

    @Test
    void testSumar() {
        assertEquals(5, calculadora.sumar(2, 3));
    }

    @Test
    void testRestar() {
        assertEquals(1, calculadora.restar(3, 2));
    }
    @Test
    void testMultiplicar() {
        assertEquals(6, calculadora.multiplicar(2, 3));
    }
    @Test
    void testDividir() {
        assertEquals(2, calculadora.dividir(6, 3));
    }

    @Test
    void testDividirPorCero() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculadora.dividir(5, 0);
        });
    }

    @AfterEach
    void tearDown() {
        System.out.println("Después de cada test");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Después de todos los tests");
    }
}




