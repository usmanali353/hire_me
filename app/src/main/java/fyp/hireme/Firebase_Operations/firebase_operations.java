package fyp.hireme.Firebase_Operations;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import fyp.hireme.Model.user;

public class firebase_operations {
    public static void SignIn(String email, String password, final Context context){
        final ProgressDialog pd=new ProgressDialog(context);
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        pd.setMessage("Authenticating User...");
        pd.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            pd.dismiss();
                            if(documentSnapshot.exists()){
                                user u=documentSnapshot.toObject(user.class);
                                prefs.edit().putString("user_info",new Gson().toJson(u)).apply();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                         Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void ForgotPassword(String email, final Context context){
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Reseting Password...");
        pd.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(context,"Password Reset Mail is Sent",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void Register(final String name, final String email, final String password, final String phone, final String role, final String offered_service, final Context context){
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Registering User...");
        pd.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user u=new user(name,email,phone,password,role,offered_service);
                    FirebaseFirestore.getInstance().collection("user").document().set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(context,"User Registered Sucessfully",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             pd.dismiss();
             Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
