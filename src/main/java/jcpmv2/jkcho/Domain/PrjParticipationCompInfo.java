package jcpmv2.jkcho.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.sound.sampled.BooleanControl;

/* 테이블과의 매핑
@Entity이 붙은 클래스는 JPA가 관리함
해당 어노테이션이 붙은 클래스에는 다음 제약사항이 필요함
- 필드에 final, enum, interface, class를 사용할 수 없음
- 생성자 중 기본 생성자가 반드시 필요함 */
@Entity
// @Entity와 매핑할 테이블(DB)를 지정함
@Table(name = "prjparticipationcomp")
// 롬복 어노테이션. 각각 접근자와 설정자 메소드를 작성함
@Getter
@Setter
// JPA Entity에 사용하는 어노테이션. 실제 값이 변경된 컬럼만 처리해줌
@DynamicUpdate
public class PrjParticipationCompInfo {
    // JPA가 객체를 관리할 때 식별할 기본키를 지정
    @Id
    /*
        기본키의 값을 위한 자동 생성 전략을 명시하는데 사용
        선택적 속성으로 generator와 strategy가 있음
        - generator : SequenceGenerator나 TableGenerator 어노테이션에서 명시된 기본키 생성자를 재사용할 때 쓰임. (Default: "")
        - strategy : persistence provider가 엔티티의 기본키를 생성할 때 사용해야하는 기본키 생성 전략을 의미함. (Default: AUTO)
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") // 객체 필드를 테이블 컬럼에 매핑
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
