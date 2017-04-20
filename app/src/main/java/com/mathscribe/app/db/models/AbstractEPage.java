package com.mathscribe.app.db.models;

import java.io.Serializable;
import java.util.Set;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.query.Result;


@Entity
abstract class AbstractEPage {
    @Key @Generated
    int page_id;

    int idx;

    @OneToMany
    Set<EPath> paths;

    public String toString() {
        return "EPage id " + page_id + " index " + idx + " paths " + paths.toString();
    }
}
