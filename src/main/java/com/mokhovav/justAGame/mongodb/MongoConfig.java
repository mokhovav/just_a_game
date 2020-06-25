package com.mokhovav.justAGame.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration  {

    //@Value("mongodb://root:toor@localhost:6546")
    @Value("mongodb://root:toor@mongodb:27017")
    private String connectionString;


    // Annotation based index creation is now turned OFF by default and needs to be enabled
   /* @Override
    protected boolean autoIndexCreation() {
        return true;
    }/**/

    // The MongoDB UUID representation can now be configured with different formats.
 /*   @Override
    public void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.uuidRepresentation(UuidRepresentation.STANDARD);
    }/**/

    @Override
    protected String getDatabaseName() {
        return "games_db";
    }

    @Override
    public MongoClient mongoClient() {
        /*
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .codecRegistry(codecRegistry)
                .build());
/**/
        return MongoClients.create(connectionString);
    }

    @Override
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), "games_db");
    }


    @Override
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }


}
