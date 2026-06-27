package managment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Bucket<K, V> extends HashMap<K, V> {

    private final ArrayList<Consumer<K>> additionCallbacks = new ArrayList<>();
    public void addAdditionCallback(Consumer<K> callback){
        additionCallbacks.add(callback);
    }

    private final ArrayList<Consumer<K>> removalCallbacks = new ArrayList<>();
    public void addRemovalCallback(Consumer<K> callback){
        removalCallbacks.add(callback);
    }

    @Override
    public V put(K key, V value) {
        V temp = super.put(key, value);

        for (Consumer<K> callback : additionCallbacks){
            callback.accept(key);
        }
        return temp;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean temp = super.remove(key, value);

        for (Consumer<K> callback : removalCallbacks){
            callback.accept((K) key);
        }
        return temp;
    }
}