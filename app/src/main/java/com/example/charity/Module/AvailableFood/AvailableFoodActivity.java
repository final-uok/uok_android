package com.example.charity.Module.AvailableFood;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Module.FoodDetails.FoodDetailsActivity;
import com.example.charity.Model.Charity;
import com.example.charity.Model.Home;
import com.example.charity.R;
import com.example.charity.Utility.DividerItemDecoration;
import com.example.charity.Utility.Utilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailableFoodActivity extends AppCompatActivity implements AvailableFoodItemDelegate {

    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private List<Charity> charities;

    private AvailableFoodAdapter adapter;

    private ProgressBar progressBar;

    private TextView txtNoMessage;
    private Button btnTryAgain;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_available_food);

        findViews();

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        AvailableFoodActivity.this.setTitle(getResources().getString(R.string.available_food_screen_title));

        if (!Utilities.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.toast_no_internet_before_acttion),
                    Toast.LENGTH_SHORT).show();
        } else {
            callNewFoodsListApi();
        }

        btnTryAgain.setOnClickListener(v -> callNewFoodsListApi());

    }

    private void callNewFoodsListApi() {
        Call<Home> call = apiInterface.newFoodsList();

        call.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                Home homeResponse = response.body();

                if (homeResponse != null) {
                    if (homeResponse.getResponseCode() == 100) {
                        txtNoMessage.setVisibility(View.INVISIBLE);
                        btnTryAgain.setVisibility(View.INVISIBLE);
                        charities = homeResponse.getCharities();
                        setupRecyclerView();
                    } else if (homeResponse.getResponseCode() == 9) {
                        btnTryAgain.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new AvailableFoodAdapter(charities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.delegate = this;
    }

    private void findViews() {
        recyclerView = findViewById(R.id.availableFood_recyclerView);
        progressBar = findViewById(R.id.progressBar_availableFood);
        txtNoMessage = findViewById(R.id.txt_availableFood_noMessage);
        btnTryAgain = findViewById(R.id.btn_availableFood_tryAgain);
    }

    @Override
    public void itemTapped(String foodId) {
        if (!Utilities.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.toast_no_internet_before_acttion),
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(AvailableFoodActivity.this, FoodDetailsActivity.class);
            intent.putExtra("foodId", foodId);
            intent.putExtra("newFood", true);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
