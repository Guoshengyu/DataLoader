package caup.dataloader.learner;

import caup.dataloader.searcher.model.SelectionResultFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Richard on 2015/05/02 .
 */
public class ResultSetWriter {

    public void writeResultSet(String filePath, String region, List<SelectionResultFormat> selectionResultList){
        try{
            FileInputStream fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(region);
            if(sheet == null){
            //First time to write the sheet
                sheet = workbook.createSheet(region);
                writeHeader(sheet);
                writeResultForFirstTime(sheet, selectionResultList);
            } else {
            //There are existing results for searching
                writeResultForExistingHistoryResult(sheet, selectionResultList);
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void writeResultForExistingHistoryResult(Sheet sheet, List<SelectionResultFormat> selectionResultList) {
        for(int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); ++rowIndex){
            Row row = sheet.getRow(rowIndex);
            if (row == null)
                continue;
            Cell cell0 = row.getCell(0);
            String currentDBIndex = cell0.getStringCellValue();
            String currentYBIndex = searchInSelectionResult(selectionResultList, currentDBIndex);
            if(currentYBIndex.trim().toUpperCase().equals("NULL"))
                return;
            for(int columnIndex = 1; columnIndex <= row.getLastCellNum(); ++ columnIndex){
                Cell cell = row.getCell(columnIndex);
                if (cell == null)
                    continue;
                String historyYBIndex = cell.getStringCellValue();
                if(historyYBIndex.equals(currentYBIndex) )
                    break;
                if(columnIndex + 1 == row.getLastCellNum() && !historyYBIndex.equals(currentYBIndex)){
                    Cell newCell = row.createCell(++columnIndex);
                    newCell.setCellValue(currentYBIndex);
                }
            }
        }
    }

    private void writeHeader(Sheet sheet) {
        Row firstRow = sheet.createRow(0);
        Cell cell0 = firstRow.createCell(0);
        cell0.setCellValue("DBIndex");
        Cell cell1 = firstRow.createCell(1);
        cell1.setCellValue("YBResultList");
    }

    private void writeResultForFirstTime(Sheet sheet, List<SelectionResultFormat> selectionResultList) {
        for(int rowIndex = 1; rowIndex <= selectionResultList.size(); ++rowIndex){
            Row newRow = sheet.createRow(rowIndex);
            Cell cell0 = newRow.createCell(0);
            SelectionResultFormat selectionResult = selectionResultList.get(rowIndex - 1);
            cell0.setCellValue(selectionResult.getDBIndex());
            String currentYBIndex = selectionResult.getYBIndex();
            if(currentYBIndex.trim().toUpperCase().equals("NULL"))
                continue;
            Cell cell1 = newRow.createCell(1);
            cell1.setCellValue(currentYBIndex);
        }
    }

    private String searchInSelectionResult(List<SelectionResultFormat> selectionResultList, String DBIndex){
        for(SelectionResultFormat selectionResult: selectionResultList){
            if(selectionResult.getDBIndex().equals(DBIndex))
                return selectionResult.getYBIndex();
        }
        return null;
    }


}
