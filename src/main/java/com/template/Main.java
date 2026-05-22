/**
 * En este programa se implementa el sistema de control logístico y optimización de rutas de salud simulando un escenario de Covid-19.
 * Se utiliza una estructura de Grafo Dirigido basado en una Matriz de Adyacencia para el almacenamiento de la red vial.
 * Se utiliza el algoritmo de Floyd-Warshall para calcular los caminos más cortos entre las ciudades y determinar el centro del grafo.
 * El programa permite la actualización dinámica de la infraestructura vial mediante la lectura de archivos, interrupción de rutas y adición de nuevos lugares.
 * * @author Diego Ayala (25570) / David Berganza (25573)
 * @version 1.0

 */

package com.template;
import java.io.*;
import java.util.Scanner;

public class Main {
    private static final String ARCHIVO = "Lugares.txt";

    public static void main(String[] args) {
        GrafoFloyd grafo = new GrafoFloyd();
        cargarArchivo(grafo);
        grafo.calcularFloydWarshall();

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 4) {
            System.out.println("\n- Menu de rutas por COVID-19 -");
            System.out.println("1. Buscar ruta más corta entre dos ciudades");
            System.out.println("2. Mostrar la ciudad en el centro del grafo");
            System.out.println("3. Modificar grafo (Interrupción o nueva conexión)");
            System.out.println("4. Salir");
            System.out.print("Por favor, seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ciudad de origen: ");
                        String origen = scanner.nextLine();
                        System.out.print("Ciudad de destino: ");
                        String destino = scanner.nextLine();
                        System.out.println("\n" + grafo.obtenerRutaMasCorta(origen, destino));
                        break;
                    case 2:
                        System.out.println("El centro del grafo es: " + grafo.calcularCentroGrafo());
                        break;
                    case 3:
                        System.out.println("1) Interrupción de tráfico entre dos ciudades");
                        System.out.println("2) Establecer o Modificar conexión");
                        System.out.print("Opción (1/2): ");
                        String subOp = scanner.nextLine().toLowerCase();
                        if (subOp.equals("1")) {
                            System.out.print("Ciudad de origen: ");
                            String o = scanner.nextLine();
                            System.out.print("Ciudad de destino: ");
                            String d = scanner.nextLine();
                            grafo.eliminarArco(o, d);
                        } else if (subOp.equals("2")) {
                            System.out.print("Ciudad de origen: ");
                            String o = scanner.nextLine();
                            System.out.print("Ciudad de destino: ");
                            String d = scanner.nextLine();
                            System.out.print("Nueva distancia en KM: ");
                            double km = Double.parseDouble(scanner.nextLine());
                            grafo.agregarArco(o, d, km);
                        }
                        grafo.calcularFloydWarshall();
                        System.out.println("El grafo y las rutas se actualizado correctamente.");
                        break;
                    case 4:
                        System.out.println("Gracias por usar el programa!");
                        break;
                    default:
                        System.out.println("Hubo un error. Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Hubo un error en el ingreso de datos: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void cargarArchivo(GrafoFloyd grafo) {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.trim().split("\\s+");
                if (partes.length == 3) {
                    String origen = partes[0];
                    String destino = partes[1];
                    double distancia = Double.parseDouble(partes[2]);
                    grafo.agregarArco(origen, destino, distancia);
                }
            }
            System.out.println("El archivo " + ARCHIVO + " se ha cargado con éxito.");
        } catch (IOException e) {
            System.out.println("Hubo un error de lectura.No se pudo leer el archivo " + ARCHIVO + ". Iniciando un grafo vacío.");
        }
    }
}