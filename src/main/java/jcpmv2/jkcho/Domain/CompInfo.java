package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "companyinformation")
@Getter
@Setter
@DynamicUpdate
public class CompInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long cid;

    @Column(name="c_name")
    private String cname;

    @Column(name="c_boss")
    private String cboss;

    @Column(name="c_call")
    private String ccall;

    @Column(name="c_number")
    private String cnumber;

    @Column(name="c_view")
    private Boolean cview;
}
