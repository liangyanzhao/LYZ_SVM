package com.example.ziyan.lyz_svm.svm;

import com.example.ziyan.lyz_svm.util.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ziyan on 2016.07.27.
 */
public class SvmUtil {
    public static final String SEPERATOR_SPACE = " ";
    public static final String SEPERATOR_COLON = ":";

    /**
     * 读取文件到String
     * @param inputStream
     * @return
     */
    public static String myReadFileToString(InputStream inputStream) {
        InputStreamReader isr = null;
        BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。
        // 因为BufferedReader有缓冲的，而InputStreamReader没有。
        String str = "";
        try {
            // 从文件系统中的某个文件中获取字节
            isr = new InputStreamReader(inputStream); // InputStreamReader 是字节流通向字符流的桥梁
            br = new BufferedReader(isr); // 从字符输入流中读取文件中的内容,封装了一个new
            String line =""; // InputStreamReader的对象
            while ((line = br.readLine()) != null) {
                str += line + Util.getChangeRow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br!=null) {
                    br.close();
                }
                if (isr!=null) {
                    isr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    /**
     * 读取文件到String数组
     * @param inputStream
     * @return
     */
    public static String[] myReadFileToArr(InputStream inputStream) {
        String str = myReadFileToString(inputStream);
        return str.split(Util.getChangeRow());
    }

    /**
     * 读取数据成String数组
     * @param accArr
     * @return
     */
    public static String[] dataToFeaturesArr(double[] accArr){
        String[] featuresArr=new String[9];
        double min= Features.minimum(accArr);
        featuresArr[0]= Constant.FUN_101_MINIMUM_CODE+":"+min;
        double max= Features.maximum(accArr);
        featuresArr[1]= Constant.FUN_102_MAXIMUM_CODE+":"+max;
        double var= Features.variance(accArr);
        featuresArr[2]= Constant.FUN_103_VARIANCE_CODE+":"+var;
        double mcr= Features.meanCrossingsRate(accArr);
        featuresArr[3]= Constant.FUN_104_MEANCROSSINGSRATE_CODE+":"+mcr;
        double std= Features.standardDeviation(accArr);
        featuresArr[4]= Constant.FUN_105_STANDARDDEVIATION_CODE+":"+std;
        double mean= Features.mean(accArr);
        featuresArr[5]= Constant.FUN_106_MEAN_CODE+":"+mean;
        double rms= Features.rms(accArr);
        featuresArr[6]= Constant.FUN_112_RMS_CODE+":"+rms;
        double iqr= Features.iqr(accArr);
        featuresArr[7]= Constant.FUN_114_IQR_CODE+":"+iqr;
        double mad= Features.mad(accArr);
        featuresArr[8]= Constant.FUN_115_MAD_CODE+":"+mad;
        return featuresArr;
    }
}
