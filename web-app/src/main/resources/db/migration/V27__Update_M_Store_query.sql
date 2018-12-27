ALTER TABLE m_store MODIFY address varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci AFTER major_area;
ALTER TABLE m_store MODIFY phone_number varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  AFTER address;
ALTER TABLE m_store MODIFY person_in_charge varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci  AFTER phone_number;