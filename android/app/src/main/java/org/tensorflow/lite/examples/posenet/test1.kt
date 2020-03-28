package org.tensorflow.lite.examples.posenet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class test1 :
 Fragment(), CvCameraViewListener2 {

    companion object {
         val TAG = "test1"

        init {
            if (!OpenCVLoader.initDebug()) {
                Log.wtf(TAG, "OpenCV failed to load!")
            }
        }
    }

    private var  surfaceView: JavaCameraView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.tfe_pn_activity_posenet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     surfaceView = view.findViewById(R.id.surfaceView)
        Log.i(TAG, "onViewCreated")
       if (surfaceView != null) {
           surfaceView!!.enableView()
           Log.i(TAG, "loop")

       }

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }
    override fun onDestroy(){
        super.onDestroy()
        if (surfaceView != null) surfaceView!!.disableView()
    }
    override fun onPause() {
        super.onPause()
        if (surfaceView != null) surfaceView!!.disableView()
    }




    override fun onCameraViewStarted(width: Int, height: Int) {}
    override fun onCameraViewStopped() {}
    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        Log.i(TAG, "onCameraFrame")

        val input = inputFrame.gray()
        val circles = Mat()
        Imgproc.blur(input, input, Size(7.0, 7.0), Point(2.0, 2.0))
        Imgproc.HoughCircles(
            input,
            circles,
            Imgproc.CV_HOUGH_GRADIENT,
            2.0,
            200.0,
            100.0,
            90.0,
            30,
            200
        )
        Log.i(
            TAG,
            "size: " + circles.cols() + ", " + circles.rows().toString()
        )
        if (circles.cols() > 0) {
            for (x in 0 until circles.cols().coerceAtMost(1)) {
                val circleVec = circles[0, x] ?: break
                val center =
                    Point(circleVec[0].toInt().toDouble(), circleVec[1].toInt().toDouble())
                val radius = circleVec[2].toInt()
                Imgproc.circle(input, center, 3, Scalar(0.0, 0.0, 255.0), 5)
                Imgproc.circle(input, center, radius, Scalar(255.0, 255.0, 255.0), 2)
            }
        }
        circles.release()
        input.release()
        return inputFrame.rgba()
    }
}
