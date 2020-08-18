package com.example.arspapp_ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.arspapp_ui.signup.context
import com.google.ar.core.Session
import com.google.ar.core.SharedCamera
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import java.util.*
import java.util.concurrent.TimeUnit


class test35 : AppCompatActivity() {
    private var scene: Scene? = null
    private var camera: Camera? = null
    private var bulletRenderable: ModelRenderable? = null
    private var shouldStartTimer = true
    private var balloonsLeft = 20
    private var point: Point? = null
    private var balloonsLeftTxt: TextView? = null
    private  var sharedSession: Session?=null
    private var sharedCamera: SharedCamera?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        point = Point()
        display.getRealSize(point)
        setContentView(R.layout.activity_test35)
        balloonsLeftTxt = findViewById(R.id.balloonsCntTxt)
        val arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as CustomArFragment?
        scene = arFragment!!.arSceneView.scene
        camera = scene!!.getCamera()
      //  sharedSession = Session(context, EnumSet.of(Session.Feature.SHARED_CAMERA))
        //openCameraForSharing()
       // createCameraCaptureSession()
        addBalloonsToScene()
        buildBulletModel()

        val shoot = findViewById<Button>(R.id.shootButton)
        shoot.setOnClickListener { v: View? ->
            if (shouldStartTimer) {
                startTimer()
                shouldStartTimer = false
            }
            shoot()
        }
    }
    private fun openCameraForSharing() {
        //sharedCamera = sharedSession!!.sharedCamera
        // Use callback wrapper.
        //openCamera()
    }

   /* private fun openCamera() {
        sharedCamera!!.createARDeviceStateCallback(
                    appDeviceStateCallback, appHandler)
        setUpCameraOutputs()
        val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // Wait for camera to open - 2.5 seconds is sufficient
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(cameraId!!, stateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(PosenetActivity.TAG, e.toString())
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }
    }
*/

    private fun shoot() {
        val ray = camera!!.screenPointToRay(point!!.x / 2f, point!!.y / 2f)
        val node = Node()
        node.renderable = bulletRenderable
        scene!!.addChild(node)
        Thread(Runnable {
            for (i in 0..199) {
                runOnUiThread {
                    val vector3 = ray.getPoint(i * 0.1f)
                    node.worldPosition = vector3
                    val nodeInContact = scene!!.overlapTest(node)
                    if (nodeInContact != null) {
                        balloonsLeft--
                        balloonsLeftTxt!!.text = "Balloons Left: $balloonsLeft"
                        scene!!.removeChild(nodeInContact)
                    }
                }
                try {
                    Thread.sleep(10)
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
                val minutesPassed = seconds / 60
                val secondsPassed = seconds % 60
                runOnUiThread { timer.text = "$minutesPassed:$secondsPassed" }
            }
        }).start()
    }

    private fun buildBulletModel() {
        Texture
                .builder()
                .setSource(this, R.drawable.a)
                .build()
                .thenAccept { texture: Texture? ->
                    MaterialFactory
                            .makeOpaqueWithTexture(this, texture)
                            .thenAccept { material: Material? ->
                                bulletRenderable = ShapeFactory
                                        .makeSphere(0.01f,
                                                Vector3(0f, 0f, 0f),
                                                material)
                            }
                }
    }

    private fun addBalloonsToScene() {
        ModelRenderable
                .builder()
                .setSource(this, R.raw.hurdle)
                .build()
                .thenAccept { renderable: ModelRenderable? ->
                    for (i in 0..19) {
                        val node = Node()
                        node.renderable = renderable
                        scene!!.addChild(node)
                        val random = Random()
                        val x = random.nextInt(10)
                        var z = random.nextInt(10)
                        val y = random.nextInt(20)
                        z = -z
                        node.worldPosition = Vector3(
                                x.toFloat(),
                                y / 10f,
                                z.toFloat()
                        )
                    }
                }
    }
}