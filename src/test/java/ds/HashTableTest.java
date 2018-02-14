package ds;

import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HashTableTest {

    @Test
    public void putElement() {
        HashTable<String, String> myTable = new HashTable<String, String>();
        assertEquals("Value", myTable.put("Key", "Value"));
    }

    @Test
    public void putElementSameKey() {
        HashTable<String, String> myTable = new HashTable<String, String>();
        assertEquals("Value1", myTable.put("Key", "Value1"));
        assertEquals("Value2", myTable.put("Key", "Value2"));
        assertEquals(1, myTable.size());
    }

    @Test
    public void getElement() {
        HashTable<String, String> myTable = new HashTable<String, String>();
        myTable.put("Key", "Value");
        assertEquals("Value", myTable.get("Key"));
    }


    @Test
    public void removeElement() {
        HashTable<String, String> myTable = new HashTable<String, String>();
        assertEquals("Value", myTable.put("Key", "Value"));
        assertEquals("Value", myTable.remove("Key"));
        assertEquals(0, myTable.size());
    }

    @Test
    public void removeUnExistingElement() {
        HashTable<String, String> myTable = new HashTable<String, String>();
        assertEquals("Value", myTable.put("Key", "Value"));
        assertNull(myTable.remove("Key123"));
        assertEquals(1, myTable.size());
    }

    @Test
    public void testCollision() {
        HashTable<OneHash, String> myTable = new HashTable<OneHash, String>();

        myTable.put(new OneHash("1"), "1");
        myTable.put(new OneHash("2"), "2");
        myTable.put(new OneHash("3"), "3");

        assertEquals("1", myTable.get(new OneHash("1")));
        assertEquals("2", myTable.get(new OneHash("2")));
        assertEquals("3", myTable.get(new OneHash("3")));
        assertEquals(3, myTable.size());
    }

    @Test
    public void testRemoveWithCollision() {
        HashTable<OneHash, String> myTable = new HashTable<OneHash, String>();

        myTable.put(new OneHash("1"), "1");
        myTable.put(new OneHash("2"), "2");
        myTable.put(new OneHash("3"), "3");

        assertEquals("1", myTable.get(new OneHash("1")));
        assertEquals("2", myTable.get(new OneHash("2")));
        assertEquals("3", myTable.get(new OneHash("3")));
        assertEquals(3, myTable.size());

        assertEquals("2", myTable.remove(new OneHash("2")));
        assertEquals("1", myTable.get(new OneHash("1")));
        assertEquals("3", myTable.get(new OneHash("3")));
        assertEquals(2, myTable.size());

        assertEquals("3", myTable.remove(new OneHash("3")));
        assertEquals("1", myTable.get(new OneHash("1")));
        assertEquals(1, myTable.size());

        assertEquals("1", myTable.remove(new OneHash("1")));
        assertEquals(0, myTable.size());
    }

    @Test
    public void testRehash() {
        HashTable<String, String> myTable = new HashTable<String, String>(1);

        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "3");

        assertEquals("1", myTable.get("K1"));
        assertEquals("2", myTable.get("K2"));
        assertEquals("3", myTable.get("K3"));
        assertEquals(3, myTable.size());
    }

    @Test
    public void testPutAll() {

        HashTable<String, String> myTable = new HashTable<String, String>(1);
        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "3");

        HashTable<String, String> newTable = new HashTable<String, String>(1);

        newTable.putAll(myTable);
        assertEquals(3, newTable.size());
    }

    @Test
    public void testGetKeys() {

        HashTable<String, String> myTable = new HashTable<String, String>();
        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "3");

        Set<String> keys = myTable.keySet();
        assertTrue(keys.contains("K1"));
        assertTrue(keys.contains("K2"));
        assertTrue(keys.contains("K3"));
        assertTrue(keys.size() == 3);
    }

    @Test
    public void testGetKeysWithCollision() {

        HashTable<OneHash, String> myTable = new HashTable<OneHash, String>();
        myTable.put(new OneHash("1"), "1");
        myTable.put(new OneHash("2"), "2");
        myTable.put(new OneHash("3"), "3");

        Set<OneHash> keys = myTable.keySet();
        assertTrue(keys.contains(new OneHash("1")));
        assertTrue(keys.contains(new OneHash("2")));
        assertTrue(keys.contains(new OneHash("3")));
        assertTrue(keys.size() == 3);
    }

    @Test
    public void testGetValues() {

        HashTable<String, String> myTable = new HashTable<String, String>();
        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "3");

        Collection<String> values = myTable.values();
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
        assertTrue(values.contains("3"));
        assertTrue(values.size() == 3);
    }

    @Test
    public void testGetDuplicateValues() {

        HashTable<String, String> myTable = new HashTable<String, String>();
        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "1");

        Collection<String> values = myTable.values();
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
        assertTrue(values.size() == 2);
    }

    @Test
    public void testGetValuesWithCollision() {

        HashTable<OneHash, String> myTable = new HashTable<OneHash, String>();
        myTable.put(new OneHash("1"), "1");
        myTable.put(new OneHash("2"), "2");
        myTable.put(new OneHash("3"), "3");

        Collection<String> values = myTable.values();
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
        assertTrue(values.contains("3"));
        assertTrue(values.size() == 3);
    }


    @Test
    public void testEntrySet() {

        HashTable<String, String> myTable = new HashTable<String, String>();
        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "1");

        assertEquals(3, myTable.entrySet().size());
    }

    @Test
    public void testClear() {
        HashTable<String, String> myTable = new HashTable<String, String>();
        myTable.put("K1", "1");
        myTable.put("K2", "2");
        myTable.put("K3", "1");

        myTable.clear();
        assertEquals(0, myTable.size());

    }

}
