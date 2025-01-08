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

import com.example.d308.database.entity.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private final List<Excursion> excursions;
    private final Context context;

    public ExcursionAdapter(List<Excursion> excursions, Context context) {
        this.excursions = excursions;
        this.context = context;
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_excursion, parent, false);
        return new ExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        Excursion excursion = excursions.get(position);
        holder.textViewTitle.setText(excursion.getTitle());
        holder.textViewDate.setText(excursion.getDate());

        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditExcursionActivity.class);
            intent.putExtra("EXCURSION_ID", excursion.getId());
            context.startActivity(intent);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (context instanceof ExcursionListActivity) {
                ((ExcursionListActivity) context).deleteExcursion(excursion);
            }
        });
    }

    @Override
    public int getItemCount() {
        return excursions.size();
    }

    static class ExcursionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDate;
        Button buttonEdit, buttonDelete;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewExcursionTitle);
            textViewDate = itemView.findViewById(R.id.textViewExcursionDate);
            buttonEdit = itemView.findViewById(R.id.buttonEditExcursion);
            buttonDelete = itemView.findViewById(R.id.buttonDeleteExcursion);
        }
    }
}
