package com;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @date 2020/12/24 23:03
 */
public class Window extends JFrame {
    //表格
    JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            //表格设置不可编辑
            return false;
        }
    };
    private JScrollPane jScrollPane = new JScrollPane();
    private DefaultTableModel defaultTableModel;
    private DefaultTableCellRenderer defaultTableCellRenderer;
    String[] headName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    String[][] data;
    CalendarUtil calendarUtil = new CalendarUtil();
    //控制按钮
    JButton lastYear = new JButton("上年");
    JTextField yearField = new JTextField("2020");
    JButton nextYear = new JButton("下年");
    Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    JButton lastMonth = new JButton("上月");
    private JComboBox monthComboBox = new JComboBox(months);
    JButton nextMonth = new JButton("下月");
    //操作日期
    JLabel dateLabel = new JLabel("请选择日期");
    //编辑框
    JScrollPane logPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    JTextArea logArea = new JTextArea();
    //日志操作按钮
    JButton saveLogButton = new JButton("保存日志");
    JButton deleteLogButton = new JButton("删除日志");
    JButton selectAllLogButton = new JButton("查看当月日志");

    public Window() {
        this.setSize(800, 349);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("日历");
        this.setLayout(null);

        //日历控制
        this.lastYear.setBounds(0, 0, 80, 40);
        this.add(this.lastYear);
        this.yearField.setBounds(80, 0, 80, 40);
        this.yearField.setForeground(Color.RED);
        this.add(this.yearField);
        this.nextYear.setBounds(160, 0, 80, 40);
        this.add(this.nextYear);
        this.lastMonth.setBounds(240, 0, 80, 40);
        this.add(this.lastMonth);
        this.monthComboBox.setBounds(320, 0, 80, 40);
        this.monthComboBox.setForeground(Color.RED);
        this.add(this.monthComboBox);
        this.nextMonth.setBounds(400, 0, 80, 40);
        this.add(this.nextMonth);
        //上一年
        this.nextYear.addActionListener((e) -> {
            Integer year = Integer.parseInt(this.yearField.getText()) + 1;
            Integer month = (Integer) this.monthComboBox.getSelectedItem();
            this.yearField.setText(year.toString());
            this.setTableData(year, month);
        });
        //下一年
        this.lastYear.addActionListener((e) -> {
            Integer year = Integer.parseInt(this.yearField.getText()) - 1;
            Integer month = (Integer) this.monthComboBox.getSelectedItem();
            this.yearField.setText(year.toString());
            this.setTableData(year, month);
        });
        //上个月
        this.lastMonth.addActionListener((e) -> {
            Integer year = Integer.parseInt(this.yearField.getText());
            Integer month = this.monthComboBox.getSelectedIndex() + 1;
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            this.yearField.setText(year.toString());
            this.monthComboBox.setSelectedIndex(month - 1);
            this.setTableData(year, month);
        });
        //下个月
        this.nextMonth.addActionListener((e) -> {
            Integer year = Integer.parseInt(this.yearField.getText());
            Integer month = this.monthComboBox.getSelectedIndex() + 1;
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
            this.yearField.setText(year.toString());
            this.monthComboBox.setSelectedIndex(month - 1);
            this.setTableData(year, month);
        });
        //编辑年份
        this.yearField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                Integer year = Integer.parseInt(yearField.getText());
                Integer month = monthComboBox.getSelectedIndex() + 1;
                setTableData(year, month);
            }
        });
        //编辑月份
        this.monthComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Integer year = Integer.parseInt(yearField.getText());
                Integer month = monthComboBox.getSelectedIndex() + 1;
                setTableData(year, month);
            }
        });
        //表格
        this.table.setRowHeight(40);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.table.getTableHeader().setResizingAllowed(false);
        this.jScrollPane.setBounds(0, 40, 480, 263);
        this.jScrollPane.getViewport().add(this.table);
        this.defaultTableCellRenderer = new DefaultTableCellRenderer();
        this.defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        this.defaultTableCellRenderer.setVerticalAlignment(SwingConstants.CENTER);
        this.table.setDefaultRenderer(Object.class, this.defaultTableCellRenderer);
        this.add(this.jScrollPane);
        Date date = new Date();
        this.monthComboBox.setSelectedIndex(date.getMonth());
        this.setTableData(date.getYear() + 1900, date.getMonth() + 1);
        //提示
        this.dateLabel.setBounds(480, 0, 302, 40);
        this.dateLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.dateLabel.setForeground(Color.RED);
        this.dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.dateLabel);
        //编辑框
        this.logPane.setBounds(480, 40, 302, 220);
        this.logPane.getViewport().add(this.logArea);
        this.logArea.setLineWrap(true);
        this.add(this.logPane);
        //日志按钮
        this.saveLogButton.setBounds(480, 261, 90, 40);
        this.add(this.saveLogButton);
        this.deleteLogButton.setBounds(570, 261, 90, 40);
        this.add(this.deleteLogButton);
        this.selectAllLogButton.setBounds(660, 261, 122, 40);
        this.add(this.selectAllLogButton);
        //表格点击事件
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();
                if (row == -1 || data[row][col] == null) {
                    dateLabel.setText("请选择日期");
                    return;
                } else {
                    Integer year = Integer.parseInt(yearField.getText());
                    Integer month = (Integer) monthComboBox.getSelectedItem();
                    Integer day = Integer.parseInt(data[row][col]);
                    Date selectDate = new Date(year - 1900, month - 1, day);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    dateLabel.setText(dateFormat.format(selectDate));
                    String selectSql = "select date_info, log_info from log where date_info = ?;";
                    List list = DBUtil.query(selectSql, Log.class, selectDate);
                    if (list.size() != 0) {
                        logArea.setText(((Log) list.get(0)).getLog_info() + "\n");
                    } else {
                        logArea.setText("");
                    }
                }
            }
        });
        //添加日志
        this.saveLogButton.addActionListener((e) -> {
            int row = this.table.getSelectedRow();
            int col = this.table.getSelectedColumn();
            if (row == -1 || data[row][col] == null) {
                dateLabel.setText("请选择日期");
                return;
            } else {
                //获取日志信息
                if (logArea.getText() == null || "".equals(logArea.getText())) {
                    return;
                } else {
                    //获取信息
                    String logInfo = logArea.getText();
                    Integer year = Integer.parseInt(yearField.getText());
                    Integer month = (Integer) monthComboBox.getSelectedItem();
                    Integer day = Integer.parseInt(data[row][col]);
                    java.sql.Date selectDate = new java.sql.Date(year - 1900, month - 1, day);
                    //先去数据库查询如果有该日期则更新，没有则添加;
                    String selectSql = "select date_info, log_info from log where date_info = ?;";
                    List list = DBUtil.query(selectSql, Log.class, selectDate.toString());
                    if (list.size() == 0) {
                        //没查到则添加
                        String insertSql = "insert into log(date_info, log_info) values(?,?);";
                        int i = DBUtil.addDeleteUpdate(insertSql, selectDate.toString(), logInfo);
                        if (i == 1) {
                            JOptionPane.showMessageDialog(null, "添加成功", "提示", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "添加失败", "提示", JOptionPane.PLAIN_MESSAGE);
                        }
                    } else {
                        //查到了更新
                        String updateSql = "update log set log_info = ? where date_info = ?;";
                        int i = DBUtil.addDeleteUpdate(updateSql, logInfo, selectDate.toString());
                        if (i == 1) {
                            JOptionPane.showMessageDialog(null, "修改成功", "提示", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "修改失败", "提示", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
            }
        });
        //删除日志
        this.deleteLogButton.addActionListener((e) -> {
            int row = this.table.getSelectedRow();
            int col = this.table.getSelectedColumn();
            if (row == -1 || data[row][col] == null) {
                dateLabel.setText("请选择日期");
                return;
            } else {
                if (logArea.getText() == null || "".equals(logArea.getText())) {
                    return;
                } else {
                    //获取信息
                    Integer year = Integer.parseInt(yearField.getText());
                    Integer month = (Integer) monthComboBox.getSelectedItem();
                    Integer day = Integer.parseInt(data[row][col]);
                    java.sql.Date selectDate = new java.sql.Date(year - 1900, month - 1, day);
                    String deleteSql = "delete from log where date_info = ?";
                    int i = DBUtil.addDeleteUpdate(deleteSql, selectDate.toString());
                    if (i == 1) {
                        logArea.setText("");
                        JOptionPane.showMessageDialog(null, "删除成功", "提示", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "删除失败", "提示", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });
        //查看本月所有日志
        this.selectAllLogButton.addActionListener((e) -> {
            logArea.setText("");
            Integer year = Integer.parseInt(yearField.getText());
            Integer month = (Integer) monthComboBox.getSelectedItem();
            String selectThisMonthLogSql = "select * from log where year(date_info) = ? and month(date_info) = ?;";
            List<Log> list = DBUtil.query(selectThisMonthLogSql, Log.class, year.toString(), month.toString());
            for (Log log : list) {
                logArea.append("----------------\n");
                logArea.append(log.getDate_info() + ":\n" + log.getLog_info() + "\n");
                logArea.append("----------------\n");
            }
        });
        setVisible(true);
        //查询当天是否有日程
        DateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date nowSqlDate = java.sql.Date.valueOf(sqlFormat.format(date));
        String nowSql = "select * from log where date_info = ?;";
        List<Log> tiplist = DBUtil.query(nowSql, Log.class, nowSqlDate.toString());
        if (tiplist.size() != 0) {
            Log log = tiplist.get(0);
            JOptionPane.showMessageDialog(null, log.getLog_info(), "今日任务", JOptionPane.PLAIN_MESSAGE);
        }

    }

    public void setTableData(int year, int month) {
        this.calendarUtil.setYear(year);
        this.calendarUtil.setMonth(month);
        this.data = calendarUtil.getCalendar();
        this.defaultTableModel = new DefaultTableModel(this.data, this.headName);
        this.table.setModel(this.defaultTableModel);
        this.dateLabel.setText("请选择日期");
    }

    public static void main(String[] args) {
        new Window();
    }
}
