package service;

import entity.Channel;
import entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(ChannelType type, String channelname, String description);
    Channel find(UUID ChannelId);
    List<Channel> findAll();
    Channel update(UUID ChannelId, String channelname, ChannelType type);
    boolean deleteChannel(UUID ChannelId);

}
