package rsklabs.com.fuelprices_india;

/**
 * Created by Senthilkumar on 10/8/2017.
 */

public class FuelPriceOutputTO {

    private String city;
    private String petrol;

    public String getPetrol() {
        return petrol;
    }

    public void setPetrol(String petrol) {
        this.petrol = petrol;
    }

    public String getDiesel() {
        return diesel;
    }

    public void setDiesel(String diesel) {
        this.diesel = diesel;
    }

    private String diesel;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
