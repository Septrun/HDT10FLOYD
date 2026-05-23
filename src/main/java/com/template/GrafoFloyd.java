package com.template;
import java.util.*;

public class GrafoFloyd {
    private static final double INF = Double.POSITIVE_INFINITY;
    private int numVertices;
    private Map<String, Integer> ciudadAIndice;
    private Map<Integer, String> indiceACiudad;
    private double[][] matrizAdyacencia;
    private double[][] matrizDistancias;
    private int[][] matrizPredecesores;
    private int contadorIndices;

    public GrafoFloyd() {
        this.ciudadAIndice = new HashMap<>();
        this.indiceACiudad = new HashMap<>();
        this.matrizAdyacencia = new double[0][0];
        this.contadorIndices = 0;
    }

    private int registrarCiudad(String ciudad) {
        if (!ciudadAIndice.containsKey(ciudad)) {
            ciudadAIndice.put(ciudad, contadorIndices);
            indiceACiudad.put(contadorIndices, ciudad);
            contadorIndices++;
            expandirMatrices();
        }
        return ciudadAIndice.get(ciudad);
    }

    private void expandirMatrices() {
        int n = contadorIndices;
        double[][] nuevaMatriz = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(nuevaMatriz[i], INF);
            nuevaMatriz[i][i] = 0;
        }
        for (int i = 0; i < matrizAdyacencia.length; i++) {
            System.arraycopy(matrizAdyacencia[i], 0, nuevaMatriz[i], 0, matrizAdyacencia[i].length);
        }
        matrizAdyacencia = nuevaMatriz;
        numVertices = n;
    }

    public void agregarArco(String origen, String destino, double peso) {
        int u = registrarCiudad(origen);
        int v = registrarCiudad(destino);
        matrizAdyacencia[u][v] = peso;
    }

    public void eliminarArco(String origen, String destino) {
        if (ciudadAIndice.containsKey(origen) && ciudadAIndice.containsKey(destino)) {
            int u = ciudadAIndice.get(origen);
            int v = ciudadAIndice.get(destino);
            matrizAdyacencia[u][v] = INF;
        }
    }

    public void calcularFloydWarshall() {
        int n = numVertices;
        matrizDistancias = new double[n][n];
        matrizPredecesores = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizDistancias[i][j] = matrizAdyacencia[i][j];
                if (i != j && matrizAdyacencia[i][j] != INF) {
                    matrizPredecesores[i][j] = i;
                } else {
                    matrizPredecesores[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (matrizDistancias[i][k] != INF && matrizDistancias[k][j] != INF) {
                        if (matrizDistancias[i][k] + matrizDistancias[k][j] < matrizDistancias[i][j]) {
                            matrizDistancias[i][j] = matrizDistancias[i][k] + matrizDistancias[k][j];
                            matrizPredecesores[i][j] = matrizPredecesores[k][j];
                        }
                    }
                }
            }
        }
    }

    public String obtenerRutaMasCorta(String origen, String destino) {
        if (!ciudadAIndice.containsKey(origen) || !ciudadAIndice.containsKey(destino)) {
            return "Hubo un error. Una o ambas ciudades no existen en el mapa.";
        }
        int u = ciudadAIndice.get(origen);
        int v = ciudadAIndice.get(destino);

        if (matrizDistancias[u][v] == INF) {
            return "Hubo un error. No existe ruta transitable entre " + origen + " y " + destino;
        }

        List<String> camino = new ArrayList<>();
        int actual = v;
        camino.add(destino);
        while (actual != u) {
            actual = matrizPredecesores[u][actual];
            if (actual == -1) return "Hubo un error. Error en infraestructura de red vial.";
            camino.add(0, indiceACiudad.get(actual));
        }

        return "Distancia Mínima: " + matrizDistancias[u][v] + " KM\nRuta óptima: " + String.join(" -> ", camino);
    }

    /**
     * Se busca el valor de la excentricidad para cada nodo y se selecciona la columna con el valor máximo más pequeño.
     */
    public String calcularCentroGrafo() {
        if (numVertices == 0) return "Grafo vacío";
        
        double minExcentricidad = INF;
        int nodoCentro = -1;

        for (int j = 0; j < numVertices; j++) {
            double maxDistanciaEnColumna = 0;
            boolean alcanzableDesdeTodos = true;

            for (int i = 0; i < numVertices; i++) {
                if (i != j) {
                    if (matrizDistancias[i][j] == INF) {
                        alcanzableDesdeTodos = false;
                        break;
                    }
                    if (matrizDistancias[i][j] > maxDistanciaEnColumna) {
                        maxDistanciaEnColumna = matrizDistancias[i][j];
                    }
                }
            }

            // Se crea un nuevo candidato a ser el centro si es alcanzable y reduce el peor tiempo de respuesta.
            if (alcanzableDesdeTodos && maxDistanciaEnColumna < minExcentricidad) {
                minExcentricidad = maxDistanciaEnColumna;
                nodoCentro = j;
            }
        }

        return (nodoCentro != -1) ? indiceACiudad.get(nodoCentro) : "No determinable (Grafo altamente desconectado)";
    }
}