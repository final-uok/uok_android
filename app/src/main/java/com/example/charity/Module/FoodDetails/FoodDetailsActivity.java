package com.example.charity.Module.FoodDetails;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Model.Food;
import com.example.charity.Model.FoodDetail;
import com.example.charity.Model.ReserveFood;
import com.example.charity.Application.MyActivity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailsActivity extends MyActivity {

    private Button btnAccept;

    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private List<FoodDetail> foodDetails;
    private FoodDetailsAdapter adapter;

    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        btnAccept = findViewById(R.id.btn_foodDetails_accept);

        FoodDetailsActivity.this.setTitle(getResources().getString(R.string.food_details_screen_title));

        recyclerView = findViewById(R.id.foodDetails_recyclerView);

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        if (getIntent().getExtras().getBoolean("newFood")) {
            btnAccept.setVisibility(View.VISIBLE);
        } else {
            btnAccept.setVisibility(View.INVISIBLE);
        }

        callFoodDetailsApi();

        btnAccept.setOnClickListener(v -> {
            showAlert();
        });
    }

    private void showAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.warning_dialog, null);

        Button btnOk = view.findViewById(R.id.btn_addFood_warning_ok);
        Button btnNo = view.findViewById(R.id.btn_addFood_warning_no);
        TextView txtWarning = view.findViewById(R.id.txtWarning);

        txtWarning.setText(getString(R.string.food_details_alert_text));

        alert.setView(view);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        btnNo.setOnClickListener(v -> alertDialog.dismiss());
        btnOk.setOnClickListener(v -> {
            // TODO: show spinner
            if (!Utilities.isConnected(getApplicationContext())) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.toast_no_internet_before_acttion),
                        Toast.LENGTH_SHORT).show();
            } else {
                callReserveFoodApi();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void callReserveFoodApi() {
        final ReserveFood reserveFood = new ReserveFood(
                getIntent().getExtras().getString("foodId"),
                preferences.getString(Constants.CLIENT_ID_TEXT, "")
        );

        Call<ReserveFood> call = apiInterface.reserveFood(reserveFood);
        call.enqueue(new Callback<ReserveFood>() {
            @Override
            public void onResponse(Call<ReserveFood> call, Response<ReserveFood> response) {
                ReserveFood reserveFoodResponse = response.body();

                if (reserveFoodResponse != null) {
                    if (reserveFoodResponse.getResponseCode() == 100) {
                        Toast.makeText(
                                FoodDetailsActivity.this,
                                getResources().getString(R.string.toast_food_reserved)
                                , Toast.LENGTH_SHORT
                        ).show();

                        // TODO: hide spinner

                        finish();

                    } else if (reserveFoodResponse.getResponseCode() == 6) {
                        Toast.makeText(FoodDetailsActivity.this,
                                getResources().getString(R.string.error_code_6)
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FoodDetailsActivity.this,
                                "مشکلی در سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<ReserveFood> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called",
                        Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void callFoodDetailsApi() {
        final Food food = new Food(getIntent().getExtras().getString("foodId"));

        Call<Food> call = apiInterface.foodDetails(food);
        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                Food foodResponse = response.body();

                if (foodResponse != null) {
                    if (foodResponse.getCode() == 100) {
                        foodDetails = foodResponse.getFoodDetails();

                        setupFoodDetailsRecyclerView();

                    } else if (foodResponse.getCode() == 500) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.error_code_500),
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT)
                        .show();
                call.cancel();
            }
        });
    }

    private void setupFoodDetailsRecyclerView() {
        adapter = new FoodDetailsAdapter(foodDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}
