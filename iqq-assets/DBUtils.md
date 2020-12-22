dbutils 的使用，事务处理，  

多表操作，  
oracle 大数据处理  
作者：吕鹏  
时间：2011-08-08  

首先还是简单的回顾一下昨天我们讲的什么， 我们昨天讲了数据库连接池， 为防止频繁访问  
数据库而建立的连接池的实现有两种一个是自定义连接池， 使用动态代理方式， 另外一个是  
使用 DBCP,C3P0,Tomcat 等服务器自带的，都可以实现连接池。下午讲了手动编写自己的  
JDBC 框架，简化了 CRUD 操作，为我们今天讲这个 DBUtils 打下一个基础。  

@[toc]

## 一、DBUitls 框架的使用  

1、使用 dbutils 做增删改查，批处理以及大文本操作  
```java
package cn.itcast.dbutils.demo;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileReader;  
import java.util.List;  
import javax.sql.rowset.serial.SerialBlob;  
import javax.sql.rowset.serial.SerialClob;  
import org.apache.commons.dbutils.QueryRunner;  
import org.apache.commons.dbutils.handlers.BeanHandler;  
import org.apache.commons.dbutils.handlers.BeanListHandler;  
import org.junit.BeforeClass;  
import org.junit.Test;  
import cn.itcast.dbutil.JDBCUtils;  
/**  
* 使用dbutils做增删改查 批处理大文本操作  
* @author 吕鹏  
*  
*/  
public class Demo01 {  
/**创建demo表  
+-------+-------------+------+-----+---------+-------+  
| Field | Type | Null | Key | Default | Extra |  
+-------+-------------+------+-----+---------+-------+  
| id | int(11) | YES | | NULL | |  
| name | varchar(10) | YES | | NULL | |  
+-------+-------------+------+-----+---------+-------+  
*/  
static QueryRunner runner;  
@BeforeClass  
public static void beforeClass(){  
//获取一个QueryRunner对象（构造方法带数据源 自动完成连接创建和释放）  
runner = new QueryRunner(JDBCUtils.getDataSource());  
}  
/**  
* 执行插入操作  
*/  
@Test  
public void testInsert()throws Exception{  
String sql = "insert into demo values(?,?)";//声明sql  
Object\[\] params = {2,"insert"};//初始化参数  
runner.update(sql, params);  
}  
/**  
* 执行删除操作  
* @throws Exception  
*/  
@Test  
public void testDelete() throws Exception{  
String sql = "delete from demo where id = ?";  
runner.update(sql, 1);  
}  
/**  
* 执行更新操作  
* @throws Exception  
*/  
@Test  
public void testUpdate() throws Exception{  
String sql = "update demo set name=? where id=?";  
Object[] params = {"update",1};  
runner.update(sql, params);  
}  
/**  
* 查询列表操作  
* @throws Exception  
*/  
@Test  
public void testListQuery() throws Exception{  
String sql = "select * from demo";  
List<Demo> list = (List<Demo>) runner.query(sql, new  
BeanListHandler(Demo.class));  
for(int i=0;i<list.size();i++){  
System.out.println("ID:"+list.get(i).getId()+" 姓名是："+list.get(i).getName());  
}  
} 
/**  
* 查询对象操作  
* @throws Exception  
*/  
@Test  
public void testObjectQuery() throws Exception{  
String sql = "select * from demo where id = ?";  
Demo demo = (Demo) runner.query(sql, 1, new BeanHandler(Demo.class));  
System.out.println("ID:"+demo.getId()+" Name:"+demo.getName());  
}  
/**  
* 批处理操作  
* @throws Exception  
*/  
@Test  
public void testBatch() throws Exception{  
String sql = "insert into demo value (?,?)";  
Object\[\]\[\] params = new Object\[10\]\[\];  
for(int i=0;i<10;i++){  
params\[i\] = new Object\[\]{i,"batch"};  
}  
runner.batch(sql, params);  
}  
/**  
* 大文本操作  
* @throws Exception  
*/  
@Test  
public void testClob() throws Exception{  
String sql = "insert into clob values(?)";  
File file = new File("c:/a.txt");  
Long l = file.length();  
char\[\] buffer = new char\[l.intValue()\];  
FileReader reader = new FileReader(file);  
reader.read(buffer);  
SerialClob clob = new SerialClob(buffer);  
runner.update(sql, clob);  
}  
/**  
* 二进制图像操作  
* @throws Exception  
*/  
@Test  
public void testBlob() throws Exception{  
String sql = "insert into blob values(?)";  
File file = new File("c:/a.jpg");  
Long l = file.length();  
byte[] buffer = new byte\[l.intValue()\];  
FileInputStream input = new FileInputStream(file);  
input.read(buffer);  
SerialBlob blob = new SerialBlob(buffer);  
runner.update(sql,blob);  
}  
} 
```

