package com.tenco.blog.reply;

import com.tenco.blog._core.errors.exception.Exception403;
import com.tenco.blog._core.errors.exception.Exception404;
import com.tenco.blog.board.Board;
import com.tenco.blog.board.BoardJpaRepository;
import com.tenco.blog.user.SessionUser;
import com.tenco.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor // final 키워드를 가진 멤버를 초기화 해
@Service // IoC 대상
public class ReplyService {

    private final ReplyJpaRepository replyJpaRepository;
    private final BoardJpaRepository boardJpaRepository;

    // 댓글 저장 기능

    @Transactional
    public ReplyResponse.SaveDTO save(ReplyRequest.SaveDTO saveDTO, SessionUser sessionUser) {

        Board board = boardJpaRepository.findById(saveDTO.getBoardId())
                .orElseThrow(() -> new Exception404("존재하지 않는 게시글에는 댓글 작성 불가"));

        User user = User.builder()
                .id(sessionUser.getId())
                .username(sessionUser.getUsername())
                .email(sessionUser.getEmail())
                .build();
        Reply reply = saveDTO.toEntity(user, board);
        replyJpaRepository.save(reply);
        return new ReplyResponse.SaveDTO(reply);
    }

    @Transactional
    public void deleteById(Long replyId, SessionUser sessionUser) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new Exception404("삭제할 댓글이 없습니다"));
        // 현재 로그인한 사용자와 댓글 소유자 확인 더 확인
        if (!reply.isOwner(sessionUser.getId())) {
            throw new Exception403("본인이 작성한 댓글만 삭제 할 수 있음");
        }
        replyJpaRepository.deleteById(replyId);
    }

}
