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
import com.example.d308.database.entity.Vacation;
import com.example.d308.database.AppDatabase;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {
    private final List<Excursion> excursions;
    private final Context context;

    AppDatabase appDatabase;

    public ExcursionAdapter(List<Excursion> excursions, Context context) {
        this.excursions = excursions;
        this.context = context;
        this.appDatabase = AppDatabase.getInstance(context);
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

        new Thread(() -> {
            Vacation vacation = appDatabase.excursionDao().getVacationForExcursion(excursion.getId());
            if (vacation != null) {
                holder.buttonEdit.setOnClickListener(v -> {
                    Intent intent = new Intent(context, EditExcursionActivity.class);
                    intent.putExtra("EXCURSION_ID", excursion.getId());
                    intent.putExtra("VACATION_ID", excursion.getVacationId());
                    intent.putExtra("VACATION_START_DATE", vacation.getStartDate());
                    intent.putExtra("VACATION_END_DATE", vacation.getEndDate());

                    context.startActivity(intent);
                });
            }
        }).start();

        holder.buttonDelete.setOnClickListener(v -> {
            if (context instanceof ExcursionListActivity) {
                ((ExcursionListActivity) context).deleteExcursion(excursion);
            } else if (context instanceof VacationDetailsActivity) {
                ((VacationDetailsActivity) context).deleteExcursion(excursion);
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
