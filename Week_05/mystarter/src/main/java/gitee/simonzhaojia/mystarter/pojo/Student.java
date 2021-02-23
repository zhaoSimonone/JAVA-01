package gitee.simonzhaojia.mystarter.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
@Slf4j
public class Student implements Serializable {

    private String id;

    private String name;

}
