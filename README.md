## SpringSecurity-JWT (AccessToken & RefreshToken)
https://blog.naver.com/qjawnswkd/222202051145 (JWT AccessToken)<br><br>


(추가)RefreshToken의 개념을 추가해서 구현<br><br><br>

## 작동 방식
- Client가 로그인을 시도합니다<br><br>
- Server에서 로그인 검증 후 Client에게 AccessToken, RefreshToken, AccessToken 만료시간 등을 발급해주고 RefreshToken을 사용자 DB에 저장합니다 (redis 이용가능)<br><br>
- Client는 AccessToken과 RefreshToken을 안전한 곳에 저장한 후 권한이 필요한 요청마다 AccessToken을 이용해 요청합니다<br><br>
- Client는 AccessToken이 언제 만료되는지 알고있기 때문에 만료될때 쯤 body에 "grant_type":"refresh_token" 데이터를 담고 RefreshToken을 Server로 보내 AccessToken을 요청합니다<br><br>
- Server는 body에 grant_type이 있는지 없는지 검사하고 값이 refresh_token인 경우에만 받은 refresh_token을 검증하고 사용자 DB에 refresh_token하고 매칭 후 AccessToken을 발급해줍니다<br><br>
(Server에서 AccessToken과 RefreshToken을 어떻게 구별해서 받을지 정말 많이 고민해봤습니다. 고민 끝에 정해진 규격을 만들어 body에 담아 보내면 RefreshToken을 보낸걸로 정했습니다)<br><br>
<br><br>
## AccessToken만 이용했을 경우
- AccessToken의 유효시간을 길게 설정하면 사용자는 자주 로그인을 할 필요가 없어서 편하겠지만 유효시간이 긴 만큼 악성사용자가 AccessToken을 탈취할수 있어 보안성이 떨어지게 됩니다<br><br>
- 반대로 AccessToken의 유효시간을 짧게 설정하면 보안성은 높아지나 사용자가 로그인을 자주 해야해서 편의성이 떨어지게 됩니다<br><br>
- JWT를 무효화시키면 되지않나 라고 생각할수 있지만 JWT는 설정한 유효시간이 지나야지만 만료될수 있습니다. 그전에 억지로 만료시킬수 없습니다<br><br>
<br><br>
## AccessToken과 RefreshToken을 둘다 이용했을 경우
- AccessToken의 유효시간을 짧게 설정하고 RefreshToken의 유효시간을 길게 설정합니다 그러면 AccessToken의 유효시간이 만료되어도 RefreshToken으로 사용자가 로그인을 하지 않고 AccessToken을 재급 받을수 있게됩니다<br><br>
- AccessToken의 유효시간이 짧아지므로 AccessToken이 탈취당해도 정보를 취득하는데 시간이 줄어들어서 보안성이 높아집니다<br><br><br><br>

![2](https://user-images.githubusercontent.com/69130921/106415065-86d3e000-6491-11eb-97f9-82874ebfcc5a.PNG)
![1](https://user-images.githubusercontent.com/69130921/106415062-84718600-6491-11eb-82cf-258644116151.PNG)

