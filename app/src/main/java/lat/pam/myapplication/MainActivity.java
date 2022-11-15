package lat.pam.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.List;

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

        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call <List<Todos>> call = jsonPlaceHolderApi.getTodos();

        call.enqueue(new Callback<List<Todos>>() {
            @Override
            public void onResponse(Call<List<Todos>> call, Response<List<Todos>> response) {
                CheckBox newCheckbox;

                if (!response.isSuccessful()) {
                    newCheckbox = new CheckBox(MainActivity.this);
                    newCheckbox.setText("Code: " + response.code());
                    linearLayout.addView(newCheckbox);
                    return;
                }

                List<Todos> todos = response.body();

                for (Todos todo : todos) {
                    newCheckbox = new CheckBox(MainActivity.this);
                    newCheckbox.setText(todo.getTitle());
                    newCheckbox.setChecked(todo.getCompleted());
                    linearLayout.addView(newCheckbox);
                }
            }

            @Override
            public void onFailure(Call<List<Todos>> call, Throwable t) {
                CheckBox newCheckbox = new CheckBox(MainActivity.this);
                newCheckbox.setText(t.getMessage());
                linearLayout.addView(newCheckbox);
            }
        });
    }
}