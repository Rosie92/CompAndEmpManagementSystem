package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "project")
@Getter
@Setter
@DynamicUpdate
public class PrjInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long pid;

    @Column(name="p_name")
    private String pname;

    @Column(name="p_content")
    private String pcontent;

    @Column(name="p_view")
    private Boolean pview;
}
