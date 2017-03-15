package com.github.dev.williamg.red5propoc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.media.R5AudioController;
import com.red5pro.streaming.view.R5VideoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {

    private R5Configuration configuration;
    private boolean isSubscribing;
    private R5Stream stream;

    public static SubscribeFragment newInstance() {
        SubscribeFragment fragment = new SubscribeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SubscribeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configuration = new R5Configuration(R5StreamProtocol.RTSP, "192.168.43.212",  8554, "live", 1.0f);
        configuration.setLicenseKey("2WDZ-GOA3-XZJJ-YFZE");
        configuration.setBundleID(getActivity().getPackageName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_subscribe, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button publishButton = (Button) getActivity().findViewById(R.id.subscribeButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubscribeToggle();
            }
        });
    }

    private void onSubscribeToggle() {
        Button subscribeButton = (Button) getActivity().findViewById(R.id.subscribeButton);
        if(isSubscribing) {
            stop();
        }
        else {
            start();
        }
        isSubscribing = !isSubscribing;
        subscribeButton.setText(isSubscribing ? "stop" : "start");
    }

    public void start() {
        R5VideoView videoView = (R5VideoView) getActivity().findViewById(R.id.subscribeView);

        stream = new R5Stream(new R5Connection(configuration));
        videoView.attachStream(stream);
        stream.play("red5prostream");
    }

    public void stop() {
        if(stream != null) {
            stream.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isSubscribing) {
            onSubscribeToggle();
        }
    }

}
