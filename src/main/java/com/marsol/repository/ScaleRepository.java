package com.marsol.repository;

import com.marsol.model.Scale;

import java.util.List;

public interface ScaleRepository {
    List<Scale> findEnabledScales();
    List<Scale> findCargaMaestra();
    void updateLastupdateScale(Scale scale);
}
