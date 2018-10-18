package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.martinsalerno.wikitest.R;

import java.util.Random;

import classes.Friend;
import classes.RequestHandler;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolderFriend> {

    private Friend[] friends;
    private Context context;

    public FriendAdapter(Friend[] friends, Context context){
        this.friends = friends;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolderFriend onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_list, viewGroup, false);
        return new FriendAdapter.ViewHolderFriend(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolderFriend ViewHolderFriend, int i) {
        ViewHolderFriend.assignFriends(friends[i]);
    }

    @Override
    public int getItemCount() {
        return friends.length;
    }

    public class ViewHolderFriend extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView friendName;
        ImageView friendImage;
        ImageView friendBackImage;

        public ViewHolderFriend(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.friendName = itemView.findViewById(R.id.friendName);
            this.friendBackImage = itemView.findViewById(R.id.friendBackImage);
            this.friendImage = itemView.findViewById(R.id.friendImage);
        }

        public void assignFriends(Friend friend) {
            friendName.setText(friend.getUsername());
            handleBackgroundImage();
            new RequestHandler().loadSmallProfileImage((Activity) context, friendImage, friend.getId());
        }

        public void handleBackgroundImage() {
            TypedArray profileImages = context.getResources().obtainTypedArray(R.array.profileImages);
            int i = new Random().nextInt(profileImages.length());
            int id = profileImages.getResourceId(i, -1);
            profileImages.recycle();
            friendBackImage.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), id, 400, 70));
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

        @Override
        public void onClick(final View view) {
            Friend friend = friends[getAdapterPosition()];
            Toast.makeText(context, friend.getUsername(), Toast.LENGTH_LONG).show();
        }

    }
}
