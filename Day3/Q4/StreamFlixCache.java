import java.util.LinkedHashMap;
import java.util.Map;

class VideoCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public VideoCache(int capacity) {
        super(capacity + 1, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > capacity) {
            System.out.println(
                "  [EVICT] Cache full — evicting LRU entry: \"" +
                eldest.getKey() + "\""
            );
            return true;
        }
        return false;
    }

    public V getWithLog(K key) {
        V value = get(key);

        if (value != null) {
            System.out.println(
                "  [CACHE HIT] \"" + key +
                "\" accessed → moved to front of keep list"
            );
        } else {
            System.out.println(
                "  [CACHE MISS] \"" + key +
                "\" not in cache"
            );
        }

        return value;
    }

    public void printCache() {
        System.out.println("  Cache state (LRU → MRU): " + keySet());
    }
}

public class StreamFlixCache {

    public static void main(String[] args) {

        VideoCache<String, String> cache = new VideoCache<>(5);

        System.out.println(
            "===== STREAMFLIX VIDEO CACHE (capacity: 5) =====\n"
        );

        System.out.println("--- Loading 5 movies into cache ---");

        cache.put("Inception", "Sci-fi thriller by Nolan");
        cache.put("The Matrix", "Cyberpunk classic");
        cache.put("Interstellar", "Space epic by Nolan");
        cache.put("The Dark Knight", "Batman masterpiece");
        cache.put("Parasite", "Korean social thriller");

        System.out.print("After loading: ");
        cache.printCache();

        System.out.println("\n--- Accessing 'Inception' ---");
        cache.getWithLog("Inception");

        System.out.print("After accessing Inception: ");
        cache.printCache();

        System.out.println(
            "\n--- Adding 6th movie: 'Oppenheimer' ---"
        );
        cache.put("Oppenheimer", "Historical drama by Nolan");

        System.out.print("After adding Oppenheimer: ");
        cache.printCache();

        System.out.println("\n--- Verifying eviction ---");
        System.out.println(
            "  'The Matrix' in cache? " +
            cache.containsKey("The Matrix")
        );
        System.out.println(
            "  'Inception' in cache?  " +
            cache.containsKey("Inception")
        );
        System.out.println(
            "  Cache size: " + cache.size()
        );

        System.out.println("\n--- Adding 2 more movies ---");

        cache.put("Tenet", "Time-bending thriller");
        cache.put("Dunkirk", "War epic");

        System.out.print("Final cache state: ");
        cache.printCache();
    }
}
