package com.example.charity.Module.AddFood;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.example.charity.Model.BaseResponse;
import com.example.charity.Model.Food;
import com.example.charity.Model.FoodDetail;
import com.example.charity.Application.MyActivity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFoodActivity extends MyActivity implements AddFoodRecyclerItemDelegate {

    private RecyclerView recyclerView;
    private List<FoodDetail> foodDetails;
    private AddFoodAdapter adapter;

    private TextView txtNoMessage;

    private Button btnSend;
    private FloatingActionButton fab;

    private EditText edtFoodName;
    private EditText edtFoodCount;
    private Button btnAdd;
    private Button btnCancel;

    private APIInterface apiInterface;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        AddFoodActivity.this.setTitle(getResources().getString(R.string.add_food_screen_title));

        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);

        foodDetails = new ArrayList<>();

        findViews();

        setupRecyclerView();

        new Handler().postDelayed(() -> showDialog(), 150);

        fab.setOnClickListener(v -> showDialog());

        btnSend.setOnClickListener(v -> {
            if (foodDetails.isEmpty()) {
                Toast.makeText(
                        this,
                        getResources().getString(R.string.toast_no_food_exists_to_send),
                        Toast.LENGTH_SHORT
                ).show();

            } else {
                sendButtonTapped();
            }
        });
    }

    private void sendButtonTapped() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AddFoodActivity.this);
        View view = getLayoutInflater().inflate(R.layout.warning_dialog, null);

        Button btnOk = view.findViewById(R.id.btn_addFood_warning_ok);
        Button btnNo = view.findViewById(R.id.btn_addFood_warning_no);
        TextView txtWarning = view.findViewById(R.id.txtWarning);

        txtWarning.setText(getString(R.string.add_food_warning_dialog_fixed_txt));

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
                sendToServer();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showProgress() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void sendToServer() {
        showProgress();

        final Food food = new Food(Constants.CLIENT_ID, foodDetails);

        Call<BaseResponse> call = apiInterface.addFood(food);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                hideProgress();

                BaseResponse baseResponse = response.body();

                if (baseResponse != null) {
                    if (baseResponse.getCode() == 100) {
                        Toast.makeText(AddFoodActivity.this,
                                getResources().getString(R.string.toast_send_food_list_successfuly),
                                Toast.LENGTH_SHORT).show();

                        txtNoMessage.setVisibility(View.VISIBLE);
                        foodDetails.clear();
                        adapter.notifyDataSetChanged();

                    } else if (baseResponse.getCode() == 5) {
                        Toast.makeText(
                                getApplicationContext(),
                                getResources().getString(R.string.error_code_5),
                                Toast.LENGTH_SHORT
                        ).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT)
                        .show();
                call.cancel();
            }
        });
    }

    private void findViews() {
        recyclerView = findViewById(R.id.addFood_recyclerView);
        btnSend = findViewById(R.id.btn_addFood_send);
        fab = findViewById(R.id.fab_addFood_add);
        txtNoMessage = findViewById(R.id.txt_addFood_noMessage);
        progressBar = findViewById(R.id.progressBar_addFood);
    }

    private void setupRecyclerView() {
        adapter = new AddFoodAdapter(foodDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.delegate = this;
    }

    private void showDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AddFoodActivity.this);
        View view = getLayoutInflater().inflate(R.layout.add_food_dialog, null);
        findDialogViews(view);
        alert.setView(view);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCancelable(false);

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        btnAdd.setOnClickListener(v -> dialogAddButtonTapped(alertDialog));
        alertDialog.show();
    }

    private void dialogAddButtonTapped(AlertDialog alertDialog) {
        String name = edtFoodName.getText().toString().trim();
        String count = edtFoodCount.getText().toString().trim();

        if (checkEmpty(name, count)) {
            txtNoMessage.setVisibility(View.INVISIBLE);
            foodDetails.add(new FoodDetail(name, Integer.parseInt(count)));
            adapter.notifyItemChanged(foodDetails.size() - 1);
            alertDialog.dismiss();
        } else {
            Toast.makeText(this, getResources().getString(R.string.error_incomplete_input),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void findDialogViews(View view) {
        edtFoodName = view.findViewById(R.id.edt_addFood_name);
        edtFoodCount = view.findViewById(R.id.edt_addFood_count);
        btnAdd = view.findViewById(R.id.btn_addFood_add);
        btnCancel = view.findViewById(R.id.btn_addFood_cancel);
    }

    private boolean checkEmpty(String name, String count) {
        if (name.isEmpty() || count.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public void itemTapped(int index) {
        // TODO: show dialog for edit item
    }

}
