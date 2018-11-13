package com.example.martinsalerno.wikitest.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.martinsalerno.wikitest.R;

import java.util.Arrays;
import java.util.List;

import com.example.martinsalerno.wikitest.classes.Commerce;
import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.Show;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Integer width = LinearLayout.LayoutParams.MATCH_PARENT;
        Integer height =  LinearLayout.LayoutParams.WRAP_CONTENT;
        final static String SOME_PRODUCTS = "Algunos de sus productos";

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
            final View popupView = inflater.inflate(R.layout.pop_out, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setAnimationStyle(R.style.popOutAnimation);
            TextView popoutTittle = popupView.findViewById(R.id.popOutTitle);
            popoutTittle.setText(commerce.getNombre());
            TextView popoutSubTittle =  popupView.findViewById(R.id.popOutSubTittle);
            popoutSubTittle.setText(SOME_PRODUCTS);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            Button button = popupView.findViewById(R.id.popOutButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            ItemAdapter adapter = new ItemAdapter(commerce.getFormattedItems());
            RecyclerView recycler = popupView.findViewById(R.id.popOutRecycler);
            recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recycler.setHasFixedSize(true);
            recycler.setAdapter(adapter);
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
        }

    }
}
