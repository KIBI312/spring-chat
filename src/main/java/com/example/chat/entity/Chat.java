package com.example.chat.entity;

import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table("chat")
public class Chat {
    
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID id = Uuids.timeBased();
    private String fromUname;
    private String toUname;
    
    @PersistenceConstructor 
    public Chat(UUID id){
        this.id = id;
    }

    public Chat(String fromUname, String toUname){
        this.fromUname = fromUname;
        this.toUname = toUname;
    }

}
