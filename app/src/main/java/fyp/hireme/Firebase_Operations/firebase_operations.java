package fyp.hireme.Firebase_Operations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fyp.hireme.Adapters.bids_list_adapter;
import fyp.hireme.Adapters.projects_list_adapter;
import fyp.hireme.MainActivity;
import fyp.hireme.Model.Bid;
import fyp.hireme.Model.project;
import fyp.hireme.Model.user;
import fyp.hireme.Utils.utils;
import fyp.hireme.worker_home;

public class firebase_operations {
    public static void SignIn(String email, String password, final Context context, AlertDialog loginDialog){
        final ProgressDialog pd=new ProgressDialog(context);
        final SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        pd.setMessage("Authenticating User...");
        pd.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            pd.dismiss();
                            if(documentSnapshot.exists()){
                                user u=documentSnapshot.toObject(user.class);
                                prefs.edit().putString("user_info",new Gson().toJson(u)).apply();
                                Toast.makeText(context,"Login Sucess",Toast.LENGTH_LONG).show();
                                loginDialog.dismiss();
                                if(u.getRole().equals("Customer")){
                                    context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    ((AppCompatActivity)context).finish();
                                }else if(u.getRole().equals("Worker")){
                                    context.startActivity(new Intent(context, worker_home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    ((AppCompatActivity)context).finish();
                                }
                            }else{
                                Toast.makeText(context,"No User Data Exist",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            loginDialog.dismiss();
                         Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                loginDialog.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void ForgotPassword(String email, final Context context,AlertDialog forgotPasswordDialog){
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Reseting Password...");
        pd.show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                forgotPasswordDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(context,"Password Reset Mail is Sent",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                forgotPasswordDialog.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void Register(final String name, final String email, final String password, final String phone, final String role, final String offered_service, final Context context,AlertDialog registerDialog){
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Registering User...");
        pd.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user u=new user(name,email,phone,password,role,offered_service);
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            registerDialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(context,"User Registered Sucessfully",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            registerDialog.dismiss();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             pd.dismiss();
             registerDialog.dismiss();
             Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void addProject(Context context, String title, String description, Uri image,double lat,double lng){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Adding Project...");
        pd.show();
      StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String report_id = UUID.randomUUID().toString();
        final StorageReference project_images_folder = storageReference.child("project_images/" + report_id);
        project_images_folder.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
             project_images_folder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                 @Override
                 public void onSuccess(Uri uri) {
                     pd.dismiss();

                     project p=new project(title,description,uri.toString(),lat,lng,utils.getCurrentDate(),FirebaseAuth.getInstance().getCurrentUser().getUid(),"New Project");
                     FirebaseFirestore.getInstance().collection("Project").document().set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             pd.dismiss();
                             if(task.isSuccessful()){
                                 Toast.makeText(context,"Project Added",Toast.LENGTH_LONG).show();
                                 context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                 ((FragmentActivity)context).finish();
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
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     pd.dismiss();
                     Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                 }
             });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void getProjectsforCustomer(Context context, RecyclerView projectsList){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("getting Projects...");
        pd.show();
        ArrayList<project> projects=new ArrayList<>();
        ArrayList<String>  projectIds=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Project").whereEqualTo("customerId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                        projects.add(queryDocumentSnapshots.getDocuments().get(i).toObject(project.class));
                        projectIds.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                    }
                    projectsList.setAdapter(new projects_list_adapter(projects,context,projectIds));
                }else{
                    Toast.makeText(context,"No Projects Found",Toast.LENGTH_LONG).show();
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
    public static void getProjectsforBids(Context context, RecyclerView projectsList,String offeredService){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("getting Projects for Bids...");
        pd.show();
        ArrayList<project> projects=new ArrayList<>();
        ArrayList<String>  projectIds=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Project").whereEqualTo("status","New Project").whereEqualTo("offered_service",offeredService).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                        projects.add(queryDocumentSnapshots.getDocuments().get(i).toObject(project.class));
                        projectIds.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                    }
                    projectsList.setAdapter(new projects_list_adapter(projects,context,projectIds));
                }else{
                    Toast.makeText(context,"No Projects Found",Toast.LENGTH_LONG).show();
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
    public static void AddBid(Context context,String mechanic_name,String bidDate,String projectId,int price,String status,AlertDialog bidDialog){
     ProgressDialog pd=new ProgressDialog(context);
     pd.setMessage("adding Bid...");
     pd.show();
     FirebaseFirestore.getInstance().collection("Bid").document().set(new Bid(mechanic_name,bidDate,projectId,price,status,FirebaseAuth.getInstance().getCurrentUser().getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
         @Override
         public void onComplete(@NonNull Task<Void> task) {
             pd.dismiss();
             if(task.isSuccessful()){
                 bidDialog.dismiss();
                 Toast.makeText(context,"Bid added Sucessfully",Toast.LENGTH_LONG).show();
             }
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             pd.dismiss();
             bidDialog.dismiss();
             Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
         }
     });
    }
    public static void checkBidsAlreadyExist(Context context,String projectId,String workerId,String mechanic_name,String bidDate,int price,String status,AlertDialog bidDialog){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("checking if already Bidded...");
        pd.show();
        FirebaseFirestore.getInstance().collection("Bid").whereEqualTo("projectId",projectId).whereEqualTo("mechanicId",workerId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    Toast.makeText(context,"You have already bidded on this project",Toast.LENGTH_LONG).show();
                }else{
                    AddBid(context,mechanic_name,bidDate,projectId,price,status,bidDialog);
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
    public static void getBidsforProject(Context context,String projectId,RecyclerView bidList,String bidStatus){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("getting Bids for Project...");
        pd.show();
        ArrayList<Bid> bids=new ArrayList<>();
        ArrayList<String> bidId=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Bid").whereEqualTo("projectId",projectId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                  for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                      bids.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Bid.class));
                      bidId.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                  }
                   bidList.setAdapter(new bids_list_adapter(context,bids,bidId,projectId,bidStatus));
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
    public static void acceptBid(Context context,String bidId,String projectId,String workerId){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.show();
        Map<String,Object> projectUpdateData=new HashMap<>();
        projectUpdateData.put("status","Allotted");
        projectUpdateData.put("allottedTo",workerId);
        Map<String,Object> bidUpdateData=new HashMap<>();
        bidUpdateData.put("status","Accepted");
        FirebaseFirestore.getInstance().collection("Project").document(projectId).update(projectUpdateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    FirebaseFirestore.getInstance().collection("Bid").document(bidId).update(bidUpdateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(context,"Bid Accepted",Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                ((AppCompatActivity)context).finish();
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
    public static void getOngoingProjectsForWorker(Context context,String WorkerId,RecyclerView projectsList){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.show();
        ArrayList<project> projects=new ArrayList<>();
        ArrayList<String>  projectIds=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Project").whereEqualTo("allottedTo",WorkerId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                        projects.add(queryDocumentSnapshots.getDocuments().get(i).toObject(project.class));
                        projectIds.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                    }
                    projectsList.setAdapter(new projects_list_adapter(projects,context,projectIds));
                }else{
                    Toast.makeText(context,"No Projects Found",Toast.LENGTH_LONG).show();
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
