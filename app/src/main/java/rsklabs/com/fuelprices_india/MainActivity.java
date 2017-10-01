package rsklabs.com.fuelprices_india;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fuelpriceindia.herokuapp.com/")
                .build();

        FuelPriceServiceInterface service = retrofit.create(FuelPriceServiceInterface.class);

        Call<String> repos = service.getCityList();
    }
}
