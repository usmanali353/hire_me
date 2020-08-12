package fyp.hireme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import fr.ganfra.materialspinner.MaterialSpinner;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Model.user;
import fyp.hireme.Utils.utils;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Selection extends AppCompatActivity {
    Button Register,Login;
    TextView forgotPassword;
    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_page);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("user_info",null)!=null){
            user u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
            if(u!=null){
                if(u.getRole().equals("Customer")){
                    startActivity(new Intent(Selection.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }else{
                    startActivity(new Intent(Selection.this,worker_home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }  else if(prefs.getString("user_role",null)!=null){
            startActivity(new Intent(Selection.this,usersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Selection.this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(Selection.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            }else{
                requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION }, 1000);
            }
        }
        Register=findViewById(R.id.btnRegister);
        Login=findViewById(R.id.btnSignin);
        forgotPassword=findViewById(R.id.txt_forgot_password);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterDialog();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginDialog();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPasswordDialog();
            }
        });
    }
    private void openRegisterDialog(){
        View registerView= LayoutInflater.from(Selection.this).inflate(R.layout.layout_register,null);
        MaterialEditText name=registerView.findViewById(R.id.nametxt);
        MaterialEditText email=registerView.findViewById(R.id.emailtxt);
        MaterialEditText phone=registerView.findViewById(R.id.phonetxt);
        MaterialEditText password=registerView.findViewById(R.id.passwordtxt);
        MaterialSpinner offeredService=registerView.findViewById(R.id.choose_offered_services);
        MaterialSpinner roles=registerView.findViewById(R.id.role);
        roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    offeredService.setVisibility(View.VISIBLE);
                }else{
                    offeredService.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog registerDialog=new AlertDialog.Builder(Selection.this)
                .setTitle("Register")
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setView(registerView).create();
        registerDialog.show();

        registerDialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Provide valid email");
                }else if(!utils.isEmailValid(email.getText().toString())){
                    email.setError("Email Format is not correct");
                }else if(password.getText().toString().isEmpty()){
                    password.setError("Provide Password");
                }else if(!utils.isPasswordValid(password.getText().toString())){
                    password.setError("Invalid Password");
                }else if(name.getText().toString().isEmpty()) {
                    name.setError("Enter your Name");
                }else if(phone.getText().toString().isEmpty()){
                    phone.setError("Enter Your Phone");
                }else if(roles.getSelectedItem()==null){
                    roles.setError("Select Role");
                }else if(offeredService.getVisibility()==View.VISIBLE&&offeredService.getSelectedItem()==null){
                    offeredService.setError("Select Offered Service");
                }else{
                    if(utils.isNetworkAvailable(Selection.this)) {
                        if(offeredService.getVisibility()==View.GONE){
                            firebase_operations.Register(name.getText().toString(),email.getText().toString(),password.getText().toString(),phone.getText().toString(),roles.getSelectedItem().toString(),null,Selection.this,registerDialog);
                        }else{
                            firebase_operations.Register(name.getText().toString(),email.getText().toString(),password.getText().toString(),phone.getText().toString(),roles.getSelectedItem().toString(),offeredService.getSelectedItem().toString(),Selection.this,registerDialog);
                        }

                    }else{
                        Toast.makeText(Selection.this,"Network not Available",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void openLoginDialog(){
        View loginView= LayoutInflater.from(Selection.this).inflate(R.layout.login_layout,null);
        MaterialEditText email=loginView.findViewById(R.id.emailtxt);
        MaterialEditText password=loginView.findViewById(R.id.passwordtxt);
        AlertDialog loginDialog=new AlertDialog.Builder(Selection.this)
                .setTitle("Sign In")
                .setMessage("Provide valid Information")
                .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setView(loginView).create();
        loginDialog.show();

        loginDialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Provide valid email");
                }else if(!utils.isEmailValid(email.getText().toString())){
                    email.setError("Email Format is not correct");
                }else if(password.getText().toString().isEmpty()){
                    password.setError("Provide Password");
                }else if(!utils.isPasswordValid(password.getText().toString())){
                    password.setError("Invalid Password");
                }else {
                    if(utils.isNetworkAvailable(Selection.this)){
                        firebase_operations.SignIn(email.getText().toString(),password.getText().toString(),Selection.this,loginDialog);
                    }else{
                        Toast.makeText(Selection.this,"Network not Avaliable",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
    private void openForgotPasswordDialog(){
        View forgotPasswordView= LayoutInflater.from(Selection.this).inflate(R.layout.forgot_password_layout,null);
        MaterialEditText email=forgotPasswordView.findViewById(R.id.emailtxt);
        AlertDialog forgotPasswordDialog=new AlertDialog.Builder(Selection.this)
                .setTitle("Forgot Password")
                .setMessage("Provide Valid Email")
                .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setView(forgotPasswordView).create();
        forgotPasswordDialog.show();

        forgotPasswordDialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Provide valid email");
                }else if(!utils.isEmailValid(email.getText().toString())){
                    email.setError("Email Format is not correct");
                }else{
                    if(utils.isNetworkAvailable(Selection.this)){
                        firebase_operations.ForgotPassword(email.getText().toString(),Selection.this,forgotPasswordDialog);
                    }else{
                        Toast.makeText(Selection.this,"Network not Available",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 1000:

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(Selection.this,"Location Permission is Required to Use this App",Toast.LENGTH_LONG).show();
                    finish();

                    // Explain to the user that the feature is unavailable because

                    // the features requires a permission that the user has denied.

                    // At the same time, respect the user's decision. Don't link to

                    // system settings in an effort to convince the user to change

                    // their decision.

                }

        }

    }
}
