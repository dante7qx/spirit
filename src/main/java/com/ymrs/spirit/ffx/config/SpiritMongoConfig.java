package com.ymrs.spirit.ffx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class SpiritMongoConfig {

	@Autowired
	private MongoDatabaseFactory mongoDatabaseFactory;

	@Bean
	MongoTemplate mongoTemplate() {
		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDatabaseFactory),
				new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(mongoDatabaseFactory, converter);

	}
}
