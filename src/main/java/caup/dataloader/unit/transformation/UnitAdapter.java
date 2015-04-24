package caup.dataloader.unit.transformation;

/**
 * Created by Richard on 2015/04/24 .
 */
public class UnitAdapter {

    public Double getTransformResult(Double value, String fromUnit, String toUnit){
        //Check validation
        if(value == null || fromUnit == null || toUnit == null || fromUnit.toLowerCase().equals("null") || toUnit.toLowerCase().equals("null"))
            return 0.0d;
        UnitClassifer classifer = new UnitClassifer();
        UNIT_CLASS fromUnitClass = classifer.getUnitClass(fromUnit);
        UNIT_CLASS toUnitClass = classifer.getUnitClass(toUnit);
        if(!fromUnitClass.equals(toUnitClass))
            return 0.0d;

    }
}
