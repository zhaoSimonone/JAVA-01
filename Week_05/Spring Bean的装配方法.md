## 通过构造方法来创建Bean

### 使用xml来配置Bean

需要通过Setter方法来注入值，因此Student类中Setter方法是必须的，Getter方法可以不要。

Student类

```java
package spring.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//自动生成Getter和Setter方法,还会生成ToString方法
public class Student {

  private String name;
  private String id;
  private String school;
  
}
```

在resources文件夹下新建`applicationContext.xml`配置文件

```shell
D:\dev\Java\java-traning\spring-learning>tree
卷 Data 的文件夹 PATH 列表
卷序列号为 5665-4A88
D:.
├─.idea
│  └─inspectionProfiles
├─src
│  ├─main
│  │  ├─java
│  │  │  └─spring
│  │  │      ├─aop
│  │  │      └─beans
│  │  └─resources
│  └─test
│      └─java
└─target
    ├─classes
    │  └─spring
    │      └─beans
    └─generated-sources
        └─annotations
```

在`applicationContext.xml`中配置bean

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="student01" class="spring.beans.Student">
        <property name="name" value="张三"/>
        <property name="id" value="20171060001"/>
        <property name="school" value="北京大学"/>
    </bean>

</beans>
```

测试类，从`ClassPathXmlApplicationContext`中取出bean。

```java
package spring.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    Student student = (Student) context.getBean("student01");
    System.out.println(student.toString());
  }
}
```

![image-20210220151540795](https://learninig.oss-cn-beijing.aliyuncs.com/java/java-training/spring/Spring Bean的装配方法/Snipaste_2021-02-20_19-23-12.png?versionId=CAEQDhiBgICoktz8vRciIDI0NDhiNDA0NzkzMjQ5M2M4M2JkNjkyYTM0NTZhZTY4)

### 使用注解配置Bean

#### 方法一：使用@Bean显示声明

Student类

```java
package spring.beans;

import lombok.Data;

@AllArgsConstructor//有参构造方法是必需的
@ToString
public class Student {

  private String name;
  private String id;
  private String school;

}
```

Student的配置类。

带有 **@Configuration** 的注解类表示这个类可以使用 Spring IoC 容器作为 bean 定义的来源。**@Bean** 注解告诉 Spring，一个带有 @Bean 的注解方法将返回一个对象，该对象应该被注册为在 Spring 应用程序上下文中的 bean。

```java
package spring.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConf {

  @Bean(name = "student02")
  public Student student() {
    return new Student("李四", "20171060002", "清华大学");
  }
}
```

测试类：指定含有bean注解的package。

```java
package spring.beans;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext("spring.beans");
    Student student = (Student) context.getBean("student02");
    System.out.println(student.toString());
  }
}
```

![image-20210220153615065](https://learninig.oss-cn-beijing.aliyuncs.com/java/java-training/spring/Spring Bean的装配方法/Snipaste_2021-02-20_19-25-31.png?versionId=CAEQDhiBgMCYteD8vRciIDg1YWI1YTc4YjE4NzQyMTc4YzVlZTkwNzQ0MjM0ODc3)

#### 方法二：不使用@Bean显示声明

Student类。

使用`@Component`，`@Service`，`@Controller`，`@Repository`都能隐式地创建Bean。Student类中不需要有参构造方法，Setter方法也不需要。无参构造方法是默认就有的。

```java
package spring.beans;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@ToString
@Component
//@Service
//@Controller
//@Repository
public class Student {

  @Value("王五")
  private String name;

  @Value("20171060003")
  private String id;

  @Value("南京大学")
  private String school;
}
```

测试类

```java
package spring.beans;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {;
    ApplicationContext context = new AnnotationConfigApplicationContext("spring.beans");
    Student student = context.getBean(Student.class);
    System.out.println(student.toString());
  }
}
```

![image-20210220162635414](https://learninig.oss-cn-beijing.aliyuncs.com/java/java-training/spring/Spring Bean的装配方法/Snipaste_2021-02-20_19-26-31.png?versionId=CAEQDhiBgMCJneL8vRciIGYxMWI4MzkwM2Y2NjRhYzJiYjFhNDUzZmE4NjBmNzMx)

## 通过静态方法来创建Bean

Student类

```java
package spring.beans;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Student {

  private String name;

  private String id;

  private String school;

  public static Student createStudent() {
    Student student = new Student();
    student.setName("刘六");
    student.setId("20171060004");
    student.setSchool("上海交通大学");
    return student;
  }
}
```

`applicationContext.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="student04" class="spring.beans.Student" factory-method="createStudent"/>

