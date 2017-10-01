package rsklabs.com.fuelprices_india;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Senthilkumar on 10/1/2017.
 */

public interface FuelPriceServiceInterface {

    @GET("cities")
    Call<ServiceBean> getCityList();
}
