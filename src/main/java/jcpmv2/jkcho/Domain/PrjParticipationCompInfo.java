package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "prjparticipationcomp")
@Getter
@Setter
@DynamicUpdate
public class PrjParticipationCompInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "p_id")
    private Long pid;

    @Column(name = "c_id")
    private Long cid;

    @Column(name = "e_id")
    private Long eid;
}
