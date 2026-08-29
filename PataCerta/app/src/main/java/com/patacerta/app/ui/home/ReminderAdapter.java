package com.patacerta.app.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patacerta.app.R;
import com.patacerta.app.data.local.entity.Reminder;
import com.patacerta.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    public interface Listener {
        void onReminderClick(Reminder reminder);
    }

    private final List<Reminder> reminders = new ArrayList<>();
    /** Mapa opcional petId -> nome do pet, para exibir "Rex · hoje às 08:00" no dashboard. */
    private Map<Long, String> petNames;
    private final Listener listener;

    public ReminderAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setPetNames(Map<Long, String> petNames) {
        this.petNames = petNames;
        notifyDataSetChanged();
    }

    public void submitList(List<Reminder> newReminders) {
        reminders.clear();
        if (newReminders != null) reminders.addAll(newReminders);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder r = reminders.get(position);
        holder.title.setText(r.getTitle());

        String petName = petNames != null ? petNames.get(r.getPetId()) : null;
        String when = DateUtils.relativeDueLabel(r.getDueAtMillis());
        holder.subtitle.setText(petName != null ? (petName + " · " + when) : capitalize(when));

        int iconRes = R.drawable.ic_syringe;
        holder.icon.setImageResource(iconRes);
        holder.itemView.setOnClickListener(v -> listener.onReminderClick(r));
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView subtitle;
        final ImageView icon;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            subtitle = itemView.findViewById(R.id.txtSubtitle);
            icon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
