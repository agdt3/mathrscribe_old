package com.mathscribe.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mathscribe.app.db.models.EPage;
import com.mathscribe.app.utils.DBFixtures;
import com.mathscribe.app.utils.DBObservableRequestFactory;
import com.mathscribe.app.utils.DBObserverResponseFactory;
import com.mathscribe.app.views.DrawActivity;

import java.util.List;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;

public class MainActivity extends Activity {
    // TODO: Move to presenter
    private ReactiveEntityStore<Persistable> mData;
    private Boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = ((ScribeApplication) getApplication()).getDataStore();
        if (BuildConfig.DEBUG) {
            setPages(mData);
        }
        getPages(mData);

        // TODO: Show splash screen, then move to latest page
        //startPageActivity();
    }

    private void startPageActivity() {
        Intent drawActivityIntent = new Intent(this, DrawActivity.class);
        startActivity(drawActivityIntent);
    }

    private void setPages(ReactiveEntityStore<Persistable> data) {
        List<EPage> pages = DBFixtures.createPagesWithPaths(data);
        Log.d("SCRIBE", "finished set pages");
    }

    private void getPages(ReactiveEntityStore<Persistable> data) {
        Observable<ReactiveResult<EPage>> pages = DBObservableRequestFactory.getAllPageItems(data);
        DBObserverResponseFactory<EPage> pageDBObserverResponseFactory = new DBObserverResponseFactory<>();
        pages.subscribe(pageDBObserverResponseFactory.getObserverReponse());
    }
}

