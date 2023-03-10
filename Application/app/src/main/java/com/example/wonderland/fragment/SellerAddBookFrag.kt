package com.example.wonderland.fragment


import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.wonderland.R
import com.example.wonderland.activity.Login
import com.example.wonderland.databinding.FragmentSellerAddBookBinding
import com.example.wonderland.model.reponse.AddBookResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SellerAddBookFrag(private val user: String) : Fragment() {
    private lateinit var _binding: FragmentSellerAddBookBinding
    private val binding get() = _binding
    private val requestcode: Int = 1
    private val MY_REQUEST_CODE: Int = 1
    val uri: Uri? = null
    var file: File? = null
    private var mediaPath: String? = null
    private var postPath: String? = null
    val mImageFileLocation: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerAddBookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = context?.let { Cache.getToken(it) }
        if (token === null) {
            val intent_ = Intent(context, Login::class.java)
            startActivity(intent_)
        }
        val categories = resources.getStringArray(R.array.Categories)
        val spinner = binding.spnCategory
        if (spinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, categories
            )
            spinner.adapter = adapter
        }

        binding.btnChoose.setOnClickListener {
            onClickPermission()
        }

        binding.tvRelease.setOnClickListener {
            val selectedYear = 2000
            val selectedMonth = 5
            val selectedDayOfMonth = 10

            val dateSetListener =
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val month=if(monthOfYear + 1<10){
                        "0"+(monthOfYear + 1).toString()
                    }else{
                        (monthOfYear + 1).toString()
                    }
                    val day=if(dayOfMonth<10){
                        "0"+dayOfMonth.toString()
                    }else{
                        dayOfMonth.toString()
                    }
                    binding.tvRelease.setText(
                         day +"/" + month + "/" + year
                    )
                }
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth
            )
            datePickerDialog.show()
        }

        binding.addBook.setOnClickListener(({
            val author=binding.etAuthor.text.toString()
            val des=binding.etDescription.text.toString()
            val dis=binding.etDistributor.text.toString()
            val price=binding.etPrice.text.toString()
            val quan=binding.etQuantity.text.toString()
            val date=binding.tvRelease.text.toString()
            val title=binding.etTitle.text.toString()
            val cate=binding.spnCategory.selectedItem.toString()

            if(author==""||des==""||title==""||dis==""||price==""||quan==""||date==""||file==null||cate==""){
                Toast.makeText(context,"Please enter all the fields!",Toast.LENGTH_SHORT).show()
            }
            else{
            val etTitle: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.etTitle.text.toString()
            )
            val etAuthor: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.etAuthor.text.toString()
            )
            val etDistributor: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.etDistributor.text.toString()
            )
            val user: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), user)
            val etPrice: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.etPrice.text.toString()
            )

            val etCategory: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.spnCategory.selectedItem.toString()
            )
            val etRelease: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.tvRelease.text.toString()
            )
            val etDescription: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.etDescription.text.toString()
            )
            val etQuantity: RequestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                binding.etQuantity.text.toString()
            )
            val etState: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), "1")
            val etStar: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), "0")


            val requestbodyImg: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val multipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", file?.name, requestbodyImg)

            val response = MyAPI.getAPI().addBook(
                token.toString(),
                etTitle,
                etAuthor,
                etDistributor,
                user,
                etPrice,
                etCategory,
                etRelease,
                etDescription,
                etQuantity,
                etState,
                etStar, multipart
            )

            println("TEST")
            response.enqueue(object : Callback<AddBookResponse> {
                override fun onResponse(
                    call: Call<AddBookResponse>,
                    response: Response<AddBookResponse>
                ) {

                    if (response.isSuccessful) {
                        println("SUCCESS")
                        Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show()
                        binding.etAuthor.setText("")
                        binding.etTitle.setText("")
                        binding.etDistributor.setText("")
                        binding.etPrice.setText("")
                        binding.tvRelease.setText("")
                        binding.etDescription.setText("")
                        binding.etQuantity.setText("")
                        binding.ivPicture.setImageResource(0)
                        val viewPaper: ViewPager2 = activity!!.findViewById(R.id.vp_sellerOrderList)
                        viewPaper.setCurrentItem(0)
                    }
                }

                override fun onFailure(call: Call<AddBookResponse>, t: Throwable) {
                    Toast.makeText(context,"Fail!",Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
            }
        }))
    }

    private fun onClickPermission() {

        if (context?.let {
                checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {

            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, MY_REQUEST_CODE)
        }
    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("*/*")
        startActivityForResult(intent, MY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestcode && resultCode == RESULT_OK) {
//            println("!@#$%")
            if (data != null) {

                binding.ivPicture.setImageURI(data.data)
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context?.applicationContext?.contentResolver?.query(
                    selectedImage!!,
                    filePathColumn,
                    null,
                    null,
                    null
                )
                assert(cursor != null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                mediaPath = cursor.getString(columnIndex)
                cursor.close()


                postPath = mediaPath
                file = File(postPath!!)
                println("FILE $file")
            }

        }
    }

}
