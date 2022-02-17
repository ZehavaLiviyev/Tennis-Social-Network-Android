package com.example.mytennis.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mytennis.MyApplication;

@Database(entities = {Post.class}, version = 1)

abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "Db_FileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
