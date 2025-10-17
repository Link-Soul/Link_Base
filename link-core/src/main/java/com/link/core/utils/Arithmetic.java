package com.link.core.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Stack;

/**
 *@Description 运算的一个工具类
 *@author Link
 *@since 2025/10/13 15:40
 **/

public class Arithmetic {

    public Arithmetic() {

    }

    /* 操作区 */
    private static double operation(double n1, double n2, String expression) {
        if (expression.equals("+")) {
            return n1 + n2;
        } else if (expression.equals("-")) {
            return n1 - n2;
        } else if (expression.equals("*")) {
            return n1 * n2;
        } else if (expression.equals("/")) {
            return n1 / n2;
        }
        return 0;
    }

    /* 优先级 */
    private static int priority(String expression) {
        if (expression.equals("+") || expression.equals("-")) {
            return 0;
        } else if (expression.equals("*") || expression.equals("/")) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * 判断是否为一个算数运算符
     *
     * @param expression
     *            算式运算符
     * @return
     */
    private static boolean isExpression(String expression) {
        return expression.equals("+") || expression.equals("-") || expression.equals("*") || expression.equals("/");
    }

    /**
     * 计算入口
     *
     * @param numModel
     *            算式
     * @return 结果
     */
    public static double doAccount(String numModel) {
        int endIndex = 0;
        int startIndex = numModel.indexOf("(", endIndex);
        while (startIndex != -1) {
            endIndex = numModel.indexOf(")", startIndex) + 1;
            String segment = numModel.substring(startIndex, endIndex);

            double tResult = Arithmetic.account(segment.replace("(", "").replace(")", ""));
            numModel = numModel.replace(segment, String.valueOf(tResult));

            startIndex = numModel.indexOf("(", 0);
        }
        DecimalFormat format = new DecimalFormat("0.###");
        return Double.parseDouble(format.format(Arithmetic.account(numModel)));
    }

    public static double doCount(String numModel) {
        int endIndex = 0;
        int startIndex = numModel.indexOf("(", endIndex);
        while (startIndex != -1) {
            endIndex = numModel.indexOf(")", startIndex) + 1;
            String segment = numModel.substring(startIndex, endIndex);

            double tResult = Arithmetic.account(segment.replace("(", "").replace(")", ""));
            numModel = numModel.replace(segment, String.valueOf(tResult));

            startIndex = numModel.indexOf("(", 0);
        }
        DecimalFormat format = new DecimalFormat("0.##");
        return Double.parseDouble(format.format(Arithmetic.account(numModel)));
    }

    /**
     * 核心算法逻辑
     *
     * @param numModel
     *            算式
     * @return
     */
    @SuppressWarnings("unchecked")
    private static double account(String numModel) {
        String operators[] = {"\\+", "\\-", "\\*", "\\/", "\\(", "\\)"};
        for (String operator : operators) {
            numModel = numModel.replaceAll(operator, " " + operator + " ");
        }
        @SuppressWarnings("rawtypes")
        Stack operStack = new Stack();
        String numModels[] = numModel.split("\\s+");
        String preEle = "";
        for (int i = 0; i < numModels.length; i++) {
            if (isExpression(numModels[i])) {
                if (preEle == null || preEle.equals("")) {
                    preEle = numModels[i];
                } else {
                    if (priority(preEle) >= priority(numModels[i])) {
                        double d1 = Double.parseDouble(String.valueOf(operStack.pop()));
                        double d2 = Double.parseDouble(String.valueOf(operStack.pop()));
                        operStack.push(operation(d2, d1, preEle));
                        preEle = numModels[i];
                    } else if (priority(preEle) < priority(numModels[i])) {
                        double d1 = Double.parseDouble(String.valueOf(operStack.pop()));
                        double d2 = Double.parseDouble(numModels[i + 1]);
                        operStack.push(operation(d1, d2, numModels[i]));
                        i++;
                    }
                }
            } else {
                operStack.push(numModels[i]);
            }
        }

        double d1 = Double.parseDouble(String.valueOf(operStack.pop()));
        double d2 = Double.parseDouble(String.valueOf(operStack.pop()));
        return operation(d2, d1, preEle);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
//		String numModel = "111.1 * (2 - 1) + (2 - 1) + 50 - 10 * (12 + 3)";
//		String numModel = "(28.32-5)*12/3";
//		String numModel = "8*12";
//		System.out.println(Arithmetic.doAccount(numModel));
        BigDecimal bb = new BigDecimal(1.0);
        BigDecimal cc = new BigDecimal(3.0);
        System.out.println(bb.divide(cc));
    }
}
