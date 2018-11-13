package com.example.martinsalerno.wikitest.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.martinsalerno.wikitest.R;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolderItem> {

    private List<String> strings;

    public ItemAdapter(List<String> strings){
        this.strings = strings;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolderItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new ItemAdapter.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolderItem viewHolderItem, int i) {
        viewHolderItem.assignString(strings.get(i));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView itemString;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            this.itemString = itemView.findViewById(R.id.recyclerItem);
        }

        public void assignString(String string) {
            itemString.setText(string);
        }
    }
}
