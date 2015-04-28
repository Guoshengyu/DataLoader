package caup.dataloader.unit.transformation;

import caup.dataloader.core.searcher.DataModel.SelectionDataFormat;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Richard on 2015/04/22 .
 */
public class SearchResultSorter {

    public Map<String, String> sortSelectionFormatByUnit(Map<String, String> yearbookIndexUnitMap, String DBUnit){
        Map<String, String> ret = new LinkedHashMap<String, String>();
      //  UnitDictonary.Initialize();
        UnitClassifer classifer = new UnitClassifer();
        for(String ybIndex: yearbookIndexUnitMap.keySet()){
            String ybUnit = yearbookIndexUnitMap.get(ybIndex);

            if(classifer.isSameKindUnit(ybUnit, DBUnit))
                ret.put(ybIndex, ybUnit);
        }
        for(String ybIndex: yearbookIndexUnitMap.keySet()){
            String ybUnit = yearbookIndexUnitMap.get(ybIndex);
            if(!ret.containsKey(ybIndex))
                ret.put(ybIndex, ybUnit);
        }
        return ret;
    }
}
