package com.marsol.domain.repository;

import com.marsol.domain.model.Scale;

import java.util.List;

public interface ScaleDAORepository {
    List<Scale> findEnabledScales();
    void setUpdateLastupdateScale(Scale scale, boolean isSuccess, String message);
    void disabledMassive(Scale scale);
}
