package com.example.mycontacts

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, "contacts", factory, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = "create table if not exists contact (id integer primary key autoincrement, name, phone_number)"
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS contact")
        onCreate(db)
    }

    fun getProfilesCount(): Long {
        val db = this.readableDatabase
        val count = DatabaseUtils.queryNumEntries(db, "contact")
        db.close()
        return count
    }

    fun addContact(name : String, phone_number : String ){
        val values = ContentValues()
        values.put("name", name)
        values.put("phone_number", phone_number)

        val db = this.writableDatabase
        db.insert("contact", null, values)
        db.close()
    }

    fun getData(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT name, phone_number FROM contact", null)

    }



}