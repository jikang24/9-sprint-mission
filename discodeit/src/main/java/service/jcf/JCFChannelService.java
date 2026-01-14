package service.jcf;

import entity.ChannelType;
import service.ChannelService;

import entity.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
        private final List<Channel> data = new ArrayList<>();

    @Override
    public Channel create(ChannelType type, String channel, String channelname) {
        entity.Channel channel1 = new entity.Channel();
        data.add(channel1);
        return channel1;
    }

    @Override
    public Channel find(String username, String channelname) {
        for (Channel channel : data) {
            if (channel.getChannelname().equals(channelname)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel find(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return data;
    }


    @Override
    public Channel update(UUID id, String channelname, ChannelType type) {
        for (Channel channel : data) {
            if (channel.getId().equals(id)) {
                channel.updateChannel(channelname);
                return channel;
            }
        }
        return null;
    }

    @Override
    public boolean deleteChannel(UUID id) {
        Channel channel = find(id);
        if (channel == null) {
            return false;
        }
        data.remove(channel);
        return true;
    }

}
