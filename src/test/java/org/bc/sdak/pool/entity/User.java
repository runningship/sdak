package org.bc.sdak.pool.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	public String uid;
	public String name;
	
	public String password;
}
