package jdbcdemo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class demo {
    public static void main(String[] args) throws Exception {

        //注册驱动

        //获取链接
        String url = "jdbc:mysql://127.0.0.1:3306/student";
        String username = "root";
        String password = "zjx040516";

        Connection conn = DriverManager.getConnection(url, username, password);


        //数据库的user
        List<User> userList = getUser(conn);
        Scanner input = new Scanner(System.in);
        System.out.println("请输入你要进行的操作");
        System.out.println("1为添加学生信息");
        System.out.println("2为删除学生信息");
        System.out.println("3为修改学生信息");
        System.out.println("4为查看学生信息");
        switch (input.nextInt()) {
            case 1:
                insertUser(userList, input);
                break;
            case 2:
                deleteUser(userList, input);
                break;
            case 3:
                updateUser(userList, input);
                break;
            case 4:
                viewUser(userList);
                break;


        }

        deleteAllUsers(conn);

        insertAllUsers(conn, userList);


        close(conn);


    }

    private static void updateUser(List<User> userList, Scanner input) {
        System.out.println("输入id：");
        int id = input.nextInt();
        for (int i = 0; i < userList.size(); i++) {
            Integer id1 = userList.get(i).getId();
            if (id == id1) {
                userList.remove(i);
                System.out.println("输入name：");
                String name = input.next();
                User user = new User(id1, name);
                userList.add(user);
                break;
            }
            System.out.println("未找到用户");
        }
        System.out.println(userList);
    }


    private static void viewUser(List<User> userList) {
        for (User user : userList) {
            System.out.println(user);
        }

    }

    private static void deleteUser(List<User> userList, Scanner input) {

        System.out.println("输入id：");
        int id = input.nextInt();
        for (int i = 0; i < userList.size(); i++) {
            if (id == userList.get(i).getId()) {
                userList.remove(i);
                break;
            }
            System.out.println("未找到用户");
        }

//        System.out.println(userList);

    }


    private static void insertUser(List<User> userList, Scanner input) {
        User user = new User();
        List<Integer> listOfId = userList.stream().map(user1 -> user1.getId()
        ).collect(Collectors.toList());
        int id;
        boolean flag = false;
        do {
            flag = false;
            System.out.println("输入id：");
            id = input.nextInt();
            for (Integer id1 : listOfId) {
                if (id1 == id) {
                    System.out.println("id 重复");
                    flag = true;
                }
            }
        } while (flag);
        user.setId(id);
        System.out.println("输入name：");
        String name = input.next();
        user.setName(name);
        userList.add(user);

    }

    public static void insertAllUsers(Connection conn, List<User> userList) throws Exception {

        String sql = "insert into student(id, name) values(?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        for (User user : userList) {
            ps.setInt(1, user.getId());
            ps.setString(2, user.getName());
            ps.executeUpdate();
        }

        ps.close();


    }


    public static void deleteAllUsers(Connection conn) throws Exception {
        //获取执行sql的对象Statement
        Statement stmt = conn.createStatement();

        String sql = "delete from student";

        stmt.executeUpdate(sql);

        stmt.close();

    }

    public static List<User> getUser(Connection conn) throws Exception {

        //获取执行sql的对象Statement
        Statement stmt = conn.createStatement();

        //定义sql语句
//        String sql="update studentMoney set money=2000 where id=1";
        String sql = "select * from student";

        //执行sql
//        int count=stmt.executeUpdate(sql);
        ResultSet rs = stmt.executeQuery(sql);
        //处理结果
//        System.out.println(count);
        List<User> userList = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
//            System.out.println("id = " + id + " name = " + name);
            User user = new User(id, name);
            userList.add(user);
        }

        System.out.println(userList);

        //释放资源
        stmt.close();


        return userList;

    }

    public static void close(Connection conn) throws Exception {
        //释放资源

        conn.close();
    }
}
