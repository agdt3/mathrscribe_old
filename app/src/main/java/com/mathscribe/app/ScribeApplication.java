package com.mathscribe.app;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.mathscribe.app.db.models.Models;

import io.requery.reactivex.ReactiveSupport;
import io.requery.sql.Configuration;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;


public class ScribeApplication extends Application {
    ReactiveEntityStore<Persistable> dataStore;
    final private String DBNAME = "default";

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.Initializer initializer = Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build();
        Stetho.initialize(initializer);
    }

    public ReactiveEntityStore<Persistable> getDataStore() {
        // TODO: Daggerify
        if (dataStore == null) {
            if (BuildConfig.DEBUG) {
                for (String str : databaseList()) {
                    Log.d("SCRIBE", str);
                }
                deleteDatabase(DBNAME);
            }
            DatabaseSource source = new DatabaseSource(this, Models.DEFAULT, DBNAME, 1);
            Log.d("SCRIBE", BuildConfig.BUILD_TYPE);
            Log.d("SCRIBE", BuildConfig.DEBUG?"true":"false");
            if (BuildConfig.DEBUG) {
                Log.d("SCRIBE", "inDebug");
                source.setTableCreationMode(TableCreationMode.DROP_CREATE);
                source.setLoggingEnabled(true);
            }

            Configuration configuration = source.getConfiguration();
            dataStore = ReactiveSupport.toReactiveStore(
                    new EntityDataStore<Persistable>(configuration));
        }
        return dataStore;
    }
}
