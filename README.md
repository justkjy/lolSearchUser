# lolSearchUser
1. 롤 API키가 필요함
   1) 여러번 API 사용시 갱신해야함 -> 라이엇한테 API KEY 획득함 2주 걸림 -> 획득
   2) 갱신 필요시 스넥바로 404 표시  -> 추가 함
2. API 사이트
   1) https://developer.riotgames.com/apis
      
3. 페이지
   1) 시작 페이지
 ![image](https://github.com/justkjy/lolSearchUser/assets/150637462/16dc2f45-0a7f-498f-8791-cc3cbf1807e3)


   2) 시작페이지에 입력한 로그인 정보
 ![userInfo](https://github.com/justkjy/lolSearchUser/assets/150637462/00ed90e8-4d4f-4b7c-b228-5978905a393d)

   3) 아이디 검색
 ![아이디 검색](https://github.com/justkjy/lolSearchUser/assets/150637462/f2efe595-4967-42c1-a0e2-19094553691f)

     a) 대전기록
![image](https://github.com/justkjy/lolSearchUser/assets/150637462/3a7fe82f-9b1e-4318-b26a-fdb903eb88e0)


   4) 바텀 앱바의 3번째 메뉴

      a) 로테이션 챔피언과 전체 챔피언 명단
      ![image](https://github.com/justkjy/lolSearchUser/assets/150637462/be00727f-5423-4162-a5d5-a73dc61bffc5)


      b) 챔피언의 프로필 이미지 클릭
         - 상단 스킨 Tab / 하단 스토리 & 스킬
         - 스토리
         ![image](https://github.com/justkjy/lolSearchUser/assets/150637462/fce31e74-1101-4e8b-9979-65cbbc2df1d2)
        - 스킬
         ![image](https://github.com/justkjy/lolSearchUser/assets/150637462/d74b273b-bdab-4ce3-82b0-899f4cae3f45)

 
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

