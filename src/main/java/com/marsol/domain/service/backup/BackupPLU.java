package com.marsol.domain.service.backup;

import com.marsol.domain.model.PLU;
import com.marsol.utils.FileReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class BackupPLU {
    public static List<PLU> extractPLUs(String filename) {
        return FileReaderUtil.readFileAndMap(filename, values -> {
            PLU newPLU = new PLU();
            newPLU.setLFCode(Integer.parseInt(values[0]));
            newPLU.setItemCode(values[1]);
            newPLU.setDepartment(Integer.parseInt(values[2]));
            newPLU.setName1(values[3]);
            newPLU.setName2(values[4]);
            newPLU.setName3(values[5]);
            newPLU.setLabel1(Integer.parseInt(values[6]));
            newPLU.setLabel2(Integer.parseInt(values[7]));
            newPLU.setBarcodeType1(Integer.parseInt(values[8]));
            newPLU.setBarcodeType2(Integer.parseInt(values[9]));
            newPLU.setUnitPrice(Integer.parseInt(values[10]));
            newPLU.setWeightUnit(Integer.parseInt(values[11]));
            newPLU.setTareWeight(Integer.parseInt(values[12]));
            newPLU.setProducedDateTime(values[13]);
            newPLU.setPackageDate(Integer.parseInt(values[14]));
            newPLU.setPackageTime(Integer.parseInt(values[15]));
            newPLU.setValidDays(Integer.parseInt(values[16]));
            newPLU.setFreshDays(Integer.parseInt(values[17]));
            newPLU.setValidDateCountF(Integer.parseInt(values[18]));
            newPLU.setProducedDateF(Integer.parseInt(values[19]));
            newPLU.setPackageDateF(Integer.parseInt(values[20]));
            newPLU.setValidDateF(Integer.parseInt(values[21]));
            newPLU.setFreshDateF(Integer.parseInt(values[22]));
            newPLU.setDiscountFlag(Integer.parseInt(values[23]));
            return newPLU;
        });
    }

    public static List<Integer> extractLFCode(String filename) {
        return FileReaderUtil.readFileAndMap(filename, values -> Integer.parseInt(values[0]));
    }

}


