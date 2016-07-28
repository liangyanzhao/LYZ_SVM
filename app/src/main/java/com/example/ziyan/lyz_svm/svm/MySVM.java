package com.example.ziyan.lyz_svm.svm;

import com.example.ziyan.lyz_svm.util.Features;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

/**
 * Created by Ziyan on 2016.07.27.
 */
public class MySVM {
    public int featuresCount; // 特征个数
    public double scaleLower, scaleUpper; // 归一化上下界
    public double[][] scaleMap; // 每个特征上下界
    public svm_model svmModel; // SVM模型

    public MySVM(String[] ruleArr, svm_model model) {
        this.featuresCount = ruleArr.length - 2; // 特征个数
        // 读取range文件第二行
        String[] scaleBound = ruleArr[1].split(SvmUtil.SEPERATOR_SPACE);
        this.scaleLower = Double.parseDouble(scaleBound[0]); // 下界
        this.scaleUpper = Double.parseDouble(scaleBound[1]); // 上届
        // 读取每个特征上下界
        this.scaleMap = new double[featuresCount][3]; // 创建double二位数组
        for (int i = 0; i < featuresCount; i++) {
            String[] tmpList = ruleArr[i + 2].split(SvmUtil.SEPERATOR_SPACE);
            scaleMap[i][0] = Double.parseDouble(tmpList[0]);
            scaleMap[i][1] = Double.parseDouble(tmpList[1]);
            scaleMap[i][2] = Double.parseDouble(tmpList[2]);
        }
        // 保存svm模型
        this.svmModel = model;
    }

    public boolean myPredictOneData(String[] dataStr) {
/*        svm_node[] svmNode = new svm_node[featuresCount];
        for (int i = 0; i < featuresCount; i++) {
            svm_node oneNode = new svm_node();
            String[] tmpStr = dataStr[i + 1].split(SvmUtil.SEPERATOR_COLON);
            oneNode.index = Integer.parseInt(tmpStr[0]);
            oneNode.value = Features.zeroOneLibSvm(scaleLower, scaleUpper,
                    Double.parseDouble(tmpStr[1]), scaleMap[i][1], scaleMap[i][2]);
            svmNode[i] = oneNode;
        }

        double result = svm.svm_predict(svmModel, svmNode); // 预测结果*/
        double result = myGetPredictOneData(dataStr);
        return dataStr[0].equals(result + "");
    }

    public double myGetPredictOneData(String[] dataStr) {
        svm_node[] svmNode = new svm_node[featuresCount];
        for (int i = 0; i < featuresCount; i++) {
            svm_node oneNode = new svm_node();
            String[] tmpStr = dataStr[i + 1].split(SvmUtil.SEPERATOR_COLON);
            oneNode.index = Integer.parseInt(tmpStr[0]);
            oneNode.value = Features.zeroOneLibSvm(scaleLower, scaleUpper,
                    Double.parseDouble(tmpStr[1]), scaleMap[i][1], scaleMap[i][2]);
            svmNode[i] = oneNode;
        }
        return svm.svm_predict(svmModel, svmNode); // 预测结果
    }

    public double myPredict(String[] singleStr) throws IllegalArgumentException {
        if(singleStr.length<9){
            throw new IllegalArgumentException("数据格式错误，至少10列");
        }
        svm_node[] px =new svm_node[featuresCount];
        String[] tempNode;
        svm_node p;
        for(int i=0;i<featuresCount;i++){
            tempNode = singleStr[i].split(SvmUtil.SEPERATOR_COLON);
            p = new svm_node();
            p.index = Integer.parseInt(tempNode[0]);
            p.value = Features.zeroOneLibSvm(scaleLower, scaleUpper, Double.parseDouble(
                    tempNode[1]), scaleMap[i][1], scaleMap[i][2]);
            px[i] = p;
        }
        return svm.svm_predict(svmModel, px);
    }
}
