package com.example.chat.entity;

import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.chat.util.StringUtils;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

import lombok.Data;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Table("messages")
public class Message {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private final UUID id = Uuids.timeBased();
    private UUID chatId;
    private String fromUname;
    private String toUname;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    @CassandraType(type = Name.TIMESTAMP)
    private String timestamp;
    private String content;

    public Message(String timestamp) {
        this.timestamp = StringUtils.getCurrentTimestamp();
    }

}
