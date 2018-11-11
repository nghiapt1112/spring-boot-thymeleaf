INSERT INTO roles(role_id, role_name, role_description)
VALUES
(1, 'ADMIN', 'Admin of system'),
(2, 'USER', 'Normal user'),
(3, 'VIEW', 'View only')
;
-- Test data for user, default password is: "test"
INSERT INTO user(user_id, email, password, first_name, last_name) 
VALUES
(1, 'admin@lyna.com', '$2a$10$fhJ2NAuBS2KSa6aUDKxjYOBAe6FbyiqComjIVjpOPPF8iSPxmu3D2', 'admin_user', 'admin_last_name'),
(2, 'user@lyna.com', '$2a$10$fhJ2NAuBS2KSa6aUDKxjYOBAe6FbyiqComjIVjpOPPF8iSPxmu3D2', 'user_1', 'user1_last_name'),
(3, 'view1@lyna.com', '$2a$10$fhJ2NAuBS2KSa6aUDKxjYOBAe6FbyiqComjIVjpOPPF8iSPxmu3D2', 'view1', 'view1_last_name'),
(4, 'view2@lyna.com', '$2a$10$fhJ2NAuBS2KSa6aUDKxjYOBAe6FbyiqComjIVjpOPPF8iSPxmu3D2', 'view2', 'view2_last_name')
;
--
INSERT INTO users_roles (user_id, role_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3)
;