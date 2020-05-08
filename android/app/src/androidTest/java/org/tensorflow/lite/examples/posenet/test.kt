package org.tensorflow.lite.examples.posenet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.JavaCameraView
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class MainActivity : AppCompatActivity(), CvCameraViewListener2 {
    companion object {
        const val TAG = "src"

        init {
            if (!OpenCVLoader.initDebug()) {
                Log.wtf(TAG, "OpenCV failed to load!")
            }
        }
    }

    private var cameraView: JavaCameraView? = null
    private val loaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    cameraView!!.enableView()
                }
                else -> super.onManagerConnected(status)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cameraView = findViewById<View>(R.id.cameraview) as JavaCameraView
        cameraView!!.setCvCameraViewListener(this)
        //cameraView.setMaxFrameSize(1280, 720);
    }

    override fun onResume() {
        super.onResume()
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback)
    }

    override fun onPause() {
        super.onPause()
        if (cameraView != null) cameraView!!.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {}
    override fun onCameraViewStopped() {}
    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        val input = inputFrame.gray()
        val circles = Mat()
        Imgproc.blur(input, input, Size(7, 7), Point(2, 2))
        Imgproc.HoughCircles(
            input,
            circles,
            Imgproc.CV_HOUGH_GRADIENT,
            2.0,
            100.0,
            100.0,
            90.0,
            0,
            1000
        )
        Log.i(
            TAG,
            "size: " + circles.cols() + ", " + circles.rows().toString()
        )
        if (circles.cols() > 0) {
            for (x in 0 until Math.min(circles.cols(), 5)) {
                val circleVec = circles[0, x] ?: break
                val center =
                    Point(circleVec[0].toInt(), circleVec[1].toInt())
                val radius = circleVec[2].toInt()
                Imgproc.circle(input, center, 3, Scalar(255, 255, 255), 5)
                Imgproc.circle(input, center, radius, Scalar(255, 255, 255), 2)
            }
        }
        circles.release()
        input.release()
        return inputFrame.rgba()
    }
}