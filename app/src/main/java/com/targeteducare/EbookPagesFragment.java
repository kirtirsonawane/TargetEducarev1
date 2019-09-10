package com.targeteducare;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.targeteducare.Classes.EbookPageDetails;
import com.targeteducare.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EbookPagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EbookPagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EbookPagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    //TextView tv_data;
    WebView webview;
    ImageView iv_fav, iv_bookmark;
    int chapterid = 0;
    int favorite = 0;
    int bookmark = 0;
    int unitid = 0;

    // TODO: Rename and change types of parameters
    private EbookPageDetails mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EbookPagesFragment() {
        // Required empty public constructor
    }


    public static EbookPagesFragment newInstance(EbookPageDetails param1, String param2, int unitid) {
        EbookPagesFragment fragment = new EbookPagesFragment();
        try {
            Bundle args = new Bundle();
            args.putSerializable(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            fragment.unitid = unitid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (EbookPageDetails) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ebook_pages, container, false);
        //tv_data = view.findViewById(R.id.tv_data);
        //tv_data.setText(Html.fromHtml(mParam1.getPageContent()));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(recforfav, new IntentFilter("Favorite"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(recforbookmark, new IntentFilter("Bookmark"));


        webview = view.findViewById(R.id.webview);
        iv_fav = view.findViewById(R.id.iv_fav);
        iv_bookmark = view.findViewById(R.id.iv_bookmark);

        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        //stop copying from webview
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webview.setLongClickable(false);
        webview.loadData(mParam1.getPageContent(), "text/html", null);

        JSONArray array = DatabaseHelper.getInstance(getActivity()).get_ebookpages(unitid, chapterid, mParam1.getPageNo());

        if(array.length()>0){
            JSONObject obj = array.optJSONObject(0);

            if(obj.has("favorite")){
                Log.e("fav ", obj.optString("favorite"));
                favorite = obj.optInt("favorite");
                setfavorites(favorite);
            }
            if(obj.has("bookmark")){
                Log.e("fav ", obj.optString("favorite"));
                bookmark = obj.optInt("bookmark");
                setbookmarks(bookmark);
            }
        }


        iv_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mParam1.isFavorite()) {
                    iv_fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    mParam1.setFavorite(true);
                } else {
                    iv_fav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    mParam1.setFavorite(false);
                }
                update_pages();
            }
        });

        iv_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mParam1.isBookmark()) {
                    iv_bookmark.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
                    mParam1.setBookmark(true);
                } else {
                    iv_bookmark.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
                    mParam1.setBookmark(false);
                }
                update_pages();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setfavorites(int favorite) {
        if(favorite == 1){
            iv_fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
            mParam1.setFavorite(true);
        }else {
            iv_fav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
            mParam1.setFavorite(false);
        }
    }

    private void setbookmarks(int bookmark) {
        if(bookmark == 1){
            iv_bookmark.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
            mParam1.setBookmark(true);
        }else{
            iv_bookmark.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
            mParam1.setBookmark(false);
        }

    }

    private void update_pages(){
        ContentValues c = new ContentValues();
        c.put(DatabaseHelper.UNIT_ID, unitid);
        c.put(DatabaseHelper.CHAPTER_ID, chapterid);
        c.put(DatabaseHelper.PAGE_ID, mParam1.getPageNo());
        if(mParam1.isFavorite())
            c.put(DatabaseHelper.FAVORITE, 1);
        else
            c.put(DatabaseHelper.FAVORITE, 0);
        if(mParam1.isBookmark())
            c.put(DatabaseHelper.BOOKMARK, 1);
        else
            c.put(DatabaseHelper.BOOKMARK, 0);
        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());

        DatabaseHelper.getInstance(getActivity()).save_ebookpages(c, unitid, chapterid , mParam1.getPageNo());

        //Log.e("content data ", c.toString());
    }


    /*public void updateforfavorite(EbookPageDetails ebookPageDetails) {
        try {
            Intent intent = new Intent("FavQuestionfromFragment");
            intent.putExtra("Favorite", ebookPageDetails.getPageId());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        } catch (Exception e) {
            //reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }

    public void updateforbookmark(EbookPageDetails ebookPageDetails) {
        try {
            Intent intent = new Intent("BookmarkQuestionfromFragment");
            intent.putExtra("Bookmark", ebookPageDetails.getPageId());
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        } catch (Exception e) {
            //reporterror(tag, e.toString());
            e.printStackTrace();
        }
    }*/

    BroadcastReceiver recforfav = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (mParam1.getPageNo() == intent.getIntExtra("Favorite", 0)) {
                    if (mParam1.isFavorite()) {
                        iv_fav.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    }else{
                        iv_fav.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    BroadcastReceiver recforbookmark = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (mParam1.getPageNo() == intent.getIntExtra("Bookmark", 0)) {
                    if (mParam1.isBookmark()) {
                        if (mParam1.isFavorite()) {
                            iv_bookmark.setBackgroundResource(R.drawable.ic_bookmark_black_24dp);
                        }else{
                            iv_bookmark.setBackgroundResource(R.drawable.ic_bookmark_border_black_24dp);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
