package rsklabs.com.fuelprices_india;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static rsklabs.com.fuelprices_india.R.id.adView1;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedpreferences ;
    public static final String city = "cityChosen";
    public static final String MyPREFERENCES = "India_fuel_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        AdView adView = (AdView)findViewById(adView1);
        AdRequest adRequest =new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fuelpriceindia.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       final FuelPriceServiceInterface service = retrofit.create(FuelPriceServiceInterface.class);

        Call<ServiceBean> cityList = service.getCityList();

        cityList.enqueue(new Callback<ServiceBean>() {
            @Override
            public void onResponse(Call<ServiceBean> call, Response<ServiceBean> response) {
                String str = new Gson().toJson(response.body());

                try {
                    JSONObject jsonObj = new JSONObject(str);
                    JSONArray ja = jsonObj.getJSONArray("cities");
                    String[] arraySpinner = new String[39];
                    for (int i=0; i<ja.length(); i++) {
                        if( null != ja.getString(i) && !ja.getString(i).isEmpty()) {
                            arraySpinner[i] = ja.getString(i);
                        }
                    }
                    Spinner s = (Spinner) findViewById(R.id.citySpinnerId);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
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



        final Spinner spinner = (Spinner) findViewById(R.id.citySpinnerId);
        if(spinner.getAdapter() ==null){
            TextView petrolTextId = (TextView) findViewById(R.id.petrolTextViewId);
            petrolTextId.setText("Please check the internet connection.");
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
                String selected = spinner.getItemAtPosition(position).toString();
                Call<FuelPriceOutputTO> priceList = service.getCurrentPrice(selected);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                priceList.enqueue(new Callback<FuelPriceOutputTO>() {
                    @Override
                    public void onResponse(Call<FuelPriceOutputTO> call, Response<FuelPriceOutputTO> response) {
                        String str = new Gson().toJson(response.body());

                        try {
                            JSONObject jsonObj = new JSONObject(str);
                            TextView petrolTextId = (TextView) findViewById(R.id.petrolTextViewId);
                            TextView dieselTextId = (TextView) findViewById(R.id.diselTextViewId);
                            if(jsonObj.has("petrol")) {
                                petrolTextId.setText("Today's Petrol Price: Rs." + jsonObj.getString("petrol"));
                                dieselTextId.setText("Today's Diesel Price: Rs." + jsonObj.getString("diesel"));
                            }else{
                                petrolTextId.setText("Data Not available for this city.");
                                dieselTextId.setText("");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<FuelPriceOutputTO> call, Throwable t) {

                    }


                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }
}
