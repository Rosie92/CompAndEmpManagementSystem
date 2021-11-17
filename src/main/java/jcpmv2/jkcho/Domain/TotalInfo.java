package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "total")
@Getter
@Setter
@DynamicUpdate
public class TotalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "p_id")
    private Long pid;

    @Column(name = "c_id")
    private Long cid;

    @Column(name = "e_id")
    private Long eid;

    @Column(name = "p_content")
    private String pcontent;
}
