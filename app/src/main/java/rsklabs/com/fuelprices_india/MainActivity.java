package rsklabs.com.fuelprices_india;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fuelpriceindia.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FuelPriceServiceInterface service = retrofit.create(FuelPriceServiceInterface.class);

        Call<ServiceBean> cityList = service.getCityList();

        cityList.enqueue(new Callback<ServiceBean>() {
            @Override
            public void onResponse(Call<ServiceBean> call, Response<ServiceBean> response) {
                String str = new Gson().toJson(response.body());

                try {
                    JSONObject jsonObj = new JSONObject(str);
                    JSONArray ja = jsonObj.getJSONArray("cities");
                    String[] arraySpinner = new String[60];
                    for (int i=0; i<ja.length(); i++) {
                        arraySpinner[i]= ja.getString(i) ;
                    }
                    Spinner s = (Spinner) findViewById(R.id.citySpinnerId);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, arraySpinner);
                    s.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ServiceBean> call, Throwable t) {

            }


        });


    }
}
