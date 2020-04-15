package com.example.charity.Module.AvailableFood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charity.Model.Charity;
import com.example.charity.R;

import java.util.ArrayList;
import java.util.List;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

interface AvailableFoodItemDelegate {
    void itemTapped(String foodId);
}

public class AvailableFoodAdapter extends RecyclerView.Adapter<AvailableFoodAdapter.AvailableFoodVH> {

    public AvailableFoodItemDelegate delegate;

    private List<Charity> charities;

    public AvailableFoodAdapter(List<Charity> charities) {
        this.charities = (charities == null) ?
                new ArrayList<>() :
                charities;
    }

    @NonNull
    @Override
    public AvailableFoodVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_food_item, parent, false);

        return new AvailableFoodAdapter.AvailableFoodVH(itemView, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableFoodVH holder, int position) {
        holder.bind(charities.get(position));
    }

    @Override
    public int getItemCount() {
        return charities.size();
    }

    /*
     * viewHolder class
     */
    static class AvailableFoodVH extends RecyclerView.ViewHolder {

        private Charity charity;

        private TextView txtPhilanthropistName;
        private TextView txtFoodCode;
        private TextView txtPhilanthropistPhoneNumber;
        private TextView txtTimestamp;

        public AvailableFoodVH(@NonNull View itemView, final AvailableFoodItemDelegate delegate) {
            super(itemView);

            txtPhilanthropistName = itemView.findViewById(R.id.txtName_availableFood);
            txtFoodCode = itemView.findViewById(R.id.txtFoodCode_availableFood);
            txtPhilanthropistPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber_availableFood);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp_availableFood);

            itemView.setOnClickListener(v -> {
                delegate.itemTapped(charity.getFoodId());
            });

        }

        void bind(Charity charity) {
            this.charity = charity;

            PersianDate date = new PersianDate(Long.valueOf(charity.getCreateFoodTimestamp()));
            PersianDateFormat dateFormat = new PersianDateFormat("Y/m/d");

            String str = String.format(
                    "%s  -  %02d:%02d",
                    dateFormat.format(date),
                    date.getHour(),
                    date.getMinute()
            );

            txtFoodCode.setText(charity.getFoodCode() + "");
            txtPhilanthropistPhoneNumber.setText(charity.getPhoneNumber());
            txtPhilanthropistName.setText(charity.getUserName());
            txtTimestamp.setText(str);
        }

    }

}
