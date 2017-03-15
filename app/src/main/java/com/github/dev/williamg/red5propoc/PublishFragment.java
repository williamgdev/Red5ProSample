package com.github.dev.williamg.red5propoc;


import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.red5pro.streaming.R5Connection;
import com.red5pro.streaming.R5Stream;
import com.red5pro.streaming.R5StreamProtocol;
import com.red5pro.streaming.config.R5Configuration;
import com.red5pro.streaming.media.R5AudioController;
import com.red5pro.streaming.source.R5Camera;
import com.red5pro.streaming.source.R5Microphone;
import com.red5pro.streaming.R5Stream.RecordType;
import com.red5pro.streaming.source.R5Publisher;

public class PublishFragment extends Fragment implements SurfaceHolder.Callback{

    private R5Configuration configuration;
    private Camera camera;
    private boolean isPublishing;
    private R5Stream stream;

    public static PublishFragment newInstance() {
        PublishFragment fragment = new PublishFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PublishFragment() {
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
        View v = inflater.inflate(R.layout.fragment_publish, container, false);
        return v;
    }

    private void preview() {
//        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        SurfaceView surface = (SurfaceView) getActivity().findViewById(R.id.surfaceView);
        surface.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
//            camera.setPreviewDisplay(surfaceHolder);
//            camera.startPreview();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onResume() {
        super.onResume();
        preview();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button publishButton = (Button) getActivity().findViewById(R.id.publishButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPublishToggle();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isPublishing) {
            onPublishToggle();
        }
    }

    private void onPublishToggle() {
        Button publishButton = (Button) getActivity().findViewById(R.id.publishButton);
        if(isPublishing) {
            stop();
        }
        else {
            start();
        }
        isPublishing = !isPublishing;
        publishButton.setText(isPublishing ? "stop" : "start");
    }

    public void start() {
//        camera.stopPreview();

        stream = new R5Stream(new R5Connection(configuration));
        stream.setView((SurfaceView) getActivity().findViewById(R.id.surfaceView));

//        R5Camera r5Camera = new R5Camera(camera, 320, 240);
        R5Microphone r5Microphone = new R5Microphone();


        R5AudioController audioController = new R5AudioController();
        R5Publisher publisher = new R5Publisher(stream);

        audioController.StartRecording(stream, publisher);

//        stream.attachCamera(r5Camera);
        stream.attachMic(r5Microphone);

        stream.publish("Stream", RecordType.Live);

//        camera.startPreview();
    }

    public void stop() {
        if(stream != null) {
            stream.stop();
//            camera.startPreview();
        }
    }

}
