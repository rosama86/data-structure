package ds;

import java.util.*;

public class HashTable<K, V> implements Map<K, V> {

    private Entry<?, ?>[] table;
    private final int INITIAL_SIZE = 20;
    private int count;

    public HashTable() {
        table = new Entry<?, ?>[INITIAL_SIZE];
        count = 0;
    }

    public HashTable(int size) {
        table = new Entry<?, ?>[size];
        count = 0;
    }


    public int size() {
        return count;
    }


    public boolean isEmpty() {
        return count == 0;
    }


    public boolean containsKey(Object key) {

        int hash = key.hashCode();
        // since hashcode is an int between -2^31 and 2^31-1
        // we need to map to an int between [0, table.size - 1]

        // 0x7FFFFFFF is 0111 1111 1111 1111 1111 1111 1111 1111 : all 1 except the sign bit
        // (hash & 0x7FFFFFFF) will result in a positive integer
        // (hash & 0x7FFFFFFF) % table.length will be in the range of the table length.
        int index = (hash & 0X7fffffff) % table.length;
        Entry<?, ?> bucket = table[index];

        while (bucket != null) {
            if (bucket.hash == hash && bucket.key.equals(key)) {
                return true;
            } else {
                bucket = bucket.next;
            }
        }

        return false;
    }


    public boolean containsValue(Object value) {

        if (value == null) throw new NullPointerException();

        for (Entry<?, ?> bucket : table) {
            Entry<?, ?> e = bucket;
            while (e != null) {
                if (e.value.equals(value)) {
                    return true;
                } else {
                    e = e.next;
                }
            }
        }

        return false;
    }


    @SuppressWarnings("unchecked")
    public V get(Object key) {
        if (key == null) throw new NullPointerException();

        int hash = key.hashCode();
        int index = (hash & 0X7fffffff) % table.length;

        Entry<?, ?> elem = table[index];
        while (elem != null) {

            if (elem.hash == hash && elem.key.equals(key)) {
                return (V) elem.value;
            } else {
                elem = elem.next;
            }
        }

        return null;
    }


    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException();

        if (count >= table.length * 0.70) {
            rehash();
        }

        int hash = key.hashCode();
        int index = (hash & 0X7FFFFFFF) % table.length;

        Entry<K, V> elem = (Entry<K, V>) table[index];

        if (elem != null && elem.hash == hash && elem.key.equals(key)) {
            elem.value = value;
            return value;
        } else {

            table[index] = new Entry<K, V>(key, value, hash, elem);

            count++;
            return value;
        }
    }

    @SuppressWarnings("unchecked")
    private void rehash() {

        int newSize = table.length * 2;
        Entry<?, ?>[] newTable = new Entry<?, ?>[newSize];

        for (Entry<?, ?> bucket : table) {

            for (Entry<K, V> entry = (Entry<K, V>) bucket; entry != null; entry = entry.next) {
                int index = (entry.key.hashCode() & 0X7FFFFFFF) % newSize;

                entry.next = (Entry<K, V>) newTable[index];
                newTable[index] = entry;
            }
        }

        table = newTable;
    }

    @SuppressWarnings("unchecked")
    public V remove(Object key) {

        if (key == null) throw new NullPointerException();

        int hash = key.hashCode();
        int index = (hash & 0X7FFFFFFF) % table.length;

        Entry<K, V> prev = null;
        for (Entry<K, V> elem = (Entry<K, V>) table[index]; elem != null; ) {
            if (elem.key.equals(key)) {
                count--;

                if (prev == null) {
                    table[index] = elem.next;
                } else {
                    prev.next = elem.next;
                }

                return elem.value;
            }
            prev = elem;
            elem = elem.next;
        }

        return null;
    }

    public void putAll(Map<? extends K, ? extends V> m) {

        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }


    public void clear() {
        table = new Entry<?, ?>[INITIAL_SIZE];
        count = 0;
    }


    @SuppressWarnings("unchecked")
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>(count);

        for (Entry<?, ?> bucket : table) {
            for (Entry<K, V> entry = (Entry<K, V>) bucket; entry != null; entry = entry.next) {
                keys.add(entry.key);
            }
        }

        return keys;
    }


    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        Set<V> values = new HashSet<V>(count);

        for (Entry<?, ?> bucket : table) {
            for (Entry<K, V> entry = (Entry<K, V>) bucket; entry != null; entry = entry.next) {
                values.add(entry.value);
            }
        }

        return values;
    }


    @SuppressWarnings("unchecked")
    public Set<Map.Entry<K, V>> entrySet() {

        Set<Map.Entry<K, V>> values = new HashSet<Map.Entry<K, V>>(count);

        for (Entry<?, ?> bucket : table) {
            for (Entry<K, V> entry = (Entry<K, V>) bucket; entry != null; entry = entry.next) {
                values.add(new EntrySet(entry.key, entry.value));
            }
        }

        return values;
    }

    private class EntrySet implements Map.Entry<K, V> {

        private K key;
        private V value;

        public EntrySet(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            return this.value = value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EntrySet entrySet = (EntrySet) o;
            return key.equals(entrySet.key) &&
                    value.equals(entrySet.value);
        }

        @Override
        public int hashCode() {
            // Horner's method
            int hash = 17;
            hash = 31 * hash + key.hashCode();
            hash = 31 * hash + value.hashCode();
            return hash;
        }
    }

    private static class Entry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private V value;
        private final int hash;
        private Entry<K, V> next;

        public Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }


        public K getKey() {
            return key;
        }


        public V getValue() {
            return value;
        }


        public V setValue(V value) {
            return value = value;
        }
    }
}