package com.mathscribe.app.utils;

import com.mathscribe.app.db.models.EPage;
import com.mathscribe.app.db.models.EPath;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;


public class DBFixtures {
    private static final String PTS1 = "0.1,0.2,0.3,0.4";
    private static final String PTS2 = "0.5,0.6.0,0.7,0.8";
    private static final String PTS3 = "0.9,0.101.0,0.11,0.12";

    public static List<EPage> createPagesWithPaths(ReactiveEntityStore<Persistable> data) {
        EPath ePath1 = new EPath();
        ePath1.setPoints(PTS1);
        EPath ePath2 = new EPath();
        ePath2.setPoints(PTS2);
        EPath ePath3 = new EPath();
        ePath3.setPoints(PTS3);

        EPage ePage1 = new EPage();
        ePage1.setIdx(1);
        ePage1.getPaths().add(ePath1);
        ePage1.getPaths().add(ePath2);

        EPage ePage2 = new EPage();
        ePage2.setIdx(2);
        ePage2.getPaths().add(ePath3);

        List<EPage> entityList = new ArrayList<>();
        entityList.add(ePage1);
        entityList.add(ePage2);

        List<EPage> epages = (List<EPage>) data.toBlocking().insert(entityList);
        return epages;
    }
}
