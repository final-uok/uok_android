package com.example.charity.Module.FoodDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charity.Model.FoodDetail;
import com.example.charity.R;

import java.util.ArrayList;
import java.util.List;

public class FoodDetailsAdapter extends RecyclerView.Adapter<FoodDetailsAdapter.FoodDetailsViewHolder> {

    private List<FoodDetail> foodDetails;

    public FoodDetailsAdapter(List<FoodDetail> foodDetails) {
        this.foodDetails = (foodDetails == null) ?
                new ArrayList<>() :
                foodDetails;
    }

    @NonNull
    @Override
    public FoodDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_details_item, parent, false);

        return new FoodDetailsAdapter.FoodDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDetailsViewHolder holder, int position) {
        holder.bind(foodDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return foodDetails.size();
    }

    /*
     * ViewHolder class
     */
    static class FoodDetailsViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFoodName;
        private TextView txtFoodCount;

        public FoodDetailsViewHolder(@NonNull final View itemView) {
            super(itemView);

            txtFoodCount = itemView.findViewById(R.id.txtFoodDetailsFoodCount);
            txtFoodName = itemView.findViewById(R.id.txtFoodDetailsFoodName);
        }

        void bind(FoodDetail foodDetail) {
            txtFoodCount.setText(foodDetail.getCount() + "");
            txtFoodName.setText(foodDetail.getName());
        }

    }

}
