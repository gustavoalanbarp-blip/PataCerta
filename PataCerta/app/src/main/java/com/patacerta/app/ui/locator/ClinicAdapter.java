package com.patacerta.app.ui.locator;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patacerta.app.R;
import com.patacerta.app.data.remote.model.NominatimPlace;

import java.util.ArrayList;
import java.util.List;

public class ClinicAdapter extends RecyclerView.Adapter<ClinicAdapter.ViewHolder> {

    private final List<NominatimPlace> places = new ArrayList<>();

    public void submitList(List<NominatimPlace> newPlaces) {
        places.clear();
        if (newPlaces != null) places.addAll(newPlaces);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NominatimPlace place = places.get(position);
        holder.name.setText(place.getDisplayName());
        holder.type.setText(place.getType() != null ? place.getType() : "local");

        holder.itemView.setOnClickListener(v -> {
            // Abre no app de mapas do sistema — não exige SDK do Google Maps nem chave paga.
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + place.getLat() + "," + place.getLon()
                    + "(" + Uri.encode(place.getDisplayName()) + ")");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                v.getContext().startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView type;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            type = itemView.findViewById(R.id.txtType);
        }
    }
}
