package duma.asu.models.modbus;

public class PduPackage {
    public byte slave_adress;
    public int function_code;
    public int start_adress_high;

    public int start_adress_low;
    public int high_count;
    public int low_count;
    public String hing_volume;
    public String low_volume;
}
