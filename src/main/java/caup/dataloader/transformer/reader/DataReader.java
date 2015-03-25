package caup.dataloader.transformer.reader;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Richard on 2015/03/23 .
 */
public class DataReader {

    public static void main(String[] Args) {
        try {
            InputStream inputStream = new FileInputStream("D:\\IntelliJWorkspace\\DataLoader\\src\\main\\webapp\\datafile\\Jiaxing-shiqu-Test.xlsx");
            Workbook wb = WorkbookFactory.create(inputStream);

            System.out.println("fsfsf");
            Cell cell = null;
            Sheet sheet = wb.getSheetAt(0);
            for (int rowIndex = 0; rowIndex != sheet.getLastRowNum(); ++rowIndex) {
                Row row = sheet.getRow(rowIndex);
                cell = row.getCell(1);
                //  cell.setCellType(Cell.CELL_TYPE_STRING);
                if (cell != null)
                    System.out.println(cell.getStringCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
