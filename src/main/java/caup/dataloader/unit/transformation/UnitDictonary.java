package caup.dataloader.unit.transformation;

import caup.dataloader.unit.transformation.model.UnitElement;

import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 2015/04/24 .
 *
 * Read the unit dictionary
 *
 */
public class UnitDictonary {

    Map<UNIT_CLASS, List<UnitElement>> dictionary;
    public UnitDictonary(){
        //Reader file and Generate unit dictionary including normal units and prefixes
    }

    public UnitElement checkForUnit(String unit){
        //Prefix handler
    }
}
