package com.paulzixuanhugo.todo.userinfo

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.paulzixuanhugo.todo.BuildConfig
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.paulzixuanhugo.todo.network.UserInfo

class UserInfoActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val myFirstName = findViewById<EditText>(R.id.FirstNameInput)
            myFirstName.setText(userInfo.firstName)
            val myLastName = findViewById<EditText>(R.id.LastNameInput)
            myLastName.setText(userInfo.lastName)
            val myeMail = findViewById<EditText>(R.id.emailInput)
            myeMail.setText(userInfo.email)
            val myImage = findViewById<ImageView>(R.id.image_view)
            myImage?.load(userInfo.avatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userinfo)

        val takePictureButton = findViewById<Button>(R.id.take_picture_button)
        takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        val pickPictureButton = findViewById<Button>(R.id.upload_image_button)
        pickPictureButton.setOnClickListener {
            pickInGallery.launch("image/*")
        }

        val valider = findViewById<Button>(R.id.validerUI)
        valider.setOnClickListener {

            val myFirstName = findViewById<EditText>(R.id.FirstNameInput)
            val myLastName = findViewById<EditText>(R.id.LastNameInput)
            val myeMail = findViewById<EditText>(R.id.emailInput)
            val myImage = findViewById<ImageView>(R.id.image_view)

            val newUserInfo = UserInfo(
                    email = myeMail.text.toString(),
                    firstName = myFirstName.text.toString(),
                    lastName = myLastName.text.toString()
            )
            lifecycleScope.launch {
                tasksWebService.update(newUserInfo)
            }

            finish()
        }
    }

    private val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) openCamera()
                else showExplanationDialog()
            }

    private fun requestCameraPermission() =
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            handleImage(photoUri)
        } else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // convert
    private fun convert(uri: Uri) =
            MultipartBody.Part.createFormData(
                    name = "avatar",
                    filename = "temp.jpeg",
                    body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
            )

    private fun handleImage(photoUri: Uri) {
        lifecycleScope.launch {
            Api.INSTANCE.tasksWebService.updateAvatar(convert(photoUri))
            val myImage = findViewById<ImageView>(R.id.image_view)
            myImage?.load(photoUri)
        }
    }

    // register
    private val pickInGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { photoUri ->
                handleImage(photoUri)
            }

}