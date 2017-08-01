package com.youdy.mo.mochart.utils;

import com.youdy.mo.mochart.model.Course;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 2017/6/27.
 */
public class ExcelHandler {
    public List<Course>[] readExcel(FileInputStream is) {
        List<Course>[] courses = new ArrayList[20];
        for(int i = 0;i < 20;i++){
            courses[i] = new ArrayList<>();
        }
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            if(hssfSheet != null){
                for(int colNum = 1;colNum < 6;colNum++){
                    for(int rowNum = 3; rowNum < hssfSheet.getLastRowNum();rowNum++){
                        HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                        Cell cell = hssfRow.getCell(colNum);
                        if(cell != null){
                            String info = cell.toString();
                            String[] sArray = info.split("\n");
                            for(int i = 1;i < sArray.length; i+=5){
                                Course course =new Course(0,sArray[i],sArray[i+3],sArray[i+1],colNum,new int[]{(rowNum-3)*2+1,(rowNum-2)*2});
                                String[] weekLast = sArray[i+2].replace("[周]","").split("-");
                                for(int j = Integer.parseInt(weekLast[0])-1;j < Integer.parseInt(weekLast[1]);j++){
                                    courses[j].add(course);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }
    public static void writeExcel(String filePath) {

    }
}
