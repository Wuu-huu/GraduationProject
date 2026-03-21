# 绗叓闃舵浜や粯璇存槑

## 褰撳墠鍚庣鍙氦浠樺唴瀹?
### 璁よ瘉涓庣敤鎴?
- 娉ㄥ唽銆佺櫥褰曘€佽幏鍙栧綋鍓嶇敤鎴?- 鐢ㄦ埛璧勬枡鏌ヨ涓庣紪杈?- 鐢ㄦ埛璁剧疆鏌ヨ涓庣紪杈?- 鍏虫敞銆佸彇鍏炽€佺矇涓濆垪琛ㄣ€佸叧娉ㄥ垪琛?
### 瑙嗛涓庝簰鍔?
- 瑙嗛鍙戝竷銆佽崏绋夸繚瀛樸€佺紪杈戙€佸垹闄?- 瑙嗛璇︽儏銆侀椤靛垪琛ㄣ€佸垎鍖哄垪琛ㄣ€佺敤鎴锋姇绋垮垪琛?- 鍒?P 绠＄悊銆佸悎闆嗙鐞嗐€佽棰戠粺璁℃煡璇?- 鐐硅禐銆佺偣韪┿€佹姇甯併€佹敹钘忋€佺◢鍚庡啀鐪?- 璇勮銆佸洖澶嶃€佽瘎璁虹偣璧?- 寮瑰箷鍙戦€佷笌鏌ヨ

### 娑堟伅涓庡鏍?
- 绉佷俊浼氳瘽鍒涘缓銆佷細璇濆垪琛ㄣ€佹秷鎭彂閫併€佸凡璇绘洿鏂?- 閫氱煡鍒楄〃銆佹湭璇绘暟
- 瑙嗛瀹℃牳銆佽瘎璁哄鏍搞€佷妇鎶ュ鐞?- 灏佺銆佺瑷€銆侀鎺ф棩蹇?
### 鎺ㄨ崘

- `GET /api/recommend/home`
- `GET /api/recommend/videos/{videoId}/related`
- `GET /api/recommend/zones/{zoneId}`
- `GET /api/recommend/hot`
- `POST /api/recommend/admin/itemcf/{videoId}/rebuild`

## 鎺ㄨ崘閾捐矾璇存槑

- 鎺ㄨ崘鎺ュ彛浼氬啓鍏?`recommend_request`
- 鎺ㄨ崘缁撴灉浼氬啓鍏?`recommend_result`
- 杩斿洖缁撴灉鍚屾椂鍐欏叆 `video_exposure_log`
- 鐐硅禐銆佹姇甯併€佹敹钘忋€佽瘎璁恒€佸洖澶嶃€佺◢鍚庡啀鐪嬨€佸脊骞曟垚鍔熷悗浼氬彂甯?`UserBehaviorTrackEvent`
- 鎺ㄨ崘妯″潡鐩戝惉浜嬩欢骞跺啓鍏?`user_behavior_event`
- `recommend_item_similarity` 鐢ㄤ簬淇濆瓨 ItemCF 鐩镐技瑙嗛缁撴灉
- Kafka 浠ｇ爜鍙仛棰勭暀锛屽綋鍓嶉粯璁ゅ叧闂?
## 鍚姩姝ラ

1. 纭繚 MySQL銆丷edis銆丮inIO 鍙敤銆?2. 鎵ц鎺ㄨ崘妯″潡寤鸿〃璇彞鍜?[sql/stage8_demo_seed.sql](/E:/GraduationProject/GraduationProject/sql/stage8_demo_seed.sql)銆?3. 閰嶇疆 [application.yml](/E:/GraduationProject/GraduationProject/web-app/src/main/resources/application.yml)銆?4. 鍚姩 `web-app`銆?
## 鍩虹鍥炲綊寤鸿

1. 鏈櫥褰曡闂椤垫帹鑽愩€佺儹闂ㄦ銆佺浉鍏虫帹鑽愩€佸垎鍖烘帹鑽愩€?2. 鐧诲綍鍚庡畬鎴愮偣璧炪€佹姇甯併€佹敹钘忋€佽瘎璁恒€佸脊骞曠瓑浜掑姩銆?3. 鍐嶆璁块棶鎺ㄨ崘鎺ュ彛锛岃瀵熺粨鏋滃拰鍩嬬偣琛ㄦ槸鍚︿骇鐢熻褰曘€?4. 璋冪敤 ItemCF 閲嶅缓鎺ュ彛锛屾鏌?`recommend_item_similarity` 鏄惁鍐欏叆銆?5. 妫€鏌?`recommend_request`銆乣recommend_result`銆乣video_exposure_log`銆乣user_behavior_event` 鏄惁瀛樺湪瀵瑰簲鏁版嵁銆?
## 娈嬩綑椋庨櫓

- 褰撳墠 ItemCF 鏄交閲忓疄鐜帮紝閫傚悎鑱旇皟涓庢紨绀猴紝涓嶉€傚悎鐩存帴浣滀负鏈€缁堢敓浜ф帹鑽愮畻娉曘€?- Kafka 棰勭暀浠ｇ爜灏氭湭鎺ュ叆鐪熷疄 broker锛屼篃娌℃湁鍚敤寮傛娑堣垂銆?- 鎺ㄨ崘铻嶅悎鍒嗘暟鏄厤缃寲鐨勭畝鍗曞姞鏉冿紝鍚庣画浠嶉渶缁撳悎鐪熷疄鍩嬬偣鏁版嵁缁х画璋冩潈銆?- 鎼滅储椤靛綋鍓嶄粎淇濈暀鏅€氱粨鏋勯〉棰勬湡锛屼笉鍖呭惈 Elasticsearch 鎼滅储鑳藉姏銆

## 配套文档

- [系统总结文档](/E:/GraduationProject/GraduationProject/docs/system-summary.md)
- [前端联调说明](/E:/GraduationProject/GraduationProject/docs/frontend-integration.md)
- [前端所需后端能力清单](/E:/GraduationProject/GraduationProject/docs/frontend-backend-contract.md)
