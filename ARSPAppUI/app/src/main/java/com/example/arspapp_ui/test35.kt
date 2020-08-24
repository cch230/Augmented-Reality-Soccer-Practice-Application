package com.example.arspapp_ui

import android.content.ContentValues
import android.content.res.Configuration
import android.graphics.*
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.CamcorderProfile
import android.media.Image
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.Frame
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Posenet
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Semaphore
import kotlin.math.abs


class test35 : AppCompatActivity() {
    private var scene: Scene? = null
    private var camera: Camera? = null
    private var bulletRenderable: ModelRenderable? = null
    private var shouldStartTimer = true
    private var balloonsLeft = 20
    private var point: Point? = null
    private var balloonsLeftTxt: TextView? = null
    private var paint = Paint()
    private lateinit var posenet: Posenet
    private val minConfidence = 0.5
    private val TAG = "VideoRecorder"
    private val DEFAULT_BITRATE = 10000000
    private val DEFAULT_FRAMERATE = 30

    // recordingVideoFlag is true when the media recorder is capturing video.
    private var recordingVideoFlag = false

    private var mediaRecorder: MediaRecorder? = null
    private var videoSize: Size? = null

    private var sceneView: SceneView? = null
    private var videoCodec = 0
    private var videoDirectory: File? = null
    private var videoBaseName: String? = null
    private var videoPath: File? = null
    private var bitRate = DEFAULT_BITRATE
    private var frameRate = DEFAULT_FRAMERATE
    private var encoderSurface: Surface? = null

    private val FALLBACK_QUALITY_LEVELS = intArrayOf(
            CamcorderProfile.QUALITY_HIGH,
            CamcorderProfile.QUALITY_2160P,
            CamcorderProfile.QUALITY_1080P,
            CamcorderProfile.QUALITY_720P,
            CamcorderProfile.QUALITY_480P
    )

    /** List of body joints that should be connected.    */
    private val bodyJoints = listOf(
            Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
            Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
            Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
            Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
            Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
    )

    private var mNextVideoAbsolutePath: String? = null
    private val DETAIL_PATH = "DCIM/test1/"
    var Cachedir: File?=null

    /** Radius of circle used to draw keypoints.  */
    private val circleRadius = 8.0f



    /** A shape for extracting frame data.   */
    private val PREVIEW_WIDTH = 640
    private val PREVIEW_HEIGHT = 480




    /** ID of the current [CameraDevice].   */
    private var cameraId: String? = null

    /** A [SurfaceView] for camera preview.   */
    private var surfaceView: SurfaceView? = null

    /** A [CameraCaptureSession] for camera preview.   */
    private var captureSession: CameraCaptureSession? = null

    /** A [CameraCaptureSession] for camera preview.   */
    private var previewSession: CameraCaptureSession? = null

    /** A reference to the opened [CameraDevice].    */
    private var cameraDevice: CameraDevice? = null

    /** The [android.util.Size] of camera preview.  */
    private var previewSize: Size? = null

    /** The [android.util.Size.getWidth] of camera preview. */
    private var previewWidth = 0

    /** The [android.util.Size.getHeight] of camera preview.  */
    private var previewHeight = 0

    /** A counter to keep count of total frames.  */
    private var frameCounter = 0

    private var saveframe=-100

    /** An IntArray to save image data in ARGB8888 format  */
    private lateinit var rgbBytes: IntArray

    /** A ByteArray to save image data in YUV format  */
    private var yuvBytes = arrayOfNulls<ByteArray>(3)

    /** An additional thread for running tasks that shouldn't block the UI.   */
    private var backgroundThread: HandlerThread? = null

    /** A [Handler] for running tasks in the background.    */
    private var backgroundHandler: Handler? = null

    /** An [ImageReader] that handles preview frame capture.   */
    private var imageReader: ImageReader? = null

    /** [CaptureRequest.Builder] for the camera preview   */
    private var previewRequestBuilder: CaptureRequest.Builder? = null

    /** [CaptureRequest] generated by [.previewRequestBuilder   */
    private var previewRequest: CaptureRequest? = null

    /** A [Semaphore] to prevent the app from exiting before closing the camera.    */
    private val cameraOpenCloseLock = Semaphore(1)

    /** Whether the current camera device supports Flash or not.    */
    private var flashSupported = false

