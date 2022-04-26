package com.example.mycontacts

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val db = DbHelper(this, null)
    private val contacts =ArrayList<Contact>()
    private var adapter: MyAdapter = MyAdapter(contacts)
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
    }

    @SuppressLint("Range")
    fun loadData()
    {
        if(db.getProfilesCount() > 0)
        {
            val cursor = db.getData()
            cursor!!.moveToFirst()
            contacts.add(Contact(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("phone_number"))))
            while(cursor.moveToNext()){
                contacts.add(Contact(cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("phone_number"))))
            }
            cursor.close()
            if(recyclerView==null) {
                recyclerView = findViewById(R.id.r_view)
                recyclerView?.layoutManager = LinearLayoutManager(this)
                adapter = MyAdapter(contacts)
                recyclerView?.adapter = adapter

                Toast.makeText(this,"Imported Contacts: " + db.getProfilesCount(),Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            if(recyclerView==null)
            {
                recyclerView = findViewById(R.id.r_view)
                recyclerView?.layoutManager = LinearLayoutManager(this)
                permissionCheck()
                adapter = MyAdapter(contacts)
                recyclerView?.adapter = adapter

                Toast.makeText(this,"Imported Contacts: " + db.getProfilesCount(), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun permissionCheck() {
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)!=
            PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf(android.Manifest.permission.READ_CONTACTS),100)
        }
        else {
            importContacts()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            importContacts()
        }
        else
        {
           permissionCheck()
        }
    }

    @SuppressLint("Range")
    private fun importContacts() {
        val uri: Uri = ContactsContract.Contacts.CONTENT_URI
        val cursor: Cursor? =contentResolver.query(uri,null,null,null,null)
        if(cursor != null)
        {
            if (cursor.count > 0)
            {
                while(cursor.moveToNext())
                {
                    val id: String? = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name: String? = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val selection: String = ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?"
                    val phoneCursor:Cursor? = contentResolver.query(uriPhone,null,selection, arrayOf(id),null)
                    if(phoneCursor!=null)
                    {
                        if(phoneCursor.moveToNext())
                        {

                            val number: String = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            if(name!=null) {
                                val temp = Contact(name, number)
                                contacts.add(temp)
                                db.addContact(name, number)
                            }
                        }
                        phoneCursor.close()
                    }
                }
                cursor.close()
            }
            recyclerView?.layoutManager = LinearLayoutManager(this)
            adapter = MyAdapter(contacts)
            recyclerView?.adapter = adapter
        }
    }


}