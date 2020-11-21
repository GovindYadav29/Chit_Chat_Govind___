package com.govind.chitchatgovind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettongsActivity extends AppCompatActivity {

    private DatabaseReference muserdatabase;
    private FirebaseUser mCurrentUser;
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private Button mStatusBtn;
    private Button mImageBtn;
    private StorageReference mImageStorage;
    private ProgressDialog mprogressDialog;
    public static int GALLERY_PIC =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settongs);
        mDisplayImage=findViewById(R.id.settings_image);
        mStatus=findViewById(R.id.settings_status);
        mStatusBtn=findViewById(R.id.settings_stautus_btn);
        mName=findViewById(R.id.settings_display_name);
        mImageBtn=findViewById(R.id.settings_image_btn);
        mImageStorage= FirebaseStorage.getInstance().getReference();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid= mCurrentUser.getUid();
        muserdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        muserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name= snapshot.child("name").getValue().toString();
                String image= snapshot.child("image").getValue().toString();
                String status= snapshot.child("status").getValue().toString();
                String thumb_image= snapshot.child("thumb_image").getValue().toString();
                mName.setText(name);
                mStatus.setText(status);
                Picasso.get().load(image).into(mDisplayImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value= mStatus.getText().toString();
                Intent statusIntetn=new Intent(SettongsActivity.this,StatusActivity.class);
                statusIntetn.putExtra("status_value",status_value);
                startActivity(statusIntetn);
            }
        });
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PIC);


               /* CropImage.activity()

                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettongsActivity.this);  */
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PIC && resultCode==RESULT_OK){
            Uri imageUrl=data.getData();
            CropImage.activity(imageUrl)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                mprogressDialog=new ProgressDialog(SettongsActivity.this);
                mprogressDialog.setTitle("Uploading image");
                mprogressDialog.setMessage("Please wait while we upload and process the image.");
                mprogressDialog.setCanceledOnTouchOutside(false);
                mprogressDialog.show();

                Uri resultUri= result.getUri();
                String current_user_id=mCurrentUser.getUid();
                StorageReference filePath=mImageStorage.child("profile_images").child(current_user_id+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            String download_url;

                            download_url = task.getResult().toString();
                            muserdatabase.child("iamge").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mprogressDialog.dismiss();
                                        Toast.makeText(SettongsActivity.this,"Success uploading",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                        else {
                            Toast.makeText(SettongsActivity.this,"Error in uploading.",Toast.LENGTH_LONG).show();
                            mprogressDialog.dismiss();
                        }
                    }
                });
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }
    }
    public static  String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength=generator.nextInt(10);
        char tempchar;
        for(int i=0;i<randomLength;i++){
            tempchar =(char) (generator.nextInt(96)+32);
            randomStringBuilder.append(tempchar);
        }
        return randomStringBuilder.toString();

    }
}
