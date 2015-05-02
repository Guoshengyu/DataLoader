package caup.dataloader.util;

import caup.dataloader.unit.transformation.model.ComplexUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Richard on 2015/04/22 .
 */
public final class UnitUtils {

    public static boolean isComplexUnit(String unit) {
        return unit.contains("/");
    }


    public static ComplexUnit getComplexUnit(String unit){
        ComplexUnit ret = new ComplexUnit();
        ret.setUnitPart1(unit.substring(0, unit.indexOf("/")));
        ret.setUnitPart2(unit.substring(unit.indexOf("/") + 1, unit.length()));

        return ret;
    }
}
