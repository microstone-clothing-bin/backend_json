package com.example.clothing_backend.user;

import com.example.clothing_backend.marker.*;
import com.example.clothing_backend.board.Board;
import com.example.clothing_backend.board.BoardDto;
import com.example.clothing_backend.board.BoardService;
import com.example.clothing_backend.board.Wish;
import com.example.clothing_backend.board.WishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final BoardService boardService;
    private final WishService wishService;
    private final MarkerPostService markerPostService;
    private final ClothingBinService clothingBinService;

    // --- 사용자 인증 API ---

    // 로그인 처리: POST /api/user/login
    @PostMapping("/user/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String,String> loginRequest) {
        String id = loginRequest.get("id");
        String password = loginRequest.get("password");
        User user = userService.getUser(id);

        Map<String, Object> response = new HashMap<>();
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            response.put("status", "success");
            response.put("message", "로그인 성공");
            response.put("userId", user.getUserId());
            response.put("nickname", user.getNickname());
            response.put("profileImageUrl", user.getProfileImageUrl());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.status(401).body(response);
        }
    }

    // 회원가입 처리: POST /api/user/register
    @PostMapping("/user/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return ResponseEntity.ok(Map.of("status", "success", "message", "회원가입이 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "회원가입 실패: " + e.getMessage()));
        }
    }

    // 로그아웃 처리: POST /api/user/logout
    @PostMapping("/user/logout")
    public ResponseEntity<Map<String, String>> logoutUser() {
        // Stateless 방식이므로 서버에서는 특별히 할 작업이 없음.
        // 프론트엔드가 클라이언트 측 인증 정보를 삭제하는 것을 확인하는 용도의 API.
        return ResponseEntity.ok(Map.of("status", "success", "message", "로그아웃되었습니다."));
    }

    // 아이디 찾기: POST /api/user/find-id
    @PostMapping("/user/find-id")
    public ResponseEntity<Map<String, Object>> findId(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        String email = request.get("email");
        String foundId = userService.findIdByNicknameAndEmail(nickname, email);

        if (foundId != null) {
            // key를 "userId"로 반환
            return ResponseEntity.ok(Map.of("status", "success", "userId", foundId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "일치하는 계정을 찾을 수 없습니다."));
        }
    }

    // 비밀번호 찾기 (사용자 확인): POST /api/user/find-password
    @PostMapping("/user/find-password")
    public ResponseEntity<Map<String, String>> findPassword(@RequestBody Map<String, String> request) {
        String userId = request.get("userId"); // String 타입의 로그인 ID
        String email = request.get("email");

        boolean userExists = userService.verifyUserByIdAndEmail(userId, email);

        if (userExists) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "사용자 확인 완료"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "일치하는 계정을 찾을 수 없습니다."));
        }
    }

    // 비밀번호 찾기 이후 사용하는 비밀번호 재설정
    // 비밀번호 재설정: POST /api/user/reset-password
    @PostMapping("/user/reset-password")
    public ResponseEntity<Map<String, String>> resetPasswordWithoutLogin(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        try {
            // 기존 updatePassword 메소드 재활용
            userService.updatePassword(userId, email, newPassword);
            return ResponseEntity.ok(Map.of("status", "success", "message", "비밀번호가 변경되었습니다."));
        } catch (UserNotFoundException e) {
            // 사용자를 못 찾으면 404 에러와 함께 실패 메시지 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "사용자를 찾을 수 없습니다."));
        }
    }

    // --- 중복 체크 API ---

    // 아이디 중복 체크: GET /api/user/check-id
    @GetMapping("/user/check-id")
    public ResponseEntity<Map<String, Boolean>> checkIdDuplicate(@RequestParam("id") String id) {
        return ResponseEntity.ok(Collections.singletonMap("isDuplicate", userService.isDuplicate("id", id)));
    }

    // 닉네임 중복 체크: GET /api/user/check-nickname
    @GetMapping("/user/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNicknameDuplicate(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(Collections.singletonMap("isDuplicate", userService.isDuplicate("nickname", nickname)));
    }

    // 이메일 중복 체크: GET /api/user/check-email
    @GetMapping("/user/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailDuplicate(@RequestParam("email") String email) {
        return ResponseEntity.ok(Collections.singletonMap("isDuplicate", userService.isDuplicate("email", email)));
    }

    // --- 마이페이지 API ---

    // 마이페이지 정보 조회: GET /api/mypage/info
    @GetMapping("/mypage/info")
    public ResponseEntity<Map<String, Object>> getMyPageInfo(@RequestParam("userId") Long userId) {
        User user = userService.getUserByUserId(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("nickname", user.getNickname());
        response.put("profileImageUrl", user.getProfileImageUrl());

        return ResponseEntity.ok(response);
    }

    // 프로필 이미지 업로드: POST /api/mypage/uploadProfile
    @PostMapping("/mypage/uploadProfile")
    public ResponseEntity<Map<String, String>> uploadProfile(
            @RequestParam("profileImage") MultipartFile profileImage,
            @RequestParam("userId") Long userId) throws IOException {
        User user = userService.getUserByUserId(userId);
        String imageUrl = userService.saveProfileImage(profileImage, user.getId());
        return ResponseEntity.ok(Map.of("status", "success", "profileImageUrl", imageUrl));
    }

    // 마이 페이지에서 사용하는 비밀번호 재설정
    // 비밀번호 재설정: POST /api/mypage/resetPassword
    @PostMapping("/mypage/resetPassword")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody @Valid PasswordResetDto passwordResetDto,
            @RequestParam("userId") Long userId) {

        if (!passwordResetDto.getNewPassword().equals(passwordResetDto.getNewPasswordCheck())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("status", "error", "message", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다."));
        }

        User user = userService.getUserByUserId(userId);
        String newPassword = passwordResetDto.getNewPassword();
        userService.updatePassword(user.getId(), user.getEmail(), newPassword);
        return ResponseEntity.ok(Map.of("status", "success", "message", "비밀번호가 변경되었습니다. 다시 로그인해주세요."));
    }

    // 회원 탈퇴: POST /api/mypage/deleteAccount
    @PostMapping("/mypage/deleteAccount")
    public ResponseEntity<Map<String, String>> deleteAccount(@RequestParam("userId") Long userId) {
        User user = userService.getUserByUserId(userId);
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(Map.of("status", "success", "message", "회원 탈퇴가 완료되었습니다."));
    }

    // --- 지도 및 의류수거함 API ---

    // 의류 수거함 전체 조회: GET /api/clothing-bins
    @GetMapping(value = "/clothing-bins", produces = "application/json; charset=UTF-8")
    public List<MarkerDto> getClothingBins(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
            @RequestParam(required = false) Double radiusKm) {
        List<ClothingBin> bins = clothingBinService.findClothingBins(lat, lng, radiusKm);
        return bins.stream()
                .map(bin -> new MarkerDto(bin.getId(), bin.getLatitude(), bin.getLongitude(), bin.getRoadAddress()))
                .collect(Collectors.toList());
    }

    // 지도 범위 내 의류 수거함 조회: GET /api/clothing-bins/in-bounds
    @GetMapping(value = "/clothing-bins/in-bounds", produces = "application/json; charset=UTF-8")
    public List<MarkerDto> getBinsInBounds(
            @RequestParam double swLat,
            @RequestParam double swLng,
            @RequestParam double neLat,
            @RequestParam double neLng) {
        List<ClothingBin> bins = clothingBinService.findBinsInBounds(swLat, swLng, neLat, neLng);
        return bins.stream()
                .map(bin -> new MarkerDto(bin.getId(), bin.getLatitude(), bin.getLongitude(), bin.getRoadAddress()))
                .collect(Collectors.toList());
    }

    // --- 게시판 API ---

    // 게시글 리스트 조회: GET /api/boards
    @GetMapping("/boards")
    public Page<BoardDto> getBoardsApi(@PageableDefault(size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Board> boardPage = boardService.getBoards(pageable);
        return boardPage.map(BoardDto::new);
    }

    // 단일 게시글 조회: GET /api/boards/{boardId}
    @GetMapping("/boards/{boardId}")
    public BoardDto getBoardApi(@PathVariable long boardId) {
        Board board = boardService.getBoard(boardId);
        return new BoardDto(board);
    }

    // 게시글 작성: POST /api/boards
    @PostMapping("/boards")
    public ResponseEntity<Map<String, String>> writeBoard(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long userId,
            @RequestParam String nickname,

            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) Long binId) throws IOException {

        boardService.addBoard(nickname, title, content, userId, image, binId);

        return ResponseEntity.ok(Map.of("status", "success", "message", "게시글이 등록되었습니다."));
    }

    // 게시글 수정: PUT /api/boards/{boardId}
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<Map<String, String>> updateBoard(
            @PathVariable Long boardId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long userId) {

        // 서비스의 수정 로직 호출 (작성자 본인 확인은 서비스 안에서 함)
        boardService.updateBoardTextOnly(boardId, title, content, userId);

        return ResponseEntity.ok(Map.of("status", "success", "message", "게시글이 수정되었습니다."));
    }

    // 게시글 삭제: DELETE /api/boards/{boardId}
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Map<String, String>> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId) {

        // 서비스의 삭제 로직 호출 (작성자 본인 확인 포함)
        boardService.deleteBoard(userId, boardId);

        return ResponseEntity.ok(Map.of("status", "success", "message", "게시글이 삭제되었습니다."));
    }

    // --- 즐겨찾기 API ---

    // 즐겨찾기 추가: POST /api/wish/add/{binId}
    @PostMapping("/wish/add/{binId}")
    public ResponseEntity<Map<String, String>> addWish(@PathVariable Long binId, @RequestParam Long userId) {
        wishService.addWish(userId, binId);
        return ResponseEntity.ok(Map.of("status", "success", "message", "즐겨찾기가 추가되었습니다."));
    }

    // 즐겨찾기 제거: DELETE /api/wish/remove/{binId}
    @DeleteMapping("/wish/remove/{binId}")
    public ResponseEntity<Map<String, String>> removeWish(@PathVariable Long binId, @RequestParam Long userId) {
        wishService.removeWish(userId, binId);
        return ResponseEntity.ok(Map.of("status", "success", "message", "즐겨찾기가 해제되었습니다."));
    }

    // 즐겨찾기 리스트 조회: GET /api/wish/list
    @GetMapping("/wish/list")
    public ResponseEntity<List<Long>> getUserWishes(@RequestParam Long userId) {
        List<Long> userWishes = wishService.getUserWishes(userId)
                .stream()
                .map(Wish::getBinId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userWishes);
    }

    // --- 리뷰 API ---

    // 특정 의류수거함 리뷰 조회: GET /api/markers/{binId}/posts
    @GetMapping("/markers/{binId}/posts")
    public List<MarkerPostDto> getPostsForMarker(@PathVariable Long binId) {
        return markerPostService.getPostsByBinId(binId);
    }

    // 리뷰 작성: POST /api/markers/{binId}/posts
    @PostMapping("/markers/{binId}/posts")
    public ResponseEntity<Map<String, String>> createPostForMarker(
            @PathVariable Long binId,
            @RequestParam String content,
            @RequestParam Long userId,
            @RequestParam(required = false) MultipartFile image) throws IOException {
        User user = userService.getUserByUserId(userId);
        markerPostService.createPost(binId, user, content, image);
        return ResponseEntity.ok(Map.of("status", "success", "message", "게시글이 성공적으로 등록되었습니다."));
    }
}