package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.martinsalerno.wikitest.R;

import java.util.List;
import java.util.function.Function;

import classes.RequestHandler;
import classes.Show;

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
        //TextView functionList;

        public ViewHolderFunction(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.functionName = (TextView) itemView.findViewById(R.id.showName);
            this.functionDate = (TextView) itemView.findViewById(R.id.showDate);
            this.functionImage = (ImageView) itemView.findViewById(R.id.showImage);
            //this.functionList = (TextView) itemView.findViewById(R.id.showTracklist);

        }

        public void assignShow(Show show) {
            functionName.setText(show.getArtista());
            functionDate.setText(show.getFecha().toString());
            new RequestHandler().loadImageFromRef((Activity) context, functionImage, show.getImagenRef());
            //functionList.setText(show.getSetList());
        }

        @Override
        public void onClick(final View view) {
            Show show = shows.get(getAdapterPosition());
            Toast.makeText(context, show.getArtista(), Toast.LENGTH_LONG).show();
        }

    }
}
