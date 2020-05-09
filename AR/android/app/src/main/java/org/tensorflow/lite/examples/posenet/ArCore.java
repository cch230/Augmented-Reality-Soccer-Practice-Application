package org.tensorflow.lite.examples.posenet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.CameraConfig;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ArCore extends AppCompatActivity {

    private ArFragment arFragment;
    private VideoRecorder videoRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tfe_pn_activity_posenet);


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.surfaceView);

        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            final Anchor anchor = hitResult.createAnchor();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ModelRenderable.builder().setSource(this, Uri.parse("cone.sfb"))
                        .build()
                        .thenAccept(modelRederable -> addModelToScene(anchor, modelRederable))
                        .exceptionally(throwable -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(throwable.getMessage()).show();
                            return null;
                        });
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(view ->
        {
            if (videoRecorder == null) {
                videoRecorder = new VideoRecorder();
                videoRecorder.setSceneView(arFragment.getArSceneView());
                int orientation = getResources().getConfiguration().orientation;
                videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);
            }
            boolean isRecording = videoRecorder.onToggleRecord();

            if (isRecording)
                Toast.makeText(this, "Starting", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Stopping", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public void onResume(){
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
    private void addModelToScene (Anchor anchor, ModelRenderable modelRenderable){
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }
}
