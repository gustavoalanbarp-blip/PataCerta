package com.patacerta.app.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.patacerta.app.R;
import com.patacerta.app.data.local.entity.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista horizontal de pets no Dashboard, com um card final fixo para
 * "Adicionar" — evita uma tela extra apenas para o botão de cadastro (UX).
 */
public class PetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PET = 0;
    private static final int TYPE_ADD = 1;

    public interface Listener {
        void onPetClick(Pet pet);
        void onAddClick();
    }

    private final List<Pet> pets = new ArrayList<>();
    private final Listener listener;

    public PetAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<Pet> newPets) {
        pets.clear();
        if (newPets != null) pets.addAll(newPets);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position < pets.size() ? TYPE_PET : TYPE_ADD;
    }

    @Override
    public int getItemCount() {
        return pets.size() + 1; // +1 = card "adicionar"
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ADD) {
            View view = inflater.inflate(R.layout.item_add_pet_card, parent, false);
            return new AddViewHolder(view);
        }
        View view = inflater.inflate(R.layout.item_pet_card, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PetViewHolder) {
            Pet pet = pets.get(position);
            PetViewHolder h = (PetViewHolder) holder;
            h.name.setText(pet.getName());
            h.itemView.setOnClickListener(v -> listener.onPetClick(pet));
        } else {
            holder.itemView.setOnClickListener(v -> listener.onAddClick());
        }
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView image;
        PetViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtPetName);
            image = itemView.findViewById(R.id.imgPet);
        }
    }

    static class AddViewHolder extends RecyclerView.ViewHolder {
        AddViewHolder(@NonNull View itemView) { super(itemView); }
    }
}
