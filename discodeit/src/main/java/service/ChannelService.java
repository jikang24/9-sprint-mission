package service;

import entity.Channel;
import entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelType type,String channel, String channelname);
    Channel find(String username, String channelname);
    Channel find(UUID id);
    List<Channel> findAll();
    Channel update(UUID id, String channelname, ChannelType type);
    boolean deleteChannel(UUID id);

}
