package refooding.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refooding.api.domain.member.dto.MemberRequest;
import refooding.api.domain.member.dto.MemberResponse;
import refooding.api.domain.member.entity.Member;
import refooding.api.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(MemberRequest memberRequest) {
        Member member = Member.builder().name(memberRequest.getName()).build();
        validateDuplicateMember(member); // 이름이 같은 회원은 회원 가입 불가
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByNameAndDeletedAtIsNull(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이름이 같은 회원은 회원 가입할 수 없습니다.");
                });
    }

    /**
     * 회원 목록 조회 - 탈퇴한 회원도 조회
     */
    public List<MemberResponse> findMembersIncludingDeleted() {
        return memberRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * 회원 목록 조회 - 탈퇴하지 않는 회원만 조회
     */
    public List<MemberResponse> findMembers() {
        return memberRepository.findAllByDeletedAtIsNull().stream()
                .map(this::convertToDto)
                .toList();
    }

    /**
     * 회원 상세 조회
     */
    public MemberResponse findOne(Long memberId){
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new IllegalStateException("id에 해당하는 회원이 없습니다."));
        return convertToDto(member);
    }

    /**
     * 회원 이름 수정
     */
    @Transactional
    public void updateMemberName(Long memberId, String newName) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new IllegalStateException("id에 해당하는 회원이 없습니다."));
        member.updateName(newName);
    }


    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new IllegalStateException("id에 해당하는 회원이 없습니다."));
        member.delete();
    }

    private MemberResponse convertToDto(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .build();
    }


}
