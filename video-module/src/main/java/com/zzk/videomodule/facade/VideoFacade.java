package com.zzk.videomodule.facade;

import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoSnapshotVO;
import java.util.Collection;
import java.util.List;

public interface VideoFacade {

    VideoSnapshotVO getVideoSnapshot(Long vid);

    void assertVideoAccessible(Long vid);

    void adjustVideoStats(Long vid,
                          long likeDelta,
                          long dislikeDelta,
                          long coinDelta,
                          long favoriteDelta,
                          long commentDelta,
                          long danmuDelta,
                          long playDelta);

    List<VideoCardVO> listVideoCards(Collection<Long> videoIds);
}
