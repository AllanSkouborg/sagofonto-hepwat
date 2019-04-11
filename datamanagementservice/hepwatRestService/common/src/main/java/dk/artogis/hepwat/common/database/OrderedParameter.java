package dk.artogis.hepwat.common.database;

public class OrderedParameter implements Comparable<OrderedParameter> {
    public String key;
    public Integer posisiton;

    public OrderedParameter(String key, Integer position)
    {
        this.key = key;
        this.posisiton = position;

    }

    public int compareTo(OrderedParameter anotherInstance) {
        return this.posisiton - anotherInstance.posisiton;
    }

}
