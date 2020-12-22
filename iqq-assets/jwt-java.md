## Java Token的原理和生成使用机制

[心月狐](https://yq.aliyun.com/users/7ob3wj5vamtne) 2018-05-18 16:16:44 浏览22550

- [算法](https://yq.aliyun.com/tags/type_blog-tagid_37/)
- [java](https://yq.aliyun.com/tags/type_blog-tagid_41/)
- [服务器](https://yq.aliyun.com/tags/type_blog-tagid_372/)
- [加密](https://yq.aliyun.com/tags/type_blog-tagid_423/)
- [浏览器](https://yq.aliyun.com/tags/type_blog-tagid_830/)
- [string](https://yq.aliyun.com/tags/type_blog-tagid_1370/)
- [Annotation](https://yq.aliyun.com/tags/type_blog-tagid_1383/)
- [session](https://yq.aliyun.com/tags/type_blog-tagid_2150/)
- [Servlet](https://yq.aliyun.com/tags/type_blog-tagid_2384/)
- [存储](https://yq.aliyun.com/tags/type_blog-tagid_2618/)
- [cookie](https://yq.aliyun.com/tags/type_blog-tagid_2623/)
- [token](https://yq.aliyun.com/tags/type_blog-tagid_15692/)

在此之前我们先了解一下什么是Cookie、Session、Token

**1、什么是Cookie？**

1. cookie指的就是浏览器里面能永久存储数据的一种数据存储功能。cookie由服务器生成，发送给浏览器，浏览器把cookie以kv形式保存到某个目录下的文本文件内，下一次请求同一网站时会把该cookie发送给服务器。由于cookie是存在客户端上的，所以浏览器加入了一些限制确保cookie不会被恶意使用，同时不会占据太多磁盘空间，所以每个域的cookie数量是有限的。  
    
2. Cookie有什么功能特点呢？在同一个页面中设置 Cookie，实际上是按从后往前的顺序进行的。如果要先删除一个 Cookie，再写入一个 Cookie，则必须先写写入语句，再写删除语句，否则会出现错误 。  
    
3.       Cookie是面向路径的。缺省路径 (path) 属性时，Web 服务器页会自动传递当前路径给浏览器，指定路径强制服务器使用设置的路径。在一个目录页面里设置的 Cookie 在另一个目录的页面里是看不到的 。  
    
4.     Cookie 必须在 HTML 文件的内容输出之前设置；不同的浏览器 (Netscape Navigator、Internet Explorer) 对 Cookie 的处理不一致，使用时一定要考虑；客户端用户如果设置禁止 Cookie，则 Cookie 不能建立。 并且在客户端，一个浏览器能创建的 Cookie 数量最多为 300 个，并且每个不能超过 4KB，每个 Web 站点能设置的 Cookie 总数不能超过 20 个 。  
    
5. Cookie的生命周期呢？：Cookie可以保持登录信息到用户下次与服务器的会话，换句话说，下次访问同一网站时，用户会发现不必输入用户名和密码就已经登录了（当然，不排除用户手工删除Cookie）。而还有一些Cookie在用户退出会话的时候就被删除了，这样可以有效保护个人隐私。Cookie在生成时就会被指定一个Expire值，这就是Cookie的生存周期，在这个周期内Cookie有效，超出周期Cookie就会被清除。有些页面将Cookie的生存周期设置为“0”或负值，这样在关闭浏览器时，就马上清除Cookie，不会记录用户信息，更加安全。  
    

  

**2、什么是session？**

1. session就是会话。这个就类似于你和一个人交谈，你怎么知道当前和你交谈的是张三而不是李四呢？对方肯定有某种特征（长相等）表明他就是张三。  
    
2. session 也是类似的道理，服务器要知道当前发请求给自己的是谁。为了做这种区分，服务器就要给每个客户端分配不同的“身份标识sessionID”，然后客户端每次向服务器发请求的时候，都带上这个“身份标识sessionID”，服务器就知道这个请求来自于谁了。至于客户端怎么保存这个“身份标识sessionID”，可以有很多种方式，对于浏览器客户端，大家都默认采用 cookie 的方式。  
    
3.        服务器使用session把用户的信息临时保存在了服务器上，用户离开网站后session会被销毁。这种用户信息存储方式相对cookie来说更安全，可是session有一个缺陷：如果web服务器做了负载均衡，那么下一个操作请求到了另一台服务器的时候session会丢失。  
    
4.       JSP使用一个叫HttpSession的对象实现同样的功能。HTTPSession 是一个建立在cookies 和URL-rewriting上的高质量的界面。Session的信息保存在服务器端，  
    
5.       Session的id保存在客户机的cookie中。事实上，在许多服务器上，如果浏览器支持的话它们就使用cookies，但是如果不支持或废除了的话就自动转化为URL-rewriting，  
    
6.      session自动为每个流程提供了方便地存储信息的方法。  
    

**3、Httpsession具有如下API：**

1. getId　此方法返回唯一的标识，这些标识为每个session而产生。当只有一个单一的值与一个session联合时，或当日志信息与先前的sessions有关时，它被当作键名用。  
    
2. GetCreationTime　返回session被创建的时间。最小单位为千分之一秒。为得到一个对打印输出很有用的值，可将此值传给Date constructor 或者GregorianCalendar的方法setTimeInMillis.  
    
3. GetLastAccessedTime　返回session最后被客户发送的时间。最小单位为千分之一秒。  
    
4. GetMaxInactiveInterval　返回总时间（秒），负值表示session永远不会超时。  
    
5. getAttribute　取一个session相联系的信息。（在jsp1.0中为 getValue）  
    
6. Integer item = （Integer） session.getAttribute（"item"）　//检索出session的值并转化为整型  
    
7. setAttribute　提供一个关键词和一个值。会替换掉任何以前的值。（在jsp1.0中为putValue）  
    
8. session.setAttribute（"ItemValue"， itemName）；　// ItemValue 必须不是must简单类型  
    
9. 在应用中使用最多的是getAttribute和setAttribute.  
    

  

**4、传统身份验证（session）**

1. HTTP 是一种没有状态的协议，也就是它并不知道是谁是访问应用。这里我们把用户看成是客户端，客户端使用用户名还有密码通过了身份验证，不过下回这个客户端再发送请求时候，还得再验证一下。  
    
2.  解决的方法就是，当用户请求登录的时候，如果没有问题，我们在服务端生成一条记录，这个记录里可以说明一下登录的用户是谁，然后把这条记录的 ID 号发送给客户端，客户端收到以后把这个 ID 号存储在 Cookie 里，  
    
3.  下次这个用户再向服务端发送请求的时候，可以带着这个 Cookie ，这样服务端会验证一个这个 Cookie 里的信息，看看能不能在服务端这里找到对应的记录，如果可以，说明用户已经通过了身份验证，就把用户请求的数据返回给客户端。  
    
4.  上面说的就是 Session，我们需要在服务端存储为登录的用户生成的 Session ，这些 Session 可能会存储在内存，磁盘，或者数据库里。我们可能需要在服务端定期的去清理过期的 Session   
    

  

**5、什么是Token？**

        Token是服务端生成的一串字符串，以作客户端进行请求的一个令牌，当第一次登录后，服务器生成一个Token便将此Token返回给客户端，以后客户端只需带上这个Token前来请求数据即可，无需再次带上用户名和密码。  

基于 Token 的身份验证

1. 使用基于 Token 的身份验证方法，在服务端不需要存储用户的登录记录。流程是这样的：  
    
2. 客户端使用用户名跟密码请求登录  
    
3. 服务端收到请求，去验证用户名与密码  
    
4. 验证成功后，服务端会签发一个 Token，再把这个 Token 发送给客户端  
    
5. 客户端收到 Token 以后可以把它存储起来，比如放在 Cookie 里或者 Local Storage 里  
    
6. 客户端每次向服务端请求资源的时候需要带着服务端签发的 Token  
    
7. 服务端收到请求，然后去验证客户端请求里面带着的 Token，如果验证成功，就向客户端返回请求的数据  
    
8. APP登录的时候发送加密的用户名和密码到服务器，服务器验证用户名和密码，如果成功，以某种方式比如随机生成32位的字符串作为token，存储到服务器中，并返回token到APP，以后APP请求时，  
    
9. 凡是需要验证的地方都要带上该token，然后服务器端验证token，成功返回所需要的结果，失败返回错误信息，让他重新登录。其中服务器上token设置一个有效期，每次APP请求的时候都验证token和有效期。  
    

**6、由此我们可以扩展一个问题：**

1. 服务器上的token存储到数据库中，每次查询会不会很费时。如果不存储到数据库，应该存储到哪里呢。   
    
2. 客户端得到的token肯定要加密存储的，发送token的时候再解密。存储到数据库还是配置文件好呢  
    
3. token是个易失数据，丢了无非让用户重新登录一下，可以放到 MSSQL/MySQL 的内存表里（不过据说mysql的内存表性能提升有限），可以放到 Memcache里，可以放到redis里，放到 OpenResty 的变量字典里（只要你有信心不爆内存）。  
    
4. 你认为用数据库来保持token查询时间太长，会成为你系统的瓶颈或者隐患，可以放在内存当中。   
    
5. 比如memcached、redis，KV方式很适合你对token查询的需求。   
    
6. 这个不会太占内存，比如你的token是32位字符串，要是你的用户量在百万级或者千万级，那才多少内存。   
    
7. 要是数据量真的大到单机内存扛不住，或者觉得一宕机全丢风险大，只要这个token生成是足够均匀的，高低位切一下分到不同机器上就行，内存绝对不会是问题。  
    

  

**7、Token安全问题**

1. 在存储的时候把token进行对称加密存储，用时解开。   
    
2. 将请求URL、时间戳、token三者进行合并加盐签名，服务端校验有效性。   
    
3. 这两种办法的出发点都是：窃取你存储的数据较为容易，而反汇编你的程序hack你的加密解密和签名算法是比较难的。然而其实说难也不难，所以终究是防君子不防小人的做法。话说加密存储一个你要是被人扒开客户端看也不会被喷明文存储……   
    
4. 方法1它拿到存储的密文解不开、方法2它不知道你的签名算法和盐，两者可以结合食用。   
    
5. 但是如果token被人拷走，他自然也能植入到自己的手机里面，那到时候他的手机也可以以你的身份来用着，这你就瞎了。   
    
6. 于是可以提供一个让用户可以主动expire一个过去的token类似的机制，在被盗的时候能远程止损。  
    
7. 在网络层面上token明文传输的话会非常的危险，所以建议一定要使用HTTPS，并且把token放在post body里  
    

**8、基于JWT的Token认证机制实现**  

      JSON Web Token（JWT）是一个非常轻巧的规范。这个规范允许我们使用JWT在用户和服务器之间传递安全可靠的信息。其JWT的组成一个JWT实际上就是一个字符串，它由三部分组成，头部、载荷与签名。  

  

- **载荷（Payload）**  

```json
{
  "iss": "Online JWT Builder",
  "iat": 1416797419,
  "exp": 1448333419,
  "aud": "www.example.com",
  "sub": "jrocket@example.com",
  "GivenName": "Johnny",
  "Surname": "Rocket",
  "Email": "jrocket@example.com",
  "Role": [ "Manager", "Project Administrator" ]
}
```

iss: 该JWT的签发者，是否使用是可选的；
sub: 该JWT所面向的用户，是否使用是可选的；
aud: 接收该JWT的一方，是否使用是可选的；
exp(expires): 什么时候过期，这里是一个Unix时间戳，是否使用是可选的；
iat(issued at): 在什么时候签发的(UNIX时间)，是否使用是可选的；

其他还有：nbf (Not Before)：如果当前时间在nbf里的时间之前，则Token不被接受；一般都会留一些余地，比如几分钟；，是否使用是可选的；

将上面的JSON对象进行\[base64编码\]可以得到下面的字符串。这个字符串我们将它称作JWT的Payload（载荷）。

```text
eyJpc3MiOiJKb2huIFd1IEpXVCIsImlhdCI6MTQ0MTU5MzUwMiwiZXhwIjoxNDQxNTk0NzIyLCJhdWQiOiJ3d3cuZXhhbXBsZS5jb20iLCJzdWIiOiJqcm9ja2V0QGV4YW1wbGUuY29tIiwiZnJvbV91c2VyIjoiQiIsInRhcmdldF91c2VyIjoiQSJ9  
```

小知识：Base64是一种基于64个可打印字符来表示二进制数据的表示方法。由于2的6次方等于64，所以每6个比特为一个单元，对应某个可打印字符。三个字节有24个比特，对应于4个Base64单元，即3个字节需要用4个可打印字符来表示。JDK 中提供了非常方便的 BASE64Encoder 和 BASE64Decoder，用它们可以非常方便的完成基于 BASE64 的编码和解码

- **头部（Header）**  

JWT还需要一个头部，头部用于描述关于该JWT的最基本的信息，例如其类型以及签名所用的算法等。这也可以被表示成一个JSON对象。

{
    "typ": "JWT",
    "alg": "HS256"
}

在头部指明了签名算法是HS256算法。当然头部也要进行BASE64编码，编码后的字符串如下：

```text
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
```

- **签名（Signature）**  
    

将上面的两个编码后的字符串都用句号.连接在一起（头部在前），就形成了:

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcm9tX3VzZXIiOiJCIiwidGFyZ2V0X3VzZXIiOiJBIn0

  

- **最后，我们将上面拼接完的字符串用HS256算法进行加密。**  
    

在加密的时候，我们还需要提供一个密钥（secret）。如果我们用mystar作为密钥的话，那么就可以得到我们加密后的内容:rSWamyAYwuHCo7IFAgd1oRpSP7nzL7BF5t7ItqpKViM  

  

- **最后将这一部分签名也拼接在被签名的字符串后面，我们就得到了完整的JWT:**  
    

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcm9tX3VzZXIiOiJCIiwidGFyZ2V0X3VzZXIiOiJBIn0.rSWamyAYwuHCo7IFAgd1oRpSP7nzL7BF5t7ItqpKViM  

  

  

- **在我们的请求URL中会带上这串JWT字符串：**  
    

https://your.awesome-app.com/make-friend/?jwt=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmcm9tX3VzZXIiOiJCIiwidGFyZ2V0X3VzZXIiOiJBIn0.rSWamyAYwuHCo7IFAgd1oRpSP7nzL7BF5t7ItqpKViM

  

**9、认证过程**

下面我们从一个实例来看如何运用JWT机制实现认证：

  

- **登录**  
    

1. 第一次认证：第一次登录，用户从浏览器输入用户名/密码，提交后到服务器的登录处理的Action层（Login Action）；  
    
2. Login Action调用认证服务进行用户名密码认证，如果认证通过，Login Action层调用用户信息服务获取用户信息（包括完整的用户信息及对应权限信息）；  
    
3. 返回用户信息后，Login Action从配置文件中获取Token签名生成的秘钥信息，进行Token的生成；  
    
4. 生成Token的过程中可以调用第三方的JWT Lib生成签名后的JWT数据；  
    
5. 完成JWT数据签名后，将其设置到COOKIE对象中，并重定向到首页，完成登录过程；  
    

  

- **请求认证**  
    

1. 基于Token的认证机制会在每一次请求中都带上完成签名的Token信息，这个Token信息可能在COOKIE中，也可能在HTTP的Authorization头中；
2. 客户端（APP客户端或浏览器）通过GET或POST请求访问资源（页面或调用API）；  
    
3. 认证服务作为一个Middleware HOOK 对请求进行拦截，首先在cookie中查找Token信息，如果没有找到，则在HTTP Authorization Head中查找；  
    
4. 如果找到Token信息，则根据配置文件中的签名加密秘钥，调用JWT Lib对Token信息进行解密和解码；  
    
5. 完成解码并验证签名通过后，对Token中的exp、nbf、aud等信息进行验证；  
    
6. 全部通过后，根据获取的用户的角色权限信息，进行对请求的资源的权限逻辑判断；  
    
7. 如果权限逻辑判断通过则通过Response对象返回；否则则返回HTTP 401；  
    
8. 对Token认证的五点认识直接注意的地方：

-                         一个Token就是一些信息的集合；  
    
-                         在Token中包含足够多的信息，以便在后续请求中减少查询数据库的几率；  
    
-                         服务端需要对cookie和HTTP Authrorization Header进行Token信息的检查；  
    
-                         基于上一点，你可以用一套token认证代码来面对浏览器类客户端和非浏览器类客户端；  
    
-                         因为token是被签名的，所以我们可以认为一个可以解码认证通过的token是由我们系统发放的，其中带的信息是合法有效的  
    

  

**10、JWT的JAVA实现**

Java中对JWT的支持可以考虑使用JJWT开源库；JJWT实现了JWT, JWS, JWE 和 JWA RFC规范；下面将简单举例说明其使用：

- 生成Token  

```java

import javax.crypto.spec.SecretKeySpec;

import javax.xml.bind.DatatypeConverter;

import java.security.Key;

import io.jsonwebtoken.*;

import java.util.Date;    

//Sample method to construct a JWT

private String createJWT(String id, String issuer, String subject, long ttlMillis) {

//The JWT signature algorithm we will be using to sign the token

SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

long nowMillis = System.currentTimeMillis();

Date now = new Date(nowMillis);

//We will sign our JWT with our ApiKey secret

byte\[\] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());

Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

  //Let's set the JWT Claims

JwtBuilder builder = Jwts.builder().setId(id)

                                .setIssuedAt(now)

                                .setSubject(subject)

                                .setIssuer(issuer)

                                .signWith(signatureAlgorithm, signingKey);

//if it has been specified, let's add the expiration

if (ttlMillis >= 0) {

    long expMillis = nowMillis + ttlMillis;

    Date exp = new Date(expMillis);

    builder.setExpiration(exp);

}

//Builds the JWT and serializes it to a compact, URL-safe string

return builder.compact();

}
```

- 解码和验证Token码  
    

  

import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.Claims;

//Sample method to validate and read the JWT

private void parseJWT(String jwt) {

//This line will throw an exception if it is not a signed JWS (as expected)

Claims claims = Jwts.parser()        

   .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))

   .parseClaimsJws(jwt).getBody();

System.out.println("ID: " + claims.getId());

System.out.println("Subject: " + claims.getSubject());

System.out.println("Issuer: " + claims.getIssuer());

System.out.println("Expiration: " + claims.getExpiration());

}

  

  

  

- 基于JWT的Token认证的安全问题  
    

      如何保证用户名/密码验证过程的安全性；因为在验证过程中，需要用户输入用户名和密码，在这一过程中，用户名、密码等敏感信息需要在网络中传输。因此，在这个过程中建议采用HTTPS，通过SSL加密传输，以确保通道的安全性。

  

  

  

**11、Spring实现Token拦截机制**

1、JWT 简介 

     组成部分：

     Header 头部：一般为token类型和加密算法

     Payload 负载：一些用户信息和额外的声明数据

     Signature 签名：签名需要使用编码后的header和payload以及一个秘钥 （很安全），前两段的结合加密

2：jar包依赖

<dependency>

      <groupId>com.auth0</groupId>

      <artifactId>java-jwt</artifactId>

      <version>3.1.0</version>

</dependency>

<dependency>

      <groupId>io.jsonwebtoken</groupId>

      <artifactId>jjwt</artifactId>

      <version>0.6.0</version>

</dependency>

3：jwt加密和解密的工具类

package com.alienlab.news.utils;

import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.JwtBuilder;

import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;

import javax.xml.bind.DatatypeConverter;

import java.security.Key;

import java.util.Date;

/**

* Created by Msater Zg on 2017/3/13. jwt实现方式

*/

public class JwtUtils {

public static Claims parseJWT(String jsonWebToken, String base64Security) {

    try {

              Claims claims = Jwts.parser()

              .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))

              .parseClaimsJws(jsonWebToken).getBody();

               return claims;

         } catch (Exception ex) {

     return null;

   }

}

前三个参数为自己用户token的一些信息比如id，权限，名称等。不要将隐私信息放入（大家都可以获取到）

public static String createJWT(String name, String userId, String role,

                                                 String audience, String issuer, long TTLMillis, String                                                                   base64Security) {

             SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

              long nowMillis = System.currentTimeMillis();

              Date now = new Date(nowMillis);

              //生成签名密钥 就是一个base64加密后的字符串？

              byte\[\] apiKeySecretBytes =                                                                                                         DatatypeConverter.parseBase64Binary (base64Security);                                   

              Key signingKey = new SecretKeySpec(apiKeySecretBytes,                                                       signatureAlgorithm. getJcaName());

              JSONObject jsonObject = new JSONObject();

              jsonObject.put("userName", name);

              jsonObject.put("userLoginName", userId);

              //添加构成JWT的参数

              JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")

             .setIssuedAt(now) //创建时间

             .setSubject(jsonObject.toString()) //主题，也差不多是个人的一些信息

             .setIssuer(issuer) //发送谁

             .setAudience(audience) //个人签名

             .signWith(signatureAlgorithm, signingKey); //估计是第三段密钥

              //添加Token过期时间

              if (TTLMillis >= 0) {

                      //过期时间

                      long expMillis = nowMillis + TTLMillis;

                      //现在是什么时间

                      Date exp = new Date(expMillis);

                      //系统时间之前的token都是不可以被承认的

                      builder.setExpiration(exp).setNotBefore(now);

                      }

                     //生成JWT

                   return builder.compact();

              }

    }

4：使用的条件（该接口允许跨域 cors来配置跨域）

1：cors配置允许跨域

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.filter.CorsFilter;

import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;

import java.util.List;

/**

* Created by Msater Zg on 2017/4/3.

*/

@Configuration

public class CorsConfig extends WebMvcConfigurerAdapter {

              @Override

              public void addCorsMappings(CorsRegistry registry) {

                          registry.addMapping("/**")

                          .allowedOrigins("*")

                          .allowCredentials(true)

                          .allowedMethods("GET", "POST", "DELETE", "PUT")

                          .maxAge(3600);

                      }

             private CorsConfiguration buildConfig() {

                        CorsConfiguration corsConfiguration = new CorsConfiguration();

                        List<String> list = new ArrayList<>();

                        list.add("*");

                        corsConfiguration.setAllowedOrigins(list);

                        corsConfiguration.addAllowedOrigin("*"); // 1

                        corsConfiguration.addAllowedHeader("*"); // 2

                        corsConfiguration.addAllowedMethod("*"); // 3

                        return corsConfiguration;

             }

             @Bean

              public CorsFilter corsFilter() {

                            UrlBasedCorsConfigurationSource source = new                                                                       UrlBasedCorsConfigurationSource();                            

                            source.registerCorsConfiguration("/**", buildConfig()); // 4

                            return new CorsFilter(source);

                             }

     }

2：拦截器拦截方法获取token

1：拦截器配置

import org.springframework.web.servlet.HandlerInterceptor;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

/**

* Created by Msater Zg on 2017/4/5.

*拦截器

*/

public class ApiInterceptor implements HandlerInterceptor {

                 @Override

                 public boolean preHandle(HttpServletRequest request, HttpServletResponse                                                   response, Object handler) throws Exception {

                                          System.out.println("拦截了");

                                           return true;

                 }

                 @Override

                public void postHandle(HttpServletRequest request, HttpServletResponse                                                           response, Object handler, ModelAndView modelAndView) throws                           Exception {

                                           System.out.println("拦截了");

                  }

                @Override

                 public void afterCompletion(HttpServletRequest request, HttpServletResponse                                                  response, Object handler, Exception ex) throws Exception {

                                            System.out.println("拦截了");

                             }

                 }

2： 拦截器管理工具

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**

* Created by Msater Zg on 2017/4/5.

*拦截器管理工具

*/

@Configuration

public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

             @Override

              public void addInterceptors(InterceptorRegistry registry) {

              //多个拦截器组成一个拦截器链

             // addPathPatterns用于添加拦截规则

             // excludePathPatterns用户排除拦截

             registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/**"); //对来自/user/**              这个链接来的请求进行拦截

             super.addInterceptors(registry);

           }

}

5：token的发送与获取

         ajax为例子：beforeSend:function(request) {

         // token，为登陆时获取到

                request.setRequestHeader("token",token);

           },

            后台获取：request.getHeader("token");

6：token验证机制

          1：通过token解密是否成功可以判断token是否正确或者是否过期

          2：解密完成，可以对比用户属性或者用户的固定token（缓存中或者放入数据库）

  

  

  

**12、生成Token**

app登陆验证不能使用session来判断了这里，我定义了一个token类来存储token。

就是一个字符串+创建的时间戳。然后定义一个管理类来维护token。简单的实现了，但还有很多问题。比如，我对session的理解（是否可以放session，放session之后什么状态），

比如这定义的这个类在调用的时候加载，在不用的时间结束，而我希望一直存在，这个维护类怎么确保存在，这是类的声明周期问题，比如加载到内存和缓存的实现，缓存用的太少。

Token.java

package com.tixa.wedding.util;

 import java.io.Serializable;

 public class Token implements Serializable {

     /** 

     * @Fields serialVersionUID : TODO

     */ 

     private static final long serialVersionUID = -754659525548951914L;

     private String signature;

     private long timestamp;

     public Token(String signature, long timestamp) {

         if (signature == null)

             throw new IllegalArgumentException("signature can not be null");

         this.timestamp = timestamp;

         this.signature = signature;

     }

     public Token(String signature) {

         if (signature == null)

             throw new IllegalArgumentException("signature can not be null");

         this.signature = signature;

     }

     /**

      * Returns a string containing the unique signatureentifier assigned to this token.

      */

     public String getSignature() {

         return signature;

     }

     public long getTimestamp() {

         return timestamp;

     }

     /**

      * timestamp 不予考虑, 因为就算 timestamp 不同也认为是相同的 token.

      */

     public int hashCode() {

         return signature.hashCode();

     }

     public boolean equals(Object object) {

         if (object instanceof Token)

             return ((Token)object).signature.equals(this.signature);

         return false;

     }

     @Override

     public String toString() {

         return "Token \[signature=" + signature + ", timestamp=" + timestamp

                 + "\]";

     }

 }

TokenUtil.java

package com.tixa.wedding.util;

 import java.security.MessageDigest;

 import java.util.Calendar;

 import java.util.Date;

 import java.util.HashMap;

 import java.util.Map;

 import java.util.Map.Entry;

 import java.util.concurrent.Executors;

 import java.util.concurrent.ScheduledExecutorService;

 import java.util.concurrent.TimeUnit;

 import org.apache.log4j.Logger;

 public class TokenUtil {

     private static final int INTERVAL = 7;// token过期时间间隔 天

     private static final String YAN = "testMRf1$789787aadfjkds//*-+'\[\]jfeu;384785*^*&%^%$%";// 加盐

     private static final int HOUR = 3;// 检查token过期线程执行时间 时

    private static Logger logger = Logger.getLogger("visit");

  

    private static Map<Integer, Token> tokenMap = new HashMap<Integer, Token>();

    private static TokenUtil tokenUtil = null;

    static ScheduledExecutorService scheduler =Executors.newSingleThreadScheduledExecutor(); 

  

    static {

        logger.info("\\n===============进入TokenUtil静态代码块==================");

        listenTask();

    }

  

    public static TokenUtil getTokenUtil() {

        if (tokenUtil == null) {

            synInit();

        }

  

        return tokenUtil;

    }

  

    private static synchronized void synInit() {

        if (tokenUtil == null) {

            tokenUtil = new TokenUtil();

        }

    }

  

    public TokenUtil() {

    }

  

    public static Map<Integer, Token> getTokenMap() {

        return tokenMap;

    }

  

    /**

     * 产生一个token

     */

    public static Token generateToken(String uniq,int id) {

        Token token = new Token(MD5(System.currentTimeMillis()+YAN+uniq+id), System.currentTimeMillis());

        synchronized (tokenMap) {

            tokenMap.put(id, token);

        }

         return token;

     }

     /**

      * @Title: removeToken

      * @Description: 去除token

      * @param @param nonce

      * @param @return 参数

      * @return boolean 返回类型

      */

     public static boolean removeToken(int id) {

         synchronized (tokenMap) {

             tokenMap.remove(id);

             logger.info(tokenMap.get(id) == null ? "\\n=========已注销========": "\\n++++++++注销失败+++++++++++++++");

         }

         return true;

     }

     /**
      * @Title: volidateToken
      * @Description: 校验token
      * @param signature
      * @param nonce
      * @param 参数
      * @return boolean 返回类型
      */
     public static boolean volidateToken(String signature, int id) {
         boolean flag = false;
         Token token = (Token) tokenMap.get(id

         if (token != null && token.getSignature().equals(signature)) {
             logger.info("\\n=====已在线=======");
             flag = true;
         }
         return flag;
     }
     /**
      * 
      * @Title: MD5
      * @Description: 加密
      * @param @param s
      * @param @return 参数
      * @return String 返回类型
      */
     public final static String MD5(String s) {
         try {
             byte\[\] btInput = s.getBytes();
             // 获得MD5摘要算法的 MessageDigest 对象
             MessageDigest mdInst = MessageDigest.getInstance("MD5");
             // 使用指定的字节更新摘要
             mdInst.update(btInput);
             // 获得密文
             return byte2hex(mdInst.digest());
         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }
     /**
      * 将字节数组转换成16进制字符串
      * @param b
      * @return
      */
     private static String byte2hex(byte\[\] b) {
         StringBuilder sbDes = new StringBuilder();
         String tmp = null;
         for (int i = 0; i < b.length; i++) {
             tmp = (Integer.toHexString(b\[i\] & 0xFF));
             if (tmp.length() == 1) {
                 sbDes.append("0");
             }
             sbDes.append(tmp);
         }
         return sbDes.toString();
     }

     /**
     * @Title: listenTask
     * @Description: 定时执行token过期清除任务
     * @param     参数
     * @return void    返回类型
      */
     public static void listenTask(){
         Calendar calendar = Calendar.getInstance();
         int year = calendar.get(Calendar.YEAR);
         int month = calendar.get(Calendar.MONTH);
         int day = calendar.get(Calendar.DAY\_OF\_MONTH);
         //定制每天的HOUR点，从明天开始
         calendar.set(year, month, day+1, HOUR, 0, 0);
        // calendar.set(year, month, day, 17, 11, 40);
         Date date = calendar.getTime();
         scheduler.scheduleAtFixedRate( new ListenToken(), (date.getTime()-System.currentTimeMillis())/1000, 60*60*24, TimeUnit.SECONDS);
     }

     /**
      * @ClassName: ListenToken
      * @Description: 监听token过期线程runnable实现
      * @author mrf
      * @date 2015-10-21 下午02:22:24
      */
     static class ListenToken implements Runnable {
         public ListenToken() {
             super();
         }

         public void run() {
             logger.info("\\n**************************执行监听token列表****************************");
             try {
                 synchronized (tokenMap) {
                     for (int i = 0; i < 5; i++) {
                         if (tokenMap != null && !tokenMap.isEmpty()) {
                             for (Entry<Integer, Token> entry : tokenMap.entrySet()) {
                                 Token token = (Token) entry.getValue();
                                 logger.info("\\n==============已登录用户有："+entry + "=====================");
 //                            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                                 int interval = (int) ((System.currentTimeMillis() - token.getTimestamp()) / 1000 / 60 / 60 / 24);
                                 if (interval > INTERVAL) {
                                     tokenMap.remove(entry.getKey());
                                     logger.info("\\n==============移除token：" + entry+ "=====================");
                                 }
                             }
                         }
                     }
                 }
             } catch (Exception e) {
                 logger.error("token监听线程错误："+e.getMessage());
                 e.printStackTrace();
             }
         }
     }

     public static void main(String\[\] args) {
         System.out.println(generateToken( "s",1));
         System.out.println(generateToken( "q",1));
         System.out.println(generateToken( "s3",2));
         System.out.println(generateToken( "s4",3));
         System.out.println(removeToken(3));
         System.out.println(getTokenMap());
     }
 }
```