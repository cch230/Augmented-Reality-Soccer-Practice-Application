package com.example.ar_01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {

    private  ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arfragment);

        arFragment.setOnTapArPlaneListener((hitResult,plane,motionEvent)-> {
             final Anchor anchor = hitResult.createAnchor();

             ModelRenderable.builder().setSource(this, Uri.parse("cone.sfb"))
                   .build()
                   .thenAccept(modelRederable->addModelToScene(anchor, modelRederable))
                   .exceptionally(throwable->{
                                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                                builder.setMessage(throwable.getMessage()).show();
                                return null;
            });
        });
    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRederable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRederable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }
}

