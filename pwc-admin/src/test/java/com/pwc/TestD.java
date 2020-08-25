package com.pwc;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestD {
    @Test
    public void testDate(){
        System.out.println(DateUtil.today());
    }


    @Test
    public void testClasspath() {
        System.out.println(ClassUtil.getClassPath());
    }

    @Test
    public void testT() {
        String s = "123300";
        String str2 = s.replaceAll("(0)+$", "");
        System.out.println(str2);

        List list = new ArrayList();
        System.out.println(CollUtil.isEmpty(list));
        String createDate = "2020年05月29日";
        System.out.println(DateUtil.format(DateUtil.parseDate(createDate), "yyyy-MM-dd"));
    }

//    @Test
//    public void testExcel() {
//        ExcelWriter writer = ExcelUtil.getWriter("/Users/zk/Desktop/testfunc.xlsx", "Sheet2");
//        writer.setDestFile(new File("/Users/zk/Desktop/testfunc.xlsx"));
//        CellStyle cellStyle = writer.getCellStyle();
//        writer.writeCellValue(0,0,456);
//        writer.writeCellValue(1,0,789);
//        writer.setStyle(cellStyle,0,0);
//        writer.setStyle(cellStyle,1,0);
//        writer.flush();
//    }

    public static void main(String []args){
        int []arr = {9,8,7,6,5,4,3,2,1,10,11,12,30,14,29,48,49,28,58,17,44,22,23,43,98,59,87,85,82,37};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void sort(int []arr){
        int []temp = new int[arr.length];//在排序前，先建好一个长度等于原数组长度的临时数组，避免递归中频繁开辟空间
        sort(arr,0,arr.length-1,temp);
    }
    private static void sort(int[] arr,int left,int right,int []temp){
        if(left<right){
            int mid = (left+right)/2;
            sort(arr,left,mid,temp);//左边归并排序，使得左子序列有序
            sort(arr,mid+1,right,temp);//右边归并排序，使得右子序列有序
            merge(arr,left,mid,right,temp);//将两个有序子数组合并操作
        }
    }
    private static void merge(int[] arr,int left,int mid,int right,int[] temp){
        int index = 0;
        int i = left;//左序列指针
        int j = mid+1;//右序列指针
        int t = 0;//临时数组指针
        while (i<=mid && j<=right){
            if(arr[i]<=arr[j]){
                temp[t++] = arr[i++];
            }else {
                temp[t++] = arr[j++];
            }
        }
        while(i<=mid){//将左边剩余元素填充进temp中
            temp[t++] = arr[i++];
            System.out.println(index++);
        }
        while(j<=right){//将右序列剩余元素填充进temp中
            temp[t++] = arr[j++];
        }
        t = 0;
        //将temp中的元素全部拷贝到原数组中
        while(left <= right){
            arr[left++] = temp[t++];
        }
    }
}
