package com.app.bookJeog.controller.member;

import com.app.bookJeog.domain.dto.*;

import com.app.bookJeog.domain.dto.PersonalMemberDTO;

import com.app.bookJeog.domain.vo.SponsorMemberVO;
import com.app.bookJeog.mapper.SponsorMapper;
import com.app.bookJeog.service.MemberService;
import com.app.bookJeog.service.PostService;
import com.app.bookJeog.service.SponsorService;
import com.app.bookJeog.service.SponsorServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/sponsor/*")
@RequiredArgsConstructor
public class SponsorController {


    private final SponsorServiceImpl sponsorServiceImpl;
    private final SponsorService sponsorService;
    private final MemberService memberService;
    private final SponsorMapper sponsorMapper;
    private final SponsorMemberDTO sponsormemberDTO;
    private final SponsorMemberDTO sponsorMemberDTO;
    private final PostService postService;
    private HttpSession session;

    // 단체 마이페이지 조회
    @GetMapping("/mypage")
    public String getSponsorMypage(HttpSession session, Model model) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            // 세션에 sponsorMember가 없는 경우, 로그인 페이지로 리다이렉트
            return "redirect:/sponsor/login/sponsorship";
        }

        Long sponsorId = sponsorMember.getId();
        SponsorPostDTO sponsor = sponsorService.getSponsorMypage(sponsorId);
        model.addAttribute("sponsor", sponsor);

        // 게시글 및 썸네일 조회
        Map<String, Object> myPageData = postService.getMyPageData(sponsorId);
        model.addAttribute("myPageData", myPageData);
        return "organization/mypage";
    }

    // 단체 마이페이지 - 내 기부글
    @GetMapping("mypage/donation")
    public String donationMypage(HttpSession session, Model model) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            return "redirect:/sponsor/login/sponsorship";
        }

        Long memberId = sponsorMember.getId();

        List<ReceiverPostDTO> receiverPosts = postService.getMyAllReceiverPosts(memberId);
        model.addAttribute("receiverPosts", receiverPosts);

        return "organization/my-donation";
    }


    // 단체 마이페이지 - 비밀번호 확인
    @GetMapping("mypage/password-check")
    public String gotoSponsorPasswordCheck(HttpSession session) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");
        if (sponsorMember == null) {
            return "redirect:/sponsor/login/sponsorship";
        }
        return "organization/password-check";
    }

    @PostMapping("password-check")
    public String checkPassword(@RequestParam("password") String password,
                                HttpSession session,
                                RedirectAttributes redirect) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            return "redirect:/personal/login";
        }

        boolean isCorrect = sponsorService.checkSponsorPassword(sponsorMember.getId(), password);

        if (isCorrect) {
            return "redirect:/sponsor/mypage/profile";
        } else {
            redirect.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/sponsor/mypage/password-check";
        }
    }

    // 단체 마이페이지 - 내 기부 인증글
    @GetMapping("mypage/donate-cert")
    public String donateCertMypage(HttpSession session, Model model) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            return "redirect:/sponsor/login/sponsorship";
        }

        Long memberId = sponsorMember.getId();

        List<DonateCertPostDTO> donateCertPosts = postService.getMyAllDonateCerts(memberId);
        model.addAttribute("donateCertPosts", donateCertPosts);

        return "organization/my-donate-post";
    }


    // 단체 마이페이지 - 회원정보 변경
    @GetMapping("mypage/profile")
    public String SponsorMypageProfile(HttpSession session, Model model) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            return "redirect:/sponsor/login/sponsorship";
        }

        model.addAttribute("sponsor", sponsorMember);
        model.addAttribute("profileUrl", memberService.getProfileImageUrl(sponsorMember.getId()));

        return "organization/my-profile";
    }

    // 프사 생성 또는 수정
    @PostMapping("/upload-profile")
    @ResponseBody
    public ResponseEntity<String> uploadProfile(@RequestParam("file") MultipartFile file,
                                                HttpSession session) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");
        if (sponsorMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        memberService.saveOrUpdateProfileImage(file, sponsorMember.getId());
        return ResponseEntity.ok("프로필 이미지가 저장되었습니다.");
    }

    // 프로필 이미지 삭제
    @DeleteMapping("/delete-profile")
    @ResponseBody
    public ResponseEntity<String> deleteProfile(HttpSession session) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");
        if (sponsorMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        memberService.deleteProfileImage(sponsorMember.getId());
        return ResponseEntity.ok("프로필 이미지가 삭제되었습니다.");
    }

    // 단체이름 변경
    @PostMapping("/sponsor-name")
    @ResponseBody
    public ResponseEntity<String> updateSponsorName(@RequestParam("sponsorName") String sponsorName,
                                                 HttpSession session) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");
        if (sponsorMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        sponsorMember.setSponsorName(sponsorName); // DTO에 적용
        sponsorService.setSponsorName(sponsorMember);
        return ResponseEntity.ok("단체명이 변경되었습니다.");
    }

    // 아이디 변경
    @PostMapping("/sponsor-id")
    @ResponseBody
    public ResponseEntity<String> updateSponsorId(@RequestParam("sponsorId") String sponsorId,
                                                  HttpSession session) {
        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 중복 아이디 검사
        if (sponsorService.isIdExisting(sponsorId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }

        // 아이디 변경
        sponsorMember.setSponsorId(sponsorId);
        sponsorService.setSponsorId(sponsorMember);
        return ResponseEntity.ok("아이디가 변경되었습니다.");
    }

    // 비밀번호 변경
    @PostMapping("/password")
    @ResponseBody
    public ResponseEntity<String> updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            HttpSession session) {

        SponsorMemberDTO sponsorMember = (SponsorMemberDTO) session.getAttribute("sponsorMember");

        if (sponsorMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 현재 비밀번호 확인
        boolean isMatch = sponsorService.checkCurrentPassword(sponsorMember.getId(), currentPassword);

        if (!isMatch) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("현재 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        sponsorMember.setSponsorPassword(newPassword);
        sponsorService.setSponsorPassword(sponsorMember);

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // 단체 마이페이지 - 관리 메뉴
    @GetMapping("mypage/settings")
    public String settings(HttpSession session, Model model) {

        SponsorMemberDTO sponsorMemberDTO = (SponsorMemberDTO) session.getAttribute("sponsorMember");
        if(sponsorMemberDTO == null) {
            return "redirect:/sponsor/login/sponsorship";
        }
        model.addAttribute("sponsor", sponsorMemberDTO);
        return "organization/member-menu";
    }


    // 단체 회원탈퇴
    @GetMapping("leave")
    public String gotoSponsorLeave(HttpSession session, Model model) {
        SponsorMemberDTO sponsorMemberDTO = (SponsorMemberDTO) session.getAttribute("sponsorMember");
        if(sponsorMemberDTO == null) {
            return "redirect:/sponsor/login/sponsorship";
        }
        Long memberId = sponsorMemberDTO.getId();

        // 회원 정보 조회
        Map<String, Object> sponsorInfo = sponsorService.selectMyActivities(memberId);

        model.addAttribute("sponsorInfo", sponsorInfo);
        model.addAttribute("sponsorId", memberId);
        return "organization/leave-organization";
    }


    // 단체 회원탈퇴 - 탈퇴사유 입력
    @GetMapping("leave/reason")
    public String gotoSponsorLeaveReason() {
        return "organization/leave-reason";
    }

    @PostMapping("/leave/confirm")
    public String leaveMember(@RequestParam("memberId") Long memberId, HttpSession session, RedirectAttributes redirectAttributes) {
        // 회원 상태 업데이트 (탈퇴 처리)
        sponsorService.updateDeletedMemberStatus(memberId);

        // 세션 무효화
        session.invalidate();

        // Flash Attribute로 메시지 전달
        redirectAttributes.addFlashAttribute("message", "탈퇴가 완료되었습니다.");

        return "redirect:/";
    }

    // 단체 로그인
    @GetMapping("login/sponsorship")
    public String goToSponsorship() {
        return "login/sponsorship";
    }

    @PostMapping("login-sponsorship-login")
    public String loginSponsor(SponsorMemberDTO sponsorMemberDTO, HttpSession session) {
        Optional<SponsorMemberDTO> foundMember = sponsorServiceImpl.loginSponsorMember(sponsorMemberDTO);
        if(foundMember.isPresent()) {
            session.setAttribute("sponsorMember", foundMember.orElseThrow(RuntimeException::new));
            return "redirect:/main/main";
        } else {
            return "redirect:/sponsor/login/sponsorship?result=fail";
        }
    }


    // 단체 비밀번호 찾기버튼
    @GetMapping("login/findpasswd-sponsor")
    public String goToFindPasswdSponsor() {
        return "login/findpasswd-sponsor";
    }
    // 이메일 버튼을 통해 이동
    @GetMapping("login/findpasswd-sponsor-input")
    public String goToFindPasswdSponsorInput() {
        return "login/findpasswd-sponsor-input";
    }
    @PostMapping("send-mail")
    public String sendMail(SponsorMemberDTO sponsormemberDTO, HttpServletResponse response, HttpSession session) throws MessagingException {
        Optional<SponsorMemberVO> foundEmail = sponsorServiceImpl.selectEmailForPassword(sponsormemberDTO);
        if(foundEmail.isPresent()) {
                sponsorServiceImpl.sendMail(sponsormemberDTO, response, session);
                return "redirect:/sponsor/input-code";
            }
        return "redirect:/sponsor/send-mail?result=fail";
    }



    // 이메일에서 코드확인
    @GetMapping("input-code")
    public String goToCheckCode() {
        return "login/findpasswd-sponsor-certi";
    }
    @PostMapping("check-sponsor-code")
    public String checkSponsorCode(@CookieValue(name = "token", required = false) String token, String code, HttpServletRequest request) {

        if(token == null) {
            return "redirect:/personal/login?result=tokken-lose";
        }
        if (token.equals(code)) {
            return "redirect:/sponsor/change-passwd"; // 비밀번호 재설정 페이지로 리디렉션
        } else {
            return "redirect:/sponsor/input-code?result=fail";
        }
    }



    // 비밀번호 재설정
    @GetMapping("change-passwd")
    public String goToChangePasswd() {
        return "login/set-sponsor-passwd";
    }

    @PostMapping("sponsor-change-passwd")
    public String changePasswd (String newPasswd, HttpSession session){
        sponsorMemberDTO.setSponsorEmail((String) session.getAttribute("email"));
        String memberEmail = sponsorMemberDTO.getSponsorEmail();
        sponsorMemberDTO.setSponsorPassword(newPasswd);

        if(!memberEmail.isEmpty()) {
            sponsorService.changePassword(sponsorMemberDTO);
            log.info("여긴 통과해야함");
            session.invalidate();
            return "redirect:/personal/login";
        }
        return "redirect:/personal/login?result=tokken-lose";}
}


