package com.gss.community.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.gss.community.CheckNetwork;
import com.gss.community.Constant;
import com.gss.community.R;
import com.gss.community.RestJsonClient;
import com.gss.community.Utility.CommanUtils;
import com.gss.community.Utility.SharedPreferenceUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostMessageActivity extends AppCompatActivity {
    ImageView back,play_button;
    Toolbar toolbar;
    EditText title,description;
    String type="" ;
    ImageView imageView,video;
    Button submit;
    SharedPreferenceUtils preferances;
    String imageurl="",url;
    String userid,fcm_token,encodedImageData="";
    // String imgurl=null;
    String title1,description1;
    private static final String TAG = "PostMessageActivity";
    private static final int SELECT_VIDEO = 2;
    TextView upload_photo,upload_video ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
//        back=findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        back=findViewById(R.id.back);
        title=findViewById(R.id.title);
//        play_button=findViewById(R.id.play_button);
        upload_video=findViewById(R.id.upload_video);
        video = findViewById(R.id.video);
        url= Constant.SendMessage;
//        encodedImageData =getEncoded64ImageStringFromBitmap(bmap);
//        preferances = SharedPreferenceUtils.getInstance(PostMessageActivity.this);
        description=findViewById(R.id.description);
        imageView=findViewById(R.id.id_post_image);
        submit=findViewById(R.id.submit);
        upload_photo=findViewById(R.id.photo);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        userid= SharedPreferenceUtils.getInstance(this).getStringValue(CommanUtils.UserId, "");

        upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    selectVideo();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    selectImage();

            }
        });
        upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    selectImage();


            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    selectVideo();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 fcm_token=SharedPreferenceUtils.getInstance(PostMessageActivity.this).getStringValue(CommanUtils.FCM, "");
                title1=title.getText().toString().trim();
                description1=description.getText().toString().trim();
                imageView.buildDrawingCache();
                Bitmap bmap = imageView.getDrawingCache();
//                video.buildDrawingCache();
//                Bitmap vbmp = video.getDrawingCache();
//                encodedImageData =getEncoded64ImageStringFromBitmap(vbmp);
                encodedImageData =getEncoded64ImageStringFromBitmap(bmap);

                String extension = MimeTypeMap.getFileExtensionFromUrl(imageurl);
                if (extension != null) {
                    type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                }
                if(title1.equalsIgnoreCase("")){
                    Toast.makeText(PostMessageActivity.this, " Please Enter Title",Toast.LENGTH_SHORT).show();
                }else if(description1.equalsIgnoreCase("")){
                    Toast.makeText(PostMessageActivity.this, "Please Write Description", Toast.LENGTH_LONG).show();
                }else  if(type==null){
                    Toast.makeText(PostMessageActivity.this, "Please Select Image or Video", Toast.LENGTH_SHORT).show();

                }else if(CheckNetwork.isInternetAvailable(PostMessageActivity.this)){
                    new PostData(PostMessageActivity.this).execute();
                }else{
                    Toast.makeText(PostMessageActivity.this, "No Internet Connection.Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceUtils.getInstance(getApplicationContext()).setValue(CommanUtils.FCM,refreshedToken);
        Log.i(TAG, "FCM Token  onCreate: "+refreshedToken);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString1 = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString1;
    }
    class PostData extends AsyncTask<String, String, JSONObject> {
        JSONObject json= new JSONObject();
        private final Context context;
        private ProgressDialog progress;


        public PostData(PostMessageActivity context) {
            this.context = context;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("title", title1));
                nameValuePairs.add(new BasicNameValuePair("discription", description1));

                nameValuePairs.add(new BasicNameValuePair("uid", userid));


                nameValuePairs.add(new BasicNameValuePair("filetype", type));

                if(!imageurl.equalsIgnoreCase("")){
                    nameValuePairs.add(new BasicNameValuePair("videos", imageurl));
                }
                if(!encodedImageData.equalsIgnoreCase("")){
                    nameValuePairs.add(new BasicNameValuePair("files", encodedImageData));
                }

                Log.d("datap", nameValuePairs.toString());
                json = RestJsonClient.post(url, nameValuePairs);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }


        @Override
        protected void onPreExecute() {

            progress = new ProgressDialog(context);
            progress.setMessage("Please wait while loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {


            try {
                progress.dismiss();
                if (jsonObject.getString("response").equalsIgnoreCase("success")) {
                    Toast.makeText(PostMessageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent iSettings = new Intent(PostMessageActivity.this, DashboardActivity.class);
                    startActivity(iSettings);
                } else {
                    Toast.makeText(PostMessageActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private void selectVideo() {
       /* Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video"), SELECT_VIDEO);*/
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , SELECT_VIDEO);
    }

    private void selectImage() {
        /*
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(i, SELECT_IMAGE);*/
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (data != null) {
            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
//                        imageurl = getPath(resultUri);
                imageView.setImageURI(resultUri);
                imageurl = resultUri.getPath();
                Log.e("path", resultUri.getPath());

//                else if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
            } else if (requestcode == SELECT_VIDEO) {
                Uri selectedImageUri =data.getData();
                String IPath = selectedImageUri.getPath();
                File file= new File(selectedImageUri.getPath());
                //imageurl = selectedImageUri.getPath();
                imageurl = getPath(getApplicationContext(),selectedImageUri).replaceAll(" ","%20");
                Bitmap bitmap = BitmapFactory.decodeFile(imageurl);
                video.setImageBitmap(bitmap);



                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(imageurl, MediaStore.Video.Thumbnails.MINI_KIND);
                video.setImageBitmap(thumb);

//                video.setImageResource(R.drawable.pla);

                byte[] byteArrayFromFile = new byte[0];
                try {
                    String path = getVideoPath(selectedImageUri);
                    byteArrayFromFile = getByteArrayFromPath(path);
//                    byteArrayFromFile = getByteArrayFromFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Get length of file in bytes
                long fileSizeInBytes = byteArrayFromFile.length;
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB =fileSizeInBytes/1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
               long  fileSizeInMB = fileSizeInKB/1024;
//               if(fileSizeInMB>16){
//                   Toast.makeText(PostMessageActivity.this,"invalid file Size",Toast.LENGTH_SHORT).show();
//
//                }
//                encodedImageData = Base64.encodeToString(byteArrayFromFile, Base64.DEFAULT);
                Log.i(TAG, "onActivityResult: ");
            }
        }
    }


    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        String path = "";

        if (cursor.getCount() > 0)
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();
                cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                cursor.close();
            }
        return path;
    }





    private byte[] getByteArrayFromPath(String path) throws Exception {
        File file = new File(path);
        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();
        return bytesArray;
    }

    private byte[] getByteArrayFromFile(File file) throws Exception {
        //File file = new File(path);
        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); //read file into bytes[]
        fis.close();
        return bytesArray;
    }
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI", uri + "");
        String result = uri + "";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length - 1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }




    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);


                return cursor.getString(column_index);
            }


            /*String[] mediaColumns = {MediaStore.Video.Media.SIZE};
            Cursor cursor = getContext().getContentResolver().query(videoUri, mediaColumns, null, null, null);
            cursor.moveToFirst();
            int sizeColInd = cursor.getColumnIndex(mediaColumns[0]);
            long fileSize = cursor.getLong(sizeColInd);
            cursor.close();*/
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public String getVideoPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }



}
