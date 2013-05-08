package com.pickup.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface EventDao {

	public List getEventList();
}
