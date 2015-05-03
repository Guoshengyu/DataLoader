package caup.dataloader.learner;

import caup.dataloader.learner.model.ResultSetElement;
import caup.dataloader.unit.transformation.model.UnitElement;
import org.apache.poi.ss.usermodel.*;

import javax.print.attribute.standard.SheetCollate;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2015/05/02 .
 *
 * Reader the saved result set for one region
 * With method
 *
 * getHistoryResultSet(String filePath, String region)
 */
public class ResultSetReader {

   public List<String> getHistoryYBSelectedIndex(String filePath, String region, String DBIndex){
       List<ResultSetElement> resultSetElementList = getHistoryResultSet(filePath, region);
        for(ResultSetElement element: resultSetElementList){
            if(element.getDBIndex().equals(DBIndex))
                return element.getYBIndexSelectionResultList();
        }
        return null;
    }

    public List<String> getHistoryYBSelectedIndex(List<ResultSetElement> resultSetElementList, String DBIndex){
        for(ResultSetElement element: resultSetElementList){
            if(element.getDBIndex().equals(DBIndex))
                return element.getYBIndexSelectionResultList();
        }
        return null;
    }

    public List<ResultSetElement> getHistoryResultSet(String filePath, String region){
        List<ResultSetElement> ret = new ArrayList<ResultSetElement>();
        try{
            InputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(region);
            if(sheet == null)
                return ret;
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); ++rowIndex) {
                Row row = sheet.getRow(rowIndex);
                ResultSetElement resultSetElement = getResultSetFromExcelRow(row);
                if(resultSetElement != null && resultSetElement.getDBIndex() != null)
                    ret.add(resultSetElement);
            }
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    private ResultSetElement getResultSetFromExcelRow(Row row) {
        ResultSetElement resultSetElement = new ResultSetElement();
        List<String> YBindexList = new ArrayList<String>();
        for(int columnIndex = 0; columnIndex <= row.getLastCellNum(); ++columnIndex){
            if(columnIndex == 0) {
                Cell DBIndexCell = row.getCell(0);
                resultSetElement.setDBIndex(DBIndexCell.getStringCellValue());
                continue;
            }
            if(row.getCell(columnIndex) != null)
                YBindexList.add(row.getCell(columnIndex).getStringCellValue());
        }
        resultSetElement.setYBIndexSelectionResultList(YBindexList);
        return resultSetElement;
    }
}
