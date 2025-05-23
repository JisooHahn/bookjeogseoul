package com.app.bookJeog.service;

import com.app.bookJeog.domain.dto.SponsorMemberDTO;
import com.app.bookJeog.domain.dto.SponsorPostDTO;
import com.app.bookJeog.domain.vo.SponsorMemberVO;

import java.util.Map;
import java.util.Optional;

public interface SponsorService {

    // 단체 로그인
    Optional<SponsorMemberDTO> loginSponsorMember (SponsorMemberDTO sponsorMemberDTO);

    default SponsorMemberVO toSponsorMemberVO (SponsorMemberDTO sponsorMemberDTO) {
        return  SponsorMemberVO.builder()
                .id(sponsorMemberDTO.getId())
                .sponsorId(sponsorMemberDTO.getSponsorId())
                .sponsorPassword(sponsorMemberDTO.getSponsorPassword())
                .sponsorEmail(sponsorMemberDTO.getSponsorEmail())
                .sponsorName(sponsorMemberDTO.getSponsorName())
                .sponsorPhoneNumber(sponsorMemberDTO.getSponsorPhoneNumber())
                .sponsorMainAddress(sponsorMemberDTO.getSponsorMainAddress())
                .sponsorMemberStatus(sponsorMemberDTO.getSponsorMemberStatus())
                .sponsorSubAddress(sponsorMemberDTO.getSponsorSubAddress())
                .build();
    }

    default SponsorMemberDTO toSponsorMemberDTO (SponsorMemberVO sponsorMemberVO) {
        SponsorMemberDTO sponsorMemberDTO = new SponsorMemberDTO();
        sponsorMemberDTO.setId(sponsorMemberVO.getId());
        sponsorMemberDTO.setSponsorId(sponsorMemberVO.getSponsorId());
        sponsorMemberDTO.setSponsorPassword(sponsorMemberVO.getSponsorPassword());
        sponsorMemberDTO.setSponsorName(sponsorMemberVO.getSponsorName());
        sponsorMemberDTO.setSponsorMainAddress(sponsorMemberVO.getSponsorMainAddress());
        sponsorMemberDTO.setSponsorSubAddress(sponsorMemberVO.getSponsorSubAddress());
        sponsorMemberDTO.setSponsorPhoneNumber(sponsorMemberVO.getSponsorPhoneNumber());
        sponsorMemberDTO.setSponsorEmail(sponsorMemberVO.getSponsorEmail());
        sponsorMemberDTO.setSponsorMemberStatus(sponsorMemberVO.getSponsorMemberStatus());
        return sponsorMemberDTO;
    }


    // 비밀번호 변경
    void changePassword(SponsorMemberDTO sponsorMemberDTO);



    // 이메일 중복검사
    Optional<SponsorMemberVO> selectEmailForPassword(SponsorMemberDTO sponsorMemberDTO);

    // 마이페이지 기업회원 조회
    public SponsorPostDTO getSponsorMypage(Long sponsorId);

    // 비밀번호 중복검사
    public boolean checkSponsorPassword(Long memberId, String password);

    // 단체명 변경
    public void setSponsorName(SponsorMemberDTO sponsorMemberDTO);

    // 단체 중복아이디 검사
    public boolean isIdExisting(String sponsorId);

    // 아이디 변경
    public void setSponsorId(SponsorMemberDTO sponsorMemberDTO);

    // 비밀번호 변경
    public void setSponsorPassword(SponsorMemberDTO sponsorMemberDTO);

    // 비번 조회
    public boolean checkCurrentPassword(Long memberId, String currentPassword);

    // 탈퇴페이지에서 활동 조회
    public Map<String, Object> selectMyActivities(Long memberId);

    // 단체회원 탈퇴
    public void updateDeletedMemberStatus(Long memberId);
}
