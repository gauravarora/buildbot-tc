package com.iontrading.buildbot2;

import java.util.Collection;

import com.iontrading.model.Build;

public interface IQuery {

    Collection<Build> queryFails();

    Collection<Build> queryAll();

}
