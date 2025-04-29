package am.banking.system.common.enums;

/**
 * Author: Artyom Aroyan
 * Date: 13.04.25
 * Time: 23:51:21
 */
public enum PermissionEnum {
    // Guest can have view-only access
    VIEW_PUBLIC_CONTENT,

    // User permissions include guest permissions
    UPDATE_PROFILE,
    CREATE_CONTENT,
    VIEW_OWN_CONTENT,

    // Manager permissions include user permissions
    APPROVE_CONTENT,
    REJECT_CONTENT,
    MANAGE_ORDERS,
    VIEW_REPORTS,
    MODERATE_USERS,
    ASSIGN_TASKS,
    MANAGE_USERS,
    ASSIGN_ROLES,
    DELETE_CONTENT,
    CONFIGURE_SYSTEM,
    ACCESS_LOGS,
    MANAGE_SECURITY
}