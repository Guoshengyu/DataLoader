package caup.dataloader.unit.transformation;


import caup.dataloader.unit.transformation.model.ComplexUnit;
import caup.dataloader.unit.transformation.model.UnitElement;

import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 2015/04/24 .
 */
public class UnitClassifer {

    public static void main(String[] args) {
     //   UnitDictonary unitDictonary = new UnitDictonary();
       // UnitDictonary.Initialize();
        UnitClassifer classifer = new UnitClassifer();
       UnitElement element = classifer.getUnitInformation("小时");

        System.out.println(element.getUnit() + " | " + element.getValue());

      //  UnitDictonary.Initialize();
        UnitAdapter adapter = new UnitAdapter();
        System.out.println(adapter.getTransformResult(1.0, "千米/分", "米/小时"));
        System.out.println(classifer.getUnitClass("亿元"));
        System.out.println(classifer.getUnitClass("万元"));
        System.out.println(classifer.isSameKindUnit("亿元", "万元"));
        System.out.println(classifer.isSameKindUnit("亿元", "万元"));
        System.out.println(classifer.isSameKindUnit("元", "NULL"));
    }

    /*
     * For Simple Units
     */
    public UnitElement getUnitInformation(String unit) {
        UnitElement ret = new UnitElement();
        Map<String, Double> prefixDictionary = UnitDictonary.getPrefixDictionary();
        Map<UNIT_CLASS, List<UnitElement>> dictionary = UnitDictonary.getDictionary();
        unit = unit.trim();
        String prefix = UnitDictonary.getUnitPrefix(unit);
        if (prefix != null) {
            //With Prefix
            unit = unit.substring(unit.indexOf(prefix) + 1, unit.length());
            Double prefixValue = prefixDictionary.get(prefix);
            for (UNIT_CLASS unit_class : dictionary.keySet()) {
                List<UnitElement> unitElementList = dictionary.get(unit_class);
                for (UnitElement unitElement : unitElementList) {
                    if (unitElement.getUnit().equals(unit)) {
                        ret.setUnit(prefix + unitElement.getUnit());
                        ret.setValue(unitElement.getValue() * prefixValue);
                    }
                }
            }
        } else {
            //Without Prefix
            for (UNIT_CLASS unit_class : dictionary.keySet()) {
                List<UnitElement> unitElementList = dictionary.get(unit_class);
                for (UnitElement unitElement : unitElementList) {
                    if (unitElement.getUnit().equals(unit)) {
                        return unitElement;
                    }
                }
            }
        }
        return ret;
    }

    public UNIT_CLASS getUnitClass(String unit) {
        UNIT_CLASS ret = UNIT_CLASS.Na;
        Map<String, Double> prefixDictionary = UnitDictonary.getPrefixDictionary();
        Map<UNIT_CLASS, List<UnitElement>> dictionary = UnitDictonary.getDictionary();
        unit = unit.trim();

        if(unit == null)
            return  ret;
        if (UnitUtils.isComplexUnit(unit)) {
            ComplexUnit complexUnit = UnitUtils.getComplexUnit(unit);
             if(getUnitClass(complexUnit.getUnitPart1()) != UNIT_CLASS.Na && getUnitClass(complexUnit.getUnitPart2()) != UNIT_CLASS.Na)
                return UNIT_CLASS.Comeplex_Unit;
        }else {
            String prefix = UnitDictonary.getUnitPrefix(unit);
            if (prefix != null) {
                //With Prefix
                unit = unit.substring(unit.indexOf(prefix) + 1, unit.length());
            }
            //Without Prefix
            for (UNIT_CLASS unit_class : dictionary.keySet()) {
                List<UnitElement> unitElementList = dictionary.get(unit_class);
                for (UnitElement unitElement : unitElementList) {
                    if (unitElement.getUnit().equals(unit)) {
                        return unit_class;
                    }
                }
            }
        }
        return ret;
    }

    public boolean isSameKindUnit(String unit1, String unit2) {
        if(unit1 == null || unit2 == null)
            return  false;
        if (!UnitUtils.isComplexUnit(unit1) && !UnitUtils.isComplexUnit(unit2)) {
            //Simple unit cases
            if (getUnitClass(unit1) == UNIT_CLASS.Na || getUnitClass(unit1) == UNIT_CLASS.Na)
                return false;
            return getUnitClass(unit1).equals(getUnitClass(unit2));
        }
        if (UnitUtils.isComplexUnit(unit1) && UnitUtils.isComplexUnit(unit2)) {
            //Complex unit cases
            ComplexUnit _unit1 = UnitUtils.getComplexUnit(unit1);
            ComplexUnit _unit2 = UnitUtils.getComplexUnit(unit2);
            return isSameKindUnit(_unit1.getUnitPart1(), _unit2.getUnitPart1()) &&
                    isSameKindUnit(_unit1.getUnitPart2(), _unit2.getUnitPart2());
        }
        return false;
    }


}
