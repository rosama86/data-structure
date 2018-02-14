package ds;

// Always return one hash code
public class OneHash {

    private String name;

    public OneHash(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OneHash oneHash = (OneHash) o;
        return name.equals(oneHash.name);
    }

    @Override
    public int hashCode() {
        return 17;
    }

}
