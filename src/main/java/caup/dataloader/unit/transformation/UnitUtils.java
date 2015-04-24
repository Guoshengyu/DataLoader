package caup.dataloader.unit.transformation;

import caup.dataloader.unit.transformation.model.ComplexUnit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Richard on 2015/04/22 .
 */
public final class UnitUtils {

    public static boolean isSameKindUnit(String unit1, String unit2){
        if(unit1 == null || unit2 == null || unit1.toLowerCase().equals("null") || unit2.toLowerCase().equals("null"))
            return false;
        for (int i = 0; i != unit1.length(); ++i){
            if (unit2.indexOf(unit1.charAt(i)) != -1)
                return true;
        }
        return false;
    }

    /*
     * Exclude prefixes like 千，万...
     */
    public static String excludePrefix(String unit){

    }

    public static multiplierOfPrefix()
    public static boolean isSimpleUnit(String unit){

    }

    public static ComplexUnit getComplexUnit(String unit){
        ComplexUnit ret = new ComplexUnit();

        return ret;
    }
}
