package com.tiktokclone.ui.Sound;


import static com.tiktokclone.record.RecorderActivity.getAppFolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.request.DownloadRequest;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.tiktokclone.R;
import com.tiktokclone.adapter.AdapterClickListener;
import com.tiktokclone.adapter.SoundListAdapter;
import com.tiktokclone.data.datasource.model.sound.DataXXXX;
import com.tiktokclone.utils.Variables;
import com.tiktokclone.viewmodel.model.SoundViewModel;


import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SectionSoundListA extends AppCompatActivity implements Player.Listener, View.OnClickListener{

    Context context;
    TextView titleTxt;
    String id;
    ArrayList<DataXXXX> datalist;
    SoundListAdapter adapter;
    static boolean active = false;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    DownloadRequest prDownloader;
    public static String running_sound_id;
    ProgressBar pbar;
    SwipeRefreshLayout swiperefresh;
     int pageCount = 0;
    boolean ispostFinsh;
    ProgressBar loadMoreProgress;
    SoundViewModel  boardViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_sound_list);
        context = SectionSoundListA.this;

           boardViewModel = new ViewModelProvider(this).get(SoundViewModel.class);

         titleTxt = findViewById(R.id.title_txt);

        id = getIntent().getStringExtra("id");
        titleTxt.setText(getIntent().getStringExtra("name"));

        running_sound_id = "none";
        PRDownloader.initialize(context);

        findViewById(R.id.back_btn).setOnClickListener(this::onClick);
        pbar = findViewById(R.id.pbar);
        loadMoreProgress = findViewById(R.id.load_more_progress);


        recyclerView = findViewById(R.id.listview);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean userScrolled;
            int scrollOutitems;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollOutitems = linearLayoutManager.findLastVisibleItemPosition();

                 if (userScrolled && (scrollOutitems == datalist.size() - 1)) {
                    userScrolled = false;

                    if (loadMoreProgress.getVisibility() != View.VISIBLE && !ispostFinsh) {
                        loadMoreProgress.setVisibility(View.VISIBLE);
                        pageCount = pageCount + 1;
                        callApi();
                    }
                }


            }
        });


        swiperefresh = findViewById(R.id.swiperefresh);
        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                pageCount = 0;

                callApi();
            }
        });


        setAdapter();

        callApi();

    }


     public void setAdapter() {
        datalist = new ArrayList<>();

        adapter = new SoundListAdapter(context, datalist, new AdapterClickListener() {
            @Override
            public void onItemClick(View view, int pos, Object object) {

                DataXXXX item = (DataXXXX) object;
                Log.d("asdsasdasadsda","saddsasdds");
                if (view.getId() == R.id.done) {
                    stopPlaying();
                    downLoadMp3(item.getId()+"", item.getTitle(), item.getPreview());
                }
                else {
                    stopPlaying();
                    playaudio(view, item);
                }
            }
        });

        recyclerView.setAdapter(adapter);


    }


    public void callApi() {


        pbar.setVisibility(View.GONE);
        loadMoreProgress.setVisibility(View.GONE);
        parseData();

    }

    // parse  the data of sound list
    public void parseData( ) {

        boardViewModel.getTracks();

        boardViewModel.getRepos().observe(this, new Observer<List<DataXXXX>>() {
            @Override
            public void onChanged(List<DataXXXX> dataXXXXES) {
                datalist.addAll(dataXXXXES);
            }
        });



    }


    // initialize the player for play the audio

    View previousView;
    SimpleExoPlayer player;
    Thread thread;
    String previous_url = "none";

    public void playaudio(View view, final DataXXXX item) {
        previousView = view;

        if (previous_url.equals(item.getPreview())) {
            previous_url = "none";
            running_sound_id = "none";
        } else {

            previous_url = item.getPreview();
            running_sound_id = item.getId()+"";

            DefaultTrackSelector trackSelector = new DefaultTrackSelector(context);

            player = new SimpleExoPlayer.Builder(context).
                    setTrackSelector(trackSelector)
                    .build();

            DataSource.Factory cacheDataSourceFactory = new DefaultDataSourceFactory(SectionSoundListA.this, getString(R.string.app_name));
             MediaSource videoSource = new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(MediaItem.fromUri(item.getPreview()));
            player.addMediaSource(videoSource);
            player.prepare();
            player.addListener(this);


            player.setPlayWhenReady(true);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                            .setUsage(C.USAGE_MEDIA)
                            .setContentType(C.CONTENT_TYPE_MOVIE)
                            .build();
                    player.setAudioAttributes(audioAttributes, true);
                }
            }
            catch (Exception e)
            {
             }
        }

    }


    public void stopPlaying() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }

        showStopState();

    }


    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;

        running_sound_id = "null";

        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }

        showStopState();

    }


    public void showRunState() {

        if (previousView != null) {
            previousView.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            previousView.findViewById(R.id.pause_btn).setVisibility(View.VISIBLE);
            View imgDone= previousView.findViewById(R.id.done);
            View imgFav= previousView.findViewById(R.id.fav_btn);
            imgFav.animate().translationX(0).setDuration(400).start();
            imgDone.animate().translationX(0).setDuration(400).start();
        }

    }


    public void showLoadingState() {
        previousView.findViewById(R.id.play_btn).setVisibility(View.GONE);
        previousView.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
    }


    public void showStopState() {

        if (previousView != null) {
            previousView.findViewById(R.id.play_btn).setVisibility(View.VISIBLE);
            previousView.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            previousView.findViewById(R.id.pause_btn).setVisibility(View.GONE);
            View imgDone= previousView.findViewById(R.id.done);
            View imgFav= previousView.findViewById(R.id.fav_btn);
            imgDone.animate().translationX(Float.valueOf(""+getResources().getDimension(R.dimen._80sdp))).setDuration(400).start();
            imgFav.animate().translationX(Float.valueOf(""+getResources().getDimension(R.dimen._50sdp))).setDuration(400).start();
        }

        running_sound_id = "none";

    }


    public void downLoadMp3(final String id, final String sound_name, String url) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(" p");
         progressDialog.show();

        prDownloader = PRDownloader.download(url,  getAppFolder(SectionSoundListA.this)+ Variables.APP_HIDED_FOLDER, Variables.SelectedAudio_AAC)
                .build();

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                progressDialog.dismiss();
                Intent output = new Intent();
                output.putExtra("isSelected", "yes");
                output.putExtra("sound_name", sound_name);
                output.putExtra("sound_id", id);
                setResult(RESULT_OK, output);
                finish();
             }

            @Override
            public void onError(Error error) {
                progressDialog.dismiss();

            }


        });

    }


    private void callApiForFavSound(final int pos, final DataXXXX item) {



    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            showLoadingState();
        } else if (playbackState == Player.STATE_READY) {
            showRunState();
        } else if (playbackState == Player.STATE_ENDED) {
            showStopState();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                SectionSoundListA.super.onBackPressed();
                break;
        }
    }


}