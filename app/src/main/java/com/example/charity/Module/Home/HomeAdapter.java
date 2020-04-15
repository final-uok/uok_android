package com.example.charity.Module.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charity.Model.Charity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

interface PhilanthropistRecyclerItemDelegate {
    void itemTapped(String foodId);
}

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    public PhilanthropistRecyclerItemDelegate delegate;

    private List<Charity> charities;

    public HomeAdapter(List<Charity> charities) {
        this.charities = (charities == null) ?
                new ArrayList<>() :
                charities;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (Constants.USER_TYPE.equals("1")) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.philanthropist_home_item, parent, false);

            return new HomeViewHolder(itemView, delegate);

        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.charity_home_item, parent, false);

            return new HomeViewHolder(itemView, delegate);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bind(charities.get(position));
    }

    @Override
    public int getItemCount() {
        return charities.size();
    }

    /*
     * viewHolder class
     */
    static class HomeViewHolder extends RecyclerView.ViewHolder {

        private Charity charity;

        private TextView txtName;
        private TextView txtFoodCode;
        private TextView txtPhoneNumber;
        private TextView txtAddress;
        private TextView txtTimestamp;

        private TextView txtPhilanthropistName;
        private TextView txttxtPhilanthropistFoodCode;
        private TextView txtPhilanthropistPhoneNumber;
        private TextView txtTimestampCharity;

        public HomeViewHolder(@NonNull final View itemView,
                              final PhilanthropistRecyclerItemDelegate delegate) {
            super(itemView);

            // for philanthropist homeList
            txtName = itemView.findViewById(R.id.txtName);
            txtFoodCode = itemView.findViewById(R.id.txtFoodCode);
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);

            // for charity homeList
            txttxtPhilanthropistFoodCode = itemView.findViewById(R.id.txt_FoodCode_home_charity);
            txtPhilanthropistName = itemView.findViewById(R.id.txt_name_home_charity);
            txtPhilanthropistPhoneNumber = itemView.findViewById(R.id.txt_phoneNumber_home_charity);
            txtTimestampCharity = itemView.findViewById(R.id.txtTimestamp_charity);

            itemView.setOnClickListener(v -> {
                delegate.itemTapped(charity.getFoodId());
            });
        }

        void bind(Charity charity) {
            this.charity = charity;

            PersianDate date = new PersianDate(Long.valueOf(charity.getReserveFoodTimestamp()));
            PersianDateFormat dateFormat = new PersianDateFormat("Y/m/d");

            String str = String.format("%s  -  %02d:%02d",
                    dateFormat.format(date),
                    date.getHour(),
                    date.getMinute());

            if (Constants.USER_TYPE.equals("1")) { // philanthropist
                txtName.setText(charity.getUserName());
                txtFoodCode.setText(charity.getFoodCode() + "");
                txtPhoneNumber.setText(charity.getPhoneNumber());
                txtAddress.setText(charity.getAddress());
                txtTimestamp.setText(str);
            } else {
                txttxtPhilanthropistFoodCode.setText(charity.getFoodCode() + "");
                txtPhilanthropistPhoneNumber.setText(charity.getPhoneNumber());
                txtPhilanthropistName.setText(charity.getUserName());
                txtTimestampCharity.setText(str);
            }

        }
    }

}
