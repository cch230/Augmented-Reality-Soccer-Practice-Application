package org.tensorflow.lite.examples.posenet

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*

class MyFirstArActivity : AppCompatActivity() {
    private var scene: Scene? = null
    private var camera: Camera? = null
    private var bulletRenderable: ModelRenderable? = null
    private var shouldStartTimer = true
    private var balloonsLeft = 20
    private var point: Point? = null
    private var balloonsLeftTxt: TextView? = null
    private var count = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        point = Point()
        display.getRealSize(point)
        setContentView(R.layout.phy_actibity)
        balloonsLeftTxt = findViewById(R.id.balloonsCntTxt)
        val arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as CustomArFragment?
        scene = arFragment!!.arSceneView.scene
        camera = scene!!.getCamera()
        addBalloonsToScene()
        buildBulletModel()
        if (shouldStartTimer) {
            startTimer()
            shouldStartTimer = false
        }
        val shoot = findViewById<Button>(R.id.shootButton)
        shoot.setOnClickListener { v: View? ->
            shoot()
            count++
        }
        val clickAction: Runnable = object : Runnable {
            override fun run() {
                shoot.performClick()
                shoot.postDelayed(this, 1700)
            }
        }
        // To start it
        shoot.postDelayed(clickAction, 1700)
    }

    private fun shoot() {
        val ray = camera!!.screenPointToRay(point!!.x / 1.1f, point!!.y / 1.3f)
        val ray1 = camera!!.screenPointToRay(point!!.x / 7f, point!!.y / 1.3f)
        val node = Node()
        node.renderable = bulletRenderable
        scene!!.addChild(node)
        Thread(Runnable {
            for (i in 0..199) {
                runOnUiThread {
                    if (count % 2 == 1) {
                        val vector3 = ray.getPoint(i * 0.1f)
                        node.worldPosition = vector3
                    } else {
                        val vector3 = ray1.getPoint(i * 0.1f)
                        node.worldPosition = vector3
                    }
                    val nodeInContact = scene!!.overlapTest(node)
                    if (nodeInContact != null) {
                        balloonsLeft--
                        balloonsLeftTxt!!.text = "Balloons Left: $balloonsLeft"
                        scene!!.removeChild(nodeInContact)
                    }
                }
                try {
                    Thread.sleep(5)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            runOnUiThread { scene!!.removeChild(node) }
        }).start()
    }

    private fun startTimer() {
        val timer = findViewById<TextView>(R.id.timerText)
        Thread(Runnable {
            var seconds = 0
            while (balloonsLeft > 0) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                seconds++
                /*if((seconds%2)==0){
                    shoot();
                    count++;
                }*/
                val minutesPassed = seconds / 60
                val secondsPassed = seconds % 60
                runOnUiThread { timer.text = "$minutesPassed:$secondsPassed" }
            }
        }).start()
    }

    private fun buildBulletModel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Texture
                    .builder()
                    .setSource(this, R.drawable.texture)
                    .build()
                    .thenAccept { texture: Texture? ->
                        MaterialFactory
                                .makeOpaqueWithTexture(this, texture)
                                .thenAccept { material: Material? ->
                                    bulletRenderable = ShapeFactory
                                            .makeSphere(1f,
                                                    Vector3(0f, 0f, 0f),
                                                    material)
                                }
                    }
        }
    }

    private fun addBalloonsToScene() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ModelRenderable
                    .builder()
                    .setSource(this, R.raw.hurdle)
                    .build()
                    .thenAccept { renderable: ModelRenderable? ->
                        val node = Node()
                        node.renderable = renderable
                        scene!!.addChild(node)
                        val ra = camera!!.screenPointToRay(point!!.x / 2f, point!!.y / 1.3f)
                        val vector_jump = ra.getPoint(10f)
                        val q1 = node.localRotation
                        val q2 = Quaternion.axisAngle(Vector3(0f, 400f, 0f), .2f)
                        node.localRotation = Quaternion.multiply(q1, q2)
                        node.worldPosition = vector_jump
                    }
        }
    }
}