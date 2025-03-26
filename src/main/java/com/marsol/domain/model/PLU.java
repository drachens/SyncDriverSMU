package com.marsol.domain.model;


/*
Modelo de PLU para rescatar los campos necesario a llenar, además es necesario incluir acá el formato de fecha
configurado por el usuario.


 */

import com.marsol.utils.CleanTextForScale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLU {
    private int LFCode;
    private String ItemCode;
    private int Department;
    private String Name1;
    private String Name2;
    private String Name3;
    private int Label1;
    private int Label2;
    private int BarcodeType1;
    private int BarcodeType2;
    private int UnitPrice;
    private int WeightUnit;
    private int TareWeight;
    private String ProducedDateTime;
    private int PackageDate;
    private int PackageTime;
    private int ValidDays;
    private int FreshDays;
    private int ValidDateCountF;
    private int ProducedDateF;
    private int PackageDateF;
    private int ValidDateF;
    private int FreshDateF;
    private int DiscountFlag;
    private float DiscountUnitPrice;
    private String DiscountStartDateTime;
    private String DiscountEndDateTime;

    private static final Logger logger = LoggerFactory.getLogger(PLU.class);

    /*
    Es necesario vealidar y manejar los casos posible de los Set debido a TransformPLU
     */
    public PLU(){

    }

    public int getLFCode() {
        return LFCode;
    }

    public void setLFCode(int LFCode) {
        if(LFCode < 0 || LFCode > 999999) {
            logger.error("LFCode es obligatorio y debe estar entre 0 y 999999.");
        }
        this.LFCode = LFCode ;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        if(itemCode == null || itemCode.isEmpty()) {
            logger.error("ItemCode es obligatorio.");
        } else if(itemCode.length() > 16){
            logger.error("ItemCode debe tener un máximo de 16 caracteres.");
        }
        this.ItemCode = itemCode;
    }

    public int getDepartment() {
        return Department;
    }

    public void setDepartment(int department) {
        if(department < 1 || department > 99) {
            logger.error("Department es obligatorio y debe estar entre 1 y 99.");
        }
        this.Department = department;
    }

    public String getName1() {
        return Name1;
    }

    public void setName1(String name1) {
        String cleanedText = CleanTextForScale.cleanText(name1).replace('\u00a0',' ');
        if(name1 == null || name1.isEmpty()) {
            logger.error("Name1 es obligatorio.");
        }else if(name1.length() > 40) {
            logger.error("Name1 debe tener un máximo de 40 caracteres.");
            logger.warn("Se acortará el Name1 a 40 caracteres");
            this.Name1 = cleanedText.substring(0,Math.min(cleanedText.length(), 40));
        }else{
            assert name1 != null;
            this.Name1 = cleanedText;
        }
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        if (name2 == null) {
            this.Name2 = "";
        }else if(name2.length() > 40) {
            String cleanedText = CleanTextForScale.cleanText(name2).replace('\u00a0',' ');
            logger.error("Name2 debe tener un máximo de 40 caracteres.");
            logger.warn("Se acortará el Name2 a 40 caracteres");
            this.Name2 = cleanedText.substring(0,Math.min(cleanedText.length(), 40));
        }else{
            String cleanedText = CleanTextForScale.cleanText(name2).replace('\u00a0',' ');
            assert name2 != null;
            this.Name2 = cleanedText;
        }
    }

    public String getName3() {
        return Name3;
    }

    public void setName3(String name3) {
        if(name3 == null) {
            this.Name3 = "";
        }else{
            String cleanedText = CleanTextForScale.cleanText(name3).replace('\u00a0',' ');
            if(name3.length() > 40) {
                logger.error("Name3 debe tener un máximo de 40 caracteres.");
                logger.warn("Se acortará el Name3 a 40 caracteres");
                this.Name3 = cleanedText.substring(0,Math.min(cleanedText.length(), 40));
            }else{
                assert name3 != null;
                this.Name3 = cleanedText;
            }
        }

    }

    public int getLabel1() {
        return Label1;
    }

    public void setLabel1(int label1) {
        if(label1 < 0 || label1 > 32) {
            logger.error("Label1 es obligatorio y debe estar entre 0 y 32");
        }
        this.Label1 = label1;
    }

    public int getLabel2() {
        return Label2;
    }

    public void setLabel2(int label2) {
        if(label2 < 0 || label2 > 32) {
            logger.error("Label2 debe estar entre 0 y 32");
        }
        this.Label2 = label2;
    }

    public int getBarcodeType1() {
        return BarcodeType1;
    }

    public void setBarcodeType1(int barcodeType1) {
        if(barcodeType1 < 0 || barcodeType1 > 150) {
            logger.error("BarcodeType1 es obligatorio y debe tener un valor entre 0 y 150.");
        }
        this.BarcodeType1 = barcodeType1;
    }

    public int getBarcodeType2() {
        return BarcodeType2;
    }

    public void setBarcodeType2(int barcodeType2) {
        if(barcodeType2 < 0 || barcodeType2 > 150) {
            logger.error("BarcodeType2 debe tener un valor entre 0 y 150.");
        }
        this.BarcodeType2 = barcodeType2;
    }

    public int getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        if(unitPrice <= 0) {
            logger.error("UnitPrice es obligatorio y debe ser mayor que 0");
        }
        this.UnitPrice = unitPrice;
    }

    public int getWeightUnit() {
        return WeightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        if(weightUnit < 0 || weightUnit > 8) {
            logger.error("WeightUnit es obligatorio y debe tener un valor entre 0 y 8");
        }
        this.WeightUnit = weightUnit;
    }

    public int getTareWeight() {
        return TareWeight;
    }

    public void setTareWeight(int tareWeight) {
        TareWeight = tareWeight;
    }

    public String getProducedDateTime() {
        return ProducedDateTime;
    }

    public void setProducedDateTime(String producedDateTime) {
        ProducedDateTime = producedDateTime;
    }

    public int getPackageDate() {
        return PackageDate;
    }

    public void setPackageDate(int packageDate) {
        if(packageDate < 0 || packageDate > 99) {
            logger.error("PackageDate debe tener un valor entre 0 y 99.");
        }
        this.PackageDate = packageDate;
    }

    public int getPackageTime() {
        return PackageTime;
    }

    public void setPackageTime(int packageTime) {
        if(packageTime < 0 || packageTime > 99) {
            logger.error("PackageTime debe tener un valor entre 0 y 99.");
        }
        this.PackageTime = packageTime;
    }

    public int getValidDays() {
        return ValidDays;
    }

    public void setValidDays(int validDays) {
        if(validDays < 0 || validDays > 999) {
            logger.error("ValidDays es obligatorio y debe tener un valor entre 0 y 999.");
        }
        this.ValidDays = validDays;
    }

    public int getFreshDays() {
        return FreshDays;
    }

    public void setFreshDays(int freshDays) {
        if(freshDays < 0 || freshDays > 999) {
            logger.error("FreshDays debe tener un valor entre 0 y 999.");
        }
        this.FreshDays = freshDays;
    }

    public int getValidDateCountF() {
        return ValidDateCountF;
    }

    public void setValidDateCountF(int validDateCountF) {
        if(validDateCountF < 0 || validDateCountF > 1) {
            logger.error("ValidDateCountF debe tener un valor entre 0 y 1.");
        }
        this.ValidDateCountF = validDateCountF;
    }

    public int getProducedDateF() {
        return ProducedDateF;
    }

    public void setProducedDateF(int producedDateF) {
        if(producedDateF < 0 || producedDateF > 10) {
            logger.error("ProducedDateF debe tener un valor entre 0 y 1.");
        }
        this.ProducedDateF = producedDateF;
    }

    public int getPackageDateF() {
        return PackageDateF;
    }

    public void setPackageDateF(int packageDateF) {
        if(packageDateF < 0 || packageDateF > 1) {
            logger.error("PackageDateF debe tener un valor entre 0 y 1.");
        }
        this.PackageDateF = packageDateF;
    }

    public int getValidDateF() {
        return ValidDateF;
    }

    public void setValidDateF(int validDateF) {
        if(validDateF < 0 || validDateF > 3) {
            logger.error("ValidDateF debe tener un valor entre 0 y 3");
        }
        this.ValidDateF = validDateF;
    }

    public int getFreshDateF() {
        return FreshDateF;
    }

    public void setFreshDateF(int freshDateF) {
        if(freshDateF < 0 || freshDateF > 3) {
            logger.error("FreshDateF debe tener un valor enter 0 y 3.");
        }
        this.FreshDateF = freshDateF;
    }

    public int getDiscountFlag() {
        return DiscountFlag;
    }

    public void setDiscountFlag(int discountFlag) {
        if(discountFlag < 0 || discountFlag > 2) {
            logger.error("DiscountFlag debe tener un valor entre 0 y 2");
        }
        this.DiscountFlag = discountFlag;
    }

    public float getDiscountUnitPrice() {
        return DiscountUnitPrice;
    }

    public void setDiscountUnitPrice(float discountUnitPrice) {
        if(discountUnitPrice < 0) {
            logger.error("DiscountUnitPrice no puede ser negativo.");
        }
        this.DiscountUnitPrice = discountUnitPrice;
    }

    public String getDiscountStartDateTime() {
        return DiscountStartDateTime;
    }

    public void setDiscountStartDateTime(String discountStartDateTime) {
        DiscountStartDateTime = discountStartDateTime;
    }

    public String getDiscountEndDateTime() {
        return DiscountEndDateTime;
    }

    public void setDiscountEndDateTime(String discountEndDateTime) {
        DiscountEndDateTime = discountEndDateTime;
    }
    //Mostrar el PLU como linea de texto separada por espacios
    @Override
    public String toString() {
        return LFCode + "\t" + ItemCode + "\t" + Department + "\t" + Name1 + "\t" + Name2 + "\t" + Name3 + "\t" + Label1 + "\t" + Label2
                + "\t" + BarcodeType1 + "\t" + BarcodeType2 + "\t" + UnitPrice + "\t" + WeightUnit + "\t" + TareWeight + "\t" + ProducedDateTime
                + "\t" + PackageDate + "\t" + PackageTime + "\t" + ValidDays + "\t" + FreshDays + "\t" + ValidDateCountF + "\t" + ProducedDateF
                + "\t" + PackageDateF + "\t" + ValidDateF + "\t" + FreshDateF + "\t" + DiscountFlag + "\t" + DiscountUnitPrice + "\t" + DiscountStartDateTime
                + "\t" + DiscountEndDateTime;
    }
}
