package fyp.hireme;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rengwuxian.materialedittext.MaterialEditText;

import fr.ganfra.materialspinner.MaterialSpinner;
import fyp.hireme.Firebase_Operations.firebase_operations;
import fyp.hireme.Utils.utils;

import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Home extends AppCompatActivity {
    Button Register,Login;
    TextView forgotPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_page);
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
        View registerView= LayoutInflater.from(Home.this).inflate(R.layout.layout_register,null);
        MaterialEditText name=registerView.findViewById(R.id.nametxt);
        MaterialEditText email=registerView.findViewById(R.id.emailtxt);
        MaterialEditText phone=registerView.findViewById(R.id.phonetxt);
        MaterialEditText password=registerView.findViewById(R.id.passwordtxt);
        MaterialSpinner offeredService=registerView.findViewById(R.id.choose_offered_services);
        MaterialSpinner roles=registerView.findViewById(R.id.role);
        AlertDialog registerDialog=new AlertDialog.Builder(Home.this)
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
                 }else if(offeredService.getSelectedItem()==null){
                     offeredService.setError("Select Offered Service");
                 }else{
                    if(utils.isNetworkAvailable(Home.this)) {
                        firebase_operations.Register(name.getText().toString(),email.getText().toString(),password.getText().toString(),phone.getText().toString(),roles.getSelectedItem().toString(),offeredService.getSelectedItem().toString(),Home.this,registerDialog);
                    }else{
                        Toast.makeText(Home.this,"Network not Available",Toast.LENGTH_LONG).show();
                    }
                 }
             }
         });
    }
    private void openLoginDialog(){
        View loginView= LayoutInflater.from(Home.this).inflate(R.layout.login_layout,null);
        MaterialEditText email=loginView.findViewById(R.id.emailtxt);
        MaterialEditText password=loginView.findViewById(R.id.passwordtxt);
        AlertDialog loginDialog=new AlertDialog.Builder(Home.this)
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
                    if(utils.isNetworkAvailable(Home.this)){
                        firebase_operations.SignIn(email.getText().toString(),password.getText().toString(),Home.this,loginDialog);
                    }else{
                        Toast.makeText(Home.this,"Network not Avaliable",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
    private void openForgotPasswordDialog(){
        View forgotPasswordView= LayoutInflater.from(Home.this).inflate(R.layout.forgot_password_layout,null);
        MaterialEditText email=forgotPasswordView.findViewById(R.id.emailtxt);
        AlertDialog forgotPasswordDialog=new AlertDialog.Builder(Home.this)
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
                    if(utils.isNetworkAvailable(Home.this)){
                        firebase_operations.ForgotPassword(email.getText().toString(),Home.this,forgotPasswordDialog);
                    }else{
                        Toast.makeText(Home.this,"Network not Available",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
