package com.example.mainfragment

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mainfragment.DBKey.Companion.DB_ARTICLES
import com.example.mainfragment.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat


class SearchFragment : Fragment() {


    lateinit var binding: FragmentSearchBinding

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(inflater,container,false)

        takePhoto()

        upload()
        return binding.root

    }

    fun upload() {
        binding.put.setOnClickListener {
            val text2 = binding.fireprice.text.toString()
            val text4 = binding.fireexplane.text.toString()

            if (realPicture!= null) {
                val photoUri = realPicture ?: return@setOnClickListener
                uploadPhoto(photoUri,
                    successHandler = { url -> // 다운로드 url 을 받아서 처리;
                        uploadrecycler(url,text2,text4)
                    },
                    errorHandler = {
                        Toast.makeText(context, "사진 업로드 실패.", Toast.LENGTH_SHORT)
                            .show()

                    })
            } else {
                // 이미지가 없는 경우 빈 문자열
                uploadrecycler("",text2,text4)

            }


        }
    }
    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        var fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) { // 업로드 과정 완료
                    // 다운로드 url 가져오기
                    storage.reference.child("article/photo").child(fileName).downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }.addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    Log.d("sslee", it.exception.toString())
                    errorHandler()
                }
            }
    }
    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    fun uploadrecycler(text1 : String,text2: String,text4: String) {
        val memo1 = Memo(text1,"${text2}원",text4,System.currentTimeMillis())

        articleDB.push().setValue(memo1)


    }


    companion object {

        var PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                displayCameraFragment()
            }
        }

    private fun takePhoto() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            displayCameraFragment()
        }
        activity?.let {
            if (hasPermissions(activity as Context, PERMISSIONS)) {
                displayCameraFragment()
            } else {
                permReqLauncher.launch(
                    PERMISSIONS
                )
            }
        }
    }

    // util method
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun displayCameraFragment() {
        binding.fiewcamara.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageURI(newFielnamem(),"image/jpg")?.let { Uri ->
            realPicture = Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT,realPicture)
            takePicture.launch(intent)
        }

    }


    fun createImageURI(fielname : String , mimtype : String) : Uri? {
        val value = ContentValues()
        value.put(MediaStore.Images.Media.DISPLAY_NAME,fielname)
        value.put(MediaStore.Images.Media.MIME_TYPE,mimtype)
        return activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
    }

    fun newFielnamem() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val format = sdf.format(System.currentTimeMillis())
        return "${format}.png"
    }

    var realPicture : Uri? = null

    fun loadData(potoUri : Uri) : Bitmap? {
        var images : Bitmap? =null
        try {
            if(Build.VERSION.SDK_INT> Build.VERSION_CODES.O_MR1) {

                val source = ImageDecoder.createSource(activity?.contentResolver!!,potoUri)
                images = ImageDecoder.decodeBitmap(source)
            } else {
                images = MediaStore.Images.Media.getBitmap(activity?.contentResolver,potoUri)
            }
        }catch (e : Exception) {
            e.printStackTrace()
        }
        return images
    }


    val takePicture  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        realPicture?.let { Uri ->
            val bitmap = loadData(Uri)
            binding.fireimage.setImageBitmap(bitmap)


        }

    }


}