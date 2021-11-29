package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.sound.sampled.BooleanControl;

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

    @Column(name = "c_view")
    private Boolean cview;

    @Column(name = "e_view")
    private Boolean eview;
}
