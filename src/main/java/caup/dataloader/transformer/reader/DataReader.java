package caup.dataloader.transformer.reader;

import caup.dataloader.transformer.reader.DataModel.ExcelInputDataFormat;
import caup.dataloader.util.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Richard on 2015/03/23 .
 */
public class DataReader {

    String filePath;
    int yearbookIndexColumn;
    int yearbookUnitColumn;
    int yearbookValueStartColumn;
    boolean withFirstRowHeader;

    public DataReader(String _filePath, int _yearbookIndexColumn, int _yearbookUnitColumn, int _yearbookValueStartColumn, boolean _withFirstRowHeader) {
        filePath = _filePath;
        yearbookIndexColumn = _yearbookIndexColumn;
        yearbookUnitColumn = _yearbookUnitColumn;
        withFirstRowHeader = _withFirstRowHeader;
        yearbookValueStartColumn = _yearbookValueStartColumn;
    }

    public List<ExcelInputDataFormat> getYearBookIndexListNew() throws Exception {
        InputStream inputStream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(inputStream);
        List<ExcelInputDataFormat> ret = new ArrayList<ExcelInputDataFormat>();

        Sheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        for (int rowIndex = 0; rowIndex != sheet.getLastRowNum(); ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            ExcelInputDataFormat excelInputDataFormat = getInputDataFromRowNew(row, firstRow);
            ret.add(excelInputDataFormat);
        }
        inputStream.close();
        //Test
        for(ExcelInputDataFormat format: ret) {
            if(format.getYBIndex().equals("工业主要经济指标_工业增加值(生产法)_总计"))
                format.getyValueMap();
        }
        //~Test


        return getAggregateInputFormats(ret);

    }

