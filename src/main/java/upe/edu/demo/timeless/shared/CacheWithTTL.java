package upe.edu.demo.timeless.shared;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@ToString
@Slf4j
@Component
public class CacheWithTTL<K, V> {

    private final long timeToLive; // Tiempo en milisegundos
    private final Map<K, CacheObject<V>> cacheMap;

    // Clase interna para almacenar el objeto y su tiempo de creación
    private static class CacheObject<V> {
        public long creationTime;
        public V value;

        CacheObject(V value, long creationTime) {
            this.value = value;
            this.creationTime = creationTime;
        }
    }

    // Constructor
    public CacheWithTTL() {
        this.timeToLive = 120000; // Ej. 120000 ms = 2 minutos
        this.cacheMap = new ConcurrentHashMap<>();
    }

    // Método para añadir un objeto a la caché
    public void put(K key, V value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObject<>(value, System.currentTimeMillis()));
        }
    }

    // Método para obtener un valor de la caché
    public V get(K key) {
        CacheObject<V> cacheObject = cacheMap.get(key);
        if (cacheObject == null) {
            return null;
        }
        return cacheObject.value;
    }

    public boolean remove(K key) {
        synchronized (cacheMap) {
            if (cacheMap.containsKey(key)) {
                cacheMap.remove(key);
                log.debug("Entrada con clave " + key + " eliminada manualmente.");
                return true;
            } else {
                log.debug("Clave " + key + " no encontrada en el cache.");
                return false;
            }
        }
    }

    // Método programado para limpiar objetos expirados cada 30 segundos
    @Scheduled(fixedRate = 120000) // Se ejecuta cada 30 segundos
    public void cleanupExpiredItems() {
        long now = System.currentTimeMillis();
        synchronized (cacheMap) {
            Iterator<Map.Entry<K, CacheObject<V>>> iterator = cacheMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<K, CacheObject<V>> entry = iterator.next();
                long objectAgeInMillis = now - entry.getValue().creationTime;

                if (objectAgeInMillis > timeToLive) {
                    log.debug("Eliminando clave: " + entry.getKey() + " por superar el TTL de " + (timeToLive / 1000) + " segundos");
                    iterator.remove(); // Eliminar el objeto si ha superado el TTL
                }
            }
        }
    }

    // Método para imprimir el contenido de la caché
    @Scheduled(fixedRate = 30000) // Se ejecuta cada 30 segundos
    public void printCache() {
        log.debug("Contenido de la caché:");
        for (Map.Entry<K, CacheObject<V>> entry : cacheMap.entrySet()) {
            log.debug("Clave: " + entry.getKey() + ", Valor: " + entry.getValue().value);
        }
    }

}
