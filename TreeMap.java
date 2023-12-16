import java.util.Arrays;

/**
 * A basic TreeMap implementation using a binary search tree structure.
 * @author  Van Quach
 * @version Nov 20,2023
 *
 * @param   <K>     the data type for keys in the map.
 * @param   <V>     the data type for values in the map.
 */
public class TreeMap<K extends Comparable<K>,V extends Comparable<V>> implements TreeMapInterface<K,V>{
    private TreeMapNode <K,V> root;
    private int size;
    // There should be only one constructor; it should have no parameters.
    /**
     * Constructs an empty TreeMap with no parameters.
     */
    public TreeMap(){
        size = 0;
        root = null;
    }

    /**
     * Retrieves the number of key/value pair elements managed by the map
     *
     * @return      number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Clears the existing tree, removing any and all existing key/value pairs.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Retrieves the corresponding value for the specified key.
     *
     * @param   key     key of interest; key must not be null
     * @return          value corresponding to the specified key, or null if the key is not found.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    /**
     * Helper method to recursively search for the key in the tree.
     *
     * @param   root    the root of the subtree to search.
     * @param   key     the key to search for; key must not be null
     * @return          the value associated with the key, or null if the key is not found.
     */
    private V get(TreeMapNode<K,V> root, K key){
        if(root == null){
            return null;
        }
        int compareKey = key.compareTo(root.key);
        if(compareKey < 0){
            return get(root.left, key);
        } else if (compareKey > 0){
            return get(root.right, key);
        } else {
            return root.value;
        }
    }

    /**
     * Adds a key/value pair to the tree map.
     *
     * @param   key     the key. If not already in the tree, the key/value pair is added.  If already in the
     *                  tree, the existing value is replaced with the one specified here.
     * @param   value   the value in the key/value pair.
     */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
        size++;
    }

    /**
     * Helper method to recursively add or update a key/value pair in the tree.
     *
     * @param   root    the root of the subtree to update.
     * @param   key     the key to be added or updated.
     * @param   value   the value to be associated with the key.
     * @return          the updated root of the subtree.
     */
    private TreeMapNode<K,V> put(TreeMapNode <K,V> root, K key, V value){
        if (root == null){
            return new TreeMapNode<>(key,value);
        }
        int compareKey = key.compareTo(root.key);
        if(compareKey < 0){
            root.left = put(root.left, key, value);
        } else if (compareKey > 0){
            root.right = put(root.right, key, value);
        } else {
            root.value = value;
        }
        return root;
    }
    /**
     * Checks the tree to see if it contains the specified key.
     *
     * @param   key     the key to search for
     * @return          true, if the key is in the tree map; false, if not.
     */
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    /**
     * Helper method to recursively checks whether the tree contains the specified key.
     *
     * @param   root    the root of the subtree to search.
     * @param   key     the key to search for.
     * @return          true if the key is found in the tree; false otherwise.
     */
    private boolean containsKey(TreeMapNode<K,V> root, K key){
        if(root == null){
            return false;
        } else {
            int compareKey = key.compareTo(root.key);
            if (compareKey == 0){
                return true;
            } else if (compareKey < 0){
                return containsKey(root.left, key);
            } else {
                return containsKey(root.right, key);
            }
        }
    }

    /**
     * Retrieves an array of key data from the map, in order,
     *
     * @param   array   to fill in.  If smaller than the map's size, a new array will be created.  If larger than the
     *                  map's size, data will be filled in from index 0, with a null reference just after the copied-in data.
     * @return          a reference to the filled-in array; may be a different array than the one passed in.
     */
    @Override
    public K[] toKeyArray(K[] array) {
        if (array == null || array.length == 0){
            throw new IllegalArgumentException("Invalid array");
        }
        if(array.length < size){
            array = Arrays.copyOf(array, size * 2);
        }
        int lastIndex = toKeyArray(root, array, 0);

        for (int i = lastIndex; i < array.length; i++) {
            array[i] = null;
        }
        K[] resultArray = Arrays.copyOf(array, size);
        return resultArray;
    }

    /**
     * Helper method to perform an in-order traversal of the tree and fill the array with keys.
     * @param   root    the root of the subtree to traverse.
     * @param   array   the array to fill with keys.
     * @param   index   the current index in the array.
     * @return          the updated index after filling in keys.
     */
    private int toKeyArray(TreeMapNode<K, V> root, K[] array, int index) {
        if (root != null) {
            index = toKeyArray(root.left, array, index);
            array[index++] = root.key;
            index = toKeyArray(root.right, array, index);
        }
        return index;
    }

    // Updated PROJECT 5
    /**
     * Retrieves an array of value data from the map, in order,
     *
     * @param   array   to fill in.  If smaller than the map's size, a new array will be created.  If larger than the
     *                  map's size, data will be filled in from index 0, with a null reference just after the copied-in data.
     * @return          a reference to the filled-in array; may be a different array than the one passed in.
     */
    public V[] toValueArray(V[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Invalid array");
        }

        // Ensure the array has enough capacity
        if (array.length < size) {
            array = Arrays.copyOf(array, size * 2);
        }

        toValueArray(root, array, 0);
        V[] resultArray = Arrays.copyOf(array, size);
        return resultArray;
    }

    /**
     * Helper method to perform an in-order traversal of the tree and fill the array with keys.
     * @param   root    the root of the subtree to traverse.
     * @param   array   the array to fill with keys.
     * @param   index   the current index in the array.
     * @return          the updated index after filling in keys.
     */
    private int toValueArray(TreeMapNode<K, V> root, V[] array, int index) {
        if (root != null) {
            index = toValueArray(root.left, array, index);
            array[index++] = root.value;
            index = toValueArray(root.right, array, index);
        }
        return index;
    }


    /**
     * Represents a node in the binary search tree used by TreeMap.
     * @param <K> the type of keys held by this node.
     * @param <V> the type of values held by this node.
     */
    private static class TreeMapNode<K,V>{

        /** The key associated with this node.*/
        public K key;
        /**The value associated with this node.*/
        public V value;
        /**Reference to the left/right child node.*/
        public TreeMapNode<K,V> left, right;

        /**
         * Constructs a new TreeMapNode with the specified key and value.
         *
         * @param key   the key to be associated with this node.
         * @param value the value to be associated with this node.
         */
        public TreeMapNode(K key, V value){
            this.key = key;
            this.value = value;
            this.left = this.right = null;
        }
    }
}
