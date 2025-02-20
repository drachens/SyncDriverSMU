package com.marsol.repository;

import com.marsol.model.Scale;

import java.util.List;
import java.util.Map;

public interface PLURepository {
    public List<String> getAllArticles(Scale scale);
    public List<Integer> getDisabledArticles(Scale scale);
    public List<Integer> getEnabledArticles(Scale scale);
}
