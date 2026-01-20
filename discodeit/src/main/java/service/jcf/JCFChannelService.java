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
    public Channel createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(channelName, description);
        data.add(channel);
        return channel;
    }

    @Override
    public Channel findByChannelId(UUID channelId) {
        for (Channel channel : data) {
            if (channel.getChannelId().equals(channelId)) {
                return channel;
            }
        }
        throw new RuntimeException("Channel not found");
    }

    @Override
    public List<Channel> findAllChannel() {
        if (data.isEmpty()) {
            System.out.println("만들어진 채널이 없습니다.");
        }
        return data;
    }


    @Override
    public Channel updateChannel(UUID channelId, String channelName, ChannelType type, String description) {
        Channel channel = this.data.stream().filter
                (c -> c.getChannelId().equals(channelId)).findFirst().orElse(null);

        if (channel == null) {
            throw new RuntimeException("해당 Id를 가진 채널이 없습니다.");
        }

        boolean updated = false;

        if (channelName != null){
            channel.updateChannel(channelName);
            updated = true;
        }

        if (description != null){
            channel.updateDescription(description);
            updated = true;
        }

        if (!updated){
            throw new RuntimeException("수정할 값이 없습니다");
        }

        return channel;
    }


    @Override
    public boolean deleteChannel(UUID channelId) {
        Channel channel = findByChannelId(channelId);
        if (channel == null) {
            System.out.println("삭제할 채널이 없습니다.");
            return false;
        }
        return data.remove(channel);
    }

}
