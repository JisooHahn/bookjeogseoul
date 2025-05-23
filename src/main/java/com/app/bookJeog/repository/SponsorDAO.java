package com.app.bookJeog.repository;


import com.app.bookJeog.domain.dto.SponsorPostDTO;
import com.app.bookJeog.domain.vo.SponsorMemberVO;
import com.app.bookJeog.mapper.SponsorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class SponsorDAO {

    private final SponsorMapper sponsorMapper;

    // 단체 로그인
    public Optional<SponsorMemberVO> findSponsorMember (SponsorMemberVO sponsorMemberVO) {
       return sponsorMapper.loginSponsorMember(sponsorMemberVO);
    };


    // 이메일 중복검사
    public Optional<SponsorMemberVO> findSponsorMemberEmail(SponsorMemberVO sponsorMemberVO) {
       return sponsorMapper.selectSponsorMember(sponsorMemberVO);
    }


    // 비밀번호 변경
    public void updatePassword(SponsorMemberVO sponsorMemberVO) {
        sponsorMapper.updateSponsorPasswd(sponsorMemberVO);
    }

    // 마이페이지 기업회원 조회
    public SponsorPostDTO selectSponsorMypage(Long sponsorId){
        return sponsorMapper.selectSponsorMypage(sponsorId);
    };

    // 단체명 변경
    public void setSponsorName(SponsorMemberVO sponsorMemberVO){sponsorMapper.updateSponsorName(sponsorMemberVO);};

    // 단체 중복아이디 검사
    public boolean isIdExisting(String sponsorId){return sponsorMapper.isIdExisting(sponsorId);};

    // 아이디 변경
    public void setSponsorId(SponsorMemberVO sponsorMemberVO){sponsorMapper.updateSponsorId(sponsorMemberVO);};

    // 비밀번호 변경
    public void setSponsorPassword(SponsorMemberVO sponsorMemberVO){sponsorMapper.updateSponsorPassword(sponsorMemberVO);};

    // 회원 비번 조회
    public String getPasswordById(Long sponsorId){return sponsorMapper.getPasswordById(sponsorId);};

    // 탈퇴페이지에서 활동 조회
    public Map<String, Object> selectMyActivities(Long memberId){return sponsorMapper.selectMyActivities(memberId);};

    // 단체회원 탈퇴
    public void updateDeletedMemberStatus(Long memberId){sponsorMapper.updateDeletedMemberStatus(memberId);};
}
