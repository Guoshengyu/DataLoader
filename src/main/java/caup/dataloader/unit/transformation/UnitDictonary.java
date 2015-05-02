package caup.dataloader.unit.transformation;

import caup.dataloader.unit.transformation.model.UnitElement;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 2015/04/24 .
 * <p/>
 * Reader file and Generate unit dictionary including normal units and prefixes
 */
public class UnitDictonary {

    static Map<UNIT_CLASS, List<UnitElement>> dictionary = new HashMap<UNIT_CLASS, List<UnitElement>>();
    static Map<String, Double> prefixDictionary = new HashMap<String, Double>();



    public static void Initialize(String dictionaryPath) {
        if (UnitDictonary.dictionary.size() != 0 && UnitDictonary.prefixDictionary.size() != 0)
            return;
        try {
               InputStream inputStream = new FileInputStream(dictionaryPath);
        //    InputStream inputStream = resource.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            generateDictionary(workbook);
            generatePrefixDictionary(workbook);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refresh(String dictionaryPath) {
        UnitDictonary.dictionary = new HashMap<UNIT_CLASS, List<UnitElement>>();
        UnitDictonary.prefixDictionary = new HashMap<String, Double>();
        UnitDictonary.Initialize(dictionaryPath);
    }
    public static Map<UNIT_CLASS, List<UnitElement>> getDictionary() {
        return UnitDictonary.dictionary;
    }

    public static Map<String, Double> getPrefixDictionary() {
        return UnitDictonary.prefixDictionary;
    }



    private static void generateDictionary(Workbook wb) {
        generateUnitClassDic(wb, "People_REN", UNIT_CLASS.People_REN);
        generateUnitClassDic(wb, "People_HU", UNIT_CLASS.People_HU);
        generateUnitClassDic(wb, "Finance_YUAN", UNIT_CLASS.Finance_YUAN);
        generateUnitClassDic(wb, "Number_GE", UNIT_CLASS.Number_GE);
        generateUnitClassDic(wb, "Ratio_PERCENTAGE", UNIT_CLASS.Ratio_PERCENTAGE);
        generateUnitClassDic(wb, "Weight_KE", UNIT_CLASS.Weight_KE);
        generateUnitClassDic(wb, "Length_MI", UNIT_CLASS.Length_MI);
        generateUnitClassDic(wb, "Time_MINUTE", UNIT_CLASS.Time_MINUTE);
        generateUnitClassDic(wb, "Area_PINGFANGMI", UNIT_CLASS.Area_PINGFANGMI);
        generateUnitClassDic(wb, "Volumn_LIFANGMI", UNIT_CLASS.Volumn_LIFANGMI);
        generateUnitClassDic(wb, "Publish_LING", UNIT_CLASS.Publish_LING);
        generateUnitClassDic(wb, "Power_WA", UNIT_CLASS.Power_WA);
        generateUnitClassDic(wb, "Temperature_SHESHIDU", UNIT_CLASS.Temperature_SHESHIDU);
    }

    private static List<UnitElement> generateUnitClassDic(Workbook wb, String unitClass, UNIT_CLASS unit_class) {
        Sheet sheet = wb.getSheet(unitClass);
        //    Row row = sheet.getRow(1);
        List<UnitElement> unitElementList = new ArrayList<UnitElement>();
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            UnitElement unitElement = getUnitElementFromEXCELRow(row);
            unitElementList.add(unitElement);
        }
        UnitDictonary.dictionary.put(unit_class, unitElementList);
        return unitElementList;
    }

    private static UnitElement getUnitElementFromEXCELRow(Row row) {
        UnitElement ret = new UnitElement();
        Cell unitNameCell = row.getCell(0);
        Cell unitValueCell = row.getCell(1);
        ret.setUnit(unitNameCell.getStringCellValue());
        ret.setValue(unitValueCell.getNumericCellValue());
        return ret;
    }

    private static void generatePrefixDictionary(Workbook wb) {
        Sheet sheet = wb.getSheet("PREFIX");
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            UnitElement unitElement = getUnitElementFromEXCELRow(row);
            UnitDictonary.prefixDictionary.put(unitElement.getUnit(), unitElement.getValue());
        }
    }

    public static String getUnitPrefix(String unit) {
        for (String prefix : UnitDictonary.prefixDictionary.keySet()) {
            if (unit.indexOf(prefix) != -1)
                return prefix;
        }
        return null;
    }


}
