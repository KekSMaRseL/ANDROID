package algonquin.cst2335.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.android.R;
import algonquin.cst2335.android.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        binding.forecastButton.setOnClickListener(click -> {
            String cityName = binding.cityTextField.getText().toString();
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    URLEncoder.encode(cityName) + "&appid=5fba057b853cdce8abb373f194fc6cc2&units=metric";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (successfulResponse) -> {
                        try {
                            JSONObject main = successfulResponse.getJSONObject("main");
                            double temp = main.getDouble("temp");
                            double maxTemp = main.getDouble("temp_max");
                            double minTemp = main.getDouble("temp_min");
                            int humidity = main.getInt("humidity");

                            JSONArray weatherArray = successfulResponse.getJSONArray("weather");
                            JSONObject pos0 = weatherArray.getJSONObject(0);
                            String description = pos0.getString("description");
                            String iconName = pos0.getString("icon");

                            String pictureURL = "http://openweathermap.org/img/w/" + iconName + ".png";

                            ImageRequest imgReq = new ImageRequest(pictureURL, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    runOnUiThread(() -> {
                                        binding.weatherImageView.setImageBitmap(bitmap);
                                        binding.weatherImageView.setVisibility(View.VISIBLE);

                                        binding.tempTextView.setText(getString(R.string.current_temperature, temp));
                                        binding.tempTextView.setVisibility(View.VISIBLE);

                                        binding.maxTempTextView.setText(getString(R.string.maximum_temperature, maxTemp));
                                        binding.maxTempTextView.setVisibility(View.VISIBLE);

                                        binding.minTempTextView.setText(getString(R.string.minimum_temperature, minTemp));
                                        binding.minTempTextView.setVisibility(View.VISIBLE);

                                        binding.humidityTextView.setText(getString(R.string.humidity, humidity));
                                        binding.humidityTextView.setVisibility(View.VISIBLE);

                                        binding.descriptionTextView.setText(getString(R.string.description, description));
                                        binding.descriptionTextView.setVisibility(View.VISIBLE);
                                    });
                                }
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null,

                                    (error) -> {
                                        runOnUiThread(() -> {
                                            binding.textView.setText(getString(R.string.error_fetch_image));
                                            binding.textView.setVisibility(View.VISIBLE);
                                        });
                                    });
                            queue.add(imgReq);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (vError) -> {
                        runOnUiThread(() -> {
                            binding.textView.setText(getString(R.string.error_fetch_data));
                            binding.textView.setVisibility(View.VISIBLE);
                        });
                    });

            queue.add(request);
        });
    }
}