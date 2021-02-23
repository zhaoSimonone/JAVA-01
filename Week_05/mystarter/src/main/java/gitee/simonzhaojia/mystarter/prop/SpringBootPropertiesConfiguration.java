package gitee.simonzhaojia.mystarter.prop;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mystarter.student")
@Getter
@Setter
@Data
public class SpringBootPropertiesConfiguration {

  private String id;
  private String name;

}
