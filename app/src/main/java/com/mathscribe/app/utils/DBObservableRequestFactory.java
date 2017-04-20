package com.mathscribe.app.utils;

import com.mathscribe.app.db.models.EPage;

import io.reactivex.Observable;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;
import io.requery.reactivex.ReactiveResult;


public class DBObservableRequestFactory {

   public static Observable<ReactiveResult<EPage>> getAllPageItems(ReactiveEntityStore<Persistable> data) {
      return data.select(EPage.class).get().observableResult();
   }

   public static Observable<ReactiveResult<EPage>> getPageItemById(ReactiveEntityStore<Persistable> data, int id) {
      return data.select(EPage.class).where(EPage.PAGE_ID.eq(id)).get().observableResult();
   }
}
