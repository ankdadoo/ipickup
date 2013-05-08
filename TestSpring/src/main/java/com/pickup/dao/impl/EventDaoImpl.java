package com.pickup.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.pickup.dao.EventDao;

@Repository
public class EventDaoImpl implements EventDao{

	public List getEventList() {
		
		return new ArrayList();
	}
}
