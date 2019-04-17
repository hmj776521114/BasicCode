package com.hmj.CalculatorDemo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorMain extends JFrame implements ActionListener {
    private Container container = getContentPane();//容器
    private String showContent = "";//文本框显示的内容
    private JTextField textArea; //文本框
    private JPanel[] jbp = new JPanel[3];//三个大面板
    private String[] number = {"AC", "Back", "%", "7", "8", "9", "4", "5", "6", "1", "2", "3"};//数字键及功能键
    private String[] operate = {"÷", "×", "+", "-"};//运算符
    private int fontSize;//文本框字体大小
    private Font font;//文本框字体
    private  boolean isOperation=false;//标记值，用于解决多运算符的问题
    public CalculatorMain() {
        container.setLayout(new BorderLayout());
        //this.setUndecorated(true);// 去掉窗口的边框
        fontSize = 30;
        setBounds(300, 300, 230, 320);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }//构造

    public void setTextArea() {
        textArea = new JTextField(showContent);
        textArea.setEditable(false);//设置文本框为不可编辑
        textArea.setPreferredSize(new Dimension(320, 80));
        textArea.setHorizontalAlignment(SwingConstants.RIGHT); //左对齐
        font = new Font("微软雅黑", Font.PLAIN, fontSize);//设置字体及大小
        textArea.setFont(font);
        textArea.setForeground(Color.white);//显示字体为白色
        textArea.setBackground(Color.black);//背景颜色设置为黑色
        container.add(BorderLayout.NORTH, textArea);//添加文本框至容器顶部

    }//设置文本框方法

    public void setBigPanel() {
        Dimension dimension[] = new Dimension[3];
        String[] border = {BorderLayout.SOUTH, BorderLayout.EAST, BorderLayout.CENTER};
        dimension[0] = new Dimension(320, 48);
        dimension[1] = new Dimension(48, 240);
        dimension[2] = new Dimension(194, 48);
        for (int i = 0; i < 3; i++) {
            jbp[i] = new JPanel(new BorderLayout());
            jbp[i].setPreferredSize(dimension[i]);
            jbp[i].setBorder(new EmptyBorder(0, 0, 0, 0));
            container.add(border[i], jbp[i]);
        }
    }//设置3个大面板方法

    public void setSmallPanel() {

        jbp[0].setLayout(new BorderLayout());
        JPanel[] p = new JPanel[3];
        Button[] bb = new Button[3];
        Dimension[] dimensions = {new Dimension(122, 48), new Dimension(56, 48), new Dimension(50, 48)};
        String[] border = {BorderLayout.WEST, BorderLayout.CENTER, BorderLayout.EAST};
        String[] bname = {"0", ".", "="};
        for (int i = 0; i < 3; i++) {
            p[i] = new JPanel(new BorderLayout());
            p[i].setPreferredSize(dimensions[i]);
            bb[i] = new Button(bname[i]);
            bb[i].addActionListener(this);
            p[i].add(bb[i]);
            jbp[0].add(border[i], p[i]);
        }
        jbp[1].setLayout(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            JPanel jP = new JPanel(new GridLayout());
            Button b = new Button(operate[i]);
            b.addActionListener(this);
            b.setPreferredSize(new Dimension(56, 48));
            jP.add(b);
            jbp[1].add(jP);

        }

        jbp[2].setLayout(new GridLayout(4, 3));
        for (int i = 0; i < 12; i++) {
            JPanel jP = new JPanel(new GridLayout());
            Button b = new Button(number[i]);
            b.addActionListener(this);
            b.setPreferredSize(new Dimension(56, 48));
            jP.add(b);
            jbp[2].add(jP);

        }


    }//设置三个大面板内个各种小按钮方法

    private String compute(String str) {
        String array[];
        array = str.split(" ");
        Stack<Double> s = new Stack<>();
        Double a = Double.parseDouble(array[0]);
        s.push(a);
        for (int i = 1; i < array.length; i++) {
            if (i % 2 == 1) {
                if (array[i].compareTo("+") == 0) {
                    double b = Double.parseDouble(array[i + 1]);
                    s.push(b);
                }
                if (array[i].compareTo("-") == 0) {
                    double b = Double.parseDouble(array[i + 1]);
                    s.push(-b);
                }
                if (array[i].compareTo("×") == 0) {
                    double b = Double.parseDouble(array[i + 1]);
                    double c = s.pop();
                    c *= b;
                    s.push(c);
                }
                if (array[i].compareTo("÷") == 0) {
                    double b = Double.parseDouble(array[i + 1]);
                    double c = s.peek();
                    s.pop();
                    c /= b;
                    String x =String.valueOf(c);
                    if(x.length()>17){//若遇到除不尽的情况,则只取字符串前17位
                        x=x.substring(0,11);
                    }
                    c=Double.parseDouble(x);
                    s.push(c);
                }
                if (array[i].compareTo("%") == 0) {
                    double c = s.peek();
                    s.pop();
                    c /= 100;
                    s.push(c);
                    return String.valueOf(c);
                }
            }
        }
        double sum = 0;
        while (!s.isEmpty()) {
            sum += s.pop();
        }
        String result = String.valueOf(sum);
        return result;
    }//计算结果方法

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        int flag = 0;
        try {
            if (str.equals("+") || str.equals("-") || str.equals("×") || str.equals("÷")) {
                if(!isOperation){//若前面无运算符
                    isOperation=true;
                    showContent += " "+str+" ";
                }
                else {//若前面有运算符则标记为真
                    showContent=showContent.substring(0,showContent.length()-3);
                    showContent += " "+str+" ";
                }

            } else if (str.equals("AC")) {
                showContent = "";
                fontSize = 30;
                isOperation=false;//取消掉标记
            } else if (str.equals("Back")) {
                isOperation=false;//取消掉标记
                if ((showContent.charAt(showContent.length() - 1)) == ' ') //检测字符串的最后一个字符是否为空格
                {
                    showContent = showContent.substring(0, showContent.length() - 3); //如果是则删除末尾3个字符(含操作符)
                } else //否则删除1个字符（数字）
                {
                    showContent = showContent.substring(0, showContent.length() - 1);
                }
            } else if (str.equals("=")) {
                showContent = compute(showContent);
                textArea.setText(showContent);
                isOperation=false;//取消掉标记
                if (showContent.length() >= 11 && fontSize>=14) {
                    fontSize -= 2;
                    font = new Font("微软雅黑", Font.PLAIN, fontSize);//设置字体及大小
                    textArea.setFont(font);
                }
                flag=1;
                //isOperation=true;
            } else if (str.equals("%")) {
                showContent += " " + str + " ";
                showContent = compute(showContent);
                isOperation=false;//取消掉标记
            } else {
                showContent += str;
                isOperation=false;//取消掉标记
                if (showContent.length() >= 11 && fontSize>=14) {
                    fontSize -= 2;
                }
            }

        }catch (Exception exception){ //捕获异常
            textArea.setText("ERROR");
            showContent="";
        }finally {
            if (flag==0) {
                font = new Font("微软雅黑", Font.PLAIN, fontSize);//设置字体及大小
                textArea.setFont(font);
                textArea.setText(showContent);
            }
        }

    }//重写动作监听方法

    public static void main(String[] args) {
        CalculatorMain test = new CalculatorMain();
        test.setTextArea();
        test.setBigPanel();
        test.setSmallPanel();
        test.setVisible(true);
        test.setResizable(false);
    }//主方法

}
