package Books;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends JFrame implements ActionListener{
    Jdbc jdbc=null;
    String name=null;
    JTable jt1=null;
    JTable jt2=null;
    JScrollPane jsp1=null;
    JScrollPane jsp2=null;
    ImageIcon background;
    JPanel panel;
    UsManage usManage1;
    UsManage usManage2;
    JLabel picLabel=null;
    JButton button1=null;
    JButton button2=null;
    JButton button3=null;
    ImageIcon icon1=null;
    ImageIcon icon2=null;
    JTextField tf=null;

    public User(Jdbc jdbc,String name){
        super("欢迎来到图书馆");
        System.out.println("这是构造函数");
        this.jdbc=jdbc;
        this.name=name;
        usManage1=new UsManage(jdbc);
        usManage2=new UsManage(jdbc,"select name,author,id,house from books where person='"
                +name+"'");
        background=new ImageIcon("Userback.png");
        JLabel backlabel=new JLabel(background);
        backlabel.setBounds(0,0,background.getIconWidth(),background.getIconHeight());
        this.getLayeredPane().add(backlabel,new Integer(Integer.MIN_VALUE));
        tf=new JTextField(10);
        tf.setFont(new Font("微软雅黑",Font.PLAIN,20));
        tf.setBounds(250,600,250,40);
        panel=(JPanel)this.getContentPane();
        panel.setLayout(null);
        panel.setOpaque(false);

        jt1=new JTable(usManage1);
        jt1.getTableHeader().setFont(new Font("楷体",Font.BOLD,20));
        DefaultTableCellRenderer r1=new DefaultTableCellRenderer();
        r1.setHorizontalAlignment(JLabel.CENTER);
        jt1.setDefaultRenderer(Object.class,r1);
        jt1.setFont(new Font("微软雅黑",Font.BOLD,20));
        jt1.setRowHeight(30);
        ///////////////////////////////////////////
        jt1.setBounds(0,0,500,571);
        ///////////////////////////////////////////

        jt1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==1){
                    int row =jt1.getSelectedRow();
                    String ss=(String)usManage1.getValueAt(row,0);
                    System.out.println(ss);
                    String sql="select path from books where name='"+ss+"'";
                    try {
                        ResultSet rs= jdbc.getSt().executeQuery(sql);
                        ImageIcon icon;
                        if(rs.next()) {
                            icon = new ImageIcon(rs.getString(1));
                            picLabel.setIcon(icon);
                            panel.add(picLabel);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        jt2=new JTable(usManage2);
        jt2.getTableHeader().setFont(new Font("楷体",Font.BOLD,20));
        DefaultTableCellRenderer r2=new DefaultTableCellRenderer();
        r2.setHorizontalAlignment(JLabel.CENTER);
        jt2.setDefaultRenderer(Object.class,r2);
        jt2.setFont(new Font("微软雅黑",Font.BOLD,20));
        jt2.setRowHeight(30);
        jt2.setBounds(500,0,500,250);

        jsp1=new JScrollPane(jt1);
        //////////////////////////////////////////////
        jsp1.setBounds(0,0,500,571);
        //////////////////////////////////////////////
        jsp2=new JScrollPane(jt2);
        jsp2.setBounds(500,0,500,250);
        jsp1.setOpaque(false);
        jsp1.getViewport().setOpaque(false);
        jsp2.setOpaque(false);
        jsp2.getViewport().setOpaque(false);
        picLabel=new JLabel();
        //////////////////////////////////////////
        picLabel.setBounds(500,250,300,320);
        /////////////////////////////////////////////
        button1=new JButton("借书");
        button2=new JButton("还书");
        button3=new JButton("查询");
        button1.setContentAreaFilled(false);
        button2.setContentAreaFilled(false);
        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        icon1=new ImageIcon("jie.png");
        icon2=new ImageIcon("huan.png");
        button1.setIcon(icon1);
        button2.setIcon(icon2);
        ////////////////////////////////////////
        button1.setBounds(800,248,200,162);
        button2.setBounds(800,409,200,162);
        button3.setBounds(500,600,100,40);
        button3.setFont(new Font("微软雅黑",Font.BOLD,20));
        ////////////////////////////////////////
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(picLabel);
        panel.add(jsp1);
        panel.add(jsp2);
        panel.add(tf);
        panel.setBounds(0,0,1000,700);

        this.setSize(background.getIconWidth(),background.getIconHeight());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button1){
            int row =jt1.getSelectedRow();
            String ss=(String)usManage1.getValueAt(row,2);
            System.out.println(ss);
            String sql="update books set state='已借',person='"+name+
                    "' where id='"+ss+"'";
            System.out.println(sql);
            try {
                jdbc.getSt().executeUpdate(sql);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            usManage1=new UsManage(jdbc);
            jt1.setModel(usManage1);
            usManage2=new UsManage(jdbc,"select name,author,id,house from books where person='"
                    +name+"'");
            jt2.setModel(usManage2);
        }
        if(e.getSource()==button2){
            int row=jt2.getSelectedRow();
            String ss=(String)usManage2.getValueAt(row,2);
            String sql="update books set state='未借',person='' where id='"+ss+"'";
            try {
                jdbc.getSt().executeUpdate(sql);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            usManage1=new UsManage(jdbc);
            jt1.setModel(usManage1);
            usManage2=new UsManage(jdbc,"select name,author,id,house from books where person='"
                    +name+"'");
            jt2.setModel(usManage2);
        }
        if(e.getSource()==button3){
            String name=this.tf.getText().trim();
            String sql="select * from books where name like '%"+name+"%'";
            usManage1=new UsManage(jdbc,sql);
            jt1.setModel(usManage1);
        }

    }

    public static void main(String[] args){
        Jdbc jdbc=new Jdbc();
        jdbc.conn();
        new User(jdbc,"select * from users where username='aa'");
    }
}
