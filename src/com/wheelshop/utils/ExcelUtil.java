package com.wheelshop.utils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

 
/** 
 * Excel文件操作工具类，包括读、写、合并等功能 
 */  
public class ExcelUtil {  
      
	
	public static void main(String[] args) throws IOException {  
          
        List<String[]> list = new ArrayList<>();
        for(int i=0; i<6; i++){
        	String[] s = new String[2]; 
            s[0]=i+"00000";
            s[1]="111111";
            
            list.add(s);
        }
        String title[] = {"高度","worker"};  
        writer("E:/excel", "123", "xls",list,title);  
    }  
	 
    public static void writer(String path, String fileName,String fileType,List<String[]> list,String titleRow[]) throws IOException {  
        Workbook wb = null; 
        String excelPath = path+File.separator+fileName+"."+fileType;
        File file = new File(excelPath);
        Sheet sheet =null;
        //创建工作文档对象   
        if (!file.exists()) {
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook();
                
            } else if(fileType.equals("xlsx")) {
                
                    wb = new XSSFWorkbook();
            } 
            //创建sheet对象   
            sheet = (Sheet) wb.createSheet("sheet1");  
            OutputStream outputStream = new FileOutputStream(excelPath);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
            
        } else {
            if (fileType.equals("xls")) {  
                wb = new HSSFWorkbook();  
                
            } else if(fileType.equals("xlsx")) { 
                wb = new XSSFWorkbook();  
            } 
        }
         //创建sheet对象   
        if (sheet==null) {
            sheet = (Sheet) wb.createSheet("sheet1");  
        }
        //添加表头  
        Row row = sheet.createRow(0);
        Cell cell ;
        CellStyle style = wb.createCellStyle(); // 样式对象      
        // 设置单元格的背景颜色为淡蓝色  
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index); 
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直      
        style.setAlignment(CellStyle.ALIGN_CENTER);// 水平   
        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
        for(int i = 0;i < titleRow.length;i++){  
            cell = row.createCell(i);  
            cell.setCellValue(titleRow[i]);  
            cell.setCellStyle(style); // 样式，居中
            sheet.setColumnWidth(i, 20 * 256); 
        }  
        row.setHeight((short) 540); 
        //循环写入行数据   
        for (int i = 0; i < list.size(); i++) {  
            row = (Row) sheet.createRow(i+1);  
            row.setHeight((short) 500); 
            for(int index=0;index<list.get(i).length;index++){
            	row.createCell(index).setCellValue(list.get(i)[index]);
            }
        }  
        //创建文件流   
        OutputStream stream = new FileOutputStream(excelPath);  
        //写入数据   
        wb.write(stream);  
        //关闭文件流   
        stream.close();  
    }  
    public static void export(String[] titleRow, OutputStream out,List<String[]> list) {                
        try{
            // 第一步，创建一个workbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("sheet1");
            //添加表头  
            Row row = sheet.createRow(0);
            Cell cell ;
            CellStyle style = wb.createCellStyle(); // 样式对象      
            // 设置单元格的背景颜色为淡蓝色  
            style.setFillForegroundColor(HSSFColor.PALE_BLUE.index); 
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直      
            style.setAlignment(CellStyle.ALIGN_CENTER);// 水平   
            style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
            for(int i = 0;i < titleRow.length;i++){  
                cell = row.createCell(i);  
                cell.setCellValue(titleRow[i]);  
                cell.setCellStyle(style); // 样式，居中
                sheet.setColumnWidth(i, 15 * 256); 
            }  
            row.setHeight((short) 450); 
            //循环写入行数据   
            for (int i = 0; i < list.size(); i++) {  
                row = (Row) sheet.createRow(i+1);  
                row.setHeight((short) 400); 
                for(int index=0;index<list.get(i).length;index++){
                	row.createCell(index).setCellValue(list.get(i)[index]);
                }
            }  
            
            // 第七步，将文件输出到客户端浏览器
            try {
                wb.write(out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace(); 
        }    
    }
    public static void exportRange(String[] titleRow1,CellRangeAddress[] range1,
    		String[] titleRow2,CellRangeAddress[] range2, OutputStream out,List<String[]> list) {                
    	try{
    		// 第一步，创建一个workbook，对应一个Excel文件
    		HSSFWorkbook wb = new HSSFWorkbook();
    		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
    		HSSFSheet sheet = wb.createSheet("sheet1");
    	 
    		//添加表头  
    		Row row = sheet.createRow(0);
    		Cell cell ;
    		CellStyle style = wb.createCellStyle(); // 样式对象      
    		// 设置单元格的背景颜色为淡蓝色  
    		style.setFillForegroundColor(HSSFColor.PALE_BLUE.index); 
    		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直      
    		style.setAlignment(CellStyle.ALIGN_CENTER);// 水平   
    		style.setWrapText(true);// 指定当单元格内容显示不下时自动换行
    		 
		
    		for(int i = 0;i < titleRow1.length;i++){  
    			sheet.addMergedRegion(range1[i]); 
    			cell = row.createCell(range1[i].getFirstColumn());  
    			cell.setCellValue(titleRow1[i]);  
    			cell.setCellStyle(style); // 样式，居中
    			sheet.setColumnWidth(range1[i].getFirstColumn(), 15 * 256); 
    		}  
    		row.setHeight((short) 450); 
    		
    		row = sheet.createRow(1);
    		for(int i = 0;i < titleRow2.length;i++){  
    			//sheet.addMergedRegion(range2[i]); 
    			cell = row.createCell(range2[i].getFirstColumn());  
    			cell.setCellValue(titleRow2[i]);  
    			cell.setCellStyle(style); // 样式，居中
    			sheet.setColumnWidth(range2[i].getFirstColumn(), 15 * 256); 
    		}  
    		row.setHeight((short) 450); 
    		
    		//循环写入行数据   
    		for (int i = 0; i < list.size(); i++) {  
    			row = (Row) sheet.createRow(i+2);  
    			row.setHeight((short) 400); 
    			for(int index=0;index<list.get(i).length;index++){
    				row.createCell(index).setCellValue(list.get(i)[index]);
    			}
    		}  
    		
    		// 第七步，将文件输出到客户端浏览器
    		try {
    			wb.write(out);
    			out.flush();
    			out.close();
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}catch(Exception e){
    		e.printStackTrace(); 
    	}    
    }
}  