    private ExcelInputDataFormat getInputDataFromRowNew(Row row, Row headerRow) {

        ExcelInputDataFormat ret = new ExcelInputDataFormat();
        Cell regionCell = row.getCell(0);
        Cell yearbookIndexCell1 = row.getCell(1);
        Cell yearbookIndexCell2 = row.getCell(3);
        Cell yearbookUnitCell = row.getCell(5);
        Cell yearbookYearCell = row.getCell(2);
        Cell yearbookValueCell = row.getCell(4);

        if (regionCell != null && regionCell.getCellType() == Cell.CELL_TYPE_STRING)
            ret.setRegion((regionCell == null || regionCell.getStringCellValue().isEmpty()) ? "NULL" : regionCell.getStringCellValue());
        else
            ret.setRegion("NULL");

        if (yearbookUnitCell != null && yearbookUnitCell.getCellType() == Cell.CELL_TYPE_STRING)
            ret.setUnit((yearbookUnitCell == null || yearbookUnitCell.getStringCellValue().isEmpty()) ? "NULL" : yearbookUnitCell.getStringCellValue());
        else
            ret.setUnit("NULL");
        //For index name
        String indexName, indexName1, indexName2 = new String();
        if (yearbookIndexCell1 != null && yearbookIndexCell1.getCellType() == Cell.CELL_TYPE_STRING)
            indexName1 = (yearbookIndexCell1 == null || yearbookIndexCell1.getStringCellValue().isEmpty()) ? "NULL" : yearbookIndexCell1.getStringCellValue();
        else
            indexName1 = "NULL";

        if (yearbookIndexCell2 != null && yearbookIndexCell2.getCellType() == Cell.CELL_TYPE_STRING)
            indexName2 = (yearbookIndexCell2 == null || yearbookIndexCell2.getStringCellValue().isEmpty()) ? "NULL" : yearbookIndexCell2.getStringCellValue();
        else
            indexName2 = "NULL";

        if (indexName1.equals("NULL") && indexName2.equals("NULL")) {
            indexName = "NULL";
        } else if (indexName1.equals("NULL") || indexName2.equals("NULL")) {
            indexName = indexName1.equals("NULL") ? indexName2 : indexName1;
        } else {
            indexName = indexName1 + "_" + indexName2;
        }
        indexName = indexName.replaceAll("[0-9]{1,2}-[0-9]{1,2}", "").trim();
        indexName = indexName.replaceAll("续表 [0-9]{1,2}", "").trim();
        indexName = indexName.replaceAll("续表[0-9]{1,2}", "").trim();
        ret.setYBIndex(indexName);
        //For year & unit
        Map<String, Double> yearbookValue = new HashMap<String, Double>();
        String year = new String();
        if (yearbookYearCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
            year = (yearbookYearCell == null) ? "NULL" : Double.valueOf(yearbookYearCell.getNumericCellValue()).toString();
        else
            year = (yearbookYearCell == null || yearbookYearCell.getStringCellValue().isEmpty()) ? "NULL" : yearbookYearCell.getStringCellValue();

        // String value = (yearbookValueCell == null || yearbookValueCell.getStringCellValue().isEmpty()) ? "NULL" : yearbookValueCell.getStringCellValue();
        if (yearbookValueCell != null && yearbookValueCell.getCellType() == Cell.CELL_TYPE_STRING) {
            try {
                yearbookValue.put(year, yearbookValueCell.getStringCellValue() == null || yearbookValueCell.getStringCellValue().equals("null") ?
                        0.0 : Double.valueOf(StringUtils.replaceSpecialtyStr(yearbookValueCell.getStringCellValue().trim().replaceAll(" ", ""), StringUtils.pattern, "")));
            } catch (NumberFormatException e) {
                yearbookValue.put(year, 0.0);
            }
        } else if (yearbookValueCell != null && yearbookValueCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            yearbookValue.put(year, yearbookValueCell.getNumericCellValue() == 0.0 ? 0.0 : yearbookValueCell.getNumericCellValue());
        }
        ret.setyValueMap(yearbookValue);
        return ret;
    }

    public List<ExcelInputDataFormat> getYearBookIndexListOld() throws Exception {
        InputStream inputStream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(inputStream);
        List<ExcelInputDataFormat> ret = new ArrayList<ExcelInputDataFormat>();

        Sheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        for (int rowIndex = 1; rowIndex != sheet.getLastRowNum(); ++rowIndex) {
            Row row = sheet.getRow(rowIndex);
            ExcelInputDataFormat excelInputDataFormat = getInputDataFromRowOld(row, firstRow);
            ret.add(excelInputDataFormat);
        }
        inputStream.close();
        return ret;
    }

    public List<String> getHeader() throws Exception {
        InputStream inputStream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(inputStream);
        List<String> ret = new ArrayList<String>();

        Sheet sheet = wb.getSheetAt(0);
        Row firstRow = sheet.getRow(0);
        for (int columnIndex = yearbookValueStartColumn + 1; columnIndex != firstRow.getLastCellNum(); ++columnIndex) {
            Cell cell = firstRow.getCell(columnIndex);
            String str = cell.getStringCellValue();
            ret.add(str);
        }
        inputStream.close();
        return ret;
    }

    /**
     * Read one row from the excel to set the InputDataFormat data model with the first row defined as the header
     */
    private ExcelInputDataFormat getInputDataFromRowOld(Row row, Row headerRow) {
        Cell yearbookIndexCell = null;
        Cell yearbookUnitCell = null;
        Cell yearbookValueCell = null;
        Map<String, Double> yearbookValue = new HashMap<String, Double>();
        ExcelInputDataFormat ret = new ExcelInputDataFormat();

        yearbookIndexCell = row.getCell(yearbookIndexColumn);
        yearbookUnitCell = row.getCell(yearbookUnitColumn);

        ret.setYBIndex((yearbookIndexCell == null || yearbookIndexCell.getStringCellValue().isEmpty()) ? "NULL" : yearbookIndexCell.getStringCellValue());
        ret.setUnit((yearbookUnitCell == null || yearbookUnitCell.getStringCellValue().isEmpty()) ? "NULL" : yearbookUnitCell.getStringCellValue());
        for (int columnNum = yearbookValueStartColumn; columnNum != row.getLastCellNum(); ++columnNum) {
            yearbookValueCell = row.getCell(columnNum);
            if (yearbookValueCell.getCellType() == Cell.CELL_TYPE_STRING) {
                try {
                    yearbookValue.put(headerRow.getCell(columnNum).getStringCellValue(), yearbookValueCell.getStringCellValue() == null || yearbookValueCell.getStringCellValue().equals("null") ?
                            0.0 : Double.valueOf(StringUtils.replaceSpecialtyStr(yearbookValueCell.getStringCellValue().trim().replaceAll(" ", ""), StringUtils.pattern, "")));
                } catch (NumberFormatException e) {
                    yearbookValue.put(headerRow.getCell(columnNum).getStringCellValue(), 0.0);
                }
            } else if (yearbookValueCell.getCellType() == Cell.CELL_TYPE_NUMERIC)
                yearbookValue.put(headerRow.getCell(columnNum).getStringCellValue(), yearbookValueCell.getNumericCellValue() == 0.0 ? 0.0 : yearbookValueCell.getNumericCellValue());
        }
        ret.setyValueMap(yearbookValue);
        return ret;
    }

    private List<ExcelInputDataFormat> getAggregateInputFormats(List<ExcelInputDataFormat> excelInputDataFormatList){
        List<ExcelInputDataFormat> ret = new ArrayList<ExcelInputDataFormat>();
        for(ExcelInputDataFormat excelInputDataFormat1 : excelInputDataFormatList){
            for(ExcelInputDataFormat excelInputDataFormat2 : excelInputDataFormatList){
                if(excelInputDataFormat1.getYBIndex().equals(excelInputDataFormat2.getYBIndex())){
                    for(String key: excelInputDataFormat2.getyValueMap().keySet())
                        excelInputDataFormat1.getyValueMap().put(key, excelInputDataFormat2.getyValueMap().get(key) );
                }

            }
            ret.add(excelInputDataFormat1);
        }
        return ret;
    }
}
