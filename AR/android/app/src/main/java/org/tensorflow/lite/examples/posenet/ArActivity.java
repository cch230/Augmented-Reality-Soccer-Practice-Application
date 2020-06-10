/*
package org.tensorflow.lite.examples.posenet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ArActivity extends AppCompatActivity {
    //private CustomArFragment customArFragment;
    //private ArFragment arFragment;
    private VideoRecorder videoRecorder;
    private ModelRenderable modelRenderable;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tfe_pn_activity_posenet);

//        customArFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arfragment);
        ArFragment customArFragment =(ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        ModelRenderable
                .builder()
                .setSource(this, Uri.parse("cone.sfb"))
                .build()
                .thenAccept(renderable->{
                    modelRenderable = renderable;
                    Node node = new Node();
                    node.setParent(customArFragment.getArSceneView().getScene());
                    node.setRenderable(renderable);
                    Node node1 = new Node();
                    node1.setParent(customArFragment.getArSceneView().getScene());
                    node1.setRenderable(renderable);
                    customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
                        Camera camera = customArFragment.getArSceneView().getScene().getCamera();
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size=new Point();
                        display.getSize(size);
                        Ray ray1 = camera.screenPointToRay(size.x,size.y);
                        Ray ray2 = camera.screenPointToRay(500,size.y);

                        Vector3 newPosition = ray1.getPoint(3f);
                        Vector3 newPosition1 = ray2.getPoint(3f);
                        node.setLocalPosition(newPosition);
                        node1.setLocalPosition(newPosition1);

                    });

                });


      customArFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
          @Override
          public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
              if (videoRecorder == null) {
                  videoRecorder = new VideoRecorder();
                  videoRecorder.setSceneView(customArFragment.getArSceneView());

                  int orientation = ArActivity.this.getResources().getConfiguration().orientation;
                  videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_HIGH, orientation);
              }
              boolean isRecording = videoRecorder.onToggleRecord();
              if (isRecording)
                  Toast.makeText(ArActivity.this, "Starting", Toast.LENGTH_SHORT).show();
              else
                  Toast.makeText(ArActivity.this, "end", Toast.LENGTH_SHORT).show();

          }
      });
    }

 private void addModelToScene(Anchor anchor, ModelRenderable modelRederable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(customArFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRederable);
        customArFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }
}

*/
