-- 第八阶段演示数据脚本
-- 执行前请确认业务库已初始化，且推荐相关表已建好。
-- 本文件只提供安全的演示数据模板，避免直接覆盖现有业务数据。

-- 示例管理员账号，密码请自行替换为项目实际使用的 bcrypt 密文。
-- insert ignore into user_info(username, password_hash, email, phone, state, role, register_time, last_login_time)
-- values ('admin_demo', '$2a$10$replace_with_real_bcrypt_hash', 'admin_demo@test.com', '18800000001', 0, 1, now(), now());

-- insert ignore into user_profile(uid, nickname, avatar_url, background_url, gender, birthday, signature, province, city, auth_type, auth_desc, update_time)
-- select uid, '管理员演示账号', null, null, 2, null, '后台管理演示账号', null, null, 0, null, now()
-- from user_info where username = 'admin_demo';

-- insert ignore into user_setting(uid, open_recommend, open_push, open_dm, open_follow_visible, open_favorite_visible, update_time)
-- select uid, 1, 1, 1, 1, 1, now()
-- from user_info where username = 'admin_demo';

-- insert ignore into user_stat(uid, fans_count, following_count, like_received_count, video_count, play_received_count)
-- select uid, 0, 0, 0, 0, 0
-- from user_info where username = 'admin_demo';

-- 推荐链路演示建议准备以下数据：
-- 1. 至少 5 条公开视频
-- 2. 至少 3 个普通用户
-- 3. 至少若干条 video_action 行为数据
-- 4. 至少 1 个管理员账号用于调用 ItemCF 重建接口

-- 然后可调用：
-- POST /api/recommend/admin/itemcf/{videoId}/rebuild
-- 检查 recommend_item_similarity 是否生成相似度记录。