以上代码是使用 DButils 框架完成的增删改差批处理以及大文本操作，其中，使用了 QueryRunner 这个类，这个类是专门负责处理 sql 语句的，有四个构造方法，我们经常使用到的是一个无参的构造方法和一个有参的， 参数就是数据源， 当我们给其提供数据源的时候  
就是让框架为我们自动的创建数据库连接， 并释放连接， 当这是处理一般操作的时候， 当我们要进行事务处理的时候， 那么连接的释放就要由我们自己来决定了， 所以我们就不再使用带参数的 QueryRunner 构造方法了。具体用法下面详解。  

2、使用 DBUtils 框架管理事务。  

刚才我们的增删改差是为了演示我们的例子， 所以我们忽略了异常也没有使用事务， 现在我  
们使用事务模拟一个银行转账，看一下 DBUtils 框架是如何帮助我们完成事务管理的。  

在讲解事务之前，需要让大家理解一件事情就是，我们使用事务就不能使用其 QueryRunner  
的有参构造方法了， 因为我们要自己定义事务的开关， 如果使用带参数的构造就要让框架帮  
助我们关闭数据库连接了， 所以这里首先大家要明白。 那么我们如何将我们的连接传递给持  
久层呢，有同学说使用构造方法，是挺好的，通过构造方法将我们的 conn 传递给持久层，  
但是为了降低耦合度， 我们不想在业务层出现持久层的代码， 那我们就使用一个类帮助我们  
把这个连接传递过去， 就相当于使用工厂帮我们生成持久层对象一样， 那我们使用什么类呢，  
我们使用 ThreadLoacl 这个类  

关于 ThreadLocal 这个类， 可以查阅帮助文档了解， 在这里我简单的说明一下它的作用，  
它的作用是在一个线程当中记录我们的变量，这个变量可以是任意变量，包括我们的连接，  
就是说我们生成一个连接以后可以放在这个线程中， 这样， 只要是这个线程中任何对象都可  
以共享这个连接，当这个线程结束以后，线程要删除这个连接。  

具体使用方法：  

