-- SYSTEM
INSERT INTO user_db.usr.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM user_db.usr.role r
         JOIN user_db.usr.permission p ON TRUE
WHERE r.role_name = 'SYSTEM'
  AND p.permission_name = 'DO_INTERNAL_TASKS'
ON CONFLICT (role_id, permission_id) DO NOTHING;


-- GUEST
INSERT INTO user_db.usr.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM user_db.usr.role r
         JOIN user_db.usr.permission p
              ON p.permission_name IN (
                                       'VIEW_PUBLIC_INFO',
                                       'REGISTER_ACCOUNT',
                                       'LOGIN',
                                       'LOGOUT')
WHERE r.role_name = 'GUEST'
  AND EXISTS (SELECT 1 FROM user_db.usr.role WHERE role_name = 'GUEST')
  AND EXISTS (SELECT 1 FROM user_db.usr.permission
              WHERE permission_name IN ('VIEW_PUBLIC_INFO', 'REGISTER_ACCOUNT', 'LOGIN', 'LOGOUT'))
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- USER
INSERT INTO user_db.usr.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM user_db.usr.role r,
     user_db.usr.permission p
WHERE r.role_name = 'USER'
  AND p.permission_name IN (
                            'VIEW_PUBLIC_INFO',
                            'LOGOUT',
                            'VIEW_OWN_PROFILE',
                            'UPDATE_OWN_PROFILE',
                            'OPEN_ACCOUNT',
                            'VIEW_OWN_ACCOUNTS',
                            'MAKE_TRANSACTION',
                            'VIEW_OWN_TRANSACTIONS',
                            'VIEW_OWN_NOTIFICATIONS'
    )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- MANAGER
INSERT INTO user_db.usr.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM user_db.usr.role r,
     user_db.usr.permission p
WHERE r.role_name = 'MANAGER'
  AND p.permission_name IN (
                            'VIEW_PUBLIC_INFO',
                            'LOGOUT',
                            'VIEW_OWN_PROFILE',
                            'UPDATE_OWN_PROFILE',
                            'VIEW_ALL_USERS',
                            'OPEN_ACCOUNT',
                            'VIEW_OWN_ACCOUNTS',
                            'VIEW_ALL_ACCOUNTS',
                            'FREEZE_ACCOUNT',
                            'UNFREEZE_ACCOUNT',
                            'CLOSE_ACCOUNT',
                            'MAKE_TRANSACTION',
                            'VIEW_OWN_TRANSACTIONS',
                            'VIEW_ALL_TRANSACTIONS',
                            'APPROVE_LARGE_TRANSACTION',
                            'VIEW_OWN_NOTIFICATIONS'
    )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- ADMIN
INSERT INTO user_db.usr.role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM user_db.usr.role r,
     user_db.usr.permission p
WHERE r.role_name = 'ADMIN'
  AND p.permission_name IN (
                            'VIEW_PUBLIC_INFO',
                            'LOGOUT',
                            'VIEW_OWN_PROFILE',
                            'UPDATE_OWN_PROFILE',
                            'VIEW_ALL_USERS',
                            'UPDATE_ANY_USER',
                            'DELETE_USER',
                            'OPEN_ACCOUNT',
                            'VIEW_OWN_ACCOUNTS',
                            'VIEW_ALL_ACCOUNTS',
                            'FREEZE_ACCOUNT',
                            'APPROVE_LARGE_TRANSACTION',
                            'ROLLBACK_TRANSACTION',
                            'VIEW_ROLES_AND_PERMISSIONS',
                            'ASSIGN_ROLES',
                            'MANAGE_PERMISSIONS',
                            'VIEW_OWN_NOTIFICATIONS',
                            'VIEW_AUDIT_LOGS'
    )
ON CONFLICT (role_id, permission_id) DO NOTHING;