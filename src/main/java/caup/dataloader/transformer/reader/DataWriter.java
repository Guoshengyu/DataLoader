package caup.dataloader.transformer.reader;

import caup.dataloader.transformer.reader.DataModel.InputDataFormat;
import caup.dataloader.transformer.reader.DataModel.OutputDataFormat;
import caup.dataloader.transformer.reader.DataModel.SelectionResultFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Richard on 2015/03/31 .
 */
public class DataWriter {

    //For new input version
    public String writeResultEXCELNew(String strJsonResult, String fileRealPath) {
        List<OutputDataFormat> outputDataFormatList = new ArrayList<OutputDataFormat>();
        List<SelectionResultFormat> resultFormat = getResultFormat(strJsonResult);
        System.out.println(resultFormat.toString());
        File  file = new File(fileRealPath.substring(0, fileRealPath.lastIndexOf(".")) + "-output" + fileRealPath.substring(fileRealPath.lastIndexOf(".")));

        try {
            //The number below doesn't make sense
            DataReader reader = new DataReader(fileRealPath, 1, 100, 2, true);
            List<InputDataFormat> inputDataFormatList = reader.getYearBookIndexListNew();


            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("result");

            for(SelectionResultFormat selectionResultFormat: resultFormat){
                OutputDataFormat outputDataFormat = new OutputDataFormat();
                Map<String, Double> resultMap = new TreeMap<String, Double>();
                outputDataFormat.setIndexName(selectionResultFormat.getDBIndex());
                List<InputDataFormat> searchResult = searchForYBNew(selectionResultFormat.getYBIndex(), inputDataFormatList);
                //Test
                if(selectionResultFormat.getYBIndex().equals("工业主要经济指标_工业增加值(生产法)_总计")){
                    searchResult.get(0).getyValueMap();
                }

                //~Test
                for(InputDataFormat format: searchResult){
                    for(Map.Entry<String, Double> entry: format.getyValueMap().entrySet())
                        resultMap.put(entry.getKey(), entry.getValue());
                }
                outputDataFormat.setyValueMap(resultMap);
                outputDataFormatList.add(outputDataFormat);
            }


            //Create the first header row
            Set<String> timeList = getAllKeysFromOutputFormats(outputDataFormatList);
            List<String> headerList = new ArrayList<String>(createHeaderRowNew(timeList, sheet));
            Collections.sort(headerList);
            //Create other rows
            for (int rowIndex = 1; rowIndex != resultFormat.size(); ++rowIndex) {
                Row newRow = sheet.createRow(rowIndex);
                InputDataFormat inputDataFormat = searchForYB(resultFormat.get(rowIndex - 1).getYBIndex(), inputDataFormatList);
                // int columnIndex = 0;
                Cell newCell0 = newRow.createCell(0);
                newCell0.setCellValue(resultFormat.get(rowIndex - 1).getDBIndex());
                int columnIndex = 1;
                for (String header : headerList) {
                    Cell newCell = newRow.createCell(columnIndex++);
                    if (inputDataFormat.getyValueMap() != null && inputDataFormat.getyValueMap().containsKey(header))
                        newCell.setCellValue(inputDataFormat.getyValueMap().get(header));
                    else
                        newCell.setCellValue(0.0);
                }
            }


            OutputStream outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getName();
    }

    //For old version input
    public String writeResultEXCELOld(String strJsonResult, String fileRealPath) {
        List<SelectionResultFormat> resultFormat = getResultFormat(strJsonResult);
        System.out.println(resultFormat.toString());
        File  file = new File(fileRealPath.substring(0, fileRealPath.lastIndexOf(".")) + "-output" + fileRealPath.substring(fileRealPath.lastIndexOf(".")));
        try {
            DataReader reader = new DataReader(fileRealPath, 1, 100, 2, true);
            List<InputDataFormat> inputDataFormatList = reader.getYearBookIndexListNew();


            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("result");
            //Create the first header row
            List<String> headerList = new ArrayList<String>(createHeaderRow(inputDataFormatList, sheet));
            Collections.sort(headerList);
            //Create other rows
            for (int rowIndex = 1; rowIndex != resultFormat.size(); ++rowIndex) {
                Row newRow = sheet.createRow(rowIndex);
                InputDataFormat inputDataFormat = searchForYB(resultFormat.get(rowIndex - 1).getYBIndex(), inputDataFormatList);
                // int columnIndex = 0;
                Cell newCell0 = newRow.createCell(0);
                newCell0.setCellValue(resultFormat.get(rowIndex - 1).getDBIndex());
                int columnIndex = 1;
                for (String header : headerList) {
                    Cell newCell = newRow.createCell(columnIndex++);
                    if (inputDataFormat.getyValueMap() != null && inputDataFormat.getyValueMap().containsKey(header))
                        newCell.setCellValue(inputDataFormat.getyValueMap().get(header));
                    else
                        newCell.setCellValue(0.0);
                }
            }


            OutputStream outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getName();
    }

    private Set<String> createHeaderRow(List<InputDataFormat> inputDataFormatList, XSSFSheet sheet) {

        Row firstRow = sheet.createRow(0);
        Cell _cell = firstRow.createCell(0);
        _cell.setCellValue("DBIndex");

        Set<String> headerList = inputDataFormatList.get(0).getyValueMap().keySet();
        int _columnIndex = 1;
        for (String header : headerList) {
            _cell = firstRow.createCell(_columnIndex++);
            _cell.setCellValue(header);
        }
        return headerList;
    }

    private Set<String> createHeaderRowNew(Set<String> yearList, XSSFSheet sheet) {

        Row firstRow = sheet.createRow(0);
        Cell _cell = firstRow.createCell(0);
        _cell.setCellValue("DBIndex");

        Set<String> headerList = yearList;
        int _columnIndex = 1;
        for (String header : headerList) {
            _cell = firstRow.createCell(_columnIndex++);
            _cell.setCellValue(header);
        }
        return headerList;
    }

    private List<InputDataFormat> searchForYBNew(String YBIndex, List<InputDataFormat> inputDataFormatList) {
        List<InputDataFormat> ret = new ArrayList<InputDataFormat>();
        for (InputDataFormat inputDataFormat : inputDataFormatList) {
            if (inputDataFormat.getIndexName().equals(YBIndex))
                ret.add(inputDataFormat);
        }
        return ret;
    }

    private Set<String> getAllKeysFromOutputFormats(List<OutputDataFormat> outputDataFormatList){
        Set<String> ret = new HashSet<String>();
        for(OutputDataFormat outputDataFormat: outputDataFormatList){
            for(String str: outputDataFormat.getyValueMap().keySet())
                ret.add(str);
        }
        return ret;
    }

    private InputDataFormat searchForYB(String YBIndex, List<InputDataFormat> inputDataFormatList) {
        for (InputDataFormat inputDataFormat : inputDataFormatList) {
            if (inputDataFormat.getIndexName().equals(YBIndex))
                return inputDataFormat;
        }
        return new InputDataFormat();
    }

    private List<SelectionResultFormat> getResultFormat(String jsonStr) {
        List<SelectionResultFormat> ret = new ArrayList<SelectionResultFormat>();
        JSONArray jsonArray = new JSONArray(jsonStr);
        for (int i = 0; i != jsonArray.length(); ++i) {
            ret.add(new SelectionResultFormat(jsonArray.getJSONObject(i).getString("DBIndex"), jsonArray.getJSONObject(i).getString("DBUnit"), jsonArray.getJSONObject(i).getString("YBIndex"), jsonArray.getJSONObject(i).getString("YBUnit")));
        }
        return ret;
    }
}
