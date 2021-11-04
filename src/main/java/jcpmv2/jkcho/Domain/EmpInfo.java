package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "employeeinformation")
@Getter
@Setter
@DynamicUpdate
public class EmpInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long eid;

    @Formula("(select c.c_name from companyinformation c, employeeinformation e where c.id = e.e_compid and e.e_compid=e_compid limit 1)")
    private String cname;

    @Column(name="e_name")
    private String ename;

    @Column(name="e_email")
    private String eemail;

    @Column(name="e_phone")
    private String ephone;

    @Column(name="e_position")
    private String eposition;

    @Column(name="e_affiliation")
    private String eaffiliation;

    @Column(name="e_compid")
    private Long ecompid;

    @Column(name="e_view")
    private Boolean eview;
}
