# lolSearchUser
1. 롤 API키가 필요함
   1) 여러번 API 사용시 갱신해야함 -> 라이엇한테 API KEY 획득함 2주 걸림
   2) 갱신 필요시 스넥바로 404 표시  -> 추가 함
2. API 사이트
   1) https://developer.riotgames.com/apis
      
3. 페이지
   1) 시작 페이지 

  ![home](https://github.com/justkjy/lolSearchUser/assets/150637462/3509da43-1a45-4eb1-a538-b543a3fb8b78)

   2) 시작페이지에 입력한 로그인 정보
      ![userInfo](https://github.com/justkjy/lolSearchUser/assets/150637462/00ed90e8-4d4f-4b7c-b228-5978905a393d)

   3) 아이디 검색
      ![아이디 검색](https://github.com/justkjy/lolSearchUser/assets/150637462/f2efe595-4967-42c1-a0e2-19094553691f)

   4) 바텀 앱바의 3번째 메뉴
      a) 로테이션 챔피언과 전체 챔피언 명단
      ![챔피언 정보](https://github.com/justkjy/lolSearchUser/assets/150637462/158ed2e7-ac3f-4f72-bb2d-e5551d71502c)

      b) 챔피언의 프로필 이미지 클릭
         ![스킨_스토리](https://github.com/justkjy/lolSearchUser/assets/150637462/9dbaf9ff-da69-497a-9ddf-18ee250d8d4f)

         ![스킨_스토리2](https://github.com/justkjy/lolSearchUser/assets/150637462/739c3c0e-a76a-4e43-b1ca-bddd1c79255c)
 
 5. 해결해야될 문제
   1) 시작 페이지 > 바텀 엡바 현재 패이지 표시(레드 컬러)가 표시안됨
   2) 챔피언 정보에 마지막 검색한 사용자이름으로 표시가 안됨 -> 수정
   3) 챔피언 상세 페이지의 상단에 이미지 리스트가 기본적인 LazyRow로 표시
      a) 이미지 리스트가 스크롤 속도에 따라 빠르게 지나감 -> Tab으로 수정
      b) 유튜브 쇼츠처럼 한장씩 한페이지에 보여줘야하함 
   4) 전략적 팀 전투도 해아함       -> 삭제 예정
   5) 대전 기록 해야함 (시간이 많이 필요함) -> 추가 함 
   6) SharedView를 해결해야함(중요한 문제)  -> 뷰모델 수정 해야함
    
 
    ![구조](https://github.com/justkjy/lolSearchUser/assets/150637462/733ea4c9-bbf5-499c-8fcd-8635f991e503)

