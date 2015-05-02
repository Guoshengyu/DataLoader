package caup.dataloader.unit.transformation;

import caup.dataloader.unit.transformation.model.ComplexUnit;
import caup.dataloader.util.UnitUtils;

/**
 * Created by Richard on 2015/04/24 .
 */
public class UnitAdapter {

    public Double getTransformResult(Double value, String fromUnit, String toUnit){
        //Check validation
       if(preconditionTest(value, fromUnit, toUnit) == false)
           return 0.0d;
        UnitClassifer classifer = new UnitClassifer();
        if(UnitUtils.isComplexUnit(fromUnit) && UnitUtils.isComplexUnit(toUnit)){
            //Complex Unit
            ComplexUnit fromComplexUnit = UnitUtils.getComplexUnit(fromUnit);
            ComplexUnit toComplexUnit = UnitUtils.getComplexUnit(toUnit);
            Double fromValuePart1 = classifer.getUnitInformation(fromComplexUnit.getUnitPart1()).getValue();
            Double fromValuePart2 = classifer.getUnitInformation(fromComplexUnit.getUnitPart2()).getValue();
            Double toValuePart1 = classifer.getUnitInformation(toComplexUnit.getUnitPart1()).getValue();
            Double toValuePart2 = classifer.getUnitInformation(toComplexUnit.getUnitPart2()).getValue();
            return value * ((fromValuePart1 / fromValuePart2) / (toValuePart1 / toValuePart2));
        }else {
            //Simple Unit
            Double fromValue = classifer.getUnitInformation(fromUnit).getValue();
            Double toValue = classifer.getUnitInformation(toUnit).getValue();
            return value * (fromValue / toValue);
        }
    }

    private boolean preconditionTest(Double value, String fromUnit, String toUnit){
        if(value == null || fromUnit == null || toUnit == null || fromUnit.toLowerCase().equals("null") || toUnit.toLowerCase().equals("null"))
            return false;
        UnitClassifer classifer = new UnitClassifer();
        UNIT_CLASS fromUnitClass = classifer.getUnitClass(fromUnit);
        UNIT_CLASS toUnitClass = classifer.getUnitClass(toUnit);
        if(fromUnitClass == UNIT_CLASS.Na || toUnitClass == UNIT_CLASS.Na)
            return false;
        if(!classifer.isSameKindUnit(fromUnit, toUnit))
            return false;
        return true;
    }
}