</beans>
```

测试类

```java
package spring.beans;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
  public static void main(String[] args) {
    BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
    Student student = beanFactory.getBean("student04", Student.class);
    System.out.println(student.toString());
  }
}
```

![image-20210220175057141](https://learninig.oss-cn-beijing.aliyuncs.com/java/java-training/spring/Spring Bean的装配方法/Snipaste_2021-02-20_19-27-06.png?versionId=CAEQDhiBgICEo.P8vRciIDQ2NTA0MjZkYTUxMjQ0MGQ5OTNlMmUzMGFkYjY3MDY4)

## 通过实例化方法创建Bean

Student类保持不变

```java
package spring.beans;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Student {

  private String name;

  private String id;

  private String school;

  public static Student createStudent() {
    Student student = new Student();
    student.setName("刘六");
    student.setId("20171060004");
    student.setSchool("上海交通大学");
    return student;
  }
}
```

定义一个工厂接口

```java
package spring.beans.factory;

import spring.beans.Student;

public interface StudentFactory {

  default Student factoryCreateStudent() {
    return Student.createStudent();
  }
}
```

定义接口的实现类，由于接口中有默认实现了，所以不需要实现。

```java
package spring.beans.factory;

public class DefaultStudentFactory implements StudentFactory {

}
```

`applicationContext.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 静态方法实例化bean -->
    <bean id="student04" class="spring.beans.Student" factory-method="createStudent"/>

    <bean id="useBeanFactory" class="spring.beans.factory.DefaultStudentFactory"/>

    <!-- 实例方法实例化bean -->
    <bean id="student05" factory-bean="useBeanFactory" factory-method="factoryCreateStudent"/>

</beans>
```

测试类。这种方式与使用静态方法来创建Bean相似，但是不相同。

```java
package spring.beans;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
  public static void main(String[] args) {
    BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
    Student student04 = beanFactory.getBean("student04", Student.class);
    Student student05 = beanFactory.getBean("student05", Student.class);
    System.out.println(student04);
    System.out.println(student05);
    System.out.println(student04 == student05);
  }
}
```

![image-20210220185824916](https://learninig.oss-cn-beijing.aliyuncs.com/java/java-training/spring/Spring Bean的装配方法/Snipaste_2021-02-20_19-27-51.png?versionId=CAEQDhiBgMDzz.T8vRciIDVmMWUyODI0ZmUwMjRkZWFhYjIxNDVhNzgxOTg2YzVk)

## 通过FactoryBean来创建Bean

Student保存不变

```java
package spring.beans;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class Student {

  private String name;

  private String id;

  private String school;

  public static Student createStudent() {
    Student student = new Student();
    student.setName("刘六");
    student.setId("20171060004");
    student.setSchool("上海交通大学");
    return student;
  }
}
```

定义StudentFactoryBean实现FactoryBean

```java
package spring.beans.factory;

import org.springframework.beans.factory.FactoryBean;
import spring.beans.Student;

public class StudentFactoryBean implements FactoryBean {

  @Override
  public Object getObject() throws Exception {
    return Student.createStudent();
  }

  @Override
  public Class<?> getObjectType() {
    return Student.class;
  }
}
```

`applicationContext.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 静态方法实例化bean -->
    <bean id="student04" class="spring.beans.Student" factory-method="createStudent"/>

    <bean id="useBeanFactory" class="spring.beans.factory.DefaultStudentFactory"/>

    <!-- 实例方法实例化bean -->
    <bean id="student05" factory-bean="useBeanFactory" factory-method="factoryCreateStudent"/>

    <!-- 通过FactoryBean来创建Bean -->
    <bean id="studentfactoryBean" class="spring.beans.factory.StudentFactoryBean"/>

</beans>
```

测试类

```java
package spring.beans;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
  public static void main(String[] args) {
    BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
    Student student04 = beanFactory.getBean("student04", Student.class);
    Student student05 = beanFactory.getBean("student05", Student.class);
    Student studentFactoryBean = beanFactory.getBean("studentfactoryBean", Student.class);
      
    System.out.println(student04);
    System.out.println(student05);
    System.out.println(studentFactoryBean);

    System.out.println(student04 == student05);
    System.out.println(student05 == studentFactoryBean);
  }
}
```

![image-20210220191337587](https://learninig.oss-cn-beijing.aliyuncs.com/java/java-training/spring/Spring Bean的装配方法/Snipaste_2021-02-20_19-28-25.png?versionId=CAEQDhiBgMDO7uX8vRciIDExNTQ1NDVlZTI0ZjQ1YjRhODg1NTQ0ZGRmNjhlMTNm)