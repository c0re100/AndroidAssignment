package moe.husky.paperscissorstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class PlayerListAdapter extends ArrayAdapter<Player> {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView playerName;
        TextView oppoName;
        TextView date;
        TextView time;
        TextView status;
        ImageView playerHand;
        ImageView oppoHand;
    }

    public PlayerListAdapter(Context context, int resource, ArrayList<Player> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        setupImageLoader();

        String playerName = getItem(position).getPlayerName();
        String oppoName = getItem(position).getOppoName();
        String date = getItem(position).getDate();
        double time = getItem(position).getTime();
        String status = getItem(position).getStatus();
        int playerHand = getItem(position).getPlayerHand();
        int oppoHand = getItem(position).getOppoHand();

        final View result;

        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.playerName = (TextView) convertView.findViewById(R.id.playerName);
            holder.oppoName = (TextView) convertView.findViewById(R.id.oppoName);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.gameTime);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.playerHand = (ImageView) convertView.findViewById(R.id.playerHand);
            holder.oppoHand = (ImageView) convertView.findViewById(R.id.oppoHand);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.playerName.setText(playerName);
        holder.oppoName.setText(oppoName);
        holder.date.setText(date);
        holder.time.setText("Duration: " + Double.toString(time) +"s");
        holder.status.setText(status);

        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = mContext.getResources().getIdentifier("@drawable/image_failed", null, mContext.getPackageName());

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        String[] imgUrl = checkHand(playerHand, oppoHand);
        imageLoader.displayImage(imgUrl[0], holder.playerHand, options);
        imageLoader.displayImage(imgUrl[1], holder.oppoHand, options);

        return convertView;
    }

    private void setupImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }

    public String[] checkHand(int selfHand, int oppoHand) {
        String[] imgHand = new String[2];

        switch (selfHand) {
            case 0:
                imgHand[0] = "drawable://" + R.drawable.paper;
                break;
            case 1:
                imgHand[0] = "drawable://" + R.drawable.scissors;
                break;
            case 2:
                imgHand[0] = "drawable://" + R.drawable.stone;
                break;
        }

        switch (oppoHand) {
            case 0:
                imgHand[1] = "drawable://" + R.drawable.paper;
                break;
            case 1:
                imgHand[1] = "drawable://" + R.drawable.scissors;
                break;
            case 2:
                imgHand[1] = "drawable://" + R.drawable.stone;
                break;
        }

        return imgHand;
    }
}



































