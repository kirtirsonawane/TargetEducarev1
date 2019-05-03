package com.targeteducare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class Sample_Paper_Video_Activity extends Activitycommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample__paper__video_);
        TextView video_text_view= (TextView)findViewById(R.id.sample_papers_video_view_text);
        setmaterialDesign();
        back();

        Intent i = getIntent();
        String Url = i.getStringExtra("Video_Url");
       String Url_1="http://192.168.1.59:8097/images/Uploadvideos/20190430163430_file_example_MP4_480_1_5MG.mp4";
       String Name_of_subject=i.getStringExtra("Name_of_subject");
       setTitle(Name_of_subject+" Video");
        String Name_of_course=i.getStringExtra("Name_of_course");


        VideoView videoView =(VideoView)findViewById(R.id.sample_papers_video_view);
        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Location of Media File

        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(Url_1));
        videoView.requestFocus();
        videoView.start();
        video_text_view.setTypeface(Fonter.getTypefacebold(context));
        video_text_view.setText(Name_of_course);
    }
}
