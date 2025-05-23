package com.app.bookJeog.mapper;


import com.app.bookJeog.domain.dto.SponsorPostDTO;
import com.app.bookJeog.domain.vo.SponsorMemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
import java.util.Optional;

@Mapper
public interface SponsorMapper {

    // 단체 로그인
    public Optional<SponsorMemberVO> loginSponsorMember (SponsorMemberVO sponsorMemberVO);


    // 이메일 중복검사
    public Optional<SponsorMemberVO> selectSponsorMember (SponsorMemberVO sponsorMemberVO);


    // 비밀번호 변경
    public void updateSponsorPasswd(SponsorMemberVO sponsorMemberVO);

    // 마이페이지 기업회원 조회
    public SponsorPostDTO selectSponsorMypage(Long sponsorId);

    // 단체명 변경
    public void updateSponsorName(SponsorMemberVO sponsorMemberVO);

    // 단체 중복아이디 검사
    public boolean isIdExisting(String sponsorId);

    // 아이디 변경
    public void updateSponsorId(SponsorMemberVO sponsorMemberVO);

    // 비밀번호 변경
    public void updateSponsorPassword(SponsorMemberVO sponsorMemberVO);

    // 회원 비번 조회
    public String getPasswordById(Long sponsorId);

    // 탈퇴페이지에서 활동 조회
    public Map<String, Object> selectMyActivities(Long memberId);

    // 단체회원 탈퇴
    public void updateDeletedMemberStatus(Long memberId);
}
