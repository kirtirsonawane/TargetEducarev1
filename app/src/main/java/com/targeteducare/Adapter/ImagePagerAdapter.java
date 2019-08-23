package com.targeteducare.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.targeteducare.Activitycommon;
import com.targeteducare.Classes.QuestionURL;
import com.targeteducare.R;
import com.targeteducare.TouchImageView;
import java.io.File;
import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<QuestionURL> arrayList;
    String url = "http://photogallery.ida.org.in/ImgHandler.ashx?imgID=";

    public ImagePagerAdapter(Context context, ArrayList<QuestionURL> arrayList) {

        try {
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrayList = arrayList;
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("ImagePagerAdapter", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.image_viewpager_layout, container, false);
        try {
            ImageView imageView = (ImageView) itemView.findViewById(R.id.viewPagerItem_image1);
            final TouchImageView imageView1 = (TouchImageView) itemView.findViewById(R.id.viewPagerItem_image2);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            File f = new File(arrayList.get(position).getImagemainsource());
            imageView1.setZoom(2);
            imageView1.setMaxZoom(100);
            if (f.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(arrayList.get(position).getImagemainsource(), bmOptions);
                Log.e("demo ", "demo " + bitmap.toString());
                imageView1.setImageBitmap(bitmap);
                imageView.setImageBitmap(bitmap);
            } else {
                Picasso.with(context).load(arrayList.get(position).getImagemainsource())
                        .placeholder(R.drawable.pkgdefault)
                        .error(R.drawable.pkgdefault)
                        .into(imageView1);
            }
            SeekBar seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
            seekBar.setProgress(20);
            final TextView txtprogress = (TextView) itemView.findViewById(R.id.textview_1);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    try {
                        imageView1.setZoom(i / 10);
                        Log.e("zoomlevel ", " " + i / 10);
                        txtprogress.setText(i + "/100");
                    } catch (Exception e) {
                        ((Activitycommon) context).reporterror("ImagePagerAdapter", e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            container.addView(itemView);
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("ImagePagerAdapter", e.toString());
            e.printStackTrace();
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        try {
            container.removeView((LinearLayout) object);
        } catch (Exception e) {
            ((Activitycommon) context).reporterror("ImagePagerAdapter", e.toString());
            e.printStackTrace();
        }
    }
}
