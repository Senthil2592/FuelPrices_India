package rsklabs.com.fuelprices_india;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
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


       Log.d("city", String.valueOf(cityList));

        cityList.enqueue(new Callback<ServiceBean>() {
            @Override
            public void onResponse(Call<ServiceBean> call, Response<ServiceBean> response) {
                String str = new Gson().toJson(response.body());
                try {
                    JSONObject jsonObj = new JSONObject(str);
                    JSONArray ja = jsonObj.getJSONArray("cities");
                    Log.i("resp", String.valueOf(ja));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("resp", str);
            }

            @Override
            public void onFailure(Call<ServiceBean> call, Throwable t) {

            }


        });


    }
}
