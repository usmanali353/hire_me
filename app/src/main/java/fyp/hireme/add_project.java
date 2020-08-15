package fyp.hireme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import fr.ganfra.materialspinner.MaterialSpinner;

public class add_project extends AppCompatActivity {
MaterialEditText projectTitle,projectDescription,budget;
MaterialSpinner serviceFor;
Button proceed;
ImageView img;
Bitmap bitmap;
Uri image_project_uri;
String resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        projectTitle=findViewById(R.id.title);
        projectDescription=findViewById(R.id.description);
        serviceFor=findViewById(R.id.project_for);
        proceed=findViewById(R.id.proceed);
        budget=findViewById(R.id.budget);
        img=findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(add_project.this);
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(projectTitle.getText().toString().isEmpty()){
                    projectTitle.setError("Enter Some Project Title");
                }else if(projectDescription.getText().toString().isEmpty()){
                    projectDescription.setError("Enter some project Description");
                }else if(serviceFor.getSelectedItem()==null){
                    serviceFor.setError("Select something from Dropdown");
                }else if(bitmap==null){
                    Toast.makeText(add_project.this,"You have not selected any Image",Toast.LENGTH_LONG).show();
                }else if(budget.getText().toString().isEmpty()){
                    budget.setError("Enter Your Project Budget");
                }else if(Integer.parseInt(budget.getText().toString())==0||Integer.parseInt(budget.getText().toString())<1000){
                    budget.setError("Project Budget too Low");
                }else{
                    startActivity(new Intent(add_project.this,MapsActivity.class).putExtra("title",projectTitle.getText().toString()).putExtra("description",projectDescription.getText().toString()).putExtra("image_uri",image_project_uri.toString()).putExtra("budget",budget.getText().toString()).putExtra("service_for",serviceFor.getSelectedItem().toString()));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resultUri = result.getUri().getPath();
            image_project_uri = result.getUri();
            bitmap = BitmapFactory.decodeFile(resultUri);
            img.setImageBitmap(bitmap);
        }

    }
}
