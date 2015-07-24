package com.holypasta.trainer.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.wallet.LineItem;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    //имя файла базы данных который будет храниться в /data/data/APPNAME/DATABASE_NAME.db
    private static final String DATABASE_NAME ="myappname.db";

    //с каждым увеличением версии, при нахождении в устройстве БД с предыдущей версией будет выполнен метод onUpgrade();
    private static final int DATABASE_VERSION = 1;

    //ссылки на DAO соответсвующие сущностям, хранимым в БД
//    private GoalDAO goalDao = null;
    private RoleDAO roleDao = null;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Выполняется, когда файл с БД не найден на устройстве
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource){
        try {
//            TableUtils.createTable(connectionSource, Goal.class);
            TableUtils.createTable(connectionSource, LineItem.Role.class);
        } catch (SQLException e){
            Log.e(TAG, "error creating DB " + DATABASE_NAME);
            throw new RuntimeException(e);
        }
    }

    //Выполняется, когда БД имеет версию отличную от текущей
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer){
        try {
            //Так делают ленивые, гораздо предпочтительнее не удаляя БД аккуратно вносить изменения
//            TableUtils.dropTable(connectionSource, Goal.class, true);
            TableUtils.dropTable(connectionSource, LineItem.Role.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e){
            Log.e(TAG,"error upgrading db "+DATABASE_NAME+"from ver "+oldVer);
            throw new RuntimeException(e);
        }
    }

//    //синглтон для GoalDAO
//    public GoalDAO getGoalDAO() throws SQLException{
//        if(goalDao == null){
//            goalDao = new GoalDAO(getConnectionSource(), Goal.class);
//        }
//        return goalDao;
//    }

    //синглтон для RoleDAO
    public RoleDAO getRoleDAO() throws SQLException{
        if(roleDao == null){
            roleDao = new RoleDAO(getConnectionSource(), LineItem.Role.class);
        }
        return roleDao;
    }

    //выполняется при закрытии приложения
    @Override
    public void close(){
        super.close();
//        goalDao = null;
        roleDao = null;
    }
}
