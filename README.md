# GraduationProject

绫诲摂鍝╁摂鍝╄棰戝钩鍙板悗绔」鐩紝褰撳墠閲囩敤 Spring Boot 澶氭ā鍧楀崟浣撴灦鏋勶紝鎸変笟鍔″煙鎷嗗垎妯″潡锛屽苟涓哄悗缁崌绾у埌 Spring Cloud 棰勭暀娓呮櫚杈圭晫銆?
## 妯″潡

- `common`锛氱粺涓€鍝嶅簲銆佸紓甯搞€丣WT銆佸畨鍏ㄣ€佸垎椤点€丱penAPI銆佸叕鍏遍厤缃?- `auth-module`锛氭敞鍐屻€佺櫥褰曘€佸綋鍓嶇敤鎴疯璇佽兘鍔?- `user-module`锛氱敤鎴疯祫鏂欍€佸叧娉ㄥ叧绯汇€佺敤鎴疯缃€佺敤鎴风粺璁?- `video-module`锛氳棰戝彂甯冦€佽崏绋裤€佽鎯呫€佸垎 P銆佸悎闆嗐€佺粺璁?- `interaction-module`锛氱偣璧炪€佹姇甯併€佹敹钘忋€佽瘎璁恒€佸脊骞曘€佺◢鍚庡啀鐪?- `message-module`锛氱淇°€侀€氱煡銆佹湭璇绘暟
- `audit-module`锛氳棰戝鏍搞€佽瘎璁哄鏍搞€佷妇鎶ュ鐞嗐€佸皝绂佺瑷€銆侀鎺ф棩蹇?- `recommend-module`锛氱儹闂ㄦ銆侀椤垫帹鑽愩€佺浉鍏虫帹鑽愩€佸垎鍖烘帹鑽愩€佹帹鑽愭棩蹇椼€佽涓哄煁鐐广€両temCF 涓棿鑳藉姏
- `admin-module`锛氬悗鍙板熀纭€绠＄悊鎺ュ彛
- `web-app`锛氱粺涓€鍚姩鍏ュ彛

## 鐜

- JDK 17
- Maven 3.9+
- MySQL 8
- Redis
- MinIO

## 鍚姩

1. 鍒濆鍖栦笟鍔℃暟鎹簱锛屽苟鎵ц鎺ㄨ崘妯″潡鐩稿叧寤鸿〃璇彞銆?2. 濡傞渶婕旂ず鏁版嵁锛屽彲鎵ц [sql/stage8_demo_seed.sql](/E:/GraduationProject/GraduationProject/sql/stage8_demo_seed.sql)銆?3. 淇敼 [application.yml](/E:/GraduationProject/GraduationProject/web-app/src/main/resources/application.yml) 涓殑鏁版嵁搴撱€丷edis銆丮inIO 閰嶇疆銆?4. 缂栬瘧骞跺惎鍔?`web-app`銆?
## 褰撳墠瀹屾垚闃舵

- 绗?1 闃舵锛氭柟妗堜笌楠ㄦ灦璁捐
- 绗?2 闃舵锛氬熀纭€宸ョ▼鎼缓
- 绗?3 闃舵锛氳璇佷笌鐢ㄦ埛鍩?- 绗?4 闃舵锛氳棰戝煙
- 绗?5 闃舵锛氫簰鍔ㄥ煙
- 绗?6 闃舵锛氭秷鎭笌瀹℃牳鍩?- 绗?7 闃舵锛氭帹鑽愭ā鍧楀熀纭€鑳藉姏涓庡煁鐐归棴鐜?
## 鎺ㄨ崘妯″潡璇存槑

褰撳墠闃舵宸插畬鎴愶細

- 鏁版嵁椹卞姩鎺ㄨ崘
- 鐑棬姒?- 棣栭〉鎺ㄨ崘
- 鐩稿叧鎺ㄨ崘
- 鍒嗗尯鎺ㄨ崘
- `recommend_request` / `recommend_result` 璁板綍
- `video_exposure_log` 鏇濆厜璁板綍
- 浜掑姩鎴愬姛鍚庣殑 `user_behavior_event` 鍚屾钀藉簱
- `recommend_item_similarity` ItemCF 涓棿琛ㄨ兘鍔?
褰撳墠浠呴鐣欍€佷笉鍚敤锛?
- Kafka 寮傛鐢熶骇涓庢秷璐归鏋?- 鏇村鏉傜殑鍦ㄧ嚎鐗瑰緛鏈嶅姟
- UserCF銆佸悜閲忓彫鍥炪€佹繁搴﹀涔犳帓搴?
## 浜や粯鏉愭枡

- [绗叓闃舵浜や粯璇存槑](/E:/GraduationProject/GraduationProject/docs/stage8-delivery.md)
- [鍓嶇鑱旇皟璇存槑](/E:/GraduationProject/GraduationProject/docs/frontend-integration.md)
- [婕旂ず鏁版嵁鑴氭湰](/E:/GraduationProject/GraduationProject/sql/stage8_demo_seed.sql)