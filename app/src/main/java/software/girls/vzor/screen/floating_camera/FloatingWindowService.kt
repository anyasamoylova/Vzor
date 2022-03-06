package software.girls.vzor.screen.floating_camera

import android.content.ContentValues
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import software.girls.vzor.R
import software.girls.vzor.databinding.FragmentCameraBinding
import software.girls.vzor.screen.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class FloatingWindowService : LifecycleService() {

    private lateinit var floatView: ViewGroup
    private lateinit var floatWindowLayoutParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowManager: WindowManager

    private lateinit var binding: FragmentCameraBinding
    private var imageCapture: ImageCapture? = null

    override fun onCreate() {
        super.onCreate()

        inflateView()
        initCameraPreview()
        initView()
    }

    private fun inflateView() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        floatView = inflater.inflate(R.layout.fragment_camera, null) as ViewGroup
        binding = FragmentCameraBinding.bind(floatView)

        LAYOUT_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_TOAST
        }
    }

    private fun initCameraPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun initView() {
        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        floatWindowLayoutParams = WindowManager.LayoutParams(
            (width * 0.3f).toInt(),
            (height * 0.3f).toInt(),
            LAYOUT_TYPE ?: WindowManager.LayoutParams.TYPE_TOAST,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        floatWindowLayoutParams.gravity = Gravity.CENTER
        floatWindowLayoutParams.x = 0
        floatWindowLayoutParams.y = 0
        windowManager.addView(floatView, floatWindowLayoutParams)

        binding.btnCloseFloatingWindow.setOnClickListener {
            stopSelf()
            windowManager.removeView(floatView)

            val back = Intent(this, MainActivity::class.java)
            back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            startActivity(back)
        }


        floatView.setOnTouchListener(object : View.OnTouchListener {

            val updatedFloatWindowLayoutParams = floatWindowLayoutParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = updatedFloatWindowLayoutParams.x.toDouble()
                        y = updatedFloatWindowLayoutParams.y.toDouble()

                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()
                    }

                    MotionEvent.ACTION_MOVE -> {
                        updatedFloatWindowLayoutParams.x = (x + event.rawX - px).toInt()
                        updatedFloatWindowLayoutParams.y = (y + event.rawY - py).toInt()

                        windowManager.updateViewLayout(floatView, updatedFloatWindowLayoutParams)
                    }
                }
                return false
            }
        })

        binding.btnStartRecording.setOnClickListener {
            takePhoto(60000, 3)
        }
    }

    private fun takePhoto(freqMs: Long, count: Int) {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat("yyyy.MM.dd.ss.SSSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Vzor_Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        repeat(count) {
            Thread.sleep(freqMs)
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        //TODO
                    }
                }
            )
        }
    }

    companion object {
        private const val TAG = "FloatingWindowService"
    }
}