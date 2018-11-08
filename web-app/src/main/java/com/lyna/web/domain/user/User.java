package com.lyna.web.domain.user;

import com.lyna.web.infrastructure.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Data
public class User extends AbstractEntity {
}
