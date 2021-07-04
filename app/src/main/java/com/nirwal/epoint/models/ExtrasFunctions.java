package com.nirwal.epoint.models;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class ExtrasFunctions {
    private Context _context;

    public ExtrasFunctions(Context context){
        this._context = context;
    }

    public String backUpContactAtPath(){

        checkPermition();

        final String vfile = "epoint.vcf";
        StringBuffer stringBuffer = new StringBuffer();

        Cursor phones = _context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);

        phones.moveToFirst(); // move cursor to start

        AssetFileDescriptor fd; // file descriptor resolver

        do {

            String lookupKey = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);

            try {

                fd = _context.getContentResolver().openAssetFileDescriptor(uri, "r"); // r = read, w = write
                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);

                String vCard = new String(buf);
                stringBuffer.append(vCard);

            } catch (Exception e1) {
                // read error from input stream, buffer
                e1.printStackTrace();
            }

        }while (phones.moveToNext());

        Log.d("Vcard",  stringBuffer.toString());

        String path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
        FileOutputStream mFileOutputStream = null;

        // save to external storage
        try {

            mFileOutputStream = new FileOutputStream(path, false);
            mFileOutputStream.write(stringBuffer.toString().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();

    }

    public void checkPermition(){

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M) return;

        if(_context.getApplicationContext().checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) _context,
                    new String[]{Manifest.permission.INTERNET},1);

        }

        if(_context.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) _context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }


        if(_context.getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) _context,
                    new String[]{Manifest.permission.READ_CONTACTS},3);
        }


    }


}
