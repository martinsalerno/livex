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
import com.example.martinsalerno.wikitest.R;

import java.util.Arrays;
import java.util.List;

import com.example.martinsalerno.wikitest.classes.RequestHandler;
import com.example.martinsalerno.wikitest.classes.Show;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolderFunction> {

    private List<Show> shows;
    private Context context;

    public ShowAdapter(List<Show> shows, Context context){
        this.shows = shows;
        this.context = context;
    }

    @NonNull
    @Override
    public ShowAdapter.ViewHolderFunction onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_list, null, false);
        return new ShowAdapter.ViewHolderFunction(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowAdapter.ViewHolderFunction ViewHolderFunction, int i) {
        ViewHolderFunction.assignShow(shows.get(i));
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public class ViewHolderFunction extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView functionName;
        TextView functionDate;
        ImageView functionImage;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Integer width = LinearLayout.LayoutParams.MATCH_PARENT;
        Integer height =  LinearLayout.LayoutParams.WRAP_CONTENT;
        final static String SOME_SONGS = "Algunas de sus canciones";

        public ViewHolderFunction(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.functionName =  itemView.findViewById(R.id.showName);
            this.functionDate =  itemView.findViewById(R.id.showDate);
            this.functionImage =  itemView.findViewById(R.id.showImage);

        }

        public void assignShow(Show show) {
            functionName.setText(show.getArtista());
            functionDate.setText(show.getFecha());
            new RequestHandler().loadImageFromRef((Activity) context, functionImage, show.getImagenRef());
        }

        @Override
        public void onClick(final View view) {
            Show show = shows.get(getAdapterPosition());
            final View popupView = inflater.inflate(R.layout.pop_out, null);
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setAnimationStyle(R.style.popOutAnimation);
            TextView popoutTittle = popupView.findViewById(R.id.popOutTitle);
            popoutTittle.setText(show.getArtista());
            TextView popoutSubTittle =  popupView.findViewById(R.id.popOutSubTittle);
            popoutSubTittle.setText(SOME_SONGS);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            Button button = popupView.findViewById(R.id.popOutButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            List<String> strings = Arrays.asList(show.getSetList().split(","));
            ItemAdapter adapter = new ItemAdapter(strings);
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
