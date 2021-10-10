package com.example.triviaproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class SignUpActivity extends AppCompatActivity {

    private EditText edUsername;
    private EditText edPassword;
    private EditText edVerifyPassword;
    private Button btnCreateUser;
    private ImageView profileImage;
    private Player player;
    private ActivityResultLauncher<Intent> resultLauncherCapture;
    private  ActivityResultLauncher<Intent> resultLauncherPick;


    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    private static final int PERMISSION_CODE = 1001;
    private Workbook workbook;
    private AsyncHttpClient asyncHttpClient;
    final String TABLE_NAME = "Trivia_Questions";;
    final String CREATE_TABLE_CMD="CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT, question TEXT, answer TEXT, asked TEXT);";;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edVerifyPassword = findViewById(R.id.ed_verify_pwd);
        btnCreateUser = findViewById(R.id.btn_create_user);
        profileImage = findViewById(R.id.profile_image);


        resultLauncherCapture = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap)bundle.get("data");
                        profileImage.setBackgroundResource(R.color.transparent);
                        profileImage.setImageBitmap(bitmap);
                    }
                });

        resultLauncherPick = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent intent = result.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),intent.getData()
                            );
                            profileImage.setBackgroundResource(R.color.transparent);
                            profileImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setUsername = edUsername.getText().toString();
                String setPassword = edPassword.getText().toString();
                String setVerifyPassword = edVerifyPassword.getText().toString();

                BitmapDrawable convertImageToBitmap = (BitmapDrawable) profileImage.getDrawable();

                if(setPassword.equals("") || setUsername.equals("") || setVerifyPassword.equals("") || convertImageToBitmap == null){
                    Toast.makeText(SignUpActivity.this, "Error! Please complete all data", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (setPassword.equalsIgnoreCase(setVerifyPassword)){

                        Bitmap bitmap = convertImageToBitmap.getBitmap();
                        String setProfileImage = encodeToBase64(bitmap);

                        player = new Player(setUsername,setPassword,setProfileImage, "true");

                        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = credentials.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(player);
                        editor.putString("Player",json);
                        editor.commit();
                        try {
                            database = openOrCreateDatabase("TriviaQ", MODE_PRIVATE,null);
                            database.close();
                            ResetQuestionsDB();
                        }catch(Exception e){createDB();}
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Error! The passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        selectImage();
                    }
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    resultLauncherCapture.launch(takePicture);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickImageFromGallery = new Intent(Intent.ACTION_PICK);
                    pickImageFromGallery.setType("image/*");
                    resultLauncherPick.launch(pickImageFromGallery);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
        private String encodeToBase64(Bitmap image) {
        Bitmap BitImage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    //Creates an SQLite Database if one doesn't exist yet. Will do so on first run of the app after installation.
    public void createDB ()
    {
        String URL = "https://github.com/Sagie110/Trivia-Project/raw/main/Trivias_Final.xls";
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(URL, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(SignUpActivity.this, "Error: Please verify Internet Connectivity", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {

                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);
                if (file != null) {
                    try {
                        workbook = Workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);
                        database = openOrCreateDatabase("TriviaQ", MODE_PRIVATE,null);
                        database.execSQL(CREATE_TABLE_CMD);
                        ContentValues contentsVal = new ContentValues();
                        for (int i = 0; i < sheet.getRows(); i++) {
                            Cell[] row = sheet.getRow(i);
                            contentsVal.put("category", row[0].getContents());
                            contentsVal.put("question", row[1].getContents());
                            contentsVal.put("answer", row[2].getContents());
                            contentsVal.put("asked", row[3].getContents());
                            database.insert(TABLE_NAME, null, contentsVal);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }

            } });
    }
    //Reset all questions that were asked, so they could be asked again.
    private void ResetQuestionsDB() {
        database = openOrCreateDatabase("TriviaQ", MODE_PRIVATE,null);
        ContentValues contentVal = new ContentValues();
        contentVal.put("asked", "n");
        database.update(TABLE_NAME,contentVal,null , null);
    }
}