package com.pickup.convertors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.pickup.model.KidProfile;

public class JsonHttpMessageConvertor extends
		MappingJacksonHttpMessageConverter {

	

	@Override
	protected JavaType getJavaType(Class<?> clazz) {
		System.out.println("Came in here to test the class");
		   if (List.class.isAssignableFrom(clazz)) {
		     return TypeFactory.collectionType(ArrayList.class, KidProfile.class);
		   } else {
		     return super.getJavaType(clazz);
		   }
		}
	
	

}
