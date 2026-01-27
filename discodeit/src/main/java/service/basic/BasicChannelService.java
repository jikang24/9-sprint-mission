package service.basic;

import entity.Channel;
import entity.ChannelType;
import org.springframework.stereotype.Service;
import repository.ChannelRepository;
import service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }


    @Override
    public Channel createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(channelName, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findByChannelId(UUID ChannelId) {
        return channelRepository.findByChannelId(ChannelId)
                .orElseThrow(() -> new NoSuchElementException
                        ("Channel with id " + ChannelId + " not found"));
    }

    @Override
    public List<Channel> findAllChannel() {
        return channelRepository.findAllChannel();
    }

    @Override
    public Channel updateChannel(UUID ChannelId, String channelName, ChannelType type, String description) {
        Channel channel = channelRepository.findByChannelId(ChannelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + ChannelId + " not found"));
        return channelRepository.save(channel);
    }

    @Override
    public boolean deleteChannel(UUID ChannelId) {
        if (channelRepository.existsById(ChannelId)) {
            throw new NoSuchElementException("Channel with id " + ChannelId + " not found");
        }
        channelRepository.deleteById(ChannelId);
        return true;
    }
}
