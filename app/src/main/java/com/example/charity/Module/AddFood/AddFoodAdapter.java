package com.example.charity.Module.AddFood;

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

interface AddFoodRecyclerItemDelegate {
    void itemTapped(int index);
}

public class AddFoodAdapter extends RecyclerView.Adapter<AddFoodAdapter.AddFoodViewHolder> {

    public AddFoodRecyclerItemDelegate delegate;

    private List<FoodDetail> foodDetails;

    public AddFoodAdapter(List<FoodDetail> foodDetails) {
        this.foodDetails = (foodDetails == null) ?
                new ArrayList<>() :
                foodDetails;
    }

    @NonNull
    @Override
    public AddFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_food_item, parent, false);

        return new AddFoodAdapter.AddFoodViewHolder(itemView, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFoodViewHolder holder, int position) {
        holder.bind(foodDetails.get(position), position);
    }

    @Override
    public int getItemCount() {
        return foodDetails.size();
    }

    static class AddFoodViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFoodName;
        private TextView txtFoodCount;

        private int position;

        public AddFoodViewHolder(@NonNull View itemView, AddFoodRecyclerItemDelegate delegate) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.txtAddFoodName);
            txtFoodCount = itemView.findViewById(R.id.txtAddFoodCount);

            itemView.setOnClickListener(v -> {
                delegate.itemTapped(position);
            });
        }

        void bind(FoodDetail foodDetail, int position) {
            this.position = position;

            txtFoodCount.setText(foodDetail.getCount() + "");
            txtFoodName.setText(foodDetail.getName());
        }

    }

}
