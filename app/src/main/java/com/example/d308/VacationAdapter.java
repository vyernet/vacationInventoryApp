package com.example.d308;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.database.entity.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private final List<Vacation> vacations;
    private final Context context;

    public VacationAdapter(List<Vacation> vacations, Context context) {
        this.vacations = vacations;
        this.context = context;
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacation, parent, false);
        return new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        Vacation vacation = vacations.get(position);
        holder.textViewTitle.setText(vacation.getTitle());
        holder.textViewHotel.setText(vacation.getHotel());

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditVacationActivity.class);
            intent.putExtra("VACATION_ID", vacation.getId());
            context.startActivity(intent);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                ((MainActivity) context).deleteVacation(vacation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vacations.size();
    }

    static class VacationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewHotel;
        Button buttonEdit, buttonDelete;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewVacationTitle);
            textViewHotel = itemView.findViewById(R.id.textViewVacationHotel);
            buttonEdit = itemView.findViewById(R.id.buttonEditVacation);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteVacation);
        }
    }
}