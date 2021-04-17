package com.du.de.rasandummy.RoomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class RasanDatabase extends RoomDatabase {
    public abstract ProductDao dao();
}
