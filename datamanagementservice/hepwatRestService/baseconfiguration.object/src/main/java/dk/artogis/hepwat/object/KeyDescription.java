package dk.artogis.hepwat.object;

public class KeyDescription {
    public String field;
    public String type;
    public String value;

    public String toJson()
    {
        return  "[{\"field\":\"" + field + "\",\"type\":\"" + type + "\",\"value\":\"" + value  + "\"}]";
    }
}
