package caup.dataloader.transformer.reader;

import caup.dataloader.transformer.reader.DataModel.InputDataFormat;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Richard on 2015/03/23 .
 */
public class DataReader {

    String filePath;
    int yearbookIndexColumn;
    int yearbookUnitColumn;
    int yearbookValueStartColumn;
    boolean withFirstRowHeader;

    public DataReader(String _filePath, int _yearbookIndexColumn, int _yearbookUnitColumn, int _yearbookValueStartColumn, boolean _withFirstRowHeader){
        filePath = _filePath;
        yearbookIndexColumn = _yearbookIndexColumn;
        yearbookUnitColumn = _yearbookUnitColumn;
        withFirstRowHeader = _withFirstRowHeader;
        yearbookValueStartColumn = _yearbookValueStartColumn;
    }

    public List<InputDataFormat> getYearBookIndexList() throws Exception{
        InputStream inputStream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(inputStream);
        List<InputDataFormat> ret = new ArrayList<InputDataFormat>();

        Sheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        for (int rowIndex = 1; rowIndex != sheet.getLastRowNum(); ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            InputDataFormat inputDataFormat = getInputDataFromRow(row, firstRow);
            ret.add(inputDataFormat);
        }
        return ret;
    }

    private InputDataFormat getInputDataFromRow(Row row, Row headerRow){
        Cell yearbookIndexCell = null;
        Cell yearbookUnitCell = null;
        Cell yearbookValueCell = null;
        Map<String, Double> yearbookValue = new HashMap<String, Double>();
        InputDataFormat ret = new InputDataFormat();

        yearbookIndexCell = row.getCell(yearbookIndexColumn);
        yearbookUnitCell = row.getCell(yearbookUnitColumn);

        ret.setIndexName((yearbookIndexCell == null || yearbookIndexCell.getStringCellValue().isEmpty())? "NULL": yearbookIndexCell.getStringCellValue());
        ret.setUnit((yearbookUnitCell == null || yearbookUnitCell.getStringCellValue().isEmpty()) ? "NULL" : yearbookUnitCell.getStringCellValue());
        for(int columnNum = yearbookValueStartColumn; columnNum != row.getLastCellNum(); ++columnNum){
            yearbookValueCell = row.getCell(columnNum);
           // headerRow.getCell(columnNum).getStringCellValue();
            if(yearbookValueCell.getCellType() == Cell.CELL_TYPE_STRING) {
                try {
                    yearbookValue.put(headerRow.getCell(columnNum).getStringCellValue(), yearbookValueCell.getStringCellValue() == null || yearbookValueCell.getStringCellValue().equals("null") ?
                            0 : Double.valueOf(replaceSpecialtyStr(yearbookValueCell.getStringCellValue().trim().replaceAll(" ", ""), "&|[\uFE30-\uFFA0]|‘’|“”", "")));
                }catch (NumberFormatException e){
                    yearbookValue.put(headerRow.getCell(columnNum).getStringCellValue(), 0.0);
                }
            }
            else if (yearbookValueCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                yearbookValue.put(headerRow.getCell(columnNum).getStringCellValue(), yearbookValueCell.getNumericCellValue() == 0? 0: yearbookValueCell.getNumericCellValue());
        }
        ret.setyValueMap(yearbookValue);
        return ret;
    }

    private  String replaceSpecialtyStr(String str,String pattern,String replace){
        if(isBlankOrNull(pattern))
            pattern="\\s*|\t|\r|\n";//去除字符串中空格、换行、制表
        if(isBlankOrNull(replace))
            replace="";
        return Pattern.compile(pattern).matcher(str).replaceAll(replace);

    }

    public  boolean isBlankOrNull(String str){
        if(null==str)return true;
        //return str.length()==0?true:false;
        return str.length()==0;
    }
    /**清除数字和空格*/
    public   String cleanBlankOrDigit(String str){
        if(isBlankOrNull(str))return "null";
        return Pattern.compile("\\d|\\s").matcher(str).replaceAll("");
    }

}
