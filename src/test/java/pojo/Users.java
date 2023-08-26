package pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Users {
    //with using lombok
    private String name;
    private String id;
    private String email;
    private String gender;
    private String status;
}
