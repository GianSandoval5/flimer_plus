package com.gs.flimer

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import io.flutter.plugin.common.PluginRegistry
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/** FlimerPlugin */
class FlimerPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
  private lateinit var channel : MethodChannel
  private var activity: Activity? = null
  private var result: Result? = null
  private var imageUri: Uri? = null

  companion object {
    private const val PICK_IMAGE_REQUEST = 1
    private const val PICK_IMAGES_REQUEST = 2
    private const val CAMERA_REQUEST = 3
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flimer")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    this.result = result
    
    when (call.method) {
      "pickImage" -> {
        val source = call.argument<String>("source") ?: "gallery"
        val maxWidth = call.argument<Double>("maxWidth")
        val maxHeight = call.argument<Double>("maxHeight")
        val imageQuality = call.argument<Int>("imageQuality")
        
        if (source == "camera") {
          openCamera()
        } else {
          openGallery(false)
        }
      }
      "pickImages" -> {
        val maxWidth = call.argument<Double>("maxWidth")
        val maxHeight = call.argument<Double>("maxHeight")
        val imageQuality = call.argument<Int>("imageQuality")
        
        openGallery(true)
      }
      else -> {
        result.notImplemented()
      }
    }
  }

  private fun openGallery(multiple: Boolean) {
    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
      type = "image/*"
      if (multiple) {
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
      }
    }
    
    val requestCode = if (multiple) PICK_IMAGES_REQUEST else PICK_IMAGE_REQUEST
    activity?.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode)
  }

  private fun openCamera() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    
    // Create a temporary file for the image
    try {
      val imageFile = createImageFile()
      imageUri = FileProvider.getUriForFile(
        activity!!,
        "${activity!!.packageName}.fileprovider",
        imageFile
      )
      intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
      activity?.startActivityForResult(intent, CAMERA_REQUEST)
    } catch (ex: IOException) {
      result?.error("CAMERA_ERROR", "Failed to create image file", null)
    }
  }

  @Throws(IOException::class)
  private fun createImageFile(): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File = activity!!.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)!!
    return File.createTempFile(
      "JPEG_${timeStamp}_",
      ".jpg",
      storageDir
    )
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
    if (resultCode != Activity.RESULT_OK) {
      result?.success(null)
      return true
    }

    when (requestCode) {
      PICK_IMAGE_REQUEST -> {
        data?.data?.let { uri ->
          val path = getPathFromUri(uri)
          result?.success(mapOf("path" to path))
        } ?: result?.success(null)
      }
      PICK_IMAGES_REQUEST -> {
        val imagesList = mutableListOf<Map<String, String>>()
        
        if (data?.clipData != null) {
          // Multiple images selected
          val clipData = data.clipData!!
          for (i in 0 until clipData.itemCount) {
            val uri = clipData.getItemAt(i).uri
            val path = getPathFromUri(uri)
            if (path != null) {
              imagesList.add(mapOf("path" to path))
            }
          }
        } else if (data?.data != null) {
          // Single image selected
          val path = getPathFromUri(data.data!!)
          if (path != null) {
            imagesList.add(mapOf("path" to path))
          }
        }
        
        result?.success(imagesList)
      }
      CAMERA_REQUEST -> {
        imageUri?.let { uri ->
          val path = getPathFromUri(uri)
          result?.success(mapOf("path" to path))
        } ?: result?.success(null)
      }
    }
    
    return true
  }

  private fun getPathFromUri(uri: Uri): String? {
    return try {
      val scheme = uri.scheme
      if (scheme == "file") {
        uri.path
      } else {
        // For content:// URIs, we'll return the URI string itself
        // The Flutter side should handle this appropriately
        uri.toString()
      }
    } catch (e: Exception) {
      null
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addActivityResultListener(this)
  }

  override fun onDetachedFromActivity() {
    activity = null
  }
}
