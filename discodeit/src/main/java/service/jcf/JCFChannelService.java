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
    public Channel create(ChannelType type, String channelname, String description) {
        Channel channel = new Channel(channelname, description);
        data.add(channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        for (Channel channel : data) {
            if (channel.getChannelId().equals(channelId)) {
                return channel;
            }
        }
        throw new RuntimeException("Channel not found");
    }

    @Override
    public List<Channel> findAll() {
        if (data.isEmpty()) {
            System.out.println("만들어진 채널이 없습니다.");
        }
        return data;
    }


    @Override
    public Channel update(UUID channelId, String channelname, ChannelType type) {
        for (Channel channel : data) {
            if (channel.getChannelId().equals(channelId)) {
                channel.updateChannel(channelname);
                return channel;
            }
        }
        if (find(channelId) == null) {
            throw new RuntimeException("해당 ID의 채널이 없습니다.");
        }
        throw new RuntimeException("수정할 수 없습니다.");
    }


    @Override
    public boolean deleteChannel(UUID channelId) {
        Channel channel = find(channelId);
        if (channel == null) {
            System.out.println("삭제할 채널이 없습니다.");
            return false;
        }
        data.remove(channel);
        return true;
    }

}
