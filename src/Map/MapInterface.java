package Map;

public interface MapInterface<K, V> {

    public void put(K key, V value);

    public V get(K key);

    public boolean containsKey(K key);

    public V remove(K key);

    public int size();

    public boolean isEmpty();
    
    // used for graph
    public void putIfAbsent(K key, V value);
    
    public Iterable<K> keySet();           

    public V getOrDefault(K key, V defaultValue); 
    
    public Iterable<V> values();
}