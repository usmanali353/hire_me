package fyp.hireme.Firebase_Operations;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;
import fyp.hireme.Adapters.bids_list_adapter;
import fyp.hireme.Adapters.fav_projects_adapter;
import fyp.hireme.Adapters.projects_list_adapter;
import fyp.hireme.Adapters.user_list_adapter;
import fyp.hireme.MainActivity;
import fyp.hireme.Model.Bid;
import fyp.hireme.Model.favourite_projects;
import fyp.hireme.Model.project;
import fyp.hireme.Model.user;
import fyp.hireme.R;
import fyp.hireme.Utils.utils;
import fyp.hireme.dbhelper;
import fyp.hireme.usersList;
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
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals("csAMKO8zUraljjOc7uJpjGRUCfP2")){
                        prefs.edit().putString("user_role","Admin").apply();
                        Toast.makeText(context,"Login Sucess",Toast.LENGTH_LONG).show();
                        loginDialog.dismiss();
                        context.startActivity(new Intent(context, usersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        ((AppCompatActivity)context).finish();
                    }else{
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
    public static void addProject(Context context, String title, String description, Uri image,double lat,double lng,String serviceFrom,int budget){
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

                     project p=new project(title,description,uri.toString(),lat,lng,utils.getCurrentDate(),FirebaseAuth.getInstance().getCurrentUser().getUid(),"New Project",serviceFrom,budget);
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
        FirebaseFirestore.getInstance().collection("Project").whereEqualTo("status","New Project").whereEqualTo("requiredService",offeredService).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
        FirebaseFirestore.getInstance().collection("Project").whereEqualTo("allottedTo",WorkerId).whereEqualTo("status","Allotted").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
    public static void getAllUsers(Context context,RecyclerView usersList){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Fetching User List");
        pd.show();
        ArrayList<user> users=new ArrayList<>();
        ArrayList<String> userId=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                pd.dismiss();
                if(queryDocumentSnapshots.getDocuments().size()>0){
                    for(int i=0;i<queryDocumentSnapshots.getDocuments().size();i++){
                        users.add(queryDocumentSnapshots.getDocuments().get(i).toObject(user.class));
                        userId.add(queryDocumentSnapshots.getDocuments().get(i).getId());
                    }
                    usersList.setAdapter(new user_list_adapter(users,userId,context));
                }else{
                    Toast.makeText(context,"No Users Found",Toast.LENGTH_LONG).show();
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
    public static void deleteUsers(Context context,String userId){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Deleting User");
        pd.show();
        FirebaseFirestore.getInstance().collection("Users").document(userId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(context,"User Deleted Sucessfully",Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context,usersList.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
    public static void rateComment(Context context,String projectId,String rating,String comment){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Rating the Project....");
        pd.show();
        Map<String,Object> projectUpdate=new HashMap<>();
        projectUpdate.put("status","Completed");
        projectUpdate.put("rating",rating);
        projectUpdate.put("comments",comment);
        FirebaseFirestore.getInstance().collection("Project").document(projectId).update(projectUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(context,"Rating and comments added",Toast.LENGTH_LONG).show();
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
    public static void getCompletedProjectsForWorker(Context context,String WorkerId,RecyclerView projectsList){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.show();
        ArrayList<project> projects=new ArrayList<>();
        ArrayList<String>  projectIds=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Project").whereEqualTo("allottedTo",WorkerId).whereEqualTo("status","Completed").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
    public static void updateProfile(Context context,String userId,String name,String phone,String offeredService){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Please Wait...");
        pd.show();
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
        Map<String,Object> profileUpdateData=new HashMap<>();
        profileUpdateData.put("name",name);
        profileUpdateData.put("phone",phone);
        profileUpdateData.put("offered_service",offeredService);
        user u=new Gson().fromJson(prefs.getString("user_info",null),user.class);
        FirebaseFirestore.getInstance().collection("Users").document(userId).update(profileUpdateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful()){
                    prefs.edit().putString("user_info",new Gson().toJson(new user(name,u.getEmail(),phone,u.getPassword(),u.getRole(),offeredService))).apply();
                    Toast.makeText(context,"Profile Updated",Toast.LENGTH_LONG).show();
                    if(u.getRole().equals("Customer")) {
                        context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        ((AppCompatActivity) context).finish();
                    }else if(u.getRole().equals("Worker")){
                        context.startActivity(new Intent(context, worker_home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        ((AppCompatActivity) context).finish();
                    }
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
    public static void getProfile(Context context,String userId,String profileType){
        ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Fetching Profile....");
        pd.show();
        FirebaseFirestore.getInstance().collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pd.dismiss();
                if(documentSnapshot.exists()){
                    user u=documentSnapshot.toObject(user.class);
                    View v= LayoutInflater.from(context).inflate(R.layout.profile_layout,null);
                    MaterialEditText name=v.findViewById(R.id.nametxt);
                    MaterialEditText phone=v.findViewById(R.id.phonetxt);
                    MaterialEditText email=v.findViewById(R.id.emailtxt);
                    MaterialEditText offered_service=v.findViewById(R.id.offeredService);
                    if(u.getRole().equals("Customer")) {
                        offered_service.setVisibility(View.GONE);
                    }
                    email.setText(u.getEmail());
                    name.setText(u.getName());
                    phone.setText(u.getPhone());
                    if(u.getRole().equals("Worker")&&u.getOffered_service()!=null){
                        offered_service.setVisibility(View.VISIBLE);
                        offered_service.setText(u.getOffered_service());
                    }
                    AlertDialog changeProfileDialog =new AlertDialog.Builder(context)
                            .setTitle(profileType+" "+"Profile")
                           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setView(v).create();
                    changeProfileDialog.show();
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
    public static void getFavProjects(Context context,RecyclerView fav_projects) {
        ArrayList<favourite_projects> projects=new ArrayList<>();
        Cursor projectList = new dbhelper(context).get_projects(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (projectList.getCount() == 0) {
            Toast.makeText(context, "No Projects Found", Toast.LENGTH_LONG).show();
        } else {
            while (projectList.moveToNext()) {
                projects.add(new favourite_projects(projectList.getString(1),projectList.getString(4),projectList.getString(5),projectList.getString(8),projectList.getString(9),projectList.getString(7),projectList.getString(6),projectList.getString(3),projectList.getInt(10)));
            }
            if(projects.size()>0){
                fav_projects.setAdapter(new fav_projects_adapter(projects,context));
            }
        }
    }
}
