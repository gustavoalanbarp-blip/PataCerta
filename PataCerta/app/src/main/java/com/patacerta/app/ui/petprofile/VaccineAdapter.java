package com.patacerta.app.ui.petprofile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patacerta.app.R;
import com.patacerta.app.data.local.entity.Reminder;
import com.patacerta.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Exibe lembretes do tipo "vacina" como itens da carteira de vacinação
 * (RF03), reaproveitando a mesma entidade Reminder para evitar duplicar
 * modelo de dados apenas por rótulo de UI.
 */
public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.ViewHolder> {

    private final List<Reminder> vaccines = new ArrayList<>();

    public void submitList(List<Reminder> newList) {
        vaccines.clear();
        if (newList != null) {
            for (Reminder r : newList) {
                if (Reminder.TYPE_VACCINE.equals(r.getType())) vaccines.add(r);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vaccine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder r = vaccines.get(position);
        holder.title.setText(r.getTitle());

        boolean overdue = r.getDueAtMillis() < System.currentTimeMillis() && !r.isDone();
        holder.subtitle.setText((r.isDone() ? "Aplicada em " : "Vence em ") + DateUtils.formatShort(r.getDueAtMillis()));
        holder.dot.setBackgroundResource(r.isDone()
                ? R.drawable.bg_status_dot
                : (overdue ? R.drawable.bg_status_dot_warning : R.drawable.bg_status_dot_warning));
    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView subtitle;
        final View dot;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            subtitle = itemView.findViewById(R.id.txtSubtitle);
            dot = itemView.findViewById(R.id.dotStatus);
        }
    }
}