    /** Orientation of the camera sensor.   */
    private var sensorOrientation: Int? = null

    /** Abstract interface to someone holding a display surface.    */
    private var surfaceHolder: SurfaceHolder? = null

    private var recordingSurface: Surface?=null

    private var mRecorderSurface: Surface?=null

    private var deviceOrientation: DeviceOrientation? = null

    private lateinit var mSensorManager: SensorManager

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    var left_ankle:Point= Point(-1,-1)
    var right_ankle:Point=Point(-1,-1)
    var nose:Point=Point(-1,-1)
    var left_knee:Point=Point(-1,-1)
    var right_knee:Point?=Point(-1,-1)
    var Result_Boundary_Check= 0

    // var tracking = com.example.arspapp_ui.tracking()
    //var test = com.example.arspapp_ui.trapping_tracking()
    var setting_time:Int?=null
    var key_list=java.util.ArrayList<Point>()
    var start_joint_list=java.util.ArrayList<Point>()
    var stop_joint_list=java.util.ArrayList<Point>()
    var startPoint:Point?=null
    var stopPoint:Point?=null
    var angle_sig= 0
    var shoot_check=false
    var min=0
    var trapping_check=false
    // The UI to record.
    private var recordButton: FloatingActionButton? = null

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

        val orientation = resources.configuration.orientation
        setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation)
        setSceneView(arFragment!!.arSceneView)
        recordButton = findViewById(R.id.record)
        recordButton!!.setOnClickListener(View.OnClickListener { unusedView: View? -> toggleRecording(unusedView) })
        recordButton!!.setEnabled(true)
        recordButton!!.setImageResource(R.drawable.round_videocam)
    }

    override fun onPause() {
        if (isRecording()) {
            toggleRecording(null)
        }
        super.onPause()
    }

    /*
     * Used as a handler for onClick, so the signature must match onClickListener.
     */
    private fun toggleRecording(unusedView: View?) {

        val recording = onToggleRecord()
        if (recording) {
            recordButton!!.setImageResource(R.drawable.round_stop)
        } else {
            recordButton!!.setImageResource(R.drawable.round_videocam)
            val videoPath = videoPath!!.absolutePath
            Toast.makeText(this, "Video saved: $videoPath", Toast.LENGTH_SHORT).show()

            // Send  notification of updated content.
            val values = ContentValues()
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video")
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            values.put(MediaStore.Video.Media.DATA, videoPath)
            contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

    /** Set the paint color and size.    */
    private fun setPaint() {
        paint.color = Color.RED
        paint.textSize = 80.0f
        paint.strokeWidth = 8.0f
    }

    private fun setPaint2() {
        paint.color = Color.BLUE
        paint.textSize = 80.0f
        paint.strokeWidth = 8.0f
    }
    private fun setPaint3() {
        paint.color = Color.WHITE
        paint.textSize = 80.0f
        paint.strokeWidth = 15.0f
    }
    private fun setPaint4() {
        paint.color = Color.GREEN
        paint.textSize = 80.0f
        paint.strokeWidth = 15.0f
    }

    private fun draw(canvas: Canvas, person: Person, bitmap: Bitmap) {

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        // Draw `bitmap` and `person` in square canvas.
        val screenWidth: Int
        val screenHeight: Int
        val left: Int
        val right: Int
        val top: Int
        val bottom: Int
        if (canvas.height > canvas.width) {
            screenWidth = canvas.width
            screenHeight = canvas.width
            left = 0
            top = (canvas.height - canvas.width) / 2
        } else {
            screenWidth = canvas.height
            screenHeight = canvas.height
            left = (canvas.width - canvas.height) / 2
            top = 0
        }
        right = left + screenWidth
        bottom = top + screenHeight

        setPaint()
        canvas.drawBitmap(
                bitmap,
                Rect(0, 0, bitmap.width, bitmap.height),
                Rect(left, top, right, bottom),
                paint
        )

        val widthRatio = screenWidth.toFloat() / MODEL_WIDTH
        val heightRatio = screenHeight.toFloat() / MODEL_HEIGHT
        var footkey=0
        var footflag=0
        // Draw key points over the image.
        for (keyPoint in person.keyPoints) {

            if (keyPoint.score > minConfidence) {
                val position = keyPoint.position

                if(footkey==15) {
                    left_ankle=Point(position.x,position.y)
                    if(position.x!=0) Log.i("관절", "왼쪽발목 :"+left_ankle.toString())
                }
                if(footkey==16) {
                    right_ankle=Point(position.x,position.y)
                    if(position.x!=0) Log.i("관절", "오른쪽발목 :"+right_ankle.toString())
                }


                val adjustedX: Float = position.x.toFloat() * widthRatio + left
                val adjustedY: Float = position.y.toFloat() * heightRatio + top
                if(frameCounter==saveframe+1&&angle_sig==0){
                    angle_sig=1
                    key_list!!.add(footflag, Point(position.x,position.y))
                    //Log.i("원",key_list.get(footkey).toString())
                    footflag++
                }

                canvas.drawCircle(adjustedX, adjustedY, circleRadius, paint)
            }
            footkey++
        }

        var bodykey=0
        for (line in bodyJoints) {

            if (
                    (person.keyPoints[line.first.ordinal].score > minConfidence) and
                    (person.keyPoints[line.second.ordinal].score > minConfidence)
            ) {
                if(frameCounter==saveframe+1&&angle_sig==1){
                    angle_sig=2
                    var startX=person.keyPoints[line.first.ordinal].position.x
                    var startY=person.keyPoints[line.first.ordinal].position.y
                    var stopX=person.keyPoints[line.second.ordinal].position.x
                    var stopY=person.keyPoints[line.second.ordinal].position.y
                    startPoint=Point(startX,startY)
                    stopPoint=Point(stopX,stopY)
                    start_joint_list!!.add(bodykey,startPoint!!)
                    stop_joint_list!!.add(bodykey,stopPoint!!)
                    bodykey++
                }else{
                    if(frameCounter==saveframe+1)
                    {
                        var startX=person.keyPoints[line.first.ordinal].position.x
                        var startY=person.keyPoints[line.first.ordinal].position.y
                        var stopX=person.keyPoints[line.second.ordinal].position.x
                        var stopY=person.keyPoints[line.second.ordinal].position.y
                        startPoint=Point(startX,startY)
                        stopPoint=Point(stopX,stopY)
                        start_joint_list!!.add(bodykey,startPoint!!)
                        stop_joint_list!!.add(bodykey,stopPoint!!)
                        bodykey++
                    }
                }
                canvas.drawLine(
                        person.keyPoints[line.first.ordinal].position.x.toFloat() * widthRatio + left,
                        person.keyPoints[line.first.ordinal].position.y.toFloat() * heightRatio + top,
                        person.keyPoints[line.second.ordinal].position.x.toFloat() * widthRatio + left,
                        person.keyPoints[line.second.ordinal].position.y.toFloat() * heightRatio + top,
                        paint
                )
            }
        }
        trapping_check=false
        if(nose!=null&&nose!!.x!=null&&trapping_check==false) {
            var check = 0
            //check = test.BoundaryCheck(nose)
            if (check != 0) {
                trapping_check = true
                Result_Boundary_Check += check
            }
        }
        if(left_knee!=null&&left_knee!!.x!=null&&trapping_check==false) {
            var check = 0
            //check = test.BoundaryCheck(left_knee)
            if (check != 0) {
                trapping_check = true
                Result_Boundary_Check += check
            }
        }
        if(right_knee!=null&&right_knee!!.x!=null&&trapping_check==false) {
            var check = 0
            //check = test.BoundaryCheck(right_knee)
            if (check != 0) {
                trapping_check = true
                Result_Boundary_Check += check
            }
        }
        if(left_ankle!=null&&left_ankle!!.x!=null&&trapping_check==false) {
            var check = 0
            //check = test.BoundaryCheck(left_ankle)
            if (check != 0) {
                trapping_check = true
                Result_Boundary_Check += check
            }
        }
        if(right_ankle!=null&&right_ankle!!.x!=null&&trapping_check==false) {
            var check = 0
            //check = test.BoundaryCheck(right_ankle)
            if (check != 0) {
                trapping_check = true
                Result_Boundary_Check += check
            }
        }
        var resource=this.resources
        var GoalpostImage = BitmapFactory.decodeResource(resource, R.drawable.goalpost1)
        canvas.drawBitmap(GoalpostImage,15.0f,120.0f,paint)
        setPaint4()
        canvas.drawLine(15.0f,180.0f,375.0f,180.0f,paint) //가로
        canvas.drawLine(15.0f,240.0f,375.0f,240.0f,paint)
        canvas.drawLine(135.0f,120.0f,135.0f,300.0f,paint)  //세로
        canvas.drawLine(255.0f,120.0f,255.0f,300.0f,paint)

        setPaint3()
        canvas.drawText(
                "거리: 14.21 m",
                (15.0f * widthRatio+right),
                (30.0f * heightRatio),
                paint
        )
        canvas.drawText(
                "인식 속도: %.2f ms".format(posenet.lastInferenceTimeNanos * 1.0f / 1_000_000),
                (15.0f * widthRatio+right),
                (50.0f * heightRatio ),
                paint
        )

        setPaint3()


        frameCounter++


        // Draw!
        surfaceHolder!!.unlockCanvasAndPost(canvas)

    }

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
    private fun cropBitmap(bitmap: Bitmap): Bitmap {
        val bitmapRatio = bitmap.height.toFloat() / bitmap.width
        val modelInputRatio = MODEL_HEIGHT.toFloat() / MODEL_WIDTH
        var croppedBitmap = bitmap

        // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
        val maxDifference = 1e-5

        // Checks if the bitmap has similar aspect ratio as the required model input.
        when {
            abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
            modelInputRatio < bitmapRatio -> {
                // New image is taller so we are height constrained.
                val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
                croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        (cropHeight / 2).toInt(),
                        bitmap.width,
                        (bitmap.height - cropHeight).toInt()
                )
            }
            else -> {
                val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
                croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        (cropWidth / 2).toInt(),
                        0,
                        (bitmap.width - cropWidth).toInt(),
                        bitmap.height
                )
            }
        }
        return croppedBitmap
    }
    fun VideoRecorder() {
        recordingVideoFlag = false
    }

    fun getVideoPath(): File? {
        return videoPath
    }

    fun setBitRate(bitRate: Int) {
        this.bitRate = bitRate
    }

    fun setFrameRate(frameRate: Int) {
        this.frameRate = frameRate
    }

    fun setSceneView(sceneView: SceneView?) {
        this.sceneView = sceneView
    }

    /**
     * Toggles the state of video recording.
     *
     * @return true if recording is now active.
     */
    fun onToggleRecord(): Boolean {
        if (recordingVideoFlag) {
            stopRecordingVideo()
        } else {
            startRecordingVideo()
        }
        return recordingVideoFlag
    }

    private fun startRecordingVideo() {
        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder()
        }
        try {
            buildFilename()
            setUpMediaRecorder()
        } catch (e: IOException) {
            Log.e(TAG, "Exception setting up recorder", e)
            return
        }
        // We capture images from preview in YUV format.
        imageReader = ImageReader.newInstance(
                videoSize!!.width, videoSize!!.height, 0x1, 20
        )
        imageReader!!.setOnImageAvailableListener(imageAvailableListener, backgroundHandler)
        //sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        // Set up Surface for the MediaRecorder
        encoderSurface = imageReader!!.getSurface();
        previewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)


        previewHeight = previewSize!!.height
        previewWidth = previewSize!!.width

        // Initialize the storage bitmaps once when the resolution is known.
        rgbBytes = IntArray(previewWidth * previewHeight)
        val surfaces: MutableList<Surface> = ArrayList()
        // This is the surface we need to record images for processing.
        recordingSurface = imageReader!!.surface
        // We set up a CaptureRequest.Builder with the output Surface.
       /* previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                CameraDevice.TEMPLATE_PREVIEW
        )*/
        surfaces.add(recordingSurface!!)

       /* previewRequestBuilder!!.addTarget(recordingSurface)
        Log.i(this.TAG,"start")*/

        //surfaces.add(mRecorderSurface!!)
        sceneView!!.startMirroringToSurface(
                encoderSurface , 0, 0, videoSize!!.width, videoSize!!.height)
        recordingVideoFlag = true
    }

    private fun buildFilename() {
        if (videoDirectory == null) {
            videoDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/Sceneform")
        }
        if (videoBaseName == null || videoBaseName!!.isEmpty()) {
            videoBaseName = "Sample"
        }
        videoPath = File(
                videoDirectory, videoBaseName + java.lang.Long.toHexString(System.currentTimeMillis()) + ".mp4")
        val dir = videoPath!!.parentFile
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private fun stopRecordingVideo() {
        // UI
        recordingVideoFlag = false
        if (encoderSurface != null) {
            sceneView!!.stopMirroringToSurface(encoderSurface)
            encoderSurface = null
        }
        // Stop recording
        mediaRecorder!!.stop()
        mediaRecorder!!.reset()
    }

    @Throws(IOException::class)
    private fun setUpMediaRecorder() {
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setOutputFile(videoPath!!.absolutePath)
        mediaRecorder!!.setVideoEncodingBitRate(bitRate)
        mediaRecorder!!.setVideoFrameRate(frameRate)
        mediaRecorder!!.setVideoSize(videoSize!!.width, videoSize!!.height)
        mediaRecorder!!.setVideoEncoder(videoCodec)
        mediaRecorder!!.prepare()
        try {
            mediaRecorder!!.start()
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Exception starting capture: " + e.message, e)
        }
    }

    fun setVideoSize(width: Int, height: Int) {
        videoSize = Size(width, height)
    }

    fun setVideoQuality(quality: Int, orientation: Int) {
        var profile: CamcorderProfile? = null
        if (CamcorderProfile.hasProfile(quality)) {
            profile = CamcorderProfile.get(quality)
        }
        if (profile == null) {
            // Select a quality  that is available on this device.
            for (level in FALLBACK_QUALITY_LEVELS) {
                if (CamcorderProfile.hasProfile(level)) {
                    profile = CamcorderProfile.get(level)
                    break
                }
            }
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoSize(profile!!.videoFrameWidth, profile.videoFrameHeight)
        } else {
            setVideoSize(profile!!.videoFrameHeight, profile.videoFrameWidth)
        }
        setVideoCodec(profile.videoCodec)
        setBitRate(profile.videoBitRate)
        setFrameRate(profile.videoFrameRate)
    }

    fun setVideoCodec(videoCodec: Int) {
        this.videoCodec = videoCodec
    }

    fun isRecording(): Boolean {
        return recordingVideoFlag
    }

    private var imageAvailableListener = object : ImageReader.OnImageAvailableListener {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onImageAvailable(imageReader: ImageReader) {
            // We need wait until we have some size from onPreviewSizeChosen
            if (previewWidth == 0 || previewHeight == 0) {
                return
            }

            val image = imageReader.acquireLatestImage() ?: return
            //fillBytes(image.planes, yuvBytes)

           /* ImageUtils.convertYUV420ToARGB8888(
                    yuvBytes[0]!!,
                    yuvBytes[1]!!,
                    yuvBytes[2]!!,
                    previewWidth,
                    previewHeight,
                    *//*yRowStride=*//* image.planes[0].rowStride,
                    *//*uvRowStride=*//* image.planes[1].rowStride,
                    *//*uvPixelStride=*//* image.planes[1].pixelStride,
                    rgbBytes
            )*/

            // Create bitmap from int array
            val imageBitmap = Bitmap.createBitmap(
                    rgbBytes, previewWidth, previewHeight,
                    Bitmap.Config.ARGB_8888
            )

            // Create rotated version for portrait display
            val rotateMatrix = Matrix()
            rotateMatrix.postRotate(90.0f)

            val rotatedBitmap = Bitmap.createBitmap(
                    imageBitmap, 0, 0, previewWidth, previewHeight,
                    rotateMatrix, true
            )
            image.close()
            // Process an image for analysis in every 3 frames.


            processImage(imageBitmap)

        }
    }
    private fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
        // Row stride is the total number of bytes occupied in memory by a row of an image.
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (i in planes.indices) {
            val buffer = planes[i].buffer
            if (yuvBytes[i] == null) {
                yuvBytes[i] = ByteArray(buffer.capacity())
            }
            buffer.get(yuvBytes[i]!!)
        }
    }
    private fun processImage(bitmap: Bitmap) {
        // Crop bitmap.

        val croppedBitmap = cropBitmap(bitmap)
        // Created scaled version of bitmap for model input.
        val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, MODEL_WIDTH, MODEL_HEIGHT, true)

        // Perform inference.
        val person = posenet.estimateSinglePose(scaledBitmap)
        val canvas: Canvas = surfaceHolder!!.lockCanvas()
        draw(canvas, person, scaledBitmap)

    }
}
