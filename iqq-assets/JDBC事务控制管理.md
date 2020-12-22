# JDBC事务控制管理

![](https://csdnimg.cn/release/phoenix/template/new_img/reprint.png)

[caomiao2006](https://me.csdn.net/caomiao2006) 2014-03-28 16:55:11 ![](https://csdnimg.cn/release/phoenix/template/new_img/articleRead.png) 33203 ![](https://csdnimg.cn/release/phoenix/template/new_img/collect.png) ![](https://csdnimg.cn/release/phoenix/template/new_img/tobarCollectionActive.png) 收藏  5 

最后发布:2014-03-28 16:55:11首发:2014-03-28 16:55:11

分类专栏： [数据库](https://blog.csdn.net/caomiao2006/category_581813.html) [JAVA](https://blog.csdn.net/caomiao2006/category_582306.html)

1、事务

（1）事务的概念

事务指逻辑上的一组操作，组成这组操作的各个单元，要不全部成功，要不全部不成功。

例如：A——B转帐，对应于如下两条sql语句

```sql
update account set money=money-100 where name=‘a’; 

update account set money=money+100 where name=‘b’;
```

数据库默认事务是自动提交的，也就是发一条sql它就执行一条。如果想多条sql放在一个事务中执行，则需要使用如下语句。

（2）数据库开启事务命令

方式一：利用SQL语句管理事务


start transaction;--开启事务，这条语句之后的sql语句将处在一个事务当中，这些sql语句并不会立即执行

Commit--提交事务，一旦提交事务，事务中的所有sql语句才会执行。

Rollback -- 回滚事务，将之前所有的sql取消。

方式二：在数据库中存在一个自动提交变量，通过show variables like '%commit%'-----autocommit 值是on，说明开启了事务自动提交。

可以 set autocommint = off（set autocommint=0），关闭自动提交，此时输入的sql语句就不会自动提交了，需要手动roolback或commit

2、使用事务

（1）当Jdbc程序向数据库获得一个Connection对象时，默认情况下这个Connection对象会自动向数据库提交在它上面发送的SQL语句。若想关闭这种默认提交方式，让多条SQL在一个事务中执行，可使用下列语句：

（2）JDBC控制事务语句

Connection.setAutoCommit(false); //  相当于start transaction

Connection.rollback();  rollback

Connection.commit();  commit

3、演示银行转帐案例

（1）在JDBC代码中使如下转帐操作在同一事务中执行。

  update from account set money=money-100 where name=‘a’;

  update from account set money=money+100 where name=‘b’;

（2）设置事务回滚点

```java
Savepoint sp = conn.setSavepoint();

Conn.rollback(sp);

Conn.commit();   //回滚后必须要提交

package com.itheima.transaction;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.sql.Savepoint;

import org.junit.Test;

import com.itheima.util.DaoUtil;

public class Demo1 {

@Test

public void test1(){

Connection conn = null;

PreparedStatement ps1 = null;

PreparedStatement ps2 = null;

Savepoint sp = null;

try{
    Class.forName("com.mysql.jdbc.Driver");
    conn = DriverManager.getConnection("jdbc:mysql:///day11", "root", "root");
    conn.setAutoCommit(false);
    ps1 = conn.prepareStatement("update account set money = money+100 where name=?");
    ps1.setString(1, "b");
    ps1.executeUpdate();
    //int i = 1/0;
    ps2 = conn.prepareStatement("update account set money = money-100 where name=?");
    ps2.setString(1, "a");
    ps2.executeUpdate();
    sp = conn.setSavepoint();
    //-----------------------------------
    ps1 = conn.prepareStatement("update account set money = money+100 where name=?");
    ps1.setString(1, "b");
    ps1.executeUpdate();
    int i = 1/0;
    ps2 = conn.prepareStatement("update account set money = money-100 where name=?");
    ps2.setString(1, "a");
    ps2.executeUpdate();
    conn.commit();
    }catch (Exception e) {
    e.printStackTrace();
    try {
    if(sp !=null){
    conn.rollback(sp);
    conn.commit();
    }else{
    conn.rollback();
    }
} catch (SQLException e1) {
// TODO Auto-generated catch block
e1.printStackTrace();
}
}finally{
DaoUtil.close(conn, ps1, null);
DaoUtil.close(conn, ps2, null);
}
}
}
```
  

JDBC API支持事务对数据库的加锁，并且提供了5种操作支持，2种加锁密度。

5种加锁支持为：

```sql
static int TRANSACTION\_NONE = 0;

static int TRANSACTION\_READ\_UNCOMMITTED = 1;

static int TRANSACTION\_READ\_COMMITTED = 2;

static int TRANSACTION\_REPEATABLE\_READ = 4;

static int TRANSACTION\_SERIALIZABLE = 8;
```

具体的说明见表4-2。

2种加锁密度：

最后一项为表加锁，其余3～4项为行加锁。

  

JDBC根据数据库提供的默认值来设置事务支持及其加锁，当然，也可以手工设置：

setTransactionIsolation(TRANSACTION\_READ\_UNCOMMITTED);

可以查看数据库的当前设置：

getTransactionIsolation ()

需要注意的是，在进行手动设置时，数据库及其驱动程序必须得支持相应的事务操作操作才行。

上述设置随着值的增加，其事务的独立性增加，更能有效地防止事务操作之间的冲突，同时也增加了加锁的开销，降低了用户之间访问数据库的并发性，程序的运行效率也会随之降低。因此得平衡程序运行效率和数据一致性之间的冲突。一般来说，对于只涉及到数据库的查询操作时，可以采用TRANSACTION\_READ\_UNCOMMITTED方式；对于数据查询远多于更新的操作，可以采用TRANSACTION\_READ\_COMMITTED方式；对于更新操作较多的，可以采用TRANSACTION\_REPEATABLE\_READ；在数据一致性要求更高的场合再考虑最后一项，由于涉及到表加锁，因此会对程序运行效率产生较大的影响。

另外，在Oracle中数据库驱动对事务处理的默认值是TRANSACTION\_NONE，即不支持事务操作，所以需要在程序中手动进行设置。总之，JDBC提供的对数据库事务操作的支持是比较完整的，通过事务操作可以提高程序的运行效率，保持数据的一致性。

  

4、事务的特性(ACID)

（1）原子性（Atomicity）  
原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生。

（2）一致性（Consistency）  
事务前后数据的完整性必须保持一致。

（3）隔离性（Isolation）  
事务的隔离性是指多个用户并发访问数据库时，一个用户的事务不能被其它用户的事务所干扰，多个并发事务之间数据要相互隔离。

（4）持久性（Durability）  
持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来即使数据库发生故障也不应该对其有任何影响

5、事务的隔离级别

（1）多个线程开启各自事务操作数据库中数据时，数据库系统要负责隔离操作，以保证各个线程在获取数据时的准确性。

（2）如果不考虑隔离性，可能会引发如下问题：

脏读（dirty reads） 

一个事务读取了另一个未提交的并行事务写的数据。 

不可重复读（non-repeatable reads） 

一个事务重新读取前面读取过的数据， 发现该数据已经被另一个已提交的事务修改过。 

幻读（phantom read） 

一个事务重新执行一个查询，返回一套符合查询条件的行， 发现这些行因为其他最近提交的事务而发生了改变。

6、事务的隔离性

（1）脏读：

指一个事务读取了另外一个事务未提交的数据。

这是非常危险的，假设Ａ向Ｂ转帐１００元，对应sql语句如下所示

1.update account set money=money+100 while name=‘b’;

2.update account set money=money-100 while name=‘a’;

当第1条sql执行完，第2条还没执行(A未提交时)，如果此时Ｂ查询自己的帐户，就会发现自己多了100元钱。如果A等B走后再回滚，B就会损失100元。

        a 1000

b 1000

a:

start transaction;

update account set money=money-100 where name='a';

update account set money=money+100 where name='b';

b:

start transaction;

select \* from account where name='b';

a:

rollback;

b:

strat transaction;

select \* from account where name='b';

（2）不可重复读：

在一个事务内读取表中的某一行数据，多次读取结果不同。

例如银行想查询A帐户余额，第一次查询A帐户为200元，此时A向帐户存了100元并提交了，银行接着又进行了一次查询，此时A帐户为300元了。银行两次查询不一致，可能就会很困惑，不知道哪次查询是准的。

和脏读的区别是，脏读是读取前一事务未提交的脏数据，不可重复读是重新读取了前一事务已提交的数据。

很多人认为这种情况就对了，无须困惑，当然是后面的为准。我们可以考虑这样一种情况，比如银行程序需要将查询结果分别输出到电脑屏幕和写到文件中，结果在一个事务中针对输出的目的地，进行的两次查询不一致，导致文件和屏幕中的结果不一致，银行工作人员就不知道以哪个为准了。

        a 1000

b 1000

start transaction;

select sum(money) from account; ---- 总存款：2000

select count(\*) from account; ---- 总账户数：2

\-------------------------

b:

start transaction;

update account set money = money-1000 where name='b';

commit;

\-------------------------

select avg(money) from account; ---- 账户平均金额：500

（3）虚读(幻读)

是指在一个事务内读取到了别的事务插入的数据，导致前后读取不一致。

如丙存款100元未提交，这时银行做报表统计account表中所有用户的总额为500元，然后丙提交了，这时银行再统计发现帐户为600元了，造成虚读同样会使银行不知所措，到底以哪个为准。

            a 1000

b 1000

c 1000

start transaction;

select sum(money) from account; ---- 总存款：2000

\-------------------------

c:

start transaction;

insert into account values(null,'c',1000);

commit;

\-------------------------

select count(\*) from account; ---- 总账户数：3

7、事务隔离性的设置语句

数据库共定义了四种隔离级别：

Serializable：可避免脏读、不可重复读、虚读情况的发生。（串行化）

Repeatable read：可避免脏读、不可重复读情况的发生。（可重复读）不可以避免虚读

Read committed：可避免脏读情况发生（读已提交）

Read uncommitted：最低级别，以上情况均无法保证。(读未提交)

set \[global/session\]  transaction isolation level 设置事务隔离级别

select @@tx\_isolation查询当前事务隔离级别

安全性来说：Serializable>Repeatable read>Read committed>Read uncommitted

效率来说：Serializable<Repeatable read<Read committed<Read uncommitted

通常来说，一般的应用都会选择Repeatable read或Read committed作为数据库隔离级别来使用。

mysql默认的数据库隔离级别为：REPEATABLE-READ

如何查询当前数据库的隔离级别？select @@tx\_isolation;

如何设置当前数据库的隔离级别？set \[global/session\] transaction isolation level ...;

~此种方式设置的隔离级别只对当前连接起作用。

set transaction isolation level read uncommitted;

set session transaction isolation level read uncommitted;

~此种方式设置的隔离级别是设置数据库默认的隔离级别

set global transaction isolation level read uncommitted;

8、事务的丢失更新问题(lost update )

（1）两个或多个事务更新同一行，但这些事务彼此之间都不知道其它事务进行的修改，因此第二个更改覆盖了第一个修改 

（2）共享锁：共享锁和共享锁可以共存。共享锁和排他锁不能共存。在Serializable隔离级别下一个事务进行查询操作将会加上共享锁。

（3）排他锁：排他锁和所有锁都不能共存。无论什么隔离级别执行增删改操作时，会加上排他锁

（4）.数据库设计为Serializable隔离级别，就可以防止更新丢失问题。

乐观锁和悲观锁并不是数据库中真实存在的锁，是我们如何利用共享和排他锁解决更新丢失问题的两种解决方案，体现了人们看待事务的态度：

悲观锁：悲观的认为大部分情况下进行操作都会出现更新丢失问题。

在每次进行查询的时候，都手动的加上一个排他锁。

select \* from table lock in share mode（读锁、共享锁）

select \* from table for update （写锁、排它锁）

乐观锁：乐观的认为大部分的情况下都不会有更新丢失问题。通过时间戳字段，

在表中设计一个版本字段version，当每次对数据库中的数据进行修改操作时，版本号都要进行增加。

（5）如果我的程序修改比较少查询比较多：乐观锁

如果我的程序查询比较少修改比较多：悲观锁