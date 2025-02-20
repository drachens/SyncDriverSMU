package com.marsol.repository;

import com.marsol.model.Scale;

import java.util.List;

public interface ScaleRepository {
    List<Scale> findEnabledScales();
    void setUpdateLastupdateScale(Scale scale, boolean isSuccess, String message);

    void disabledMassive(Scale scale);
}
