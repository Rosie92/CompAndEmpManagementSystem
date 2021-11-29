package jcpmv2.jkcho.Domain;

public interface IPrjParticipationCompGetName {

    String getCname();
    /*
        CompInfo, PrjInfo, PrjParticipationCompInfo
        세 개의 도메인을 조인하여
        프로젝트에 참여하고 있는 회사 이름을 가져오고자 함
        IcompJpaTryRepository 에서 쿼리문을 이용, 데이터를 받아왔지만 이를 담는 과정에서
        Repository에서 CompInfo만 참조하고 있기에 오류가 발생
        이 정보들을 담아오기 위해 임시로 해당 interface를 활용
     */
    String getCount();
    String getCid();
}
