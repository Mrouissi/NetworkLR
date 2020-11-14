package fr.istic.mob.networkLR

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import fr.istic.mob.networkLR.data.Connection
import fr.istic.mob.networkLR.data.Graph
import fr.istic.mob.networkLR.data.SmartObject
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.simpleName
        var mGraph = Graph("", arrayListOf(), arrayListOf())
        var objectId = 1

    }

    var editMode = false
    var StartTouchTime: Long = 0
    var moveMode = false
    var writeMode = false
    var Xpos = 0
    var addConnection = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

    }

    private fun initViews() {
        var startX = 0
        var startY = 0
        var downX = 0
        var downY = 0

        activity_main_custom_view.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_MOVE) {
                    startX = event.x.toInt()
                    startY = event.y.toInt()
                    if (moveMode) {
                        var currentSmartObject = mGraph.smartObject?.find { smartObject ->
                            smartObject.positionX == startX || smartObject.positionX <= startX + 45
                        }
                        currentSmartObject?.let {
                            it.positionX = startX
                            it.positionY = startY
                            currentSmartObject = null
                        }
                        activity_main_custom_view.invalidate()
                    }
                    return true
                }
                if (event.action == MotionEvent.ACTION_DOWN) {

                    downX = event.x.toInt()
                    downY = event.y.toInt()
                    Log.e("down", startX.toString() + "===" + startY.toString())
                    StartTouchTime = System.currentTimeMillis()

                    return true
                }
                if (event.action == MotionEvent.ACTION_UP) {
                    val endX = event.x.toInt()
                    val endY = event.y.toInt()
                    Log.e("ACTION_UP", endX.toString() + "===" + endY.toString())

                    val time = System.currentTimeMillis() - StartTouchTime
                    if (addConnection) {
                        val firstSmartObject = mGraph.smartObject?.find { smartObject ->
                            smartObject.positionX == downX || smartObject.positionX <= downX + 45
                        }
                        val secondSmartObject = mGraph.smartObject?.find { smartObject ->
                            ((smartObject.positionX == endX || smartObject.positionX <= endX + 45)
                                    && smartObject != firstSmartObject)
                        }
                        if (firstSmartObject != null && secondSmartObject != null) {
                            val rnd = Random()
                            val color: Int = Color.argb(
                                255,
                                rnd.nextInt(256),
                                rnd.nextInt(256),
                                rnd.nextInt(256)
                            )
                            val ep = 15f
                            var name = ""
                            var editText = EditText(v?.context)
                            var alertDialog = v?.context?.let {
                                AlertDialog.Builder(it)
                                    .setTitle(getString(R.string.name))
                                    .setMessage(getString(R.string.put_con))
                                    .setView(editText)
                                    .setPositiveButton(
                                        "ok",
                                        DialogInterface.OnClickListener { dialogInterface, i ->
                                            var mGraphList: ArrayList<Graph> = Paper.book().read(
                                                "graphs",
                                                ArrayList()
                                            )
                                            val rnd = Random()
                                            val color: Int = Color.argb(
                                                255,
                                                rnd.nextInt(256),
                                                rnd.nextInt(256),
                                                rnd.nextInt(256)
                                            )
                                            Log.e("createx", "$endX----$endY")
                                            name = editText.text.toString()
                                            mGraph.connection?.add(
                                                Connection(
                                                    firstSmartObject,
                                                    secondSmartObject,
                                                    color,
                                                    name,
                                                    ep
                                                )
                                            )
                                            Paper.book().write("graphs", mGraphList)
                                        })
                                    .setNegativeButton(
                                        getString(R.string.cancel),
                                        DialogInterface.OnClickListener { dialogInterface, i ->
                                            null
                                        })
                                    .create()
                            }
                            alertDialog?.show()

                            activity_main_custom_view.invalidate()

                        }
                        Xpos = downX

                        // ADD CONDITION TO CONTROL DELETE CONNEXION
                        if (editMode) {
                            val canvas = findViewById<View>(R.id.activity_main_custom_view)
                            registerForContextMenu(canvas);
                            openContextMenu(canvas);
                        }


                    }
                    if (time > 1000) {
                        if (writeMode) {


                            val SmartObject = mGraph.smartObject?.find { smartObject ->
                                smartObject.positionX == downX || smartObject.positionX <= downX + 45
                            }
                            if (SmartObject == null) {
                                var name = ""
                                var editText = EditText(v?.context)
                                var alertDialog = v?.context?.let {
                                    AlertDialog.Builder(it)
                                        .setTitle(getString(R.string.name))
                                        .setMessage(getString(R.string.name_graph))
                                        .setView(editText)
                                        .setPositiveButton(
                                            "ok",
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                var mGraphList: ArrayList<Graph> =
                                                    Paper.book().read(
                                                        "graphs",
                                                        ArrayList()
                                                    )
                                                val rnd = Random()
                                                val color: Int = Color.argb(
                                                    255,
                                                    rnd.nextInt(256),
                                                    rnd.nextInt(256),
                                                    rnd.nextInt(256)
                                                )
                                                Log.e("createx", "$endX----$endY")
                                                name = editText.text.toString()
                                                mGraph.smartObject?.add(
                                                    SmartObject(
                                                        objectId++.toString(),
                                                        null,
                                                        color,
                                                        endX,
                                                        endY,
                                                        name
                                                    )
                                                )
                                                Paper.book().write("graphs", mGraphList)
                                            })
                                        .setNegativeButton(
                                            getString(R.string.cancel),
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                null
                                            })
                                        .create()
                                }
                                alertDialog?.show()


                            }
                        } else {
                            if (editMode) {
                                Xpos = downX

                                val SmartObject = mGraph.smartObject?.find { smartObject ->
                                    smartObject.positionX == downX || smartObject.positionX <= downX + 45
                                }
                                if (SmartObject != null) {
                                    val canvas = findViewById<View>(R.id.activity_main_custom_view)
                                    registerForContextMenu(canvas);
                                    openContextMenu(canvas);
                                }

                            }

                            activity_main_custom_view.invalidate()
                        }


                    }
                    return true
                }



                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val title = item.title

        Log.i(TAG, "$title Clicked")
        if (id == R.id.action_init_network) {
            mGraph.smartObject?.clear()
            mGraph.connection?.clear()
            Toast.makeText(this, getString(R.string.reset), Toast.LENGTH_LONG).show()
            objectId = 0
            activity_main_custom_view.invalidate()
            return true
        }
        if (id == R.id.edit_network) {
            editMode = !editMode
            if (editMode) {
                writeMode = false
                Toast.makeText(this, getString(R.string.edit), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.edit_dis), Toast.LENGTH_LONG)
                    .show()

            }
            return true
        }
        if (id == R.id.action_add_object) {
            writeMode = !writeMode
            if (writeMode) {
                moveMode = false
                editMode = false

                addConnection = false
                Toast.makeText(this, getString(R.string.add_obj), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.add_obj_dis), Toast.LENGTH_LONG).show()

            }
            return true
        }
        if (id == R.id.action_add_connection) {
            addConnection = !addConnection
            if (addConnection) {
                moveMode = false
                editMode = false
                writeMode = false
                Toast.makeText(this, getString(R.string.add_con), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.add_con_dis), Toast.LENGTH_LONG).show()

            }
            return true


        }
        if (id == R.id.action_upload) {
            selectImageInAlbum()
            Toast.makeText(this, getString(R.string.upload), Toast.LENGTH_LONG).show()


            return true


        }
        if (id == R.id.action_save) {
            var editText = EditText(this)
            var alertDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.name))
                .setMessage(getString(R.string.put_text))
                .setView(editText)
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                    var mGraphList: ArrayList<Graph> = Paper.book().read("graphs", ArrayList())
                    val graph =
                        Graph(editText.text.toString(), mGraph.smartObject, mGraph.connection)
                    mGraphList.add(graph)
                    Paper.book().write("graphs", mGraphList)
                })
                .setNegativeButton(
                    getString(R.string.cancel),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        null
                    })
                .create()
            alertDialog.show()
            Toast.makeText(this, getString(R.string.net_save), Toast.LENGTH_LONG).show()

            return true


        }
        if (id == R.id.upload_network) {
            var spinner = Spinner(this)
            var alertDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.name))
                .setMessage(getString(R.string.put_text))
                .setView(spinner)
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .setNegativeButton(
                    getString(R.string.cancel),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        null
                    })
                .create()
            var graphObject = arrayListOf<Graph>()
            try {
                graphObject = Paper.book().read("graphs")
            } catch (e: Exception) {

            }


            val dataAdapter: ArrayAdapter<Graph> = ArrayAdapter<Graph>(
                this,
                android.R.layout.simple_spinner_item, graphObject
            )

            spinner.setAdapter(dataAdapter)
            spinner.isSelected = false
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    dataAdapter.getItem(position)?.let {
                        mGraph = it
                        activity_main_custom_view.invalidate()
                    }
                }

            }
            alertDialog.show()
            return true
        }
        if (id == R.id.send_mail) {
            var screen = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            var displayMetrics = DisplayMetrics()
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
            var height = displayMetrics.heightPixels
            var width = displayMetrics.widthPixels

            screen = takeScreenshotOfView(
                window.decorView.findViewById(android.R.id.content),
                height,
                width
            );
            // Create the file directory

            try {
                val cachePath: File = File(applicationContext.getCacheDir(), "images")
                cachePath.mkdirs() // don't forget to make the directory
                val stream =
                    FileOutputStream("$cachePath/image.png") // overwrites this image every time
                screen.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val imagePath: File = File(this.getCacheDir(), "images")
            val newFile = File(imagePath, "image.png")
            val contentUri =
                FileProvider.getUriForFile(this, "fr.istic.mob.networkLR.fileprovider", newFile)

            sendEmail("mariem.rouiissi@gmail.com", "test", contentUri)
            return true
        }

        if (id == R.id.action_move) {
            moveMode = !moveMode
            if (moveMode) {
                writeMode = false
                addConnection = false
                Toast.makeText(this, getString(R.string.move), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.move_dis), Toast.LENGTH_LONG).show()

            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_list, menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item!!.itemId) {
            R.id.delete -> {
                if (addConnection) {

                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            println("connection is $i")
                            mGraph.connection?.removeAt(i)
                            activity_main_custom_view.invalidate()
                        }
                    }
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.con_del),
                        Toast.LENGTH_LONG
                    )
                        .show()


                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            println("position is $i")
                            mGraph.smartObject?.removeAt(i)
                            objectId--
                            activity_main_custom_view.invalidate()

                        }
                    }
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.obj_del),
                        Toast.LENGTH_LONG
                    ).show()

                }

                return true
            }
            R.id.red_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = RED
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = RED
                            activity_main_custom_view.invalidate()

                        }
                    }
                }

                return true
            }
            R.id.black_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = BLACK
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = BLACK
                            activity_main_custom_view.invalidate()

                        }
                    }
                }
                return true
            }
            R.id.green_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = GREEN
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = GREEN
                            activity_main_custom_view.invalidate()

                        }
                    }
                }
                return true
            }
            R.id.blue_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = BLUE
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = BLUE
                            activity_main_custom_view.invalidate()
                        }
                    }
                }
                return true
            }
            R.id.orange_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = Color.rgb(255, 165, 0)
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = Color.rgb(255, 165, 0)
                            activity_main_custom_view.invalidate()

                        }
                    }
                }
                return true
            }
            R.id.cyan_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = Color.rgb(0, 255, 255)
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = Color.rgb(0, 255, 255)
                            activity_main_custom_view.invalidate()

                        }
                    }
                }
                return true
            }
            R.id.magenta_item -> {
                if (addConnection) {
                    mGraph.connection?.forEachIndexed { i, element ->

                        if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                            element.color = Color.rgb(255, 0, 255)
                            activity_main_custom_view.invalidate()

                        }
                    }
                } else {
                    mGraph.smartObject?.forEachIndexed { i, element ->
                        if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                            element.color = Color.rgb(255, 0, 255)
                            activity_main_custom_view.invalidate()

                        }
                    }
                }
                return true
            }
            R.id.edit_name -> {
                var editText = EditText(this)
                var alertDialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.name))
                    .setMessage(getString(R.string.put_text))
                    .setView(editText)
                    .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                        if (addConnection) {
                            mGraph.connection?.forEachIndexed { i, element ->

                                if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                                    element.name = editText.text.toString()
                                    activity_main_custom_view.invalidate()

                                }
                            }
                        }
                    })
                    .setNegativeButton(
                        getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            null
                        })
                    .create()
                alertDialog.show()
                return true
            }

            R.id.thickness -> {

                var editText = EditText(this)
                var alertDialog = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.name))
                    .setMessage(getString(R.string.put_text))
                    .setView(editText)
                    .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                        if (addConnection) {
                            mGraph.connection?.forEachIndexed { i, element ->

                                if (element.startSmartObject.positionX == Xpos || element.startSmartObject.positionX >= Xpos + 45 || element.startSmartObject.positionX <= Xpos + 45) {
                                    element.episseur = editText.text.toString().toFloat()
                                    element.episseur = editText.text.toString().toFloat()
                                    activity_main_custom_view.invalidate()

                                }
                            }
                        }
                    })
                    .setNegativeButton(
                        getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            null
                        })
                    .create()
                alertDialog.show()
                return true
            }
            R.id.bed_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_bed"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.bath_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_bath"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.tel_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_tel"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.tn_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_tn"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.div_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_div"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.desk_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_desk"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.dt_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_dt"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.table_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_table"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }
            R.id.bas_ic -> {

                mGraph.smartObject?.forEachIndexed { i, element ->
                    if (element.positionX == Xpos || element.positionX <= Xpos + 45) {
                        element.fileName = "ic_basin"
                        activity_main_custom_view.invalidate()
                    }
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendEmail(recipient: String, subject: String, screen: Uri) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        mIntent.setDataAndType(screen, getContentResolver().getType(screen));
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_STREAM, screen);

        startActivity(Intent.createChooser(mIntent, "Send your email in:"))


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        } catch (e: Exception) {
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

    private val REQUEST_TAKE_PHOTO = 0
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1

    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val imageUri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            activity_main_plan_image_view.background = BitmapDrawable(bitmap)
        }
    }

    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }


    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("key", mGraph.smartObject as ArrayList<out Parcelable>?);

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        var list =  savedInstanceState.getParcelableArrayList<Parcelable>("key")
        mGraph.smartObject = list as ArrayList<SmartObject>?
    }


}