```java
//声明线程共享变量  
public static ThreadLocal<Connection> container = new ThreadLocal<Connection>();  
//获取共享变量  
public static ThreadLocal<Connection> getContainer(){  
return container;  
}
```  
这样，那么我们就可以帮有关事务的操作，这些操作原本应该在业务层出现的代码， 现  
在我们都把它放在工具类中， 因为不管是事务开启还是事务提交， 回滚， 都是和连接关联的，  
我们把连接放在了这个共享的线程当中，那么其方法也是共享的：工具类如下：  
package cn.itcast.dbutil;  
import java.sql.Connection;  
import java.sql.SQLException;  
import javax.sql.DataSource;  
import com.mchange.v2.c3p0.ComboPooledDataSource;  
public class JDBCUtils {  
//c3p0连接池  
public static ComboPooledDataSource ds = new ComboPooledDataSource();  
//声明线程共享变量  
public static ThreadLocal<Connection> container = new ThreadLocal<Connection>();  
//获取共享变量  
public static ThreadLocal<Connection> getContainer(){  
return container;  
}  
/**  
* 获取数据源  
*/  
public static DataSource getDataSource(){  
return ds;  
}  
/**  
* 获取当前线程上的连接 开启事务  
*/  
public static void startTransaction(){  
Connection conn = container.get();//首先获取当前线程的连接  
if(conn == null){//如果连接为空  
conn = getConnection(); //从连接池中获取连接  
container.set(conn);//将此连接放在当前线程上  
}  
try {  
conn.setAutoCommit(false);//开启事务  
} catch (SQLException e) {  
throw new RuntimeException(e.getMessage(),e);  
}  
}  
/**  
* 提交事务  
*/  
public static void commit() {  
Connection conn = container.get();// 从当前线程上获取连接  
if (conn != null) {// 如果连接为空，则不做处理  
try {  
conn.commit();// 提交事务  
} catch (SQLException e) {  
throw new RuntimeException(e.getMessage(), e);  
}  
}  
}  
/**  
*回滚事务  
*/  
public static void rollback(){  
Connection conn = container.get();//检查当前线程是否存在连接  
if(conn != null){  
try {  
conn.rollback();//回滚事务  
// container.remove();//如果回滚了，就移除这个连接  
} catch (SQLException e) {  
throw new RuntimeException(e.getMessage(),e);  
}  
}  
}  
/**  
* 关闭连接  
*/  
public static void close(){  
Connection conn = container.get();  
if(conn != null){  
try {  
conn.close();  
} catch (SQLException e) {  
throw new RuntimeException(e.getMessage(),e);  
}finally{  
container.remove();//从当前线程移除连接 切记  
}  
}  
}  
/**  
* 获取数据库连接  
* @return 数据库连接 连接方式（连接池 错c3p0）  
*/  
public static Connection getConnection(){  
try {  
return ds.getConnection();  
} catch (Exception e) {  
throw new RuntimeException();  
}  
}  
}  
在这个工具类里要特别注意， 在关闭连接方法的里面， finally 里需要将这个线程的连接  
移除，不然这个线程的连接得不到释放。  
这个明白了以后再看我们的银行转账的模拟程序：  
首先我们在模拟的 DAO 中完成查找账户和修改账户的方法：  
（1）查找账户和修改账户代码：  
/**  
* 模拟DAO 查询账户 更新账户  
* @author 吕鹏  
*  
*/  
class AccountDAO{  
//声明连接  
private Connection conn;  
public AccountDAO(){  
this.conn = JDBCUtils.getContainer().get();//线程共享对象获取连接  
}  
/*8  
* 根据ID查找账户  
*/  
public Account findAccount(int id) {  
//处理事务 ，无参构造  
QueryRunner runner = new QueryRunner();  
String sql = "select * from account where id=?";  
Object\[\] params={id};  
try {  
//附加连接 处理者为BeanHandler  
return (Account) runner.query(conn, sql, params, new  
BeanHandler(Account.class));  
} catch (Exception e) {  
System.out.println("eeeeeeeeeeeeeee");  
throw new RuntimeException(e.getMessage(),e);  
}  
}  
/**  
* 更新账户  
* @param a  
*/  
public void updateAccount(Account a){  
QueryRunner runner = new QueryRunner();  
String sql = "update account set money=? where id=?";  
Object\[\] params = {a.getMoney(),a.getId()};  
try {  
runner.update(conn, sql, params);  
} catch (SQLException e) {  
throw new RuntimeException(e.getMessage(),e);  
}  
}  
}  
（2）完成业务逻辑层的转账事务  
class AccountService{  
/**  
* 转账方法  
* @param fromID 原始账户  
* @param toID 目标账户  
* @param money 转入多少钱  
*/  
public void trafferAccount(int fromID,int toID,float money){  
try{  
JDBCUtils.startTransaction(); //开启事务  
AccountDAO dao = Factory.getInstance().getAccountDAO();//注意这行代码一  
定要放在开启事务之后 ，连接才会被创建  
Account from = dao.findAccount(fromID);  
Account to = dao.findAccount(toID);  
from.setMoney(from.getMoney()-money);  
to.setMoney(to.getMoney()+money);  
dao.updateAccount(from);  
dao.updateAccount(to);  
JDBCUtils.commit(); //提交事务  
System.out.println("转账成功");  
}catch (Exception e) {  
System.out.println("转账失败");  
JDBCUtils.rollback();//回滚事务  
throw new RuntimeException();  
}finally{  
JDBCUtils.close();  
}  
}  
}  
被标注红色的代码要注意， 其重点不只是工厂模式的使用， 更重要的一点要明白， 我们的持  
久层的连接是在其构造方法中获取的， 如果我们把这个代码放在前面的话， 就提前构造了其  
对象， 但是我们的连接却是在事务开启的时候才创建的， 如此我们的连接就无法传递到持久  
层当中了。 所以这段代码要放在之后。 待我们的连接被放置在当前线程以后再去调用工厂构  
造我们的对象。  
（3）静态工厂获取实例  
/**  
* 静态工厂 获取操作对象  
* @author 吕鹏  
*  
*/  
class Factory{  
//私有静态工厂实例  
private static Factory instance = null;  
//私有构造  
private Factory() {  
}  
/**  
* 提供一个静态公共方法获取工厂实例  
* @return  
*/  
public static Factory getInstance(){  
if (instance == null) {  
synchronized (Factory.class) {  
if (instance == null) {  
instance = new Factory();  
}  
}  
}  
return instance;  
}  
/**  
* 获取操作对象 简化代码不读配置文件了  
* @return  
*/  
public AccountDAO getAccountDAO(){  
return new AccountDAO();  
}  
public AccountService getAccountService(){  
return new AccountService();  
}  
}  
（4）测试类：  
/**  
* 转账测试类  
* @author 吕鹏  
*  
*/  
public class Test {  
public static void main(String\[\] args) {  
//使用工厂模式生成业务层对象  
AccountService service = Factory.getInstance().getAccountService();  
//调用业务层的方法 从1号账户转账100到2号账户  
service.trafferAccount(1, 2, 100);  
}  
}  
效果：  
mysql> select * from account;  
+------+------+-------+  
| id | name | money |  
+------+------+-------+  
| 1 | tom | 300 |  
| 2 | cc | 1700 |  
+------+------+-------+  
2 rows in set (0.00 sec)  
转账成功。  
从这个模拟的银行转账我们学到了：  
（1）在业务层完成事务的处理，持久层只负责操作数据库  
（1）DBUtils 对事务的操作流程  
（2）使用 ThreadLoacl 共享单线程信息  
二、使用 JDBC 操作多个表  
学习使用 JDBC 操作多个表是学习 Hibernate 的基础，掌握了 JDBC 对多表的操作其实就是  
在学习 Hibernate 实现多表操作的底层原理。下面我们来看一下，使用 JDBC 如何完成对多  
表的操作，学习多表操作之前我们需要明白什么叫多表操作：  
多表操作其实就是对 多个对象的关系的操作， 如果对带有关系的对象进行数据库操作就是  
对操作操作。  
那对象和对象之间又有什么关系呢？  
（1）一对多  
（2）多对多  
（3）一对一  
。 。 。 。  
1、一对多关系操作  
我们举部门和员工的例子， 部门和员工就是典型的一对多的范例， 那么我们如何描述他们之  
前的关系呢？要使用到外键，首先我们来看他们的表如何设计：  
package cn.itcast.dao;  
import java.util.Set;  
import org.apache.commons.dbutils.QueryRunner;  
import org.apache.commons.dbutils.handlers.BeanHandler;  
import org.apache.commons.dbutils.handlers.BeanListHandler;  
import cn.itcast.dbutils.JDBCUtils;  
import cn.itcast.domain.Department;  
import cn.itcast.domain.Employee;  
public class DepertmentDAO {  
/**  
* 增加一个部门 同时增加其员工  
* @param depart  
*/  
public void addDepartment(Department depart){  
try{  
QueryRunner run = new QueryRunner(JDBCUtils.getDataSource());  
String sql = "insert into department value(?,?,?)";  
Object\[\] params = {1,"开发部"};  
run.update(sql, params); //执行插入一个部门  
Set<Employee> ems = depart.getEmployees(); //查询出部门对象的员工集合  
for(Employee e : ems){  
String sql2 = "insert into employee value(?,?,?) where depart\_id = ?";  
Object\[\] params2 = {e.getId(),e.getName(),e.getMoney(),depart.getId()};  
run.update(sql2, params2); //执行插入多个员工  
}  
}catch(Exception e){  
throw new RuntimeException();  
}  
}  
/**  
* 查询一个部门，同时查询其部门下的员工  
*/  
public Department findDepartment(int id){  
Department depart = null;  
try{  
//首先查询部门  
QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());  
String sql = "select * from department where id = ?";  
depart = (Department) runner.query(sql, id, new BeanHandler(Department.class));  
//根据部门的ID也就是员工的外键查询员工集合 设置到depart的属性当中去  
String sql2 = "select * from employee where depart\_id = ?";  
Set<Employee> emps = (Set<Employee>) runner.query(sql2, id, new  
BeanListHandler(Employee.class));  
depart.setEmployees(emps);  
}catch (Exception e) {  
throw new RuntimeException(e.getMessage(),e);  
}  
return depart;  
}  
}  
2、多对多的关系  
多对多是比较复杂的， 在数据库中我们需要三张表来维护他们的关系， 一张中间表， 但实体  
对象只有两个， 拿学生和课程来讲， 一个学生可以选择多门课程而， 而一个课程又可以被多  
个学生选择，如果维护他们的关系呢，我们看例子：  
package cn.itcast.dao;  
import java.util.Set;  
import org.apache.commons.dbutils.QueryRunner;  
import org.apache.commons.dbutils.handlers.BeanHandler;  
import org.apache.commons.dbutils.handlers.BeanListHandler;  
import cn.itcast.dbutils.JDBCUtils;  
import cn.itcast.domain.Course;  
import cn.itcast.domain.Student;  
public class StudentDAO {  
/**  
* 添加一个学生，同时添加其选课课程  
*/  
public void addStudent(Student student){  
try{  
QueryRunner run = new QueryRunner(JDBCUtils.getDataSource());  
String sql1 = "insert into student values(?,?)";  
Object params\[\] = {student.getId(),student.getName()};  
Set<Course> courses = student.getCourses();  
for(Course c : courses){  
String sql2="insert into course values(?,?)";  
Object\[\] params2 = {c.getId(),c.getName()};  
run.update(sql2, params2);  
String sql3="insert into student\_course value(?,?)";  
Object\[\] params3={student.getId(),c.getId()};  
run.update(sql3, params3);  
}  
}catch(Exception e){  
throw new RuntimeException(e.getMessage(),e);  
}  
}  
/**  
* 查询一个学生，同时将其选课信息查询出来  
*  
*/  
public Student findStudent(int id){  
Student student = null;  
try {  
QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());  
String sql = "select * from student where id = ?";  
student = (Student) runner.query(sql, id, new BeanHandler(Student.class));  
//这句话什么意思呢？就是说我们要查询学生的课程，要保证两个条件  
//(1)保证学生的ID和中间表中的学生ID是一样的才能保证你要找的是这个学  
生  
//(2)保证课程的ID和中间表中的课程ID是一样的才能保证你要找的是这些课  
程  
//两者缺一不可，只有同时保证这个两个条件才能确认这些课程就是这个学生  
的。  
String sql2 = "select * from student\_course sc,course c where sc.sid=? and sc.cid=  
c.id";  
Set<Course> courses = (Set<Course>) runner.query(sql2, id, new  
BeanListHandler(Course.class));  
student.setCourses(courses);  
} catch (Exception e) {  
throw new RuntimeException(e.getMessage(),e);  
}  
return student;  
}  
}  
（3）一对一的关系  
身份证和人的关系不就是一对一吗？  
package cn.itcast.dao;  
import org.apache.commons.dbutils.QueryRunner;  
import org.apache.commons.dbutils.handlers.BeanHandler;  
import cn.itcast.dbutils.JDBCUtils;  
import cn.itcast.domain.Card;  
import cn.itcast.domain.Person;  
public class PersonDAO {  
/**  
* 增加一个人，同时增加一个身份证号码  
* @param person  
*/  
public void addPerson(Person person){  
try{  
QueryRunner run = new QueryRunner(JDBCUtils.getDataSource());  
String sql1 = "insert into person values(?,?)";  
Object\[\] params = {person.getId(),person.getName()};  
run.update(sql1, params);  
Card card = person.getCard();  
String sql2 = "insert into card values(?,?)";  
Object\[\] params2 = {card.getId(),card.getCard\_id()};  
run.update(sql2, params2);  
}catch(Exception e){  
throw new RuntimeException(e.getMessage(),e);  
}  
}  
/**  
* 查询一个人，同时查询这个人的身份证号码  
*/  
public Person findPerson(int id){  
Person person = null;  
try {  
QueryRunner runner = new QueryRunner(JDBCUtils.getDataSource());  
String sql = "select * from person where id = ?";  
person = (Person) runner.query(sql, id, new BeanHandler(Person.class));  
String sql2 = "select * from card where person\_id = ?";  
Card card = (Card) runner.query(sql2, id, new BeanHandler(Card.class));  
person.setCard(card);  
} catch (Exception e) {  
throw new RuntimeException(e.getMessage(),e);  
}  
return person;  
}  
}  
注意事项：  
不管 java 的对象存在何种关系， 反映到关系型数据库中， 都是使用外键表示纪录 （即对象）  
的关联关系。  
设计 java 对象如涉及到多个对象相互引用，要尽量避免使用一对多，或多对多关系，而应  
使用多对一描述对象之间的关系(或使用延迟加载的方式)。  
三、Oracle 中大数据的处理  
注意：面试题，如何在 Oracle 数据库中保存图片（二进制数据） ？  
Oracle 定义了一个 BLOB 字段用于保存二进制数据，但这个字段并不能存放真正的二进制  
数据，只能向这个字段存一个指针，然后把数据放到指针所指向的 Oracle 的 LOB 段中，  
LOB 段是在数据库内部表的一部分。  
因而在操作 Oracle 的 Blob 之前，必须获得指针（定位器）才能进行 Blob 数据的读取和写  
入。  
如何获得表中的 Blob 指针呢？ 可以先使用 insert 语句向表中插入一个空的 blob（调用  
oracle 的函数 empty\_blob() ） ，这将创建一个 blob 的指针，然后再把这个 empty 的 blob  
的指针查询出来，这样就可得到 BLOB 对象，从而读写 blob 数据了。  
1、插入空 blob  
insert into test(id,image) values(?,empty\_blob());  
2、获得 blob 的 cursor  
select image from test where id= ? for update;  
Blob b = rs.getBlob(“image”);  
注意: 须加 for update，锁定该行，直至该行被修改完毕，保证不产生并发冲突。  
3、利用 io，和获取到的 cursor 往数据库读写数据  
注意：以上操作需开启事务。  
package cn.itcast.oracle;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.Statement;  
import org.junit.Test;  
import oracle.sql.BLOB;  
public class TestOracleBLOB {  
//将图片存入Oracle数据库  
@Test  
public void tesetInsert() throws Exception{  
Class.forName("oracle.jdbc.OracleDriver");  
String url = "jdbc:oracle:thin:@localhost:1521:orcl";  
String accounts = "scott";  
String password = "tiger";  
Connection conn = DriverManager.getConnection(url, accounts, password);  
//开启事务  
conn.setAutoCommit(false);  
// 插入空指针  
String sql = "insert into testblob values(?,empty\_blob())";  
PreparedStatement pst = conn.prepareStatement(sql);  
pst.setInt(1, 1);  
//执行  
pst.executeUpdate();  
//查询该空指针  
sql = "select image from testblob where bid=1";  
Statement st = conn.createStatement();  
ResultSet rs = st.executeQuery(sql);  
if(rs.next()){  
BLOB blob = (BLOB) rs.getBlob(1);  
//得到输出流  
OutputStream out = blob.getBinaryOutputStream();  
//创建输入流  
File file = new File("c:\\\\Sunset.jpg");  
FileInputStream in = new FileInputStream(file);  
byte\[\] buffer = new byte\[1024\];  
while(in.read(buffer)>0){  
out.write(buffer);  
}  
out.flush();  
out.close();  
in.close();  
}  
//提交  
conn.commit();  
//释放资源  
rs.close();  
st.close();  
pst.close();  
conn.close();  
System.out.println("完成");  
}  
//取出Oracle数据库中的照片  
@Test  
public void testFindImage() throws Exception{  
Class.forName("oracle.jdbc.OracleDriver");  
String url = "jdbc:oracle:thin:@localhost:1521:orcl";  
String accounts = "scott";  
String password = "tiger";  
Connection conn = DriverManager.getConnection(url, accounts, password);  
String sql = "select image from testblob where bid=1";  
Statement st = conn.createStatement();  
ResultSet rs = st.executeQuery(sql);  
if(rs.next()){  
BLOB blob = (BLOB) rs.getBlob(1);  
//输入流  
InputStream in = blob.getBinaryStream();  
FileOutputStream out = new FileOutputStream("c:\\\\1234.jpg");  
byte\[\] buffer = new byte\[1024\];  
while(in.read(buffer)>0){  
out.write(buffer);  
}  
out.flush();  
out.close();  
in.close();  
}  
rs.close();  
st.close();  
conn.close();  
}  
}
