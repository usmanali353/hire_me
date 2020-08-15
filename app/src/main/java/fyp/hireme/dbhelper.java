package fyp.hireme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbhelper extends SQLiteOpenHelper {
    Context c;
    String[] columns={"id","projectId","userId","image","title","description","requiredService","status","latitude","longitude","budget"};
    String creat_table="create table projects (id integer primary key autoincrement,projectId text,userId text,image text,requiredService text,latitude text,longitude text,status text,title text,description text,budget integer);";
    public dbhelper(Context context) {
        super(context,"projects", null, 1);
        this.c=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(creat_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS projects");
        onCreate(sqLiteDatabase);
    }
    public Boolean insert_projects(String projectId, String image, String title, String Username, String description, String status,String latitude,String longitude,String requiredService,int budget){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("projectId ",projectId);
        cv.put("image ",image);
        cv.put("title",title);
        cv.put("description",description);
        cv.put("userId",Username);
        cv.put("status",status);
        cv.put("latitude",latitude);
        cv.put("longitude",longitude);
        cv.put("requiredService",requiredService);
        cv.put("budget",budget);
        long i= db.insert("projects",null,cv);
        if(i==-1){
            return false;
        }else{
            return true;
        }

    }
    public Cursor get_projects(String Username)throws SQLException {
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("projects",columns,"userId = ?",new String[]{Username},null,null,null,null);
        return products;
    }
    public int get_num_of_rows(String Username){
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("projects",columns,"userId = ?",new String[]{Username},null,null,null,null);
        return products.getCount();
    }
    public int check_if_already_exist(String Username, String productId){
        SQLiteDatabase db= getReadableDatabase();
        Cursor products=db.query("projects",columns,"userId = ? and  projectId = ?",new String[]{Username,productId},null,null,null,null);
        return products.getCount();
    }

    public Integer delete(String id){
        SQLiteDatabase sdb = this.getReadableDatabase();
        return sdb.delete("projects", "projectId = ?", new String[] {id});
    }
    public void delete_all(String Username){
        SQLiteDatabase sdb = this.getReadableDatabase();

        sdb.delete("projects", "userId = ?", new String[] {Username});
    }

}
