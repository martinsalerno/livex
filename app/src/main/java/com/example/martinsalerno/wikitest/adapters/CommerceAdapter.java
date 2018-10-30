package com.example.martinsalerno.wikitest.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.martinsalerno.wikitest.R;

import java.util.List;

import com.example.martinsalerno.wikitest.classes.Commerce;
import com.example.martinsalerno.wikitest.classes.RequestHandler;

public class CommerceAdapter extends RecyclerView.Adapter<CommerceAdapter.ViewHolderCommerce> {

    private List<Commerce> commerces;
    private Context context;

    public CommerceAdapter(List<Commerce> commerces, Context context){
        this.commerces = commerces;
        this.context = context;
    }

    @NonNull
    @Override
    public CommerceAdapter.ViewHolderCommerce onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.commerce_list, null, false);
        return new CommerceAdapter.ViewHolderCommerce(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommerceAdapter.ViewHolderCommerce ViewHolderCommerce, int i) {
        ViewHolderCommerce.assignCommerces(commerces.get(i));
    }

    @Override
    public int getItemCount() {
        return commerces.size();
    }

    public class ViewHolderCommerce extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView commerceName;
        ImageView commerceImage;

        public ViewHolderCommerce(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.commerceName = (TextView) itemView.findViewById(R.id.commerceName);
            this.commerceImage = itemView.findViewById(R.id.commerceImage);
        }

        public void assignCommerces(Commerce commerce) {
            commerceName.setText(commerce.getNombre());
            new RequestHandler().loadImageFromRef((Activity) context, commerceImage, commerce.getImagenRef());

        }

        @Override
        public void onClick(final View view) {
            Commerce commerce = commerces.get(getAdapterPosition());
            Toast.makeText(context, commerce.getNombre(), Toast.LENGTH_LONG).show();
        }

    }
}
