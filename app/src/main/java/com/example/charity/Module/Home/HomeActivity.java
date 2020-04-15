package com.example.charity.Module.Home;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Module.AddFood.AddFoodActivity;
import com.example.charity.Module.FoodDetails.FoodDetailsActivity;
import com.example.charity.Model.Charity;
import com.example.charity.Model.Home;
import com.example.charity.Application.MyActivity;
import com.example.charity.R;
import com.example.charity.Application.CharityPollingService;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends MyActivity implements PhilanthropistRecyclerItemDelegate {

    private APIInterface apiInterface;

    private RecyclerView recyclerView;
    private List<Charity> charities;
    private HomeAdapter adapter;

    private Button btnAdd;
    private Button btnTryAgain;

    private TextView txtNoMessage;

    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_home);

        hideBackButton();
        findViews();
        apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);
        setTitle();

        if (!Utilities.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.toast_no_internet_before_acttion),
                    Toast.LENGTH_SHORT).show();
        } else {
            loadHomeList();
        }

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddFoodActivity.class));
        });

        btnTryAgain.setOnClickListener(v -> {
            if (!Utilities.isConnected(getApplicationContext())) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.toast_no_internet_before_acttion),
                        Toast.LENGTH_SHORT).show();
            } else {
                loadHomeList();
            }
        });

    }

    private void findViews() {
        recyclerView = findViewById(R.id.phianthropist_recyclerView);
        btnAdd = findViewById(R.id.btn_home_add);
        btnTryAgain = findViewById(R.id.btn_home_tryAgain);
        txtNoMessage = findViewById(R.id.txt_home_noMessage);
        progressBar = findViewById(R.id.progressBar_home);
        relativeLayout = findViewById(R.id.relativeLayout_home);
    }

    private void setTitle() {
        if (Constants.USER_TYPE.equals("1")) {
            HomeActivity.this.setTitle(getResources().getString(R.string.home_philanthropist_screen_title));
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            HomeActivity.this.setTitle(getResources().getString(R.string.home_charity_screen_title));
            btnAdd.setVisibility(View.INVISIBLE);
        }
    }

    private void loadHomeList() {
        showProgress();

        getCallObject().enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                hideProgress();

                Home homeResponse = response.body();

                if (homeResponse != null) {
                    if (homeResponse.getResponseCode() == 100) {
                        charities = homeResponse.getCharities();

                        if (charities.size() > 0) {
                            txtNoMessage.setVisibility(View.INVISIBLE);
                            btnTryAgain.setVisibility(View.INVISIBLE);
                        }

                        setupRecyclerView();

                    } else {
                        Toast.makeText(getApplicationContext(), "Server Error",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onFailure called ", Toast.LENGTH_SHORT)
                        .show();
                call.cancel();
            }
        });

    }

    private Call<Home> getCallObject() {
        final Home home = new Home(Constants.CLIENT_ID, Constants.USER_TYPE);

        if (Constants.USER_TYPE.equals("1")) {
            return apiInterface.philanthropistHomeList(home);
        } else {
            return apiInterface.charityHomeList(home);
        }
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

    private void setupRecyclerView() {
        adapter = new HomeAdapter(charities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.delegate = this;
    }

    /*
     * interfaces
     */
    @Override
    public void itemTapped(String foodId) {
        if (!Utilities.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.toast_no_internet_before_acttion),
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(HomeActivity.this, FoodDetailsActivity.class);
            intent.putExtra("foodId", foodId);
            intent.putExtra("newFood", false);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.charity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_exit) {
            stopService(new Intent(getApplicationContext(), CharityPollingService.class));
            clearConstants();
            clearSharedPref();
            this.finishAffinity();
        }

        return true;
    }

    private void clearConstants() {
        Constants.PHONE_NUMBER = "";
        Constants.USER_TYPE = "1";
        Constants.CLIENT_ID = "";
        Constants.TAP_COUNT = 0;
    }

    private void clearSharedPref() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isLogin", false);
        editor.putString(Constants.CLIENT_ID_TEXT, "");
        editor.putString(Constants.PHONE_NUMBER_TEXT, "");
        editor.putString(Constants.USER_TYPE_TEXT, "");
        editor.apply();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Constants.TAP_COUNT += 1;

            if (Constants.TAP_COUNT == 1) {
                Toast.makeText(this, getResources().getString(R.string.toast_exit_warning),
                        Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> Constants.TAP_COUNT = 0, 3000);

            } else {
                this.finishAffinity();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
