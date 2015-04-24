package caup.dataloader.unit.transformation;


import caup.dataloader.unit.transformation.model.ComplexUnit;

import java.util.Map;

/**
 * Created by Richard on 2015/04/24 .
 */
public class UnitClassifer {

    public UNIT_CLASS getUnitClass(String unit){

    }

    public  boolean isSameKindUnit(String unit1, String unit2) {

        if(UnitUtils.isSimpleUnit(unit1) &&  UnitUtils.isSimpleUnit(unit2)) {
            //Simple unit cases
            if(getUnitClass(unit1) == UNIT_CLASS.Na || getUnitClass(unit1) == UNIT_CLASS.Na)
                return false;
            return getUnitClass(unit1).equals(getUnitClass(unit2));
        }
        if(!UnitUtils.isSimpleUnit(unit1) &&  !UnitUtils.isSimpleUnit(unit2)) {
            //Complex unit cases
            ComplexUnit _unit1 =  UnitUtils.getComplexUnit(unit1);
            ComplexUnit _unit2 =  UnitUtils.getComplexUnit(unit2);
            return isSameKindUnit(_unit1.getUnitPart1(), _unit2.getUnitPart1()) &&
                    isSameKindUnit(_unit1.getUnitPart2(), _unit2.getUnitPart2());
        }
    }
}
