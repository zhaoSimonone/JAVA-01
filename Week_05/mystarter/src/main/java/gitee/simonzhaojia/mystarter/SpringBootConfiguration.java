package gitee.simonzhaojia.mystarter;


import gitee.simonzhaojia.mystarter.pojo.Student;
import gitee.simonzhaojia.mystarter.prop.SpringBootPropertiesConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SpringBootPropertiesConfiguration.class)
@ConditionalOnProperty(prefix = "mystarter.student", name = "enabled", havingValue = "true", matchIfMissing = true)
//@ComponentScan("gitee.simonzhaojia.mystarter")
@Slf4j
public class SpringBootConfiguration {

  @Autowired
  public SpringBootPropertiesConfiguration props;

  @Bean
  public Student student() {
    log.info("Student id : " + props.getId());
    log.info("Student name : " + props.getName());
    return new Student(props.getId(), props.getName());
  }
}
