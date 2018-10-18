package com.berryjam.loftcoin.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.berryjam.loftcoin.data.db.room.AppDatabase;
import com.berryjam.loftcoin.data.db.room.DatabaseRoomImpl;

public class DatabaseInitializer {

    public Database init(Context context) {
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "loftcoin.db")
                .fallbackToDestructiveMigration()
                .build();
        return new DatabaseRoomImpl(appDatabase);
    }

}
