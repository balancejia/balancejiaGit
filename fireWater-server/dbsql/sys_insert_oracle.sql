/*组织机构——新增公司*/
INSERT INTO org_company VALUES ('1', '泰和鑫软件有限公司', '', '', null, null, null, null, null,to_date('06-08-2014', 'dd-mm-yyyy'), '1');

/*角色模块——新增角色*/
INSERT INTO org_role VALUES ('1', '超级管理员', 'Administrator', '1', '拥有授权权限', '1', to_date('06-08-2014', 'dd-mm-yyyy'));

/*用户模块——新增用户*/
INSERT INTO org_user VALUES ('1', null, 'test', null, null, null, '$2a$12$5IFElNwdZusYfLA7Qu2yreL5GQVahYjWeVD9gDmp/4zcf6Q6RVerW', 'bcrypt', '0', '0', null, null, null, null, '0', null, '0', null, null, to_date('06-08-2014', 'dd-mm-yyyy'), '1', '1', null, null, 'all');

/*用户模块——新增用户角色映射关系*/
INSERT INTO org_user_roles VALUES ('1', '1');

/*权限模块——新增权限类型*/
INSERT INTO privilege_type VALUES ('1', '菜单权限', 'sys_menu', 'com.thx.resource.PriviHandleImpl', '菜单授权');

/*权限模块——新增角色权限映射关系*/
INSERT INTO privilege_role VALUES ('1', '1', 'sys_menu', '1');
INSERT INTO privilege_role VALUES ('1', '2', 'sys_menu', '1');
INSERT INTO privilege_role VALUES ('1', '7', 'sys_menu', '1');


/*资源管理模块——新增资源*/
INSERT INTO sys_resource VALUES ('1', '系统管理', 'sys', null, '', '', '0', '', '', '', '1', '0');
INSERT INTO sys_resource VALUES ('10', '客户端数据管理', 'client_data_manager', null, null, null, '0', '', '', '', '1', '0');
INSERT INTO sys_resource VALUES ('11', '通知公告', 'capp_news', '10', null, null, '1', 'appserver/news!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('12', '意见反馈', 'opinion', '10', null, null, '2', 'appserver/opinion!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('13', '客户端软件版本管理', 'client_soft_version_manager', '10', null, null, '3', 'appserver/cappsoft!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('14', '软件测试设备管理', 'soft_test_mobile', '10', null, null, '4', 'appserver/testMobile!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('15', '新增用户', 'sys_user_add', '2', null, null, '0', 'user/user!input.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('16', '删除用户', 'sys_user_del', '2', null, null, '1', 'user/user!setDelStatus.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('17', '重置密码', 'sys_resetPwd', '2', null, null, '2', 'user/user!sysResetPwd.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('18', '角色编辑', 'sys_role_edit', '3', null, null, '0', 'user/role!input.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('19', '角色删除', 'sys_role_del', '3', null, null, '1', 'user/role!setDelStatus.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('2', '组织机构管理', 'sys_orguser', '1', '', '', '1', 'pages/system/org/list.jsp', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('20', '角色关联人员', 'sys_role_Associated_Persons', '3', null, null, '2', 'user/role!toSelectUsers.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('21', '字典编辑', 'sys_dict_edit', '4', null, null, '0', 'sys/dictName!input.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('22', '字典单删', 'sys_dict_del', '4', null, null, '1', 'sys/dictName!delete.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('23', '字典多删', 'sys_dict_delMulti', '4', null, null, '2', 'sys/dictName!delMulti.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('24', '日志单删', 'sys_log_del', '5', null, null, '0', 'sys/log!delete.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('25', '日志多删', 'sys_log_delMulti', '5', null, null, '1', 'sys/log!delMulti.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('26', '新增资源', 'sys_res_add', '6', null, null, '0', 'sys/resource!input.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('27', '资源单删', 'sys_res_del', '6', null, null, '1', 'sys/resource!delete.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('28', '资源多删', 'sys_res_delMuti', '6', null, null, '2', 'sys/resource!delMulti.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('29', '通知公告编辑', 'capp_news_edit', '11', null, null, '0', 'appserver/news!input.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('3', '角色管理', 'sys_role', '1', '', '', '2', 'user/role!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('30', '通知公告单删', 'capp_news_del', '11', null, null, '1', 'appserver/news!delete.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('31', '通知公告多删', 'capp_news_delMulti', '11', null, null, '2', 'appserver/news!delMulti.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('32', '意见单删', 'opinion_del', '12', null, null, '0', 'appserver/opinion!delete.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('33', '意见多删', 'opinion_delMulti', '12', null, null, '1', 'appserver/opinion!delMulti.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('34', '软件新增', 'capp_soft_add', '13', null, null, '0', 'appserver/cappsoft!input.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('35', '软件修改', 'capp_soft_edit', '13', null, null, '1', 'appserver/cappsoft!editSoft.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('36', '软件版本修改', 'capp_version_edit', '13', null, null, '2', 'appserver/cappsoft!inputCappSoftVersion.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('37', '软件版本发布', 'capp_version_release', '13', null, null, '3', 'appserver/cappsoft!releaseSoft.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('38', '软件版本删除', 'capp_version_del', '13', null, null, '4', 'appserver/cappsoft!delCappSoftVersion.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('4', '字典管理', 'sys_dict', '1', '', '', '3', 'sys/dictName!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('5', '日志管理', 'sys_log', '1', '', '', '4', 'sys/log!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('6', '资源管理', 'sys_res', '1', null, null, '5', 'sys/resource!list.action', '', '', '1', '1');
INSERT INTO sys_resource VALUES ('7', '授权', 'sys_privelege', '1', null, null, '6', 'privilege/privilege!auth.action', '', '', '0', '1');
INSERT INTO sys_resource VALUES ('8', '联系人管理', 'contacts', null, '', '', '0', '', '', '', '1', '0');
INSERT INTO sys_resource VALUES ('9', '联系人管理', 'contacts_manager', '8', '', '', '1', 'contacts/contacts!list.action', '', '', '1', '1');