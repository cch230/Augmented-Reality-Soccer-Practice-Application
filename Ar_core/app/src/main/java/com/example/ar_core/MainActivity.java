package com.example.ar_core;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Display;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.collision.Ray;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Scene scene;
    private Camera camera;
    private ModelRenderable bulletRenderable;
    private boolean shouldStartTimer = true;
    private int balloonsLeft = 20;
    private Point point;
    private TextView balloonsLeftTxt;
    private int count=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getRealSize(point);
        setContentView(R.layout.activity_main);
        balloonsLeftTxt = findViewById(R.id.balloonsCntTxt);
        CustomArFragment arFragment =
                (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        scene = arFragment.getArSceneView().getScene();
        camera = scene.getCamera();
        addBalloonsToScene();
        buildBulletModel();
        if (shouldStartTimer) {
            startTimer();
            shouldStartTimer = false;
        }
        Button shoot = findViewById(R.id.shootButton);
        shoot.setOnClickListener(v -> {
            shoot();
            count++;
        });
        Runnable clickAction = new Runnable() {
            @Override
            public void run() {
                shoot.performClick();
                shoot.postDelayed(this, 1700);
            }
        };
// To start it
        shoot.postDelayed(clickAction, 1700);
    }


    private void shoot() {

            Ray ray = camera.screenPointToRay(point.x / 1.1f, point.y / 1.3f);
            Ray ray1 = camera.screenPointToRay(point.x / 7f, point.y / 1.3f);
            Node node = new Node();
            node.setRenderable(bulletRenderable);
            scene.addChild(node);

            new Thread(() -> {
                for (int i = 0; i < 200; i++) {
                    int finalI = i;
                    runOnUiThread(() -> {
                        if ((count % 2) == 1) {
                            Vector3 vector3 = ray.getPoint(finalI * 0.1f);
                            node.setWorldPosition(vector3);
                        } else {
                            Vector3 vector3 = ray1.getPoint(finalI * 0.1f);
                            node.setWorldPosition(vector3);
                        }
                        Node nodeInContact = scene.overlapTest(node);
                        if (nodeInContact != null) {
                            balloonsLeft--;
                            balloonsLeftTxt.setText("Balloons Left: " + balloonsLeft);
                            scene.removeChild(nodeInContact);
                        }
                    });
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(() -> scene.removeChild(node));
            }).start();
    }

    private void startTimer() {
        TextView timer = findViewById(R.id.timerText);
        new Thread(() -> {
            int seconds = 0;
            while (balloonsLeft > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                seconds++;
                /*if((seconds%2)==0){
                    shoot();
                    count++;
                }*/
                int minutesPassed = seconds / 60;
                int secondsPassed = seconds % 60;
                runOnUiThread(() -> timer.setText(minutesPassed + ":" + secondsPassed));
            }
        }).start();
    }

    private void buildBulletModel() {
        Texture
                .builder()
                .setSource(this, R.drawable.texture)
                .build()
                .thenAccept(texture -> {
                    MaterialFactory
                            .makeOpaqueWithTexture(this, texture)
                            .thenAccept(material -> {
                                bulletRenderable = ShapeFactory
                                        .makeSphere(1f,
                                                new Vector3(0f, 0f, 0f),
                                                material);
                            });
                });
    }

    private void addBalloonsToScene() {

        ModelRenderable
                .builder()
                .setSource(this,R.raw.hurdle)
                .build()
                .thenAccept(renderable -> {
                    Node node = new Node();
                    node.setRenderable(renderable);
                    scene.addChild(node);
                    Ray ra = camera.screenPointToRay(point.x / 2f, point.y / 1.3f);
                    Vector3 vector_jump = ra.getPoint(10f);
                    Quaternion q1 = node.getLocalRotation();
                    Quaternion q2 = Quaternion.axisAngle(new Vector3(0, 400f, 0f), .2f);
                    node.setLocalRotation(Quaternion.multiply(q1, q2));
                    node.setWorldPosition(vector_jump);
                   /* for (int i = 0;i < 20;i++) {

                            Node node = new Node();
                            node.setRenderable(renderable);
                            scene.addChild(node);


                        Random random = new Random();
                        int x = random.nextInt(10);
                        int z = random.nextInt(10);
                        int y = random.nextInt(20);

                        z = -z;

                        node.setWorldPosition(new Vector3(
                                (float) x,
                                (float) y,
                                (float) z
                        ));

                    }
*/
                });

    }
}