package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flavienlaurent.notboringactionbar.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-11.
 */
public class UploadsActivity extends AppCompatActivity {
    String key = null;

    /*****사진 불러오기 및 사진 서버 전송******/
    ImageView myPhoto1;
    ImageView myPhoto2;
    ImageView myPhoto3;

    TextView messageText;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    String uploadFileName = "";
    String uploadFilePath = "";

    TextView te;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        Intent intent = getIntent();
        String nickname = (String) intent.getSerializableExtra("nickname");

        getKeyPhp(nickname);
        te = (TextView)findViewById(R.id.tetete);

        /******사진 전송******/
        myPhoto1 = (ImageView) findViewById(R.id.myPhoto1);
        myPhoto2 = (ImageView) findViewById(R.id.myPhoto2);
        myPhoto3 = (ImageView) findViewById(R.id.myPhoto3);

        messageText  = (TextView)findViewById(R.id.messageText);
        upLoadServerUri = "http://164.125.154.54/upload.php";//서버컴퓨터의 ip주소
    }

    public void onUploadsNextClicked(View v){
        Intent intent = new Intent(getApplication(), TabWidget.class);
        startActivity(intent);
    }

    public void onImgView1Clicked(View v) { //앨범 불러오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }
    public void onImgView2Clicked(View v) { //앨범 불러오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 102);
    }
    public void onImgView3Clicked(View v) { //앨범 불러오기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 103);
    }

    //사진 전송
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 101) {
            Uri uri = data.getData();
            myPhoto1.setImageURI(uri);

            final String path = getRealPathFromURI(uri);

            String[] file = path.split("/");

            uploadFileName = file[file.length - 1];
            uploadFilePath = file[0];

            for(int i=1; i<file.length - 1; i++){
                uploadFilePath += file[i] + "/";
            }

            dialog = ProgressDialog.show(UploadsActivity.this, "", "Uploading file...", true);
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("uploading started.....");
                        }
                    });

                    uploadFile(uploadFilePath + "" + uploadFileName, "1");
                }
            }).start();
        }

        else if (resultCode == RESULT_OK && requestCode == 102) {
            Uri uri = data.getData();
            myPhoto2.setImageURI(uri);

            final String path = getRealPathFromURI(uri);

            dialog = ProgressDialog.show(UploadsActivity.this, "", "Uploading file...", true);
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("uploading started.....");
                        }
                    });
                    uploadFile(path, "2");

                }
            }).start();
        }

        else if (resultCode == RESULT_OK && requestCode == 103) {
            Uri uri = data.getData();
            myPhoto3.setImageURI(uri);

            final String path = getRealPathFromURI(uri);

            dialog = ProgressDialog.show(UploadsActivity.this, "", "Uploading file...", true);
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("uploading started.....");
                        }
                    });

                    uploadFile(path, "3");
                }
            }).start();
        }
    }

    // 사진의 Uri와 사진의 이름으로 사용할 order
    public int uploadFile(String sourceFileUri, String order) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; //사진의 크기

        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                   // messageText.setText("Source File not exist :"+uploadFilePath + "" + uploadFileName);
                }
            });
            return 0;
        }
        else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                //filename : 서버에서 사용할 변수명
                //order + ".jpg" 서버에 저장될 파일명
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                //Key값을 php로 전달하여 폴더명으로 사용
                dos.writeBytes("Content-Disposition: form-data; name=\"key\"\r\n\r\n" + key);
                dos.writeBytes("\r\n--" + boundary + "\r\n");

                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + order + ".jpg" + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +uploadFileName;

                            //messageText.setText(msg);
                            Toast.makeText(UploadsActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        //messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadsActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                       // messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadsActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload to exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    //사진의 경로인 Uri를 String으로 변환
    public String getRealPathFromURI(Uri contentUri){
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //사진을 저장할 폴더명을 Key 값으로 하기위해 Key 값을 받아옴
    private void getKeyPhp(String nickname){
        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UploadsActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                key = s;
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String nickname = (String) params[0];

                    String link = "http://164.125.154.54/getkey.php?nickname=" + nickname;

                    String data = URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch(Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(nickname);
    }
}
