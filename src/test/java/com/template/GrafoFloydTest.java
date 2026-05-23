package com.template;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GrafoFloydTest {
    private GrafoFloyd grafo;

    @BeforeEach
    public void setUp() {
        grafo = new GrafoFloyd();
        grafo.agregarArco("Mixco", "Antigua", 30);
        grafo.agregarArco("Antigua", "Escuintla", 25);
        grafo.calcularFloydWarshall();
    }

    @Test
    public void testRutaExistente() {
    String resultado = grafo.obtenerRutaMasCorta("Mixco", "Escuintla");
    System.out.println(resultado);

    assertTrue(resultado.contains("55"));
}
    @Test
    public void testEliminarArco() {
        grafo.eliminarArco("Antigua", "Escuintla");
        grafo.calcularFloydWarshall();
        String resultado = grafo.obtenerRutaMasCorta("Mixco", "Escuintla");
        assertTrue(resultado.contains("No existe ruta"));
    }
}