import networkx as nx

def cargar_grafo(archivo_nombre):
    G = nx.DiGraph()
    try:
        with open(archivo_nombre, 'r') as f:
            for linea in f:
                partes = linea.strip().split()
                if len(partes) == 3:
                    u, v, peso = partes[0], partes[1], float(partes[2])
                    G.add_edge(u, v, weight=peso)
        return G
    except FileNotFoundError:
        print(f"Hubo un error. No se encontró el archivo: {archivo_nombre}")
        return None

def main():
    G = cargar_grafo("Lugares.txt")
    if G is None or len(G.nodes) == 0:
        return

    # Se crea la matriz completa de distancias usando el algoritmo de Floyd
    distancias = nx.floyd_warshall(G)
    
    # Se encuentra el valor máximo de cada columna hacia v.
    excentricidades = {}
    for v in G.nodes():
        max_costo_hacia_v = 0
        es_accesible = True
        for u in G.nodes():
            if u != v:
                costo = distancias[u][v]
                if costo == float('inf'):
                    es_accesible = False
                    break
                if costo > max_costo_hacia_v:
                    max_costo_hacia_v = costo
        if es_accesible:
            excentricidades[v] = max_costo_hacia_v

    if excentricidades:
        # El centro es el nodo que minimiza este valor máximo 
        centro = min(excentricidades, key=excentricidades.get)
        print(f"Centro del grafo: {centro}")
    else:
        print("El grafo no cuenta con un centro completamente conectado.")

if __name__ == "__main__":
    